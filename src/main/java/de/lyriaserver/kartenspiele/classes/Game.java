package de.lyriaserver.kartenspiele.classes;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.gui.screens.LobbyScreen;
import de.lyriaserver.kartenspiele.players.Player;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class Game<G extends Game<G, P>, P extends Player> {
    protected final List<P> players = new ArrayList<>();
    protected final List<Spectator> spectators = new ArrayList<>();

    protected final LobbyScreen.StartGameButton<G, P> startGameButton;
    protected Status status = Status.Lobby;
    protected P winner = null;

    protected Game() {
        this.startGameButton = new LobbyScreen.StartGameButton<>(this);
    }

    /**
     * Returns the name of the game (used for example as the lobby title)
     * @return the name of the game
     */
    public abstract String getName();

    /**
     * Returns the lowest number of players that can play the game
     * @return the lowest number of players that can play the game
     */
    public abstract int getMinPlayers();
    /**
     * Returns the highest number of players that can play the game
     * @return the highest number of players that can play the game
     */
    public abstract int getMaxPlayers();

    /**
     * Returns the lore used in the buttons shown to the opponents to indicate a player's stats (like cards in their hand,
     * or chips in their bank)
     * @param player the player whose stats should be displayed
     * @return an array of strings to use as item lore, each containing one line
     */
    public abstract String[] getOpponentLore(P player);

    /**
     * Creates a new {@link Player} from the given {@link HumanEntity}. Can be overridden to provide a customized
     * implementation.
     * @param player The minecraft player to create a game player from
     * @return The newly created game player
     */
    public abstract P createPlayer(HumanEntity player);

    /**
     * Lets a player join the game. This is called whenever a player clicks a deck that's already running a game.
     * If the player was previously part of the game, this method will open the GUI for them again.
     * If the player is new, but the game isn't in the lobby phase anymore, it will instead add them as a spectator
     * and open the GUI.
     * @param player The player to join the game
     */
    public void playerJoin(HumanEntity player) {
        Optional<P> existing = players.stream().filter(existingPlayer -> existingPlayer.getUid().equals(player.getUniqueId())).findFirst();
        if (existing.isPresent()) {
            showScreenToPlayer(existing.get());
        }
        else if (status == Status.Lobby) {
            P newPlayer = createPlayer(player);
            players.add(newPlayer);
            showScreenToPlayer(newPlayer);
        }
        else {
            Spectator newSpectator = new Spectator(player);
            spectators.add(newSpectator);
            showScreenToSpectator(newSpectator);
        }
    }

    /**
     * Shows the given player the currently relevant screen. The implementation will probably involve using
     * {@link org.bukkit.entity.Player#openInventory(Inventory)} using some variation of {@link xyz.janboerman.guilib.api.menu.MenuHolder}
     * @param player The player to show the screen to
     */
    public abstract void showScreenToPlayer(P player);

    public abstract void showScreenToSpectator(Spectator spectator);

    /**
     * Updates the screens of all players and spectators involved in the game.
     */
    public void updatePlayerScreens() {
        for (Player player : players) {
            player.getScreen().update();
        }
        for (Spectator spectator : spectators) {
            spectator.getScreen().update();
        }
    }

    /**
     * Returns true if the game can start, e.g. all players are ready and there is a sufficient amount of them.
     * Should be overridden if additional criteria must be met to start.
     * @return true if the game can start
     */
    public boolean canStart() {
        return players.stream().allMatch(Player::isReady)
                && status == Status.Lobby
                && players.size() >= getMinPlayers()
                && players.size() <= getMaxPlayers();
    }

    /**
     * Starts the game. The implementation should initialize the game to be ready to receive the first player input.
     */
    public abstract void startGame();

    public void finishGame() {
        status = Status.Ended;
        if (winner != null) broadcastMessage("%s hat gewonnen!", winner.getName());
        LyrianischeKartenspiele.INSTANCE.getGames().values().removeAll(Collections.singleton(this));
    }

    /**
     * Broadcasts a message to all players in the game
     * @param message The message to send to all players, formatted using {@link String#format(String, Object...)}
     * @param format The list of objects passed to {@link String#format(String, Object...)}
     */
    public void broadcastMessage(String message, Object... format) {
        for (Player player : players) {
            player.sendMessage(message, format);
        }
        for (Spectator spectator : spectators) {
            spectator.sendMessage(message, format);
        }
    }

    /**
     * Returns a read-only view of the list of players, using {@link Collections#unmodifiableList(List)}
     * @return the list of players
     */
    @Unmodifiable
    public List<P> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public P getWinner() {
        return winner;
    }

    public Status getStatus() {
        return status;
    }

    public LobbyScreen.StartGameButton<G, P> getStartGameButton() {
        return startGameButton;
    }

    @Unmodifiable
    public List<Spectator> getSpectators() {
        return Collections.unmodifiableList(spectators);
    }

    public enum Status {
        Lobby,
        Started,
        Ended
    }
}
