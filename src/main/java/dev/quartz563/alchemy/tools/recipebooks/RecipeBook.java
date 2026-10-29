package dev.quartz563.alchemy.tools.recipebooks;

import dev.quartz563.alchemy.brewing.potion.PotionRecipe;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.nbt.NBT;

public class RecipeBook {

    private final PotionRecipe recipe;
    private String name;
    private boolean glow;

    public RecipeBook(PotionRecipe recipe){
        this.recipe = recipe;
        this.name = "Potion Recipe Books - " + recipe.getName();
        this.glow = false;
    }

    public RecipeBook(PotionRecipe recipe, String name) {
        this.recipe = recipe;
        this.name = name;
        this.glow = false;
    }

    public RecipeBook(PotionRecipe recipe, String name, boolean glow) {
        this.recipe = recipe;
        this.name = name;
        this.glow = glow;
    }

    public void name(String name){
        this.name = name;
    }

    public void glow(boolean toggle) {
        this.glow = toggle;
    }

    public ItemStack make() {
        ItemStack book = ItemCreator.of(CompMaterial.BOOK)
                .name(name)
                .lore(
                        "&6-------------------------",
                        "&6Looks like a Recipe book",
                        "&6for the " + recipe.getName() + " potion",
                        "&6-------------------------"
                )
                .glow(glow)
                .make();
        NBT.modify(book, t -> {
            t.setString("RecipeBookValue", recipe.getKey());
        });
        return book;
    }
}
