package dev.quartz563.alchemy.structures.impl.cauldron;

import dev.quartz563.alchemy.brewing.BrewingState;
import dev.quartz563.alchemy.configs.AlchemistData;
import dev.quartz563.alchemy.brewing.potion.BrewQualityScorer;
import dev.quartz563.alchemy.brewing.potion.PotionRecipe;
import dev.quartz563.alchemy.brewing.potion.RecipeEffect;
import dev.quartz563.alchemy.registeries.RecipeRegistry;
import dev.quartz563.alchemy.configs.AlchemySettings;
import dev.quartz563.alchemy.registeries.StructureRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.mineacademy.fo.Common;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Handles all brewing logic for the Alchemical Cauldron.
 * <p>
 * Separated from the listener so that brew logic can be called and tested
 * independently of Bukkit event handling.
 */
public class CauldronController {

    private CauldronController() {}

    /**
     * Attempts to add the item in the player's main hand to the cauldron.
     * Consumes one item from the player's hand on success.
     */
    public static void addIngredient(Player player, AlchemicalCauldron cauldron) {
        final ItemStack held = player.getInventory().getItemInMainHand();

        if (held.getType().isAir()) {
            Common.tell(player, "&cYou are not holding anything.");
            return;
        }

        final BrewingState state = (BrewingState) StructureRegistry.getState(cauldron).orElse(null);
        if (state == null) {
            Common.tell(player, "&cThis cauldron is not active.");
            return;
        }

        state.addIngredient(held);

        // Consume one item from the stack
        if (held.getAmount() > 1)
            held.setAmount(held.getAmount() - 1);
        else
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        Common.tell(player, "&aAdded &f" + held.displayName() + "&a to the cauldron.");
    }

    /**
     * Attempts to finish the current brew and match it against loaded recipes.
     * Gives the player a potion on success, or sends a failure message and resets the state.
     */
    public static void finishBrew(Player player, AlchemicalCauldron cauldron) {
        final BrewingState state = (BrewingState) StructureRegistry.getState(cauldron).orElse(null);

        if (state == null) {
            Common.tell(player, "&cThis cauldron is not active.");
            return;
        }

        if (state.isEmpty()) {
            Common.tell(player, "&cThe cauldron is empty. Add ingredients first.");
            return;
        }

        // Find the recipe with the highest quality score
        final Optional<ScoredRecipe> best = RecipeRegistry.getAll().stream()
                .map(recipe -> new ScoredRecipe(recipe, BrewQualityScorer.score(recipe.getSteps(), state.getSteps())))
                .max(Comparator.comparingDouble(ScoredRecipe::score));

        state.reset();

        if (best.isEmpty() || best.get().score() < AlchemySettings.Brewing.MINIMUM_QUALITY) {
            Common.tell(player, "&cThe brew failed. The mixture dissolves into the cauldron.");
            return;
        }

        final ScoredRecipe result = best.get();
        givePotion(player, result.recipe(), result.score());
        AlchemistData.get(player).discoverRecipe(result.recipe().getKey());
        Common.tell(player, "&aYou brewed a &f" + result.recipe().getName()
                + "&a! &7(Quality: " + formatScore(result.score()) + ")");
    }

    private static void givePotion(Player player, PotionRecipe recipe, double quality) {

        final ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        final PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.customName(Component.text("&r" + recipe.getName()));
        meta.lore(List.of(Component.text("&7Quality: " + formatScore(quality))));

        for (final RecipeEffect effect : recipe.getEffects())
            meta.addCustomEffect(
                    new PotionEffect(effect.type(), effect.duration(), effect.power() - 1),
                    true
            );

        potion.setItemMeta(meta);
        player.getInventory().addItem(potion);
    }

    private static String formatMaterial(Material material) {
        final String raw = material.name().replace("_", " ").toLowerCase();
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }

    private static String formatScore(double score) {
        return String.format("%.0f%%", score * 100);
    }

    private record ScoredRecipe(PotionRecipe recipe, double score) {}
}
