package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.classes.Spectator;
import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import de.lyriaserver.kartenspiele.classes.cardgames.Decks;
import de.lyriaserver.kartenspiele.classes.cardgames.Pile;
import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import de.lyriaserver.kartenspiele.gui.screens.LobbyScreen;
import de.lyriaserver.kartenspiele.gui.screens.MauMauScreen;
import de.lyriaserver.kartenspiele.players.CardGamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;

public final class MauMau extends TurnBasedGame<MauMau, CardGamePlayer> implements CardGame, StackGame {
    private static final String NAME = "Mau-Mau";
    public static final ItemStack ICON =
            new ItemBuilder(Material.PAPER)
            .name(NAME)
            .addLore("Gegenstand: Verdeckte Karte")
                    .build();
    private static final int STARTING_HAND_CARDS = 6;
    private final Stack stack;
    private final Pile pile;
    public MauMau() {
        super();
        stack = new Stack(Decks.FULL_DECK);
        pile = new Pile();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getMinPlayers() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 10;
    }

    @Override
    public String[] getOpponentLore(CardGamePlayer player) {
        return new String[] {
                String.format("%d Karten", player.getCardAmount())
        };
    }

    @Override
    public CardGamePlayer createPlayer(HumanEntity player) {
        return new CardGamePlayer(player);
    }

    @Override
    public void showScreenToPlayer(CardGamePlayer player) {
        if (status == Status.Lobby) {
            player.openScreen(new LobbyScreen<>(this, player));
        }
        else {
            player.openScreen(new MauMauScreen(this, player));
        }
    }

    @Override
    public void showScreenToSpectator(Spectator spectator) {
        spectator.openScreen(new MauMauScreen(this, null));
    }

    @Override
    public void runGame() {
        status = Status.Started;
        stack.shuffle();
        pile.placeCard(stack.draw());
        for (CardGamePlayer player : players) {
            player.drawCards(stack, STARTING_HAND_CARDS);
            showScreenToPlayer(player);
        }
    }

    @Override
    public boolean playerUseCard(CardGamePlayer player, Card card) {
        if (status != Status.Started) return false;
        if (currentTurnPlayer != player) return false;
        if (!cardCanBePlayed(card)) return false;
        if (!player.playCard(card, pile)) return false;
        broadcastMessage("%s legt %s.", player.getName(), card.getName());
        if (card.value() == Card.Value.Eight) {
            broadcastMessage("Richtungswechsel!");
            toggleDirection();
        }
        nextTurn();
        if (card.value() == Card.Value.Seven) {
            broadcastMessage("%s muss zwei Karten ziehen!", currentTurnPlayer.getName());
            currentTurnPlayer.drawCards(stack, 2);
        }
        else if (card.value() == Card.Value.Ace) {
            broadcastMessage("%s muss aussetzen!", currentTurnPlayer.getName());
            nextTurn();
        }

        updatePlayerScreens();
        if (player.isHandEmpty()) {
            winner = player;
            finishGame();
        }
        return true;
    }

    private boolean cardCanBePlayed(Card card) {
        Card topCard = pile.getTopCard();
        return card.color() == topCard.color() || card.value() == topCard.value();
    }

    public Pile getPile() {
        return pile;
    }

    @Override
    public Stack getStack() {
        return stack;
    }
}
