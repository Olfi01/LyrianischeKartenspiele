package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.Spectator;
import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import de.lyriaserver.kartenspiele.classes.cardgames.Decks;
import de.lyriaserver.kartenspiele.classes.cardgames.Pile;
import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import de.lyriaserver.kartenspiele.classes.poker.PokerCombination;
import de.lyriaserver.kartenspiele.classes.poker.Pot;
import de.lyriaserver.kartenspiele.gui.screens.MoneyLobbyScreen;
import de.lyriaserver.kartenspiele.gui.screens.PokerScreen;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import de.lyriaserver.kartenspiele.util.EconomyHelper;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.util.*;

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
    private final List<PokerPlayer> buyIns = new ArrayList<>();
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
        List<String> lines = new ArrayList<>();
        lines.add(String.format("Chips: %d", player.getChips()));
        lines.add(String.format("Bisher gesetzt: %d", pot.getBet(player).orElse(0)));
        lines.add(player.hasFolded() ? "Runde aufgegeben" : "Noch dabei");
        if (smallBlind == player) lines.add("Small Blind");
        else if (playerAfter(smallBlind) == player) lines.add("Big Blind");
        return lines.toArray(new String[0]);
    }

    @Override
    public PokerPlayer createPlayer(HumanEntity player) {
        return new PokerPlayer(player, buyIn);
    }

    @Override
    public void showScreenToPlayer(PokerPlayer player) {
        if (status == GameStatus.Lobby) {
            player.openScreen(new MoneyLobbyScreen<>(this, player));
        } else {
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
            showScreenToPlayer(player);
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
        players.addAll(buyIns);
        buyIns.clear();
        if (!canContinue()) {
            broadcastMessage("Es sind nicht genug Spieler übrig, um weiterzuspielen!");
            finishGame();
            return;
        }

        stack.refillFromPileAndShuffle(pile, false);
        for (PokerPlayer player : players) {
            player.drawCards(stack, 2);
        }

        currentTurnPlayer = smallBlind;
        if (pot.deposit(currentTurnPlayer, AMOUNT_SMALL_BLIND)) {
            broadcastMessage("%s setzt den Small Blind von %d.",
                    currentTurnPlayer.getName(), AMOUNT_SMALL_BLIND);
        } else {
            pot.allIn(currentTurnPlayer);
            broadcastMessage("%s setzt alles!", currentTurnPlayer.getName());
        }
        nextTurn();
        if (pot.deposit(currentTurnPlayer, AMOUNT_BIG_BLIND)) {
            broadcastMessage("%s setzt den Big Blind von %d.",
                    currentTurnPlayer.getName(), AMOUNT_BIG_BLIND);
        } else {
            pot.allIn(currentTurnPlayer);
            broadcastMessage("%s setzt alles!", currentTurnPlayer.getName());
        }
        nextTurn();
        startBettingRound();
    }

    private void startBettingRound() {
        for (PokerPlayer player : players) {
            player.setHasRaised(false);
            player.setHasChecked(false);
        }
        updatePlayerScreens();
    }

    public void playerFold(PokerPlayer player) {
        if (player.hasFolded()) return;
        player.discardCards(pile);
        broadcastMessage("%s hat aufgegeben.", player.getName());
        if (player == currentTurnPlayer) {
            nextTurn();
            checkBettingRoundEnd();
        }
        updatePlayerScreens();
    }

    public boolean placeBet(PokerPlayer player, int amount) {
        int playerChips = player.getChips();
        if (currentTurnPlayer != player || playerChips < amount) return false;
        int playerCallAmount = pot.playerCallAmount(player);
        if (player.hasRaised() && amount > playerCallAmount) return false;
        if (amount < playerCallAmount || amount == playerChips) {
            if (amount < playerChips) return false;
            pot.allIn(player);
            if (amount > playerCallAmount) {
                player.setHasRaised(true);
                players.forEach(p -> p.setHasChecked(false));
                player.setHasChecked(true);
            } else player.setHasChecked(true);
            broadcastMessage("%s setzt alles!", player.getName());
        } else if (amount == playerCallAmount) {
            if (amount == 0) {
                broadcastMessage("%s gibt weiter.", player.getName());
            } else {
                pot.deposit(player, amount);
                broadcastMessage("%s geht mit.", player.getName());
            }
            player.setHasChecked(true);
        } else {
            pot.deposit(player, amount);
            player.setHasRaised(true);
            players.forEach(p -> p.setHasChecked(false));
            player.setHasChecked(true);
            broadcastMessage("%s erhöht um %d.", player.getName(), amount - playerCallAmount);
        }
        nextTurn();
        updatePlayerScreens();
        checkBettingRoundEnd();
        return true;
    }

    private void checkBettingRoundEnd() {
        if (players.stream().filter(player -> !player.hasFolded()).count() < 2) {
            endRound();
        }
        if (players.stream().filter(player -> !player.hasFolded())
                .allMatch(player -> pot.playerCallAmount(player) == 0)
                && players.stream().filter(player -> !player.hasFolded())
                .allMatch(player -> player.hasRaised() || player.hasChecked())) {
            endBettingRound();
        }
    }

    private void endBettingRound() {
        if (center[4] != null) {
            endRound();
        }
        else if (center[3] != null) {
            center[4] = stack.draw();
            currentTurnPlayer = smallBlind;
            startBettingRound();
        }
        else if (center[0] != null) {
            center[3] = stack.draw();
            currentTurnPlayer = smallBlind;
            startBettingRound();
        }
        else {
            for (int i = 0; i < 3; i++) {
                center[i] = stack.draw();
            }
            currentTurnPlayer = smallBlind;
            startBettingRound();
        }
    }

    private void endRound() {
        PokerPlayer winner = determineWinner(players.stream().filter(player -> !player.hasFolded()).toList(), true);
        int amountBefore = winner.getChips();
        // TODO: show winner cards?
        Optional<Pot> sidePot = pot.collectWinnings(winner);
        broadcastMessage("%s hat %d Chips gewonnen!", winner.getName(), winner.getChips() - amountBefore);
        while (sidePot.isPresent()) {
            PokerPlayer sidePotWinner = determineWinner(sidePot.get().bettingPlayers().stream()
                    .map(player -> (PokerPlayer)player)
                    .filter(player -> !player.hasFolded()).toList(), false);
            amountBefore = sidePotWinner.getChips();
            sidePot = sidePot.get().collectWinnings(sidePotWinner);
            broadcastMessage("%s hat einen Side-Pot von %d Chips gewonnen!",
                    sidePotWinner.getName(), sidePotWinner.getChips() - amountBefore);
        }
        Arrays.fill(center, null);
        smallBlind = playerAfter(smallBlind);
        updatePlayerScreens();
        startRound();
    }

    private PokerPlayer determineWinner(List<PokerPlayer> players, boolean announceHands) {
        if (players.size() == 1) {
            return players.get(0);
        }
        Map<PokerPlayer, PokerCombination> combinations = new HashMap<>();
        for (PokerPlayer player : players) {
            PokerCombination combination = PokerCombination.of(player.getCards(), center);
            combinations.put(player, combination);
            if (announceHands) announceHand(player.getName(), combination);
        }
        return combinations.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getKey();
    }

    private void announceHand(String playerName, PokerCombination combination) {
        switch (combination.getValue()) {
            case RoyalFlush -> broadcastMessage("%s hat einen Royal Flush!", playerName);
            case StraightFlush -> broadcastMessage("%s hat einen Straight Flush. Obere Karte: %s",
                    playerName, combination.getHighCard().getName());
            case FourOfAKind -> broadcastMessage("%s hat einen Vierling aus Karten vom Wert %s",
                    playerName, combination.getDeterminingCard().value().getName());
            case FullHouse -> broadcastMessage("%s hat ein Full-House vom Wert %s und %s",
                    playerName, combination.getDeterminingCard().value().getName(),
                    combination.getSecondDeterminingCard().value().getName());
            case Flush -> broadcastMessage("%s hat einen Flush. Obere Karte: %s",
                    playerName, combination.getHighCard().getName());
            case Straight -> broadcastMessage("%s hat eine Straße. Obere Karte: %s",
                    playerName, combination.getHighCard().value().getName());
            case ThreeOfAKind -> broadcastMessage("%s hat einen Drilling aus Karten vom Wert %s",
                    playerName, combination.getDeterminingCard().value().getName());
            case TwoPair -> broadcastMessage("%s hat zwei Paare, %s und %s", playerName,
                    combination.getDeterminingCard().value().getName(),
                    combination.getSecondDeterminingCard().value().getName());
            case Pair -> broadcastMessage("%s hat ein Paar aus Karten vom Wert %s", playerName,
                    combination.getDeterminingCard().value().getName());
            case HighCard -> broadcastMessage("%s hat %s als Höchste Karte", playerName,
                    combination.getDeterminingCard().value().getName());
        }
    }

    public Optional<PokerPlayer> tryBuyIn(HumanEntity human) {
        if (players.stream().anyMatch(player -> player.getUid().equals(human.getUniqueId()))
                || buyIns.stream().anyMatch(player -> player.getUid().equals(human.getUniqueId()))) {
            return Optional.empty();
        }
        PokerPlayer player = new PokerPlayer(human, buyIn);
        spectators.removeIf(spectator -> spectator.getUid().equals(human.getUniqueId()));
        return Optional.of(player);
    }

    public boolean tryBuyOut(PokerPlayer player) {
        if (player.getCardAmount() > 0) return false;
        buyOut(player);
        players.remove(player);
        spectators.add(new Spectator(player.getMcPlayer()));
        return true;
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
     *
     * @param index The 0-based index of the card.
     * @return The card at the given index, if present
     */
    public Optional<Card> getCenterCard(int index) {
        return Optional.ofNullable(center[index]);
    }

    public int getMaxBet() {
        return pot.getMaxBet();
    }

    public int getCurrentPot() {
        return pot.getBalance();
    }

    public int getPlacedBet(PokerPlayer player) {
        return pot.getBet(player).orElse(0);
    }

    public int playerCallAmount(PokerPlayer player) {
        return pot.playerCallAmount(player);
    }

    private boolean canContinue() {
        return players.size() >= getMinPlayers()
                && players.size() <= getMaxPlayers();
    }

    private void buyOut(PokerPlayer player) {
        Optional<EconomyHelper> economy = LyrianischeKartenspiele.getEconomyHelper();
        if (economy.isPresent() && playingForMoney) {
            int amount = player.getChips();
            economy.get().depositPlayer(player, amount);
            player.sendMessage("Dir wurde %s ausgezahlt.", economy.get().format(amount));
        }
    }

    @Override
    public void updatePlayerScreens() {
        super.updatePlayerScreens();
        for (PokerPlayer player : buyIns) {
            player.getScreen().update();
        }
    }
}
