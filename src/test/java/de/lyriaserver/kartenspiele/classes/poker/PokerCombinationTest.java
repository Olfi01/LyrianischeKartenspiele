package de.lyriaserver.kartenspiele.classes.poker;

import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PokerCombinationTest {

    @Test
    void shouldCompareByValue() {
        PokerCombination royalFlush = new PokerCombination(PokerCombination.Value.RoyalFlush, new Card[0]);
        PokerCombination straightFlush = new PokerCombination(PokerCombination.Value.StraightFlush, new Card[0]);
        PokerCombination fourOfAKind = new PokerCombination(PokerCombination.Value.FourOfAKind, new Card[0]);
        PokerCombination fullHouse = new PokerCombination(PokerCombination.Value.FullHouse, new Card[0]);
        PokerCombination flush = new PokerCombination(PokerCombination.Value.Flush, new Card[0]);
        PokerCombination straight = new PokerCombination(PokerCombination.Value.Straight, new Card[0]);
        PokerCombination threeOfAKind = new PokerCombination(PokerCombination.Value.ThreeOfAKind, new Card[0]);
        PokerCombination twoPair = new PokerCombination(PokerCombination.Value.TwoPair, new Card[0]);
        PokerCombination pair = new PokerCombination(PokerCombination.Value.Pair, new Card[0]);
        PokerCombination highCard = new PokerCombination(PokerCombination.Value.HighCard, new Card[0]);
        PokerCombination[] combinations = new PokerCombination[] {
                royalFlush, straightFlush, fourOfAKind, fullHouse, flush, straight, threeOfAKind, twoPair, pair, highCard
        };
        for (int i = 0; i < combinations.length; i++) {
            for (int j = 0; j < combinations.length; j++) {
                if (i != j) {
                    int compare = combinations[i].compareTo(combinations[j]);
                    if (i < j) {
                        assertTrue(compare > 0, String.format("%s should be greater than %s",
                                combinations[i].getValue(), combinations[j].getValue()));
                    }
                    else {
                        assertTrue(compare < 0);
                    }
                }
            }
        }
    }

    @Test
    void shouldCompareByDeterminingCard() {
        Card sevenOfHearts = new Card(Card.Color.Hearts, Card.Value.Seven);
        Card sevenOfClubs = new Card(Card.Color.Clubs, Card.Value.Seven);
        Card sevenOfDiamonds = new Card(Card.Color.Diamonds, Card.Value.Seven);
        Card aceOfClubs = new Card(Card.Color.Clubs, Card.Value.Ace);
        Card aceOfSpades = new Card(Card.Color.Spades, Card.Value.Ace);
        Card eightOfHearts = new Card(Card.Color.Hearts, Card.Value.Eight);
        Card eightOfClubs = new Card(Card.Color.Clubs, Card.Value.Eight);
        Card eightOfDiamonds = new Card(Card.Color.Diamonds, Card.Value.Eight);
        Card kingOfClubs = new Card(Card.Color.Clubs, Card.Value.King);
        Card kingOfSpades = new Card(Card.Color.Spades, Card.Value.King);
        PokerCombination threeSevenTwoAce = new PokerCombination(PokerCombination.Value.FullHouse, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, aceOfClubs, aceOfSpades
        });
        PokerCombination threeEightTwoKing = new PokerCombination(PokerCombination.Value.FullHouse, new Card[]{
                eightOfClubs, eightOfHearts, eightOfDiamonds, kingOfClubs, kingOfSpades
        });
        assertTrue(threeSevenTwoAce.compareTo(threeEightTwoKing) < 0);
        assertTrue(threeEightTwoKing.compareTo(threeSevenTwoAce) > 0);
    }

    @Test
    void shouldCompareByKicker() {
        Card sevenOfHearts = new Card(Card.Color.Hearts, Card.Value.Seven);
        Card sevenOfClubs = new Card(Card.Color.Clubs, Card.Value.Seven);
        Card sevenOfDiamonds = new Card(Card.Color.Diamonds, Card.Value.Seven);
        Card sevenOfSpades = new Card(Card.Color.Spades, Card.Value.Seven);
        Card aceOfClubs = new Card(Card.Color.Clubs, Card.Value.Ace);
        Card threeOfSpades = new Card(Card.Color.Spades, Card.Value.Three);
        Card twoOfDiamonds = new Card(Card.Color.Diamonds, Card.Value.Two);
        PokerCombination fourOfAKindAndAce = new PokerCombination(PokerCombination.Value.FourOfAKind, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, sevenOfSpades, aceOfClubs
        });
        PokerCombination fourOfAKindAndTwo = new PokerCombination(PokerCombination.Value.FourOfAKind, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, sevenOfSpades, twoOfDiamonds
        });
        assertTrue(fourOfAKindAndAce.compareTo(fourOfAKindAndTwo) > 0);
        assertTrue(fourOfAKindAndTwo.compareTo(fourOfAKindAndAce) < 0);
        PokerCombination threeOfAKindAceThree = new PokerCombination(PokerCombination.Value.ThreeOfAKind, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, aceOfClubs, threeOfSpades
        });
        PokerCombination threeOfAKindAceTwo = new PokerCombination(PokerCombination.Value.ThreeOfAKind, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, aceOfClubs, twoOfDiamonds
        });
        assertTrue(threeOfAKindAceThree.compareTo(threeOfAKindAceTwo) > 0);
        assertTrue(threeOfAKindAceTwo.compareTo(threeOfAKindAceThree) < 0);
    }

    @Test
    void compareEqual() {
        Card sevenOfHearts = new Card(Card.Color.Hearts, Card.Value.Seven);
        Card sevenOfClubs = new Card(Card.Color.Clubs, Card.Value.Seven);
        Card sevenOfDiamonds = new Card(Card.Color.Diamonds, Card.Value.Seven);
        Card sevenOfSpades = new Card(Card.Color.Spades, Card.Value.Seven);
        Card aceOfClubs = new Card(Card.Color.Clubs, Card.Value.Ace);
        Card aceOfSpades = new Card(Card.Color.Spades, Card.Value.Ace);
        PokerCombination fourOfAKindAndAce = new PokerCombination(PokerCombination.Value.FourOfAKind, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, sevenOfSpades, aceOfClubs
        });
        PokerCombination alsoFourOfAKindAndAce = new PokerCombination(PokerCombination.Value.FourOfAKind, new Card[]{
                sevenOfClubs, sevenOfDiamonds, sevenOfHearts, sevenOfSpades, aceOfSpades
        });
        assertEquals(0, fourOfAKindAndAce.compareTo(alsoFourOfAKindAndAce));
    }

    @ParameterizedTest
    @MethodSource
    void of(List<Card> hand, Card[] center, PokerCombination.Value value) {
        assertEquals(value, PokerCombination.of(hand, center).getValue());
    }

    public static Stream<Arguments> of() {
        Card aceOfHearts = new Card(Card.Color.Hearts, Card.Value.Ace);
        Card kingOfHearts = new Card(Card.Color.Hearts, Card.Value.King);
        Card queenOfHearts = new Card(Card.Color.Hearts, Card.Value.Queen);
        Card jackOfHearts = new Card(Card.Color.Hearts, Card.Value.Jack);
        Card tenOfHearts = new Card(Card.Color.Hearts, Card.Value.Ten);
        Card eightOfClubs = new Card(Card.Color.Clubs, Card.Value.Eight);
        Card sevenOfClubs = new Card(Card.Color.Clubs, Card.Value.Seven);
        Card sixOfClubs = new Card(Card.Color.Clubs, Card.Value.Six);
        Card sixOfHearts = new Card(Card.Color.Hearts, Card.Value.Six);
        Card fiveOfClubs = new Card(Card.Color.Clubs, Card.Value.Five);
        Card fourOfClubs = new Card(Card.Color.Clubs, Card.Value.Four);
        Card sevenOfSpades = new Card(Card.Color.Spades, Card.Value.Seven);
        Card sevenOfDiamonds = new Card(Card.Color.Diamonds, Card.Value.Seven);
        Card sevenOfHearts = new Card(Card.Color.Hearts, Card.Value.Seven);
        return Stream.of(
                Arguments.of(
                        List.of(aceOfHearts, kingOfHearts),
                        new Card[] {queenOfHearts, jackOfHearts, tenOfHearts, eightOfClubs, sevenOfClubs},
                        PokerCombination.Value.RoyalFlush),
                Arguments.of(
                        List.of(eightOfClubs, sevenOfClubs),
                        new Card[] {sixOfClubs, fiveOfClubs, fourOfClubs, sevenOfSpades, sevenOfDiamonds},
                        PokerCombination.Value.StraightFlush
                ),
                Arguments.of(
                        List.of(sevenOfSpades, sevenOfDiamonds),
                        new Card[] {sevenOfClubs, sevenOfHearts, fourOfClubs, fiveOfClubs, aceOfHearts},
                        PokerCombination.Value.FourOfAKind
                ),
                Arguments.of(
                        List.of(sevenOfSpades, sixOfClubs),
                        new Card[] {sevenOfClubs, sevenOfDiamonds, sixOfHearts, fiveOfClubs, jackOfHearts},
                        PokerCombination.Value.FullHouse
                ),
                Arguments.of(
                        List.of(aceOfHearts, kingOfHearts),
                        new Card[] {queenOfHearts, jackOfHearts, sixOfHearts, eightOfClubs, sevenOfClubs},
                        PokerCombination.Value.Flush
                ),
                Arguments.of(
                        List.of(fourOfClubs, fiveOfClubs),
                        new Card[] {sixOfHearts, sevenOfHearts, eightOfClubs, jackOfHearts, queenOfHearts},
                        PokerCombination.Value.Straight
                ),
                Arguments.of(
                        List.of(sevenOfClubs, queenOfHearts),
                        new Card[] {sevenOfHearts, sevenOfSpades, sixOfClubs, jackOfHearts, kingOfHearts},
                        PokerCombination.Value.ThreeOfAKind
                ),
                Arguments.of(
                        List.of(sevenOfClubs, sevenOfDiamonds),
                        new Card[] {sixOfHearts, sixOfClubs, queenOfHearts, kingOfHearts, jackOfHearts},
                        PokerCombination.Value.TwoPair
                ),
                Arguments.of(
                        List.of(sevenOfClubs, sevenOfDiamonds),
                        new Card[] {fourOfClubs, fiveOfClubs, queenOfHearts, jackOfHearts, kingOfHearts},
                        PokerCombination.Value.Pair
                ),
                Arguments.of(
                        List.of(fourOfClubs, kingOfHearts),
                        new Card[] {fiveOfClubs, sevenOfDiamonds, queenOfHearts, jackOfHearts, aceOfHearts},
                        PokerCombination.Value.HighCard
                )
        );
    }
}