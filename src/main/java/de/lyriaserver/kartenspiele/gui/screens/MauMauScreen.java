package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import de.lyriaserver.kartenspiele.games.MauMau;
import de.lyriaserver.kartenspiele.gui.buttons.*;
import de.lyriaserver.kartenspiele.players.CardGamePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MauMauScreen extends ScrollingScreen<MauMau, CardGamePlayer> {
    private static final int PAGE_SIZE = 24;
    private final TurnIndicator<MauMau> turnIndicator;
    private final PileView pileButton;
    private final List<OpponentInfo<MauMauScreen, CardGamePlayer>> opponents = new ArrayList<>();
    public MauMauScreen(MauMau game, @Nullable CardGamePlayer player) {
        super(game, 54, player, 1, -1);

        turnIndicator = new TurnIndicator<>(game, player);
        for (int slot = 0; slot < 9; slot++) {
            setButton(slot, turnIndicator);
        }

        setButton(12, new DrawButton<>(game, player));
        pileButton = new PileView(game.getPile());
        setButton(14, pileButton);

        Iterator<CardGamePlayer> iterator = game.getPlayers().iterator();
        for (int slot = 18; slot < 27; slot++) {
            if (!iterator.hasNext()) break;
            CardGamePlayer opponent = iterator.next();
            if (opponent == player) {
                if (!iterator.hasNext()) break;
                opponent = iterator.next();
            }
            OpponentInfo<MauMauScreen, CardGamePlayer> opponentInfo = new OpponentInfo<>(game, opponent);
            opponents.add(opponentInfo);
            setButton(slot, opponentInfo);
        }

        updateView();

        setButton(35, scrollUpButton);
        setButton(44, new CancelGameButton(game));
        setButton(53, scrollDownButton);
    }

    @Override
    public void update() {
        turnIndicator.update();
        pileButton.update();
        opponents.forEach(OpponentInfo::update);
        updateView();
    }

    @Override
    public void updateView() {
        if (player == null) return;
        Iterator<Card> cards = player.getCards().listIterator(scrollingOffset * PAGE_SIZE);
        for (int i = 27; i < 54; i++) {
            if (i == 35 || i == 44 || i == 53) continue;
            if (cards.hasNext())
                setButton(i, new UseCardButton(cards.next(), game, player));
            else
                unsetButton(i);
        }
    }

    @Override
    public int getMaxOffset() {
        if (player == null) return 0;
        return player.getCardAmount() / PAGE_SIZE;
    }
}
