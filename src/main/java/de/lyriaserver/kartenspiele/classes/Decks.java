package de.lyriaserver.kartenspiele.classes;

import java.util.ArrayList;
import java.util.List;

public class Decks {
    private static List<Card> generateFullDeck() {
        List<Card> cards = new ArrayList<>();
        for (Card.Color color : Card.Color.values()) {
            for (Card.Value value : Card.Value.values()) {
                cards.add(new Card(color, value));
            }
        }
        return cards;
    }
    public static final List<Card> FULL_DECK = generateFullDeck();
}
