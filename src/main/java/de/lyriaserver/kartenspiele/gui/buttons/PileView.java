package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Updatable;
import de.lyriaserver.kartenspiele.classes.cardgames.Pile;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import de.lyriaserver.kartenspiele.util.IconHelper;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class PileView extends ItemButton<GameScreen<?, ?>> implements Updatable {
    private final Pile pile;

    public PileView(Pile pile) {
        this.pile = pile;
        update();
    }

    public void update() {
        setIcon(IconHelper.getIcon(pile.getTopCard()));
    }
}
