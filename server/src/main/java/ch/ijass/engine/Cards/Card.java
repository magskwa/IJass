package ch.ijass.engine.Cards;

import ch.ijass.engine.Players.Player;

import java.util.ArrayList;
import java.util.Vector;

public class Card implements Comparable {
  private final CardColor color;
  private final CardValue value;

  /**
   * Constructeur privé de carte
   *
   * @param c la couleur souhaitée de la carte
   * @param v la valeur souhaitée de la carte
   */
  public Card(CardColor c, CardValue v) {
    color = c;
    value = v;
  }

  public Card() {
    color = CardColor.SPADES;
    value = CardValue.ACE;
  }

  public Card(Card other) {
    color = other.color;
    value = other.value;
  }

  /**
   * Construit un deck initial pour le jeu de Jass
   *
   * @return un nouveau vecteur de cartes contenant toutes les cartes du jeu
   */
  public static ArrayList<Card> getInitialDeck() {
    ArrayList<Card> ret = new ArrayList<>();
    for (int i = 0; i < CardColor.values().length; i++) {
      for (int j = 0; j < CardValue.values().length; j++) {
        ret.add(new Card(CardColor.values()[i], CardValue.values()[j]));
      }
    }
    return ret;
  }

  public CardValue getValue() {
    return value;
  }

  /**
   * Indique le nombre de points que vaut la carte sous la couleur d'atout spécifiée
   *
   * @param trump la couleur courante de l'atout
   * @return le nombre de points que vaut la carte
   */
  public int points(CardColor trump) {
    if (color == trump) {
      if (value == CardValue.JACK) return 20;
      else if (value == CardValue.NINE) return 14;
    }
    return value.points();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (value) {
      case SIX:
        sb.append("Six");
        break;
      case SEVEN:
        sb.append("Seven");
        break;
      case EIGHT:
        sb.append("Eight");
        break;
      case NINE:
        sb.append("Nine");
        break;
      case TEN:
        sb.append("Ten");
        break;
      case JACK:
        sb.append("Jack");
        break;
      case QUEEN:
        sb.append("Queen");
        break;
      case KING:
        sb.append("King");
        break;
      case ACE:
        sb.append("Ace");
        break;
    }
    sb.append(" of ");
    switch (color) {
      case CLUBS:
        sb.append("Clubs");
        break;
      case SPADES:
        sb.append("Spades");
        break;
      case HEARTS:
        sb.append("Hearts");
        break;
      case DIAMONDS:
        sb.append("Diamonds");
        break;
    }
    return sb.toString();
  }

  @Override
  public int compareTo(Object o) {
    if (o.getClass() != Card.class)
      throw new RuntimeException("Card comparison with incompatible class");
    else {
      Card other = (Card) o;
      int colorDiff = color.ordinal() - other.color.ordinal();
      int valueDiff = value.ordinal() - other.value.ordinal();
      if (colorDiff != 0) return colorDiff;
      else return valueDiff;
    }
  }

  public boolean isStronger(Card other) {
    if (color.isTrump()) {
      if (other.color.isTrump())
        return value == CardValue.JACK
        || (value == CardValue.NINE && other.value != CardValue.JACK)
        || (other.value != CardValue.JACK && other.value != CardValue.NINE
        && value.ordinal() > other.value.ordinal());
      else
        return true;
    } else
        return color == other.color && value.ordinal() > other.value.ordinal();
  }

  public boolean isEqual(Card other) {
    return compareTo(other) == 0;
  }

  public CardColor getColor() {
    return color;
  }
}
