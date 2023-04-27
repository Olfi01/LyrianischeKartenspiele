package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.classes.cardgames.Pile;
import de.lyriaserver.kartenspiele.classes.cardgames.Stack;
import de.lyriaserver.kartenspiele.players.CardGamePlayer;
import de.lyriaserver.kartenspiele.players.Player;

public interface StackPileGame<G extends IGame<G, P>, P extends Player> extends IGame<G, P> {
    Stack getStack();
    Pile getPile();
    boolean canPlayerDraw(CardGamePlayer player);
}
