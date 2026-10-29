package dev.quartz563.alchemy.commands.subcommands;

import dev.quartz563.alchemy.tools.recipebooks.RecipeBookCreator;
import dev.quartz563.alchemy.registeries.RecipeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public class RecipeBookCommand extends SimpleSubCommand {

    public RecipeBookCommand(SimpleCommandGroup parent) {
        super(parent, "recipebook|rb");
        setDescription("Give an alchemy recipe book");
        setUsage("<potion recipe>");
        setMinArguments(1);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player player = getPlayer();
        final String strRecipe = args[0];
        RecipeRegistry.get(strRecipe).ifPresent(recipe -> {
            ItemStack book = RecipeBookCreator.of(recipe, "DEBUG RECIPE " + recipe.getName(), true).make();
            player.getInventory().addItem(book);
        });
    }

    @Override
    protected List<String> tabComplete() {
        if(args.length == 1) {
            return completeLastWord(RecipeRegistry.getAllRecipeKeys());
        }
        return NO_COMPLETE;
    }
}
