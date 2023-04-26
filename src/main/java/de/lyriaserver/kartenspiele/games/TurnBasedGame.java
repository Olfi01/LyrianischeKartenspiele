package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.players.Player;

public abstract class TurnBasedGame<G extends TurnBasedGame<G, P>, P extends Player> extends Game<G, P> {
    protected P currentTurnPlayer = null;
    protected boolean reverseDirection = false;

    @Override
    public void startGame() {
        currentTurnPlayer = players.get(LyrianischeKartenspiele.RANDOM.nextInt(players.size()));
        runGame();
    }

    /**
     * Used in place of {@link Game#startGame()} but functions the same. Will be called after
     * {@link TurnBasedGame#currentTurnPlayer} is set to a random player.
     */
    public abstract void runGame();

    public void nextTurn() {
        int index = players.indexOf(currentTurnPlayer);
        if (reverseDirection) {
            index--;
            if (index < 0) index = players.size() - 1;
        }
        else {
            index++;
            if (index >= players.size()) index = 0;
        }
        currentTurnPlayer = players.get(index);
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void toggleDirection() {
        reverseDirection = !reverseDirection;
    }
}
