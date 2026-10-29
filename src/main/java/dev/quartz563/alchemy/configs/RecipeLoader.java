package dev.quartz563.alchemy.configs;

import dev.quartz563.alchemy.AlchemyPlugin;
import dev.quartz563.alchemy.brewing.BrewingAction;
import dev.quartz563.alchemy.brewing.BrewingStep;
import dev.quartz563.alchemy.brewing.ProcessingState;
import dev.quartz563.alchemy.brewing.TemperatureState;
import dev.quartz563.alchemy.brewing.potion.PotionIngredient;
import dev.quartz563.alchemy.brewing.potion.PotionRecipe;
import dev.quartz563.alchemy.brewing.potion.RecipeEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.util.*;

/**
 * Loads {@link PotionRecipe} definitions from recipes.yml.
 */
//todo: reformat this using the SimpleConfig system, perhaps? I think recipes being a per-file configuration could also be
    //quite useful for ease of use and visibility. Let's delve into the alchemist data file and see what we can refactor...
public final class RecipeLoader {

    private static final String RECIPES_FILE = "recipes.yml";

    private RecipeLoader() {}

    /**
     * Reads all recipes from recipes.yml in the plugin data folder.
     * Logs a warning and skips any recipe that fails to parse.
     */
    public static Collection<PotionRecipe> load() {
        final File file = new File(AlchemyPlugin.getInstance().getDataFolder(), RECIPES_FILE);

        if (!file.exists()) {
            Common.warning("recipes.yml not found — no recipes loaded.");
            return List.of();
        }

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        final ConfigurationSection recipesSection = config.getConfigurationSection("recipes");

        if (recipesSection == null) {
            Common.warning("recipes.yml has no 'recipes' section — no recipes loaded.");
            return List.of();
        }

        final List<PotionRecipe> loaded = new ArrayList<>();

        for (final String key : recipesSection.getKeys(false)) {
            try {
                final PotionRecipe recipe = parseRecipe(key, Objects.requireNonNull(recipesSection.getConfigurationSection(key)));
                loaded.add(recipe);
            } catch (final Exception ex) {
                Common.warning("Failed to load recipe '" + key + "': " + ex.getMessage());
            }
        }
        Common.log("Loaded " + loaded.size() + " recipes.");
        return loaded;
    }

    private static PotionRecipe parseRecipe(String key, ConfigurationSection section) {
        final String name = section.getString("name", key);
        final String tempStr = section.getString("standard_temp", "WARM");
        final TemperatureState standardTemp = TemperatureState.valueOf(tempStr.toUpperCase());
        final List<BrewingStep> steps = parseSteps(section);
        final List<RecipeEffect> effects = parseEffects(section);
        return new PotionRecipe(key, name, steps, effects, standardTemp);
    }

    private static List<BrewingStep> parseSteps(ConfigurationSection section) {
        final List<BrewingStep> steps = new ArrayList<>();

        for (final Object entry : section.getList("steps", List.of())) {
            if (!(entry instanceof Map<?, ?> rawMap))
                throw new IllegalArgumentException("Step entry is not a map");

            @SuppressWarnings("unchecked")
            final Map<String, Object> map = (Map<String, Object>) rawMap;

            final String type = (String) map.get("type");
            if (type == null)
                throw new IllegalArgumentException("Step entry is missing 'type'");

            if (type.equalsIgnoreCase("INGREDIENT")) {
                final String itemName = (String) map.get("item");
                if (itemName == null)
                    throw new IllegalArgumentException("INGREDIENT step is missing 'item'");

                final Material material = Material.matchMaterial(itemName);
                if (material == null)
                    throw new IllegalArgumentException("Unknown ingredient: " + itemName);

                final String stateStr = (String) map.get("state");
                final ProcessingState state = (stateStr != null) ? ProcessingState.valueOf(stateStr.toUpperCase())
                        : ProcessingState.NONE;

                final String tempStr = (String) map.getOrDefault("temperature", null);
                final TemperatureState  temp = (tempStr != null) ? TemperatureState.valueOf(tempStr) : null;

                final int amount = toInt(map.getOrDefault("amount", 1));
                for (int i = 0; i < amount; i++)
                    steps.add(new BrewingStep.IngredientStep(new PotionIngredient(material, state, temp)));

            } else if(type.equalsIgnoreCase("ACTION")) {
                final BrewingAction action;
                final String actionString = (String) map.get("action");
                try {
                    action = BrewingAction.valueOf(actionString.toUpperCase());
                } catch (final IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Unknown action step: " + actionString);
                }
                steps.add(new BrewingStep.ActionStep(action));
            } else {
                throw new IllegalArgumentException("Unknown step type: " + type);
            }
        }

        return steps;
    }

    private static List<RecipeEffect> parseEffects(ConfigurationSection section) {
        final List<RecipeEffect> effects = new ArrayList<>();
        final ConfigurationSection effectsSection = section.getConfigurationSection("effects");

        if (effectsSection == null)
            return effects;

        for (final Object entry : effectsSection.getList("vanilla_effects", List.of())) {
            if (!(entry instanceof Map<?, ?> rawMap))
                throw new IllegalArgumentException("Effect entry is not a map");

            @SuppressWarnings("unchecked")
            final Map<String, Object> map = (Map<String, Object>) rawMap;

            final String typeName = (String) map.get("type");
            final int power    = toInt(map.getOrDefault("power", 1));
            final int duration = toInt(map.getOrDefault("duration", 600));

            final PotionEffectType effectType = Registry.EFFECT.get(NamespacedKey.minecraft(typeName.toLowerCase()));
            if (effectType == null)
                throw new IllegalArgumentException("Unknown potion effect type: " + typeName);

            effects.add(new RecipeEffect(effectType, power, duration));
        }

        return effects;
    }

    private static int toInt(Object value) {
        if (value instanceof Number number)
            return number.intValue();
        return Integer.parseInt(value.toString());
    }

}
