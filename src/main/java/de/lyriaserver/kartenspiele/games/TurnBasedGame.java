package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.players.Player;

public abstract class TurnBasedGame<G extends TurnBasedGame<G, P>, P extends Player> extends Game<G, P> {
    protected P currentTurnPlayer = null;
    protected boolean reverseDirection = false;

    @Override
    public void startGame() {
        currentTurnPlayer = players.get(LyrianischeKartenspiele.RANDOM.nextInt(players.size()));
        status = GameStatus.Started;
        runGame();
    }

    /**
     * Used in place of {@link IGame#startGame()} but functions the same. Will be called after
     * {@link TurnBasedGame#currentTurnPlayer} is set to a random player.
     */
    public abstract void runGame();

    /**
     * Moves the currentTurnPlayer to the next player in the row.
     */
    public void nextTurn() {
        currentTurnPlayer = playerAfter(currentTurnPlayer);
    }

    /**
     * Returns the player whose turn it will be after the given player, respecting game direction
     * @param player the player before the player to find
     * @return the player whose turn it will be after the given player
     */
    public P playerAfter(P player) {
        int index = players.indexOf(player);
        if (reverseDirection) {
            index--;
            if (index < 0) index = players.size() - 1;
        }
        else {
            index++;
            if (index >= players.size()) index = 0;
        }
        return players.get(index);
    }


    /**
     * Returns the player whose turn it currently is.
     * @return the player whose turn it currently is
     */
    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    /**
     * Reverses the direction that the game is currently going in.
     */
    public void toggleDirection() {
        reverseDirection = !reverseDirection;
    }
}
