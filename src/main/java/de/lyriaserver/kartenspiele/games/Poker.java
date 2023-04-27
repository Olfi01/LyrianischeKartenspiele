package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.Spectator;
import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import de.lyriaserver.kartenspiele.classes.cardgames.Decks;
import de.lyriaserver.kartenspiele.classes.cardgames.Pile;
import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import de.lyriaserver.kartenspiele.classes.poker.Pot;
import de.lyriaserver.kartenspiele.gui.screens.PokerLobbyScreen;
import de.lyriaserver.kartenspiele.gui.screens.PokerScreen;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import de.lyriaserver.kartenspiele.util.EconomyHelper;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.util.ListIterator;
import java.util.Optional;

public final class Poker extends TurnBasedGame<Poker, PokerPlayer> implements MoneyGame<Poker, PokerPlayer> {
    private static final String NAME = "Poker";
    public static final ItemStack ICON_500 =
            new ItemBuilder(Material.IRON_NUGGET)
                    .name(NAME)
                    .addLore("Einkauf: 500")
                    .build();
    public static final ItemStack ICON_1000 =
            new ItemBuilder(Material.GOLD_NUGGET)
                    .name(NAME)
                    .addLore("Einkauf: 1000")
                    .build();
    public static final ItemStack ICON_2000 =
            new ItemBuilder(Material.LAPIS_LAZULI)
                    .name(NAME)
                    .addLore("Einkauf: 2000")
                    .build();
    private static final int AMOUNT_SMALL_BLIND = 1;
    private static final int AMOUNT_BIG_BLIND = AMOUNT_SMALL_BLIND * 2;
    private final int buyIn;
    private boolean playingForMoney = false;
    private final Stack stack;
    private final Pile pile;
    private PokerPlayer smallBlind;
    private final Pot pot = new Pot();
    private final Card[] center = new Card[5];

    public Poker(int buyIn) {
        this.buyIn = buyIn;
        this.stack = new Stack(Decks.FULL_DECK);
        this.pile = new Pile();
    }

    @Override
    public String getName() {
        return "Poker";
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
    public String[] getOpponentLore(PokerPlayer player) {
        return new String[0];   // TODO: fill
    }

    @Override
    public PokerPlayer createPlayer(HumanEntity player) {
        return new PokerPlayer(player, buyIn);
    }

    @Override
    public void showScreenToPlayer(PokerPlayer player) {
        if (status == GameStatus.Lobby) {
            player.openScreen(new PokerLobbyScreen(this, player));
        }
        else {
            player.openScreen(new PokerScreen(this, player));
        }
    }

    @Override
    public void showScreenToSpectator(Spectator spectator) {
        spectator.openScreen(new PokerScreen(this, null));
    }

    @Override
    public void runGame() {
        Optional<EconomyHelper> economy = LyrianischeKartenspiele.getEconomyHelper();
        for (PokerPlayer player : players) {
            if (economy.isPresent() && playingForMoney) {
                EconomyResponse response = economy.get().withdrawPlayer(player, buyIn);
                if (!response.transactionSuccess()) {
                    broadcastMessage("%s konnte den Einsatz nicht bezahlen: %s",
                            player.getName(), response.errorMessage);
                    finishGame();
                    return;
                }
            }
        }
        if (economy.isPresent() && playingForMoney) {
            broadcastMessage("Alle Spieler haben den Einsatz von %s bezahlt.", economy.get().format(buyIn));
        }
        smallBlind = currentTurnPlayer;

        startRound();
    }

    private void startRound() {
        ListIterator<PokerPlayer> iterator = players.listIterator();
        while (iterator.hasNext()) {
            PokerPlayer player = iterator.next();
            player.discardCards(pile);
            if (player.getChips() <= 0) {
                buyOut(player);
                if (player == currentTurnPlayer) nextTurn();
                iterator.remove();
                player.getMcPlayer().closeInventory();
                broadcastMessage("%s hat keine Chips mehr und ist raus!", player.getName());
            }
        }
        if (!canContinue()) {
            broadcastMessage("Es sind nicht genug Spieler übrig, um weiterzuspielen!");
            if (players.size() == 1) winner = players.get(0);
            finishGame();
            return;
        }

        stack.refillFromPileAndShuffle(pile, false);
        for (PokerPlayer player : players) {
            player.drawCards(stack, 2);
        }

        currentTurnPlayer = smallBlind;
        if (!pot.deposit(currentTurnPlayer, AMOUNT_SMALL_BLIND)) {
            pot.allIn(currentTurnPlayer);
        }
        nextTurn();
        if (!pot.deposit(currentTurnPlayer, AMOUNT_BIG_BLIND)) {
            pot.allIn(currentTurnPlayer);
        }
        nextTurn();
        updatePlayerScreens();
        // TODO: nicht vergessen, vor Start einer neuen Runde den neuen small blind setzen
    }

    @Override
    public void finishGame() {
        for (PokerPlayer player : players) {
            buyOut(player);
        }
        super.finishGame();
    }

    @Override
    public boolean isPlayingForMoney() {
        return playingForMoney;
    }

    @Override
    public void setPlayingForMoney(boolean playingForMoney) {
        this.playingForMoney = playingForMoney;
    }

    /**
     * Returns a card from the center of the table, specified by the 0-based index. 0, 1 and 2 refer to the flop,
     * 3 refers to the turn and 4 refers to the river. If no card has been dealt here, returns {@link Optional#empty()}
     * @param index The 0-based index of the card.
     * @return The card at the given index, if present
     */
    public Optional<Card> getCenterCard(int index) {
        return Optional.ofNullable(center[index]);
    }

    private boolean canContinue() {
        return canStart();
    }

    private void buyOut(PokerPlayer player) {
        Optional<EconomyHelper> economy = LyrianischeKartenspiele.getEconomyHelper();
        if (economy.isPresent() && playingForMoney) {
            int amount = player.getChips();
            economy.get().depositPlayer(player, amount);
            player.sendMessage("Dir wurde %s ausgezahlt.", economy.get().format(amount));
        }
    }
}
