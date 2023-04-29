package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.buttons.*;
import de.lyriaserver.kartenspiele.gui.buttons.poker.BuyOutButton;
import de.lyriaserver.kartenspiele.gui.buttons.poker.FoldButton;
import de.lyriaserver.kartenspiele.gui.buttons.poker.RaiseCallButton;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PokerScreen extends GameScreen<Poker, PokerPlayer> {
    private static final List<Integer> playerOnlyButtonSlots = List.of(
            18, 20, 21, 22, 23, 24, 26, 27, 30, 32
    );
    private static final ItemStack ownMoneyIcon =
            new ItemBuilder(Material.GOLD_NUGGET)
                    .name("Eigene Chips:")
                    .build();
    private static final ItemStack topBetIcon =
            new ItemBuilder(Material.GOLD_NUGGET)
                    .name("Momentaner Einsatz:")
                    .build();
    private static final ItemStack currentPotIcon =
            new ItemBuilder(Material.GOLD_INGOT)
                    .name("Geld im Pot:")
                    .build();
    private static final ItemStack amountIcon =
            new ItemBuilder(Material.IRON_INGOT)
                    .name("Einsatz:")
                    .build();
    private static final ItemStack placedBetIcon =
            new ItemBuilder(Material.LAPIS_LAZULI)
                    .name("Bereits gesetzt:")
                    .build();
    private final CardView[] cards = new CardView[7];
    private final List<IntView> intViews = new ArrayList<>();
    private TurnIndicator<Poker> turnIndicator;
    @Nullable
    private final AmountButton amountButton;
    @Nullable
    private final RaiseCallButton raiseCallButton;

    public PokerScreen(Poker game, @Nullable PokerPlayer player) {
        super(game, 54, player);

        // TODO: buy-in button in slot 0?
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            CardView button = new CardView(() -> game.getCenterCard(finalI));
            cards[i] = button;
            setButton(2 + i, button);
        }
        setButton(8, new CancelGameButton(game));

        if (player != null) {
            IntView ownMoneyButton = new IntView(ownMoneyIcon, player::getChips);
            intViews.add(ownMoneyButton);
            setButton(18, ownMoneyButton);
            setButton(20, new FoldButton(game, player));
            this.amountButton = new AmountButton(amountIcon, 0, player.getChips());
            setButton(21, new AmountButton.Subtract(amountButton));
            setButton(22, amountButton);
            setButton(23, new AmountButton.Add(amountButton));
            this.raiseCallButton = new RaiseCallButton(game, player, amountButton);
            setButton(24, raiseCallButton);
            setButton(26, new BuyOutButton(game, player));
        }
        else {
            this.amountButton = null;
            this.raiseCallButton = null;
        }


        if (player != null) {
            IntView placedBetButton = new IntView(placedBetIcon, () -> game.getPlacedBet(player));
            setButton(27, placedBetButton);
            intViews.add(placedBetButton);
            for (int i = 0; i < 2; i++) {
                final int finalI = i;
                CardView button = new CardView(() ->
                        player.getCards().size() > finalI
                                ? Optional.of(player.getCards().get(finalI))
                                : Optional.empty());
                cards[5 + i] = button;
                setButton(2 * i + 30, button);
            }
        }
        IntView topBetButton = new IntView(topBetIcon, game::getMaxBet);
        setButton(34, topBetButton);
        intViews.add(topBetButton);
        IntView currentPotButton = new IntView(currentPotIcon, game::getCurrentPot);
        setButton(35, currentPotButton);
        intViews.add(currentPotButton);

        turnIndicator = new TurnIndicator<>(game, player);
        for (int slot = 36; slot < 45; slot++) {
            setButton(slot, turnIndicator);
        }

        Iterator<PokerPlayer> iterator = game.getPlayers().iterator();
        for (int slot = 45; slot < 54; slot++) {
            if (!iterator.hasNext()) break;
            setButton(slot, new OpponentInfo<>(game, iterator.next()));
        }
    }

    @Override
    public void update() {
        for (CardView card : cards) {
            if (card != null) card.update();
        }
        for (IntView view : intViews) {
            view.update();
        }
        if (amountButton != null && player != null) {
            amountButton.setMinAmount(game.playerCallAmount(player));
            amountButton.setMaxAmount(player.getChips());
        }
        if (raiseCallButton != null) raiseCallButton.update();
        // TODO: other buttons
        turnIndicator.update();
        Iterator<PokerPlayer> iterator = game.getPlayers().iterator();
        for (int slot = 45; slot < 54; slot++) {
            if (!iterator.hasNext()) unsetButton(slot);
            else setButton(slot, new OpponentInfo<>(game, iterator.next()));
        }
    }

    public void setSpectator() {
        player = null;
        for (int slot : playerOnlyButtonSlots) {
            unsetButton(slot);
        }
        turnIndicator = new TurnIndicator<>(game, player);
        for (int slot = 36; slot < 45; slot++) {
            setButton(slot, turnIndicator);
        }
    }
}
