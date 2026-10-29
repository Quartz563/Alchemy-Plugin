package dev.quartz563.alchemy.configs;

import dev.quartz563.alchemy.element.AlchemicalRepresentation;
import dev.quartz563.alchemy.element.Element;
import dev.quartz563.alchemy.registeries.RepresentationRegistry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;

import java.io.*;
import java.util.*;

/**
 * Manages server administrator overrides for alchemical representations.
 *
 * <p>Reads and writes {@code custom_representations.yml} in the plugin's data folder.
 * Entries defined here take priority over the built-in database when
 * {@link RepresentationRegistry} merges both sources at startup.
 */
public final class CustomRepresentationConfig {

    private static final String FILENAME = "custom_representations.yml";

    private static final String FILE_HEADER =
            """
                    Custom alchemical representations for server administrator overrides.
                    Entries defined here take priority over the built-in database.
                    
                    Format:
                      MATERIAL_NAME:
                        ELEMENT_NAME: quantity
                    
                    Valid elements: FIRE, WATER, EARTH, AIR, LIFE, DEATH, MAGIC, MUNDANE
                    
                    Example:
                      STONE:
                        EARTH: 4
                        MUNDANE: 1""";

    private final File              file;
    private       YamlConfiguration config;

    public CustomRepresentationConfig(final File dataFolder) {
        this.file = new File(dataFolder, FILENAME);
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Loads the config from disk, creating it with a descriptive header if absent.
     */
    public void load() {
        if (this.file.exists()) {
            this.config = YamlConfiguration.loadConfiguration(this.file);
            return;
        }

        this.config = new YamlConfiguration();
        this.config.options().header(FILE_HEADER);
        this.save();
    }

    // -------------------------------------------------------------------------
    // Read
    // -------------------------------------------------------------------------

    /**
     * Loads all custom representation overrides from the config.
     * Called by {@code RepresentationRegistry} at startup.
     *
     * @return an unmodifiable map of all server-defined overrides
     */
    public Map<Material, AlchemicalRepresentation> loadAll() {
        final Map<Material, AlchemicalRepresentation> result = new EnumMap<>(Material.class);

        for (final String key : this.config.getKeys(false)) {
            final Material material = Material.matchMaterial(key);

            if (material == null) {
                Common.warning("[CustomRepresentationsConfig] Unknown material '" + key
                        + "' in " + FILENAME + " — skipping.");
                continue;
            }

            final ConfigurationSection section = this.config.getConfigurationSection(key);
            if (section != null)
                result.put(material, this.parseSection(section));
        }

        return Collections.unmodifiableMap(result);
    }

    // -------------------------------------------------------------------------
    // Write
    // -------------------------------------------------------------------------

    /**
     * Writes a custom representation override for a material and saves the file.
     *
     * @param material       the material to override
     * @param representation the custom representation to store
     */
    public void set(final Material material, final AlchemicalRepresentation representation) {
        final ConfigurationSection section = this.config.createSection(material.name());
        representation.elements().forEach((element, quantity) -> section.set(element.name(), quantity));
        this.save();
    }

    /**
     * Removes a custom override for a material, restoring the base database value.
     *
     * @param material the material whose override should be removed
     */
    public void remove(final Material material) {
        this.config.set(material.name(), null);
        this.save();
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private void save() {
        try {
            this.config.save(this.file);
        } catch (final IOException e) {
            Common.error(e, "[CustomRepresentationsConfig] Failed to save " + FILENAME + ".");
        }
    }

    private AlchemicalRepresentation parseSection(final ConfigurationSection section) {
        final Map<Element, Integer> components = new EnumMap<>(Element.class);

        for (final String key : section.getKeys(false)) {
            try {
                components.put(Element.valueOf(key.toUpperCase()), section.getInt(key));
            } catch (final IllegalArgumentException e) {
                Common.warning("[CustomRepresentationsConfig] Unknown element '" + key
                        + "' in " + FILENAME + " — skipping.");
            }
        }

        return new AlchemicalRepresentation(components);
    }
}
