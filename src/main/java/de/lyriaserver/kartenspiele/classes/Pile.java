package de.lyriaserver.kartenspiele.classes;

import java.util.Collections;
import java.util.List;

public class Pile extends CardStack {
    public Pile() {
        super(Collections.emptyList());
    }

    public Pile(List<Card> cards) {
        super(cards);
    }

    /**
     * Places the specified card on top of the pile
     * @param card The card to place on the pile
     */
    public void placeCard(Card card) {
        cards.add(0, card);
    }

    /**
     * Returns the topmost card of the pile without removing it
     * @return The card on top of the pile
     */
    public Card getTopCard() {
        return cards.get(0);
    }

    /**
     * Takes all cards from the pile except for the topmost one
     * @return The cards taken from the pile
     */
    public List<Card> takeAllCardsExceptTop() {
        Card top = cards.remove(0);
        List<Card> result = List.copyOf(cards);
        cards.clear();
        cards.add(top);
        return result;
    }
}
