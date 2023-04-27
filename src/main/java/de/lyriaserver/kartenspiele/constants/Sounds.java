package de.lyriaserver.kartenspiele.constants;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.intellij.lang.annotations.Subst;

public class Sounds {
    public static final Sound CARD_PLAYED = sound("item.book.page_turn", 1.3f);
    public static final Sound GENERIC_ERROR = sound("block.note_block.basedrum", 0.6f);
    public static final Sound CARD_DRAW = sound("ui.loom.take_result");
    public static final Sound GAME_STARTING = sound("entity.experience_orb.pickup");
    public static final Sound GAME_WON = sound("entity.player.levelup");
    public static final Sound GAME_LOST = sound("block.note_block.bass", 0.5f);
    public static final Sound GAME_DRAW = sound("ui.toast.out", 1.7f);
    public static final Sound TURN_SKIP = sound("item.book.put", 1.5f);
    public static final Sound REVERSE_DIRECTION = sound("ui.toast.in", 2f);

    private static Sound sound(String key) {
        return sound(key, 1);
    }
    private static Sound sound(String key, float pitch) {
        return sound(key, 1, pitch);
    }
    private static Sound sound(@Subst("some.sound.name") String key, float volume, float pitch) {
        return Sound.sound(Key.key(key), Sound.Source.BLOCK, volume, pitch);
    }
}
