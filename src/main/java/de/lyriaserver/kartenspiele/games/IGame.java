package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.classes.Spectator;
import de.lyriaserver.kartenspiele.gui.screens.LobbyScreen;
import de.lyriaserver.kartenspiele.players.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface IGame<G extends IGame<G, P>, P extends Player> {
    /**
     * Returns the name of the game (used for example as the lobby title)
     * @return the name of the game
     */
    String getName();
    /**
     * Returns the lowest number of players that can play the game
     * @return the lowest number of players that can play the game
     */
    int getMinPlayers();
    /**
     * Returns the highest number of players that can play the game
     * @return the highest number of players that can play the game
     */
    int getMaxPlayers();
    /**
     * Returns the lore used in the buttons shown to the opponents to indicate a player's stats (like cards in their hand,
     * or chips in their bank)
     * @param player the player whose stats should be displayed
     * @return an array of strings to use as item lore, each containing one line
     */
    String[] getOpponentLore(P player);
    /**
     * Creates a new {@link Player} from the given {@link HumanEntity}. Can be overridden to provide a customized
     * implementation.
     * @param player The minecraft player to create a game player from
     * @return The newly created game player
     */
    P createPlayer(HumanEntity player);
    /**
     * Lets a player join the game. This is called whenever a player clicks a deck that's already running a game.
     * If the player was previously part of the game, this method will open the GUI for them again.
     * If the player is new, but the game isn't in the lobby phase anymore, it will instead add them as a spectator
     * and open the GUI.
     * @param player The player to join the game
     */
    void playerJoin(HumanEntity player);
    /**
     * Shows the given player the currently relevant screen. The implementation will probably involve using
     * {@link org.bukkit.entity.Player#openInventory(Inventory)} using some variation of {@link xyz.janboerman.guilib.api.menu.MenuHolder}
     * @param player The player to show the screen to
     */
    void showScreenToPlayer(P player);
    /**
     * Shows the given spectator the currently relevant screen. The implementation will probably involve using
     * {@link org.bukkit.entity.Player#openInventory(Inventory)} using some variation of {@link xyz.janboerman.guilib.api.menu.MenuHolder}
     * @param spectator The player to show the screen to
     */
    void showScreenToSpectator(Spectator spectator);
    /**
     * Updates the screens of all players and spectators involved in the game.
     */
    void updatePlayerScreens();
    /**
     * Returns true if the game can start, e.g. all players are ready and there is a sufficient amount of them.
     * Should be overridden if additional criteria must be met to start.
     * @return true if the game can start
     */
    boolean canStart();
    /**
     * Starts the game. The implementation should initialize the game to be ready to receive the first player input.
     */
    void startGame();
    /**
     * Ends the game, setting its status to ended, broadcasting the winner if there is any, and removing the game
     * from the global store.
     */
    void finishGame();
    /**
     * Broadcasts a message to all players in the game
     * @param message The message to send to all players, formatted using {@link String#format(String, Object...)}
     * @param format The list of objects passed to {@link String#format(String, Object...)}
     */
    void broadcastMessage(String message, Object... format);
    /**
     * Broadcasts the given sound to all players and spectators.
     * @param sound The sound to play. Can be created using {@link Sound#sound(Key, Sound.Source, float, float)}
     */
    void broadcastSound(Sound sound);
    /**
     * Returns a read-only view of the list of players, using {@link Collections#unmodifiableList(List)}
     * @return the list of players
     */
    @Unmodifiable
    List<P> getPlayers();
    /**
     * Returns the winner of this game, if it has one.
     * @return the winner of this game, if it has one.
     */
    P getWinner();

    /**
     * Returns the status that this game is currently in
     * @return {@link GameStatus}
     */
    GameStatus getStatus();

    /**
     * Returns the button used to start this game. You'll probably want to leave this as it is, as long as you're using
     * the {@link LobbyScreen} class.
     * @return The button to start the game
     */
    LobbyScreen.StartGameButton<G, P> getStartGameButton();
    /**
     * Returns a read-only view of the set of spectators, using {@link Collections#unmodifiableSet(Set)}
     * @return the set of spectators
     */
    @Unmodifiable
    Set<Spectator> getSpectators();

    /**
     * Called when a player closes a screen
     * @param player The player that closed the screen
     */
    void playerLeftScreen(P player);

    /**
     * Sets all players in the lobby to unready. Should be called if game options are changed.
     */
    void setAllPlayersUnready();
}
