package de.lyriaserver.kartenspiele.classes.poker;

import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PokerCombination implements Comparable<PokerCombination> {
    private final Card[] cards;

    private final Value value;

    private PokerCombination(Value value, Card[] cards) {
        this.value = value;
        this.cards = cards;
    }

    @Override
    public int compareTo(@NotNull PokerCombination combination) {
        int valueDiff = value.value - combination.getValue().value;
        if (valueDiff != 0) return valueDiff;
        Card.ValueComparator cardValueComparator = new Card.ValueComparator();
        Iterator<Card> otherSortedCards = Arrays.stream(combination.cards)
                .sorted(Collections.reverseOrder(cardValueComparator)).iterator();
        Iterator<Card> thisSortedCards = Arrays.stream(cards)
                .sorted(Collections.reverseOrder(cardValueComparator)).iterator();
        while (thisSortedCards.hasNext() && otherSortedCards.hasNext()) {
            int cardDiff = cardValueComparator.compare(thisSortedCards.next(), otherSortedCards.next());
            if (cardDiff != 0) return cardDiff;
        }
        return 0;
    }

    public static PokerCombination of(List<Card> hand, Card[] center) {
        List<Card> cards = new ArrayList<>(hand);
        cards.addAll(List.of(center));
        Card.ValueComparator cardValueComparator = new Card.ValueComparator();

        for (Card ace : cards.stream().filter(card -> card.value() == Card.Value.Ace).toList()) {
            Optional<PokerCombination> royalFlush = findRoyalFlush(ace, cards.stream().filter(card -> card != ace).toList());
            if (royalFlush.isPresent()) return royalFlush.get();
        }

        Map<Card.Color, List<Card>> colors = cards.stream().collect(Collectors.groupingBy(Card::color));
        for (List<Card> colorCards : colors.values().stream().filter(list -> list.size() >= 5).toList()) {
            Optional<PokerCombination> straightFlush = findStraightFlush(colorCards);
            if (straightFlush.isPresent()) return straightFlush.get();
        }

        Map<Card.Value, List<Card>> values = cards.stream().collect(Collectors.groupingBy(Card::value));
        for (List<Card> valueCards : values.values().stream().filter(list -> list.size() >= 3)
                .sorted(Comparator.comparingInt((List<Card> list) -> list.size())
                        .thenComparing(list -> list.get(0), cardValueComparator)).toList()) {
            if (valueCards.size() == 4) {
                Card[] cardsArray = valueCards.toArray(new Card[5]);
                cardsArray[4] = cards.stream().filter(card -> !Arrays.asList(cardsArray).contains(card))
                        .max(cardValueComparator).orElseThrow();
                return new PokerCombination(Value.FourOfAKind, cardsArray);
            }

            if (valueCards.size() == 3) {
                Optional<List<Card>> pair = values.values().stream().filter(list -> list.size() >= 2)
                        .max(Comparator.comparing(list -> list.get(0), cardValueComparator));
                if (pair.isPresent()) {
                    Card[] cardArray = valueCards.toArray(new Card[5]);
                    cardArray[3] = pair.get().get(0);
                    cardArray[4] = pair.get().get(1);
                    return new PokerCombination(Value.FullHouse, cardArray);
                }
            }
        }

        for (List<Card> colorCards : colors.values().stream().filter(list -> list.size() >= 5).toList()) {
            colorCards.sort(cardValueComparator.reversed());
            Card[] cardsArray = new Card[5];
            for (int i = 0; i < 5; i++) {
                cardsArray[i] = colorCards.get(i);
            }
            return new PokerCombination(Value.Flush, cardsArray);
        }

        Optional<PokerCombination> straight = findStraight(values);
        if (straight.isPresent()) return straight.get();

        for (List<Card> valueCards : values.values().stream()
                .filter(list -> list.size() <= 3 && list.size() > 0)
                .sorted(Comparator.comparingInt((List<Card> list) -> list.size())
                        .thenComparing(list -> list.get(0), cardValueComparator))
                .toList()) {
            if (valueCards.size() == 3) {
                Card[] cardsArray = cards.toArray(new Card[5]);
                List<Card> otherCards = cards.stream().filter(card -> !valueCards.contains(card))
                        .sorted(cardValueComparator.reversed()).toList();
                cardsArray[3] = otherCards.get(0);
                cardsArray[4] = otherCards.get(1);
                return new PokerCombination(Value.ThreeOfAKind, cardsArray);
            }
            if (valueCards.size() == 2) {
                Optional<List<Card>> otherPair = values.values().stream().filter(list -> list.size() == 2 && list != valueCards)
                        .max(Comparator.comparing(list -> list.get(0), cardValueComparator));
                Card[] cardsArray = valueCards.toArray(new Card[5]);
                if (otherPair.isPresent()) {
                    cardsArray[2] = otherPair.get().get(0);
                    cardsArray[3] = otherPair.get().get(1);
                    cardsArray[4] = cards.stream()
                            .filter(card -> !valueCards.contains(card) && !otherPair.get().contains(card))
                            .max(cardValueComparator).orElseThrow();
                    return new PokerCombination(Value.TwoPair, cardsArray);
                }
                else {
                    List<Card> otherCards = cards.stream().filter(card -> !valueCards.contains(card))
                            .sorted(cardValueComparator.reversed()).toList();
                    cardsArray[2] = otherCards.get(0);
                    cardsArray[3] = otherCards.get(1);
                    cardsArray[4] = otherCards.get(2);
                    return new PokerCombination(Value.Pair, cardsArray);
                }
            }
        }

        cards.sort(cardValueComparator.reversed());
        Card[] cardsArray = new Card[5];
        for (int i = 0; i < cardsArray.length; i++) {
            cardsArray[i] = cards.get(i);
        }
        return new PokerCombination(Value.HighCard, cardsArray);
    }

    private static Optional<PokerCombination> findStraightFlush(List<Card> colorCards) {
        if (colorCards.size() < 5 || colorCards.stream().anyMatch(card -> colorCards.get(0).color() != card.color()))
            return Optional.empty();
        return findStraight(colorCards.stream().collect(Collectors.groupingBy(Card::value)));
    }

    @NotNull
    private static Optional<PokerCombination> findStraight(Map<Card.Value, List<Card>> values) {
        int i = 0;
        Iterator<List<Card>> iterator =
                values.values().stream().filter(list -> list.size() > 0)
                        .sorted(Comparator.comparing((List<Card> list) -> list.get(0),
                                new Card.ValueComparator()).reversed()).iterator();
        while (i < values.size() - 4) {
            i++;
            if (!iterator.hasNext()) break;
            List<Card> listOne = iterator.next();
            Card one = listOne.get(0);
            if (one.value().getValue() < 6) break;
            Optional<Card> two = values.values().stream()
                    .filter(list -> list.size() > 0 && list.get(0).value().getValue() == one.value().getValue() - 1)
                    .findFirst().map(list -> list.get(0));
            if (two.isEmpty()) continue;
            Optional<Card> three = values.values().stream()
                    .filter(list -> list.size() > 0 && list.get(0).value().getValue() == one.value().getValue() - 2)
                    .findFirst().map(list -> list.get(0));
            if (three.isEmpty()) continue;
            Optional<Card> four = values.values().stream()
                    .filter(list -> list.size() > 0 && list.get(0).value().getValue() == one.value().getValue() - 3)
                    .findFirst().map(list -> list.get(0));
            if (four.isEmpty()) continue;
            Optional<Card> five = values.values().stream()
                    .filter(list -> list.size() > 0 && list.get(0).value().getValue() == one.value().getValue() - 4)
                    .findFirst().map(list -> list.get(0));
            if (five.isEmpty()) continue;
            return Optional.of(new PokerCombination(Value.Straight, new Card[]{one, two.get(), three.get(), four.get(), five.get()}));
        }
        return Optional.empty();
    }

    private static Optional<PokerCombination> findRoyalFlush(Card ace, List<Card> cardsWithoutAce) {
        Optional<Card> king = cardsWithoutAce.stream().filter(card -> card.value() == Card.Value.King
                && card.color() == ace.color()).findFirst();
        if (king.isEmpty()) return Optional.empty();
        Optional<Card> queen = cardsWithoutAce.stream().filter(card -> card.value() == Card.Value.Queen
                && card.color() == ace.color()).findFirst();
        if (queen.isEmpty()) return Optional.empty();
        Optional<Card> jack = cardsWithoutAce.stream().filter(card -> card.value() == Card.Value.Jack
                && card.color() == ace.color()).findFirst();
        if (jack.isEmpty()) return Optional.empty();
        Optional<Card> ten = cardsWithoutAce.stream().filter(card -> card.value() == Card.Value.Ten
                && card.color() == ace.color()).findFirst();
        return ten.map(tenCard -> new PokerCombination(Value.RoyalFlush,
                new Card[]{ace, king.get(), queen.get(), jack.get(), tenCard}));
    }

    public Value getValue() {
        return value;
    }

    public Card getHighCard() {
        return Arrays.stream(cards).max(new Card.ValueComparator()).orElseThrow();
    }

    public Card getDeterminingCard() {
        if (value != Value.FourOfAKind && value != Value.FullHouse && value != Value.ThreeOfAKind
                && value != Value.TwoPair && value != Value.Pair && value != Value.HighCard)
            throw new UnsupportedOperationException();
        return cards[0];
    }

    public Card getSecondDeterminingCard() {
        return switch (value) {
            case FullHouse -> cards[3];
            case TwoPair -> cards[2];
            default -> throw new UnsupportedOperationException();
        };
    }

    public enum Value {
        RoyalFlush(10),
        StraightFlush(9),
        FourOfAKind(8),
        FullHouse(7),
        Flush(6),
        Straight(5),
        ThreeOfAKind(4),
        TwoPair(3),
        Pair(2),
        HighCard(1);

        private final int value;

        Value(int value) {
            this.value = value;
        }
    }
}
