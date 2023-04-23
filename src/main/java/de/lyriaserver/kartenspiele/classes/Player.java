package de.lyriaserver.kartenspiele.classes;

import de.lyriaserver.kartenspiele.gui.buttons.LobbyPlayerButton;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Player extends Spectator {
    private final List<Card> hand = new ArrayList<>();
    private final String name;
    private final LobbyPlayerButton lobbyButton;
    private boolean ready;

    public Player(HumanEntity player) {
        super(player);
        name = player.getName();
        lobbyButton = new LobbyPlayerButton(this);
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

    public UUID getUid() {
        return minecraftPlayer.getUniqueId();
    }

    public String getName() {
        return name;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        this.lobbyButton.setReady(ready);
    }

    public boolean isReady() {
        return ready;
    }

    public LobbyPlayerButton getLobbyButton() {
        return lobbyButton;
    }
}
