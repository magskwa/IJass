package ch.ijass.engine;

import ch.ijass.engine.Cards.*;
import ch.ijass.engine.Cards.InGameCard;
import ch.ijass.engine.Players.*;

import java.util.Vector;

public class GameManager {
    private Player firstForRound;
    private Player firstForFold;
    private Vector<Player> players;
    private Team team1, team2;
    int counterRound;
    int counterFold;

    public void setTrump(CardColor trump) {
        this.trump = trump;
    }

    CardColor trump;
    InGameCard playMat;
    Deck playedCards;
    GameDeck initialDeck;

    final int CINQDEDER = 5;

    final int POINTS = 1000;

    GameManager() {
        this(null);
        players.add(new BotPlayer("Lapinou ", team1));
        players.add(new BotPlayer("Chacha ", team2));
        players.add(new BotPlayer("Titi ", team1));
        players.add(new PersonPlayer("Toto ", team2));

    }

    GameManager(Vector<Player> players) {
        this.players = new Vector<>(players);
        playMat = new InGameCard();
        firstForFold = players.firstElement();
        firstForRound = players.firstElement();
        counterRound = 1;
    }

    Vector<Player> getPlayers() { return players; }

    public Player getWinner(CardColor colorAsked) {
        return playMat.getFoldWinner(colorAsked, trump);
    }

    public void initiateRound() {
        playMat = new InGameCard();
        playedCards = new Deck();
        initialDeck = new GameDeck();
        counterFold = 1;
    }

    public void distribute() {

        // Vérifie que l'état des joueurs est ok pour la distribution
        if (initialDeck.numberOfCards() != 36)
            throw new RuntimeException("Can not distribute cards if the initial deck is incomplete");
        for (Player player : players) {
            if (player.getHand().numberOfCards() != 0)
                throw new RuntimeException("Can not distribute if one of the players still have cards");
        }
        // Distribution des cartes
        while (initialDeck.numberOfCards() > 0) {
            for (Player player : players) {
                player.addCard(initialDeck.pickCardRandomly());
            }
        }
    }


    public void doOneRound() {
        initiateRound();
        distribute();
        updateFirstForRound(); // todo update le systeme de nexte player
        firstForFold = firstForRound;
        trump = firstForRound.chooseTrump();

        // Déroulement de la manche
        while (counterRound < 10) {
            doOneFold();
            counterRound++;
        }


        // Vide les mains des joueurs TODO: retirer si le joueur retire la ref dans play
        for (Player player : players) {
            player.emptyHand();
        }

        // Vide les cartes jouées pendant le round
        playedCards.emptyDeck();
    }

    public Player find7ofDiamonds(){
        for(Player player : players){
            Card card = player.getHand().findCard(CardColor.DIAMONDS, CardValue.SEVEN);
            if( card != null) return card.getOwner();
        }
        return null;
    }

    public int getHighestScore(){
        return Math.max(team1.getScore(), team2.getScore());
    }

    public void chooseTrump(){
        trump = firstForRound.chooseTrump();
    }

    public void updateFirstForRound(){
        if(counterRound == 1){
            firstForRound = find7ofDiamonds();
        } else {
            firstForRound = players.get((players.indexOf(firstForRound) + 1) % 4);
        }
    }

    private CardColor everybodyPlays(){
        Player current = firstForFold;
        Card firstCard = current.playCard(playMat, trump);
        playMat.addCard(firstCard);
        CardColor colorAsked = firstCard.getColor();

        int startIndex = players.indexOf(current) + 1;
        for (int i = 0; i < 3; ++i) {
            playMat.addCard(players.get((startIndex + i) % 4).playCard(playMat, trump));
        }
        return colorAsked;

    }


    public void doOneFold(){
        CardColor colorAsked = everybodyPlays();
        counterFold++;
        firstForFold = playMat.getFoldWinner(colorAsked, trump);
        firstForFold.getTeam().addPoints(playMat.countPoints(trump));

        playedCards.addCards(playMat.getContent());
        playMat.emptyDeck();

        if(counterFold == 9) firstForFold.getTeam().addPoints(CINQDEDER);

    }

    void playing() {
        while(getHighestScore() < POINTS){
            doOneRound();
            updateFirstForRound();
        }
    }














    public static void main(String[] args) {
        GameManager game = new GameManager();
        game.playing();
        System.out.println("Helllo");
    }



}

