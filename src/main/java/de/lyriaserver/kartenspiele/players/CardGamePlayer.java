package de.lyriaserver.kartenspiele.players;

import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import de.lyriaserver.kartenspiele.classes.cardgames.Pile;
import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardGamePlayer extends Player {
    protected final List<Card> hand = new ArrayList<>();
    public CardGamePlayer(HumanEntity player) {
        super(player);
    }

    /**
     * Adds the given cards to the hand of this player
     * @param cards List of cards to add to the hand
     */
    public void takeCards(List<Card> cards) {
        hand.addAll(cards);
    }

    /**
     * Lets the player place the specified card on the specified pile
     * @param card The card to play
     * @param pile The pile to place the card on
     * @return false if the player doesn't have that card in their hand
     */
    public boolean playCard(Card card, Pile pile) {
        if (!hand.remove(card)){
            return false;
        }
        pile.placeCard(card);
        return true;
    }

    /**
     * Lets the player draw a card from the specified stack
     * @param stack The stack to draw from
     */
    public void drawCard(Stack stack) {
        hand.add(stack.draw());
    }

    /**
     * Lets the player draw the given number of cards from the specified stack
     * @param stack The stack to draw from
     * @param amount The amount of cards to draw
     */
    public void drawCards(Stack stack, int amount) {
        takeCards(stack.draw(amount));
    }

    /**
     * Discards all the cards from the player's hand without placing them on any pile.
     */
    public void discardCards() {
        discardCards(null);
    }

    /**
     * Discards all the cards from the player's hand onto the given pile
     * @param pile The pile to place the cards on, or null to not place them anywhere
     */
    public void discardCards(@Nullable Pile pile) {
        if (pile != null) pile.placeCards(hand);
        hand.clear();
    }

    /**
     * Returns true if the player has no more cards in their hand
     * @return true if the player has no more cards in their hand
     */
    public boolean isHandEmpty() {
        return hand.isEmpty();
    }

    @Unmodifiable
    public List<Card> getCards() {
        return Collections.unmodifiableList(hand);
    }

    /**
     * Returns the number of cards the player currently has in hand
     * @return the number of cards the player currently has in hand
     */
    public int getCardAmount() {
        return hand.size();
    }
}
