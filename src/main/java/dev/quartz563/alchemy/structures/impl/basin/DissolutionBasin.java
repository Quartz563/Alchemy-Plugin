package dev.quartz563.alchemy.structures.impl.basin;

import org.bukkit.Location;
import org.bukkit.block.Block;

public final class DissolutionBasin {

    private final Location basinLocation;

    DissolutionBasin(Block basinBlock) {
        this.basinLocation = basinBlock.getLocation().toBlockLocation();
    }

    public Location getBasinLocation() {
        return basinLocation.clone();
    }

    public Block getBasinBlock(){
        return basinLocation.getBlock();
    }



}
