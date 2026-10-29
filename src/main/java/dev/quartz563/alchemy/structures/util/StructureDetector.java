package dev.quartz563.alchemy.structures.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;

public final class StructureDetector {

    private StructureDetector() {}


    public static boolean detect(Block activationBlock, StructureDefinition definition) {
        return isValidStructure(activationBlock, definition.activationMaterial(), definition.structureBlocks());
    }

    /*the intention of this is to manually validate a structure using the relative coordinates of the
     activation block. Each coordinate will also check against the set of valid materials that it can be.
    for example, the cauldron has fire below at 0, -1, 0; and the material can be fire or soul fire.
    */
    /**
     * Validates a physical structure in the world against a predefined set of structure blocks.
     * <p>
     * This method checks the activation block's material and iterates through the provided
     * {@link StructureBlock} set. Each block uses relative coordinates from the activation block
     * to verify if the world block's material matches any of the accepted materials for that position.
     *
     * @param activationBlock The origin block triggering the structure check.
     * @param activationMaterial The expected material of the activation block.
     * @param structureBlocks A set of relative blocks and their valid materials defining the structure shape.
     * @return {@code true} if the activation block and all relative blocks match the definition; {@code false} otherwise.
     */
    public static boolean isValidStructure(Block activationBlock, Material activationMaterial, Set<StructureBlock> structureBlocks) {
        if(activationBlock == null || activationMaterial == null || structureBlocks == null)
            return false;
        if(activationBlock.getType() != activationMaterial)
            return false;
        for(StructureBlock structureBlock : structureBlocks) {
            Material actualMaterial = activationBlock.getRelative(structureBlock.coordinate().x(), structureBlock.coordinate().y(), structureBlock.coordinate().z()).getType();
            if(!structureBlock.blockMaterials().contains(actualMaterial))
                return false;
        }
        return true;
    }
}
