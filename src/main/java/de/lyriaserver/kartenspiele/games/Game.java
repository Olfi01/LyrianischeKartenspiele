package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.Spectator;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.gui.screens.LobbyScreen;
import de.lyriaserver.kartenspiele.players.Player;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class Game<G extends Game<G, P>, P extends Player> implements IGame<G, P> {
    protected final List<P> players = new ArrayList<>();
    protected final List<Spectator> spectators = new ArrayList<>();

    protected final LobbyScreen.StartGameButton<G, P> startGameButton;
    protected GameStatus status = GameStatus.Lobby;
    protected P winner = null;

    protected Game() {
        this.startGameButton = new LobbyScreen.StartGameButton<>(this);
    }

    @Override
    public abstract String getName();

    @Override
    public abstract int getMinPlayers();
    @Override
    public abstract int getMaxPlayers();

    @Override
    public abstract String[] getOpponentLore(P player);

    @Override
    public abstract P createPlayer(HumanEntity player);

    @Override
    public void playerJoin(HumanEntity player) {
        Optional<P> existing = players.stream().filter(existingPlayer -> existingPlayer.getUid().equals(player.getUniqueId())).findFirst();
        if (existing.isPresent()) {
            showScreenToPlayer(existing.get());
        }
        else if (status == GameStatus.Lobby) {
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

    @Override
    public abstract void showScreenToPlayer(P player);

    @Override
    public abstract void showScreenToSpectator(Spectator spectator);

    @Override
    public void updatePlayerScreens() {
        for (Player player : players) {
            player.getScreen().update();
        }
        for (Spectator spectator : spectators) {
            spectator.getScreen().update();
        }
    }

    @Override
    public boolean canStart() {
        return players.stream().allMatch(Player::isReady)
                && status == GameStatus.Lobby
                && players.size() >= getMinPlayers()
                && players.size() <= getMaxPlayers();
    }

    @Override
    public abstract void startGame();

    @Override
    public void finishGame() {
        status = GameStatus.Ended;
        if (winner != null) {
            broadcastMessage("%s hat gewonnen!", winner.getName());
            winner.playSound(Sounds.GAME_WON);
            players.stream().filter(player -> !player.equals(winner))
                    .forEach(player -> player.playSound(Sounds.GAME_LOST));
        }
        else {
            players.forEach(player -> player.playSound(Sounds.GAME_DRAW));
        }
        LyrianischeKartenspiele.INSTANCE.getGames().values().removeAll(Collections.singleton(this));
    }

    @Override
    public void broadcastMessage(String message, Object... format) {
        for (Player player : players) {
            player.sendMessage(message, format);
        }
        for (Spectator spectator : spectators) {
            spectator.sendMessage(message, format);
        }
    }

    @Override
    public void broadcastSound(Sound sound) {
        for (Player player : players) {
            player.playSound(sound);
        }
        for (Spectator spectator : spectators) {
            spectator.playSound(sound);
        }
    }

    @Override
    @Unmodifiable
    public List<P> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public P getWinner() {
        return winner;
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    @Override
    public LobbyScreen.StartGameButton<G, P> getStartGameButton() {
        return startGameButton;
    }

    @Override
    @Unmodifiable
    public List<Spectator> getSpectators() {
        return Collections.unmodifiableList(spectators);
    }

}
