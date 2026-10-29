package dev.quartz563.alchemy.brewing.potion;

import dev.quartz563.alchemy.brewing.BrewingStep;
import dev.quartz563.alchemy.brewing.TemperatureState;

import java.util.List;

/**
 * Defines a complete potion recipe: its name, the ordered sequence of brewing steps,
 * and the vanilla effects applied on consumption.
 * <p>
 * Steps are stored as a flat list of {@link BrewingStep}s — ingredient additions and
 * actions interleaved in the order a player must perform them.
 * Amounts greater than one are expanded into repeated IngredientSteps during loading.
 */
public final class PotionRecipe {

    private final String key;
    private final String name;
    private final List<BrewingStep> steps;
    private final List<RecipeEffect> effects;
    private final TemperatureState standardTemp;

    public PotionRecipe(String key, String name, List<BrewingStep> steps, List<RecipeEffect> effects, TemperatureState standard) {
        this.key = key;
        this.name = name;
        this.steps = List.copyOf(steps);
        this.effects = List.copyOf(effects);
        this.standardTemp = standard;
    }

    /** The YAML key identifying this recipe, e.g. "swiftness_potion". */
    public String getKey() {
        return key;
    }

    /** The display name of this recipe, e.g. "Potion of Swiftness". */
    public String getName() {
        return name;
    }

    /** The ordered sequence of steps required to brew this potion. */
    public List<BrewingStep> getSteps() {
        return steps;
    }

    /** The vanilla effects applied to the player when this potion is consumed. */
    public List<RecipeEffect> getEffects() {
        return effects;
    }

    /** The temperature which the potion begins brewing at. Defaults to WARM */
    public TemperatureState getStandardTemperature() {
        return standardTemp;
    }
}
