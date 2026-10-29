package dev.quartz563.alchemy.structures.util;

import org.bukkit.Material;

import java.util.Set;

/**
 * Represents a block within the structure.
 * @param coordinate The block coordinate relative to the structure's "Activation Block"
 * @param blockMaterials The Set of valid materials that the block could be at the time of activation
 */
public record StructureBlock(RelativeCoordinate coordinate, Set<Material> blockMaterials) {
}
