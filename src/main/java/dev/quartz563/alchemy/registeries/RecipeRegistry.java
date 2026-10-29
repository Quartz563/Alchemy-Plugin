package dev.quartz563.alchemy.registeries;

import dev.quartz563.alchemy.configs.RecipeLoader;
import dev.quartz563.alchemy.brewing.potion.PotionRecipe;

import java.util.*;

/**
 * Holds all loaded potion recipes and provides lookup by key.
 * Populated at plugin startup via {@link RecipeLoader}.
 */
public final class RecipeRegistry {

    private static final Map<String, PotionRecipe> RECIPES = new LinkedHashMap<>();

    private RecipeRegistry() {}

    /** Clears and reloads all recipes from the disk. */
    public static void load() {
        RECIPES.clear();
        for (final PotionRecipe recipe : RecipeLoader.load())
            RECIPES.put(recipe.getKey(), recipe);
    }

    /** Returns the recipe with the given key, if one exists. */
    public static Optional<PotionRecipe> get(String key) {
        return Optional.ofNullable(RECIPES.get(key));
    }

    /** Returns all registered recipes. */
    public static Collection<PotionRecipe> getAll() {
        return Collections.unmodifiableCollection(RECIPES.values());
    }

    public static Collection<String> getAllRecipeKeys() { return Collections.unmodifiableCollection(RECIPES.keySet()); }

    /** Returns true if any recipes are loaded. */
    public static boolean isEmpty() {
        return RECIPES.isEmpty();
    }
}
