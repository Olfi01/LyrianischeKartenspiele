package de.lyriaserver.kartenspiele.classes.cardgames;

public record Card(Color color, Value value) {
    public String getItemLore() {
        return String.format("Gegenstand: Karte %s %s", color.getName(), value.getName());
    }

    public String getName() {
        return String.format("%s %s", color.getName(), value.getName());
    }

    public enum Color {
        Hearts("Herz"),     // Herz
        Spades("Pik"),      // Pik
        Clubs("Kreuz"),     // Kreuz
        Diamonds("Karo");   // Karo
        private final String name;

        Color(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Value {
        Two(2, "2"),
        Three(3, "3"),
        Four(4, "4"),
        Five(5, "5"),
        Six(6, "6"),
        Seven(7, "7"),
        Eight(8, "8"),
        Nine(9, "9"),
        Ten(10, "10"),
        Jack(11, "Bube"),
        Queen(12, "Dame"),
        King(13, "Koenig"),
        Ace(14, "Ass");
        private final int value;
        private final String name;

        Value(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
