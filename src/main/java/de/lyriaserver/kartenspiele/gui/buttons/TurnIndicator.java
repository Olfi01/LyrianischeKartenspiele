package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.classes.Player;
import de.lyriaserver.kartenspiele.games.TurnBasedGame;
import de.lyriaserver.kartenspiele.gui.MauMauScreen;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class TurnIndicator<G extends TurnBasedGame<G>> extends ItemButton<MauMauScreen> {
    private static final ItemStack yourTurn =
            new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                    .name("Du bist dran!")
                    .build();
    private static final ItemStack notYourTurn =
            new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                    .name("Du bist nicht dran!")
                    .build();
    private static final ItemStack gameFinished =
            new ItemBuilder(Material.GOLD_BLOCK)
                    .name("Spiel vorbei!")
                    .build();
    private final G game;
    private final Player player;
    public TurnIndicator(G game, @Nullable Player player) {
        super(notYourTurn);
        this.game = game;
        this.player = player;
        update();
    }

    public void update() {
        if (game.getStatus() == Game.Status.Ended)
            setIcon(gameFinished);
        else if (game.getCurrentTurnPlayer() == player)
            setIcon(yourTurn);
        else
            setIcon(notYourTurn);
    }
}
