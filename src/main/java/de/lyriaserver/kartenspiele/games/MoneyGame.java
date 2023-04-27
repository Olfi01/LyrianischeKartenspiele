package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.players.Player;

public interface MoneyGame<G extends IGame<G, P>, P extends Player> extends IGame<G, P> {
    boolean isPlayingForMoney();
    void setPlayingForMoney(boolean playingForMoney);
}
