package ch.ijass.engine.Players;

import ch.ijass.engine.Cards.CardColor;

public class BotPlayer extends Player {

    @Override
    int askCardToPlay() {
        return 0;
    }

    @Override
    CardColor chooseTrump() {
        return CardColor.SPADES;
    }
}
