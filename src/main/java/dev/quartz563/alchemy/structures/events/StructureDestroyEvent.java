package dev.quartz563.alchemy.structures.events;

import dev.quartz563.alchemy.structures.IStructure;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.event.SimpleCancellableEvent;

public class StructureDestroyEvent extends SimpleCancellableEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IStructure structure;
    private final Block brokenBlock;
    private final Player player;

    public StructureDestroyEvent(IStructure structure, Block brokenBlock, @Nullable Player player) {
        this.structure = structure;
        this.brokenBlock = brokenBlock;
        this.player = player;
    }

    public IStructure getStructure() {
        return structure;
    }

    public Block getBrokenBlock() {
        return brokenBlock;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
