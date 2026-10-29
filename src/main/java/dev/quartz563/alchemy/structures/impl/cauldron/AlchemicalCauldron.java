package dev.quartz563.alchemy.structures.impl.cauldron;

import dev.quartz563.alchemy.brewing.BrewingState;
import dev.quartz563.alchemy.structures.IStructure;
import dev.quartz563.alchemy.structures.IStructureState;
import dev.quartz563.alchemy.structures.util.StructureDefinition;
import org.bukkit.Location;

public final class AlchemicalCauldron implements IStructure {

    private final Location location;
    private final StructureDefinition definition;
    private final BrewingState state;

    public AlchemicalCauldron(Location location, StructureDefinition definition) {
        this.location = location;
        this.definition = definition;
        this.state = new BrewingState(location);
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public StructureDefinition definition() {
        return definition;
    }

    @Override
    public IStructureState state() {
        return state;
    }

   @Override
    public String string() {
       return "AlchemicalCauldron{" + location.getBlockX()
               + "," + location.getBlockY()
               + "," + location.getBlockZ()
               + "}";
   }

}
