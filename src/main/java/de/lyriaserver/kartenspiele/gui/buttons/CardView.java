package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Updatable;
import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import de.lyriaserver.kartenspiele.util.IconHelper;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.Optional;
import java.util.function.Supplier;

public class CardView extends ItemButton<GameScreen<?, ?>> implements Updatable {
    private final Supplier<Optional<Card>> supplier;

    public CardView(Supplier<Optional<Card>> supplier) {
        this.supplier = supplier;
        update();
    }

    public void update() {
        Optional<Card> card = supplier.get();
        if (card.isPresent()) {
            setIcon(IconHelper.getIcon(card.get()));
        }
        else {
            setIcon(null);
        }
    }
}
