package de.lyriaserver.kartenspiele.gui;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.classes.Player;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.CloseButton;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.Iterator;

public class LobbyScreen<G extends Game<G>> extends GameScreen<G> {
    private final Player player;
    private final StartGameButton<G> startGameButton;
    public LobbyScreen(G game, Player player) {
        super(game, 27, player);
        this.player = player;
        this.startGameButton = game.getStartGameButton();

        setButton(0, new ReadyButton());
        setButton(8, new CloseButton<LyrianischeKartenspiele>());

        Iterator<Player> iterator = game.getPlayers().iterator();
        for (int row = 1; row < 3; row++) {
            for (int col = 2; col < 7; col++) {
                if (!iterator.hasNext()) break;
                setButton(row * 9 + col, iterator.next().getLobbyButton());
            }
        }

        setButton(26, startGameButton);
    }

    @Override
    public void update() {
        startGameButton.update();
    }

    public class ReadyButton extends ItemButton<LobbyScreen<G>> {
        private static final ItemStack readyIcon =
                new ItemBuilder(Material.GREEN_WOOL)
                        .name("Bereit")
                        .build();
        private static final ItemStack notReadyIcon =
                new ItemBuilder(Material.RED_WOOL)
                        .name("Nicht bereit")
                        .build();

        private boolean ready = false;
        public ReadyButton() {
            super(notReadyIcon);
        }

        @Override
        public void onClick(LobbyScreen holder, InventoryClickEvent event) {
            ready = !ready;
            if (ready) setIcon(readyIcon);
            else setIcon(notReadyIcon);
            player.setReady(ready);
            startGameButton.update();
        }

        public boolean isReady() {
            return ready;
        }
    }

    public static class StartGameButton<G extends Game<G>> extends ItemButton<LobbyScreen<G>> {
        private static final ItemStack canStartIcon =
                new ItemBuilder(Material.TIPPED_ARROW)
                        .name("Spiel starten")
                        .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.JUMP)))
                        .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS))
                        .build();
        private static final ItemStack cantStartIcon =
                new ItemBuilder(Material.TIPPED_ARROW)
                        .name("Nicht alle Spieler sind bereit!")
                        .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL)))
                        .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS))
                        .build();
        private final Game<G> game;

        public StartGameButton(Game<G> game) {
            super(cantStartIcon);
            this.game = game;
            update();
        }

        public void update() {
            if (game.canStart())
                setIcon(canStartIcon);
            else
                setIcon(cantStartIcon);
        }

        @Override
        public void onClick(LobbyScreen holder, InventoryClickEvent event) {
            if (game.canStart()) {
                game.startGame();
            }
        }
    }
}
