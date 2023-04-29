package de.lyriaserver.kartenspiele.classes.cardgames;

import java.util.List;

public class Stack extends CardStack {
    public Stack(List<Card> deck) {
        super(deck);
    }

    /**
     * Draws the topmost card of the stack
     * @return The card drawn from the stack
     */
    public Card draw() {
        return cards.remove(0);
    }

    /**
     * Draws the specified amount of cards from the stack
     * @param count The amount of cards to draw
     * @return The cards drawn
     */
    public List<Card> draw(int count) {
        Card[] result = new Card[count];
        for (int i = 0; i < count; i++) {
            result[i] = draw();
        }
        return List.of(result);
    }

    /**
     * Takes all cards from the specified pile except for the topmost one and shuffles them into the deck
     * @param pile The pile to take the cards from
     */
    public void refillFromPileAndShuffle(Pile pile, boolean leaveOneCard) {
        if (leaveOneCard) cards.addAll(pile.takeAllCardsExceptTop());
        else cards.addAll(pile.takeAllCards());
        this.shuffle();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Puts all cards in the given list into this stack at the bottom of the stack
     * @param cards The cards to add into this stack
     */
    public void putCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
}
