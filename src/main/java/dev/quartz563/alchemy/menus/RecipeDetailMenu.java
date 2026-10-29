package dev.quartz563.alchemy.menus;

import dev.quartz563.alchemy.brewing.BrewingStep;
import dev.quartz563.alchemy.brewing.ProcessingState;
import dev.quartz563.alchemy.brewing.TemperatureState;
import dev.quartz563.alchemy.brewing.potion.PotionIngredient;
import dev.quartz563.alchemy.brewing.potion.PotionRecipe;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailMenu extends Menu {

    private final PotionRecipe recipe;
    private List<ItemStack> recipeList = new ArrayList<>();

    public RecipeDetailMenu(PotionRecipe recipe) {
        setTitle(recipe.getName() + " Recipe");
        setSize(9 * 3);
        this.recipe = recipe;
        recipeList = constructRecipeList();
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (slot >= recipeList.size()) {
            return NO_ITEM;
        }
        return recipeList.get(slot);
    }

    private List<ItemStack> constructRecipeList() {
        List<ItemStack> flatSteps = new ArrayList<>();
        TemperatureState currentTemp = recipe.getStandardTemperature();
        for (BrewingStep step : recipe.getSteps()) {
            if (step instanceof BrewingStep.IngredientStep ingredientStep) {
                if (ingredientStep.ingredient().temperature() != null && ingredientStep.ingredient().temperature() != currentTemp) {
                    //temp change
                    TemperatureState temp = ingredientStep.ingredient().temperature();
                    if (temp.isHigherTemp(currentTemp)) {
                        flatSteps.add(ItemCreator.of(CompMaterial.FIRE_CHARGE).name("Heat up to " + temp.name()).make());
                    } else {
                        flatSteps.add(ItemCreator.of(CompMaterial.SNOWBALL).name("Cool to " + temp.name()).make());
                    }
                    currentTemp = temp;
                }
                PotionIngredient ingredient = ingredientStep.ingredient();
                final String stateLabel = ingredient.state() != ProcessingState.NONE
                        ? ingredient.state().toString()
                        : "Unprocessed";
                flatSteps.add(ItemCreator.of(CompMaterial.fromMaterial(ingredient.material()))
                        .name(ItemUtil.bountifyCapitalized(ingredient.material().name()))
                        .lore("State: " + stateLabel)
                        .make());
            }
            if (step instanceof BrewingStep.ActionStep actionStep) {
                switch (actionStep.action()) {
                    case STIR_CLOCKWISE -> flatSteps.add(ItemCreator.of(CompMaterial.STICK).name("Stir Clockwise").glow(true).make());
                    case STIR_COUNTERCLOCKWISE -> flatSteps.add(ItemCreator.of(CompMaterial.STICK).name("Stir Counter-Clockwise").make());
                }
            }
        }
        return flatSteps;
    }

}
