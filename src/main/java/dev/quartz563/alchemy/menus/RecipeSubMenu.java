package dev.quartz563.alchemy.menus;

import dev.quartz563.alchemy.brewing.potion.PotionRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeSubMenu extends MenuPagged<PotionRecipe> {

    public RecipeSubMenu(Menu parent, List<PotionRecipe> recipeList) {
        super(parent, recipeList);
        setTitle("Known Recipes");
        setSize(9 * 3);
    }

    @Override
    protected ItemStack convertToItemStack(PotionRecipe item) {
        return ItemCreator.of(CompMaterial.POTION)
                .name("&2" + item.getName())
                .lore(
                        "&6Effects:",
                        item.getEffects().stream().map(Record::toString).collect(Collectors.joining()),
                        "&eClick to transcribe!"
                )
                .make();
    }

    @Override
    protected void onPageClick(Player player, PotionRecipe item, ClickType click) {
        new RecipeDetailMenu(item).displayTo(player);
    }
}
