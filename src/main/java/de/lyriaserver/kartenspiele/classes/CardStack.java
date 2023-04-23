package de.lyriaserver.kartenspiele.classes;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CardStack {
    protected final List<Card> cards = new ArrayList<>();

    public CardStack(List<Card> deck) {
        cards.addAll(deck);
    }

    @Unmodifiable
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards, LyrianischeKartenspiele.RANDOM);
    }
}
