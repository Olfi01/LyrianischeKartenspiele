package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.classes.Player;

public abstract class TurnBasedGame<G extends TurnBasedGame<G>> extends Game<G> {
    protected Player currentTurnPlayer = null;
    protected boolean reverseDirection = false;

    @Override
    public void startGame() {
        currentTurnPlayer = players.get(LyrianischeKartenspiele.RANDOM.nextInt(players.size()));
    }

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
