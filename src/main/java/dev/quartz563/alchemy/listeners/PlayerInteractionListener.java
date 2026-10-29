package dev.quartz563.alchemy.listeners;

import dev.quartz563.alchemy.registeries.StructureRegistry;
import dev.quartz563.alchemy.structures.IStructure;
import dev.quartz563.alchemy.structures.events.StructureDestroyEvent;
import dev.quartz563.alchemy.structures.events.StructureInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

/**
 * Listens for player interactions with Structures registered in the StructureRegistry.
 */
public final class PlayerInteractionListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        if (!event.getAction().isRightClick())
            return;

        final Block clicked = event.getClickedBlock();
        if (clicked == null)
            return;

        Optional<IStructure> structureOpt = StructureRegistry.getOrRegister(clicked, clicked.getLocation());
        if (structureOpt.isEmpty())
            return;
        event.setCancelled(true);
        final IStructure structure = structureOpt.get();
        final Player player = event.getPlayer();
        StructureInteractEvent sInteractEvent = new StructureInteractEvent(player, structure.structureId(), structure.location());
        Bukkit.getPluginManager().callEvent(sInteractEvent);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Block brokenBlock = event.getBlock();
        StructureRegistry.getStructureAt(brokenBlock.getLocation()).ifPresent(structure -> {
            StructureDestroyEvent sDestroyEvent = new StructureDestroyEvent(structure, brokenBlock, event.getPlayer());
            Bukkit.getPluginManager().callEvent(sDestroyEvent);
            if(!sDestroyEvent.isCancelled())
                StructureRegistry.remove(structure.location());
        });
    }


}
