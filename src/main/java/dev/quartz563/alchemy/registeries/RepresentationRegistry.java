package dev.quartz563.alchemy.registeries;

import dev.quartz563.alchemy.configs.CustomRepresentationConfig;
import dev.quartz563.alchemy.data.AlchemyDataStorage;
import dev.quartz563.alchemy.element.AlchemicalRepresentation;
import org.bukkit.Material;
import org.mineacademy.fo.Common;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class RepresentationRegistry {

    private static final Map<Material, AlchemicalRepresentation> representations = new EnumMap<>(Material.class);

    private final AlchemyDataStorage dataStorage;
    private final CustomRepresentationConfig customConfig;

    /***
     * Constructs a new RepresentationRegistry instance with the given data storage and custom configuration.
     * @param dataStorage the data storage to load representations from
     * @param customConfig the custom configuration to load representations from
     */
    public RepresentationRegistry(final AlchemyDataStorage dataStorage, final CustomRepresentationConfig customConfig) {
        this.dataStorage = dataStorage;
        this.customConfig = customConfig;
    }

    /***
     * Loads all representations from the data storage and custom configuration.
     */
    public void load() {
        representations.clear();
        final Map<Material, AlchemicalRepresentation> base = dataStorage.loadAllBaseRepresentations().join();
        final Map<Material, AlchemicalRepresentation> custom = this.customConfig.loadAll();
        representations.putAll(base);
        representations.putAll(custom);
        Common.log("[RepresentationRegistry] Loaded " + representations.size()
                + " representations (" + custom.size() + " custom override(s)).");
    }

    /***
     * Reloads the representation registry by loading the custom configuration and then loading all representations.
     */
    public void reload() {
        this.customConfig.load();
        this.load();
    }

    /***
     * Gets a specific representation for a material wrapped in an Optional.
     * @param material the material to get the representation for
     * @return an Optional containing the representation if it exists, otherwise an empty Optional
     */
    public static Optional<AlchemicalRepresentation> get(final Material material) {
        final AlchemicalRepresentation representation = representations.get(material);
        if(representation == null) {
            Common.warning("[RepresentationRegistry] CRITICAL: No representation registered for '"
                    + material.name() + "'. This requires developer intervention.");
        }
        return Optional.ofNullable(representation);
    }

    /***
     * Determines if a representation is registered for the given material.
     * 
     * @param material the material to check
     * @return true if a representation is registered for the given material
     */
    public boolean has(final Material material) {
        return representations.containsKey(material);
    }

    /***
     * Returns a map of all registered representations with their material.
     * @return a map of all registered representations with their material
     */
    public Map<Material, AlchemicalRepresentation> getAll() {
        return Collections.unmodifiableMap(representations);
    }

    // -------------------------------------------------------------------------
    // Runtime registration (for addon API)
    // -------------------------------------------------------------------------

    /**
     * Registers a representation at runtime.
     * <p>
     * Intended for the external addon API, allowing developers to define
     * representations for custom items. Does not persist to any file.
     *
     * @param material the material to register
     * @param representation the representation to associate with it
     */
    public void register(final Material material, final AlchemicalRepresentation representation) {
        representations.put(material, representation);
    }

}
