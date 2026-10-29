package dev.quartz563.alchemy.structures.impl.workbench;

import dev.quartz563.alchemy.structures.IStructure;
import dev.quartz563.alchemy.structures.util.StructureDefinition;
import org.bukkit.Location;

public record AlchemicWorkbench(Location location, StructureDefinition definition) implements IStructure {

    @Override
    public String string() {
        return "AlchemicWorkbench{" + location.getBlockX()
                + "," + location.getBlockY()
                + "," + location.getBlockZ() + "}";
    }
}
