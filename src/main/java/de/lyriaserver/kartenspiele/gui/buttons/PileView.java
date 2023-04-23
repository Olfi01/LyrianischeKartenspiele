package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Pile;
import de.lyriaserver.kartenspiele.gui.GameScreen;
import de.lyriaserver.kartenspiele.util.IconHelper;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class PileView extends ItemButton<GameScreen<?>> {
    private final Pile pile;

    public PileView(Pile pile) {
        this.pile = pile;
        update();
    }

    public void update() {
        setIcon(IconHelper.getIcon(pile.getTopCard()));
    }
}
