package dev.quartz563.alchemy.structures.util;

import dev.quartz563.alchemy.structures.IStructure;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Set;

import java.util.function.BiFunction;

public record StructureDefinition(
        String id,
        String name,
        Material activationMaterial,
        Set<StructureBlock> structureBlocks,
        BiFunction<Location, StructureDefinition, IStructure> factory) {

    public IStructure createInstance(Location location) {
        return factory.apply(location, this);
    }
}

