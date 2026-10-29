package dev.quartz563.alchemy.listeners;

import dev.quartz563.alchemy.brewing.ProcessingState;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.nbt.NBT;

public final class ProcessorListener implements Listener {

    //todo I am going to SHUNT all of this into their own structures

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand() != EquipmentSlot.HAND)
            return;
        if(!event.getAction().isRightClick())
            return;
        if(!event.getPlayer().isSneaking())
            return;
        final Block clicked = event.getClickedBlock();
        if(clicked == null || clicked.getType() != CompMaterial.FLETCHING_TABLE.getMaterial())
            return;
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final boolean emptyHand = player.getInventory().getItemInMainHand().getType().isAir();

        if(emptyHand)
            Common.tell(player, "You need a ingredient to process!");
        else {
            final ItemStack reagent = player.getInventory().getItemInMainHand().clone();
            final ItemStack held = player.getInventory().getItemInMainHand();
            if (held.getAmount() > 1)
                held.setAmount(held.getAmount() - 1);
            else
                player.getInventory().setItemInMainHand(new ItemStack(CompMaterial.AIR.getMaterial()));
            NBT.modify(reagent, nbt -> {
               nbt.setString("AlchemyProcessState", ProcessingState.CRUSHED.toString());
            });
            final ItemMeta meta = reagent.getItemMeta();
            final Component originalName = meta.displayName() != null
                    ? meta.displayName()
                    : Component.text(ItemUtil.bountifyCapitalized(reagent.getType()));
            meta.displayName(Component.text("Crushed ").append(originalName));
            reagent.setItemMeta(meta);
            player.getInventory().addItem(reagent);
        }
    }
}
