package dev.quartz563.alchemy.listeners;

import dev.quartz563.alchemy.menus.RecipeDetailMenu;
import dev.quartz563.alchemy.registeries.RecipeRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.nbt.NBT;
import org.mineacademy.fo.remain.nbt.ReadableItemNBT;

import java.util.function.Function;

public class RecipeBookListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand() != EquipmentSlot.HAND)
            return;
        if(!event.getAction().isRightClick())
            return;
        //refactor later
        if(event.getMaterial() != Material.BOOK)
            return;
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final ItemStack recipeBook = player.getInventory().getItemInMainHand();
        final boolean hasKey = NBT.get(recipeBook, (Function<ReadableItemNBT, Boolean>) nbt ->
            nbt.hasTag("RecipeBookValue")
        );
        if(!hasKey)
            return;
        final String potionKey = NBT.get(recipeBook, t -> {
            return t.getString("RecipeBookValue");
        });
        RecipeRegistry.get(potionKey).ifPresentOrElse(recipe -> new RecipeDetailMenu(recipe).displayTo(player),
                () -> Common.log("Error occurred! NBT key for book returns: " + potionKey)
        );
    }

}
