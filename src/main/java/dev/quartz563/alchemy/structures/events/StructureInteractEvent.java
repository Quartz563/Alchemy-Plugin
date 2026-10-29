package dev.quartz563.alchemy.structures.events;

import dev.quartz563.alchemy.registeries.StructureRegistry;
import dev.quartz563.alchemy.structures.IStructure;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.event.SimpleCancellableEvent;
import org.mineacademy.fo.exception.EventHandledException;

public class StructureInteractEvent extends SimpleCancellableEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String structureId;
    private final Location structureLocation;
    private final IStructure structure;

    public StructureInteractEvent(Player player, String structureId, Location structureLocation) {
        this.player = player;
        this.structureId = structureId;
        this.structureLocation = structureLocation;
        this.structure = getStructureFromRegistry();
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemInPlayerHand() {
        return player.getInventory().getItemInMainHand();
    }

    public void removeItemFromPlayerHand() {
        player.getInventory().setItemInMainHand(null);
    }

    public void giveItemToPlayerHand(ItemStack item) {
        player.getInventory().setItemInMainHand(item);
    }

    public String getStructureId() {
        return structureId;
    }

    public Location getStructureLocation() {
        return structureLocation;
    }

    public IStructure getStructure() {
        return structure;
    }

    public void setCancelled(boolean cancelled) {
        super.setCancelled(cancelled);
    }

    public boolean isCancelled() {
        return super.isCancelled();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private IStructure getStructureFromRegistry() {
        return StructureRegistry.getStructureAt(structureLocation).orElseThrow(() -> new EventHandledException(true, "Structure not found"));
    }
}
