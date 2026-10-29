package dev.quartz563.alchemy.brewing.potion;

import org.bukkit.potion.PotionEffectType;

public record RecipeEffect(PotionEffectType type, int power, int duration) {

    public RecipeEffect {
        if (power < 0)
            throw new IllegalArgumentException("Effect power cannot be negative, got: " + power);
        if (duration <= 0)
            throw new IllegalArgumentException("Effect duration must be positive, got: " + duration);
    }
}
