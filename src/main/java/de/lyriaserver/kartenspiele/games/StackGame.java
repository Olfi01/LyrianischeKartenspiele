package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import de.lyriaserver.kartenspiele.players.Player;

public interface StackGame<G extends IGame<G, P>, P extends Player> extends IGame<G, P> {
    Stack getStack();
}
