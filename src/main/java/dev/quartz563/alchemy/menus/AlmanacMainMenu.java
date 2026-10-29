package dev.quartz563.alchemy.menus;

import dev.quartz563.alchemy.configs.AlchemistData;
import dev.quartz563.alchemy.brewing.potion.PotionRecipe;
import dev.quartz563.alchemy.registeries.RecipeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlmanacMainMenu extends Menu {

    private final List<PotionRecipe> recipesKnown;

    public AlmanacMainMenu(Player player) {
        this.recipesKnown = new ArrayList<>();
        Set<String> recipes = AlchemistData.get(player).getDiscoveredRecipes();
        for(String r : recipes){
            RecipeRegistry.get(r).ifPresent(recipesKnown::add);
        }
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if(slot == 1) {
            return ItemCreator.of(CompMaterial.POTION).name("Known Recipes").glow(true).make();
        }
        return NO_ITEM;
    }

    @Override
    protected void onMenuClick(Player player, int slot, ItemStack clicked) {
        if(slot == 1) {
            new RecipeSubMenu(this, recipesKnown).displayTo(player);
        }
    }
}
