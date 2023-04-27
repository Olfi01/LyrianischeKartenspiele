package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.GameStatus;
import de.lyriaserver.kartenspiele.games.StackPileGame;
import de.lyriaserver.kartenspiele.games.TurnBasedGame;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import de.lyriaserver.kartenspiele.players.CardGamePlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class DrawCardButton extends ItemButton<GameScreen<?, ?>> {
    private static final ItemStack icon =
            new ItemBuilder(Material.PAPER)
                    .name("Karte ziehen")
                    .addLore("Gegenstand: Verdeckte Karte")
                    .build();
    private final StackPileGame<?, ?> game;
    private final CardGamePlayer player;
    private final boolean leaveOneCardOnRefill;

    public DrawCardButton(StackPileGame<?, ?> game, @Nullable CardGamePlayer player, boolean leaveOneCardOnRefill) {
        super(icon);
        this.game = game;
        this.player = player;
        this.leaveOneCardOnRefill = leaveOneCardOnRefill;
    }

    @Override
    public void onClick(GameScreen<?, ?> holder, InventoryClickEvent event) {
        if (player == null || game.getStatus() != GameStatus.Started || !game.canPlayerDraw(player)) return;
        Stack stack = game.getStack();
        if (stack.isEmpty()) stack.refillFromPileAndShuffle(game.getPile(), leaveOneCardOnRefill);
        player.drawCard(stack);
        game.broadcastSound(Sounds.CARD_DRAW);
        game.broadcastMessage("%s zieht eine Karte.", player.getName());
        if (game instanceof TurnBasedGame<?, ?> turnBasedGame)
            turnBasedGame.nextTurn();
        game.updatePlayerScreens();
    }
}
