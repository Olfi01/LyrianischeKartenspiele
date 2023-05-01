package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.games.MoneyGame;
import de.lyriaserver.kartenspiele.gui.buttons.PlayingForMoneyButton;
import de.lyriaserver.kartenspiele.players.Player;

public class MoneyLobbyScreen<G extends MoneyGame<G, P>, P extends Player> extends LobbyScreen<G, P> {
    private final PlayingForMoneyButton playingForMoneyButton;
    public MoneyLobbyScreen(G game, P player) {
        super(game, player);
        this.playingForMoneyButton = new PlayingForMoneyButton(game);
        setButton(4, playingForMoneyButton);
    }

    @Override
    public void update() {
        super.update();
        if (playingForMoneyButton != null) playingForMoneyButton.update();
    }
}
