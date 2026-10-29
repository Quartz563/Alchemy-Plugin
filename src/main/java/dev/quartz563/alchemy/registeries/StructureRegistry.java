package dev.quartz563.alchemy.registeries;

import dev.quartz563.alchemy.structures.*;
import dev.quartz563.alchemy.structures.util.StructureBlock;
import dev.quartz563.alchemy.structures.util.StructureDefinition;
import dev.quartz563.alchemy.structures.util.StructureDetector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public final class StructureRegistry {

    private static final Map<Location, IStructure> STRUCTURES = new HashMap<>();
    private static final Map<Location, IStructure> OCCUPIED_LOCATIONS = new HashMap<>();
    private static final Map<Location, IStructureState> STATES = new HashMap<>();
    private static final Map<Material, Set<StructureDefinition>> DEFINITIONS_BY_MATERIAL = new EnumMap<>(Material.class);

    /***
     * Registers a structure definition for a specific material.
     * @param definition the structure definition to register
     */
    public static void registerDefinition(StructureDefinition definition) {
        DEFINITIONS_BY_MATERIAL
                .computeIfAbsent(definition.activationMaterial(), k -> new HashSet<>())
                .add(definition);
    }

    /***
     * Gets or registers a structure instance for a specific block location.
     * @param block the block to get the structure instance for
     * @param blockLocation the location of the block
     * @return an Optional containing the structure instance if it exists, otherwise an empty Optional
     */
    public static Optional<IStructure> getOrRegister(Block block, Location blockLocation) {
        Set<StructureDefinition> possibleDefinitions = DEFINITIONS_BY_MATERIAL.get(block.getType());
        if(possibleDefinitions == null || possibleDefinitions.isEmpty())
            return Optional.empty();
        return possibleDefinitions.stream()
                .filter(definition -> StructureDetector.detect(block, definition))
                .findFirst()
                .map(definition -> createStructure(blockLocation, definition))
                .map(structure -> registerInstance(blockLocation, structure));
    }

    /***
     * Gets the structure instance at a specific location wrapped in an Optional.
     * @param location the location to get the structure instance for
     * @return an Optional containing the structure instance if it exists, otherwise an empty Optional
     */
    public static Optional<IStructure> getStructureAt(Location location) {
        return Optional.ofNullable(OCCUPIED_LOCATIONS.get(location.toBlockLocation()));
    }

    /***
     * Gets the state of a structure instance at a specific location wrapped in an Optional.
     * @param structure the structure instance to get the state for
     * @return an Optional containing the structure state if it exists, otherwise an empty Optional
     */
    public static Optional<IStructureState> getState (IStructure structure) {
        return Optional.ofNullable(STATES.get(structure.location().toBlockLocation()));
    }

    /***
     * Removes a structure instance from the registry.
     * @param key the location of the structure instance to remove
     */
    public static void remove(Location key) {
        IStructure structure = STRUCTURES.get(key);
        if (structure != null) {
            for (Location loc : getOccupiedLocations(structure)) {
                OCCUPIED_LOCATIONS.remove(loc);
            }
        }
        STRUCTURES.remove(key);
        STATES.remove(key);
    }

    /***
     * Clears all structure instances from the registry.
     */
    public static void clear() {
        STRUCTURES.clear();
        STATES.clear();
        OCCUPIED_LOCATIONS.clear();
    }

    private static IStructure registerInstance(Location blockLocation, IStructure structure) {
        STRUCTURES.put(blockLocation, structure);
        for(Location loc : getOccupiedLocations(structure)) {
            OCCUPIED_LOCATIONS.put(loc, structure);
        }
        if (structure.hasState()) {
            STATES.put(blockLocation, structure.state());
        }
        return structure;
    }

    private static Set<Location> getOccupiedLocations(IStructure structure) {
        Set<Location> locations = new HashSet<>();
        Location origin = structure.location().toBlockLocation();
        locations.add(origin);
        for(StructureBlock block : structure.definition().structureBlocks()) {
            Location componentLoc = origin.clone().add(
                    block.coordinate().x(),
                    block.coordinate().y(),
                    block.coordinate().z()
            );
            locations.add(componentLoc);
        }
        return locations;
    }

    private static IStructure createStructure(Location blockLocation, StructureDefinition definition) {
        return definition.factory().apply(blockLocation, definition);
    }
}
