package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.buttons.PlayingForMoneyButton;
import de.lyriaserver.kartenspiele.players.PokerPlayer;

public class PokerLobbyScreen extends LobbyScreen<Poker, PokerPlayer> {
    private final PlayingForMoneyButton<PokerLobbyScreen> playingForMoneyButton;
    public PokerLobbyScreen(Poker game, PokerPlayer player) {
        super(game, player);
        this.playingForMoneyButton = new PlayingForMoneyButton<>(game);
        setButton(4, playingForMoneyButton);
    }

    @Override
    public void update() {
        super.update();
        playingForMoneyButton.update();
    }
}
