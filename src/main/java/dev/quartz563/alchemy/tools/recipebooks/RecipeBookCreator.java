package dev.quartz563.alchemy.tools.recipebooks;

import dev.quartz563.alchemy.brewing.potion.PotionRecipe;

public class RecipeBookCreator {

    public static RecipeBook of(PotionRecipe recipe) {
        return new RecipeBook(recipe);
    }

    public static RecipeBook of(PotionRecipe recipe, String name) {
        return new RecipeBook(recipe, name);
    }

    public static RecipeBook of(PotionRecipe recipe, String name, Boolean glow) {
        return new RecipeBook(recipe, name, glow);
    }

}
