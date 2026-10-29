package dev.quartz563.alchemy.brewing.potion;

import dev.quartz563.alchemy.brewing.ProcessingState;
import dev.quartz563.alchemy.brewing.TemperatureState;
import org.bukkit.Material;


public record PotionIngredient(Material material, ProcessingState state, TemperatureState temperature) {
}
