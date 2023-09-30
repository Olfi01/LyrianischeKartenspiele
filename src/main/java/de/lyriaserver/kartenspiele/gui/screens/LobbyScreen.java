package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.IGame;
import de.lyriaserver.kartenspiele.gui.buttons.CancelGameButton;
import de.lyriaserver.kartenspiele.players.Player;
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

public class LobbyScreen<G extends IGame<G, P>, P extends Player> extends GameScreen<G, P> {
    private final Player player;
    private final StartGameButton<G, P> startGameButton;
    private final ReadyButton readyButton;
    public LobbyScreen(G game, P player) {
        super(game, 27, player);
        this.player = player;
        this.startGameButton = game.getStartGameButton();
        this.readyButton = new ReadyButton();

        setButton(0, readyButton);
        setButton(8, new CloseButton<LyrianischeKartenspiele>());

        Iterator<P> iterator = game.getPlayers().iterator();
        for (int row = 1; row < 3; row++) {
            for (int col = 2; col < 7; col++) {
                if (!iterator.hasNext()) break;
                setButton(row * 9 + col, iterator.next().getLobbyButton());
            }
        }

        setButton(18, new CancelGameButton(game));
        setButton(26, startGameButton);

        update();
    }

    @Override
    public void update() {
        Iterator<P> iterator = game.getPlayers().iterator();
        for (int row = 1; row < 3; row++) {
            for (int col = 2; col < 7; col++) {
                if (!iterator.hasNext()) unsetButton(row * 9 + col);
                else setButton(row * 9 + col, iterator.next().getLobbyButton());
            }
        }
        readyButton.update();
        startGameButton.update();
    }

    public class ReadyButton extends ItemButton<LobbyScreen<G, P>> {
        private static final ItemStack readyIcon =
                new ItemBuilder(Material.GREEN_WOOL)
                        .name("Bereit")
                        .build();
        private static final ItemStack notReadyIcon =
                new ItemBuilder(Material.RED_WOOL)
                        .name("Nicht bereit")
                        .build();

        public ReadyButton() {
            super(notReadyIcon);
            update();
        }

        @Override
        public void onClick(LobbyScreen holder, InventoryClickEvent event) {
            player.setReady(!player.isReady());
            startGameButton.update();
            this.update();
        }

        public boolean isReady() {
            return player.isReady();
        }

        public void update() {
            if (player.isReady()) setIcon(readyIcon);
            else setIcon(notReadyIcon);
        }
    }

    public static class StartGameButton<G extends IGame<G, P>, P extends Player> extends ItemButton<LobbyScreen<G, P>> {
        private static final ItemStack canStartIcon =
                new ItemBuilder(Material.TIPPED_ARROW)
                        .name("Spiel starten")
                        .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.JUMP)))
                        .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS))
                        .build();
        private static final ItemStack cantStartIcon =
                new ItemBuilder(Material.TIPPED_ARROW)
                        .name("Nicht alle Spieler sind bereit!")
                        .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL)))
                        .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS))
                        .build();
        private final IGame<G, P> game;

        public StartGameButton(IGame<G, P> game) {
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
                game.broadcastSound(Sounds.GAME_STARTING);
                game.startGame();
            }
            else {
                event.getWhoClicked().playSound(Sounds.GENERIC_ERROR);
            }
        }
    }
}
