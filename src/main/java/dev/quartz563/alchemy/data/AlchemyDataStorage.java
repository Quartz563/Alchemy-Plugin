package dev.quartz563.alchemy.data;

import dev.quartz563.alchemy.element.AlchemicalRepresentation;
import dev.quartz563.alchemy.element.Element;
import org.bukkit.Material;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.database.SimpleDatabase;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;

//todo convert most methods here into an ASYNC variant to ensure proper handling

/**
 * Database access layer for the canonical base alchemical representations.
 *
 * <p>Extends Foundation's {@code SimpleDatabase} for connection management,
 * table creation, and query execution. Handles schema versioning and seeding
 * from the bundled {@code seed.sql} resource on the first run or after a schema change.
 *
 * <p>Custom YAML overrides are handled separately by {@code CustomRepresentationsConfig}.
 */
public final class AlchemyDataStorage extends SimpleDatabase {

    /** Increment when the schema changes to trigger an automatic rebuild on the next startup. */
    private static final int SCHEMA_VERSION = 1;
    private static final String DATABASE_FILE_NAME = "alchemy_base.sqlite";

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Opens the database connection. Call from {@code onPluginStart()} before
     * any other system accesses this class.
     *
     * @param dataFolder the plugin's data folder
     */
    public void initialise(final File dataFolder) {
        if (!dataFolder.exists() && !dataFolder.mkdirs())
            throw new IllegalStateException("Failed to create plugin data folder: " + dataFolder.getAbsolutePath());

        final File databaseFile = new File(dataFolder, DATABASE_FILE_NAME);
        final String jdbcUrl = "jdbc:sqlite:" + databaseFile.getAbsolutePath();

        this.connect(jdbcUrl);
    }

    /**
     * Called by Foundation once the connection is established.
     * Creates tables and handles seeding or rebuilding as needed.
     */
    @Override
    protected void onConnected() {
        this.createSchema();
        this.handleVersionCheck();
    }

    // -------------------------------------------------------------------------
    // Schema management
    // -------------------------------------------------------------------------

    private void createSchema() {
        this.update("CREATE TABLE IF NOT EXISTS schema_version (version INTEGER NOT NULL)");

        this.update(
                "CREATE TABLE IF NOT EXISTS base_representations " +
                        "(material TEXT NOT NULL PRIMARY KEY)"
        );

        this.update(
                "CREATE TABLE IF NOT EXISTS representation_elements (" +
                        "material TEXT    NOT NULL, "                          +
                        "element  TEXT    NOT NULL, "                          +
                        "quantity INTEGER NOT NULL, "                          +
                        "PRIMARY KEY (material, element), "                    +
                        "FOREIGN KEY (material) "                              +
                        "    REFERENCES base_representations(material) "       +
                        "    ON DELETE CASCADE)"
        );
    }

    private void handleVersionCheck() {
        final int stored = this.readSchemaVersion().join();

        if (stored == SCHEMA_VERSION)
            return;

        if (stored != -1) {
            Common.warning("[AlchemyDataStore] Schema version mismatch (stored=" + stored
                    + ", expected=" + SCHEMA_VERSION + "). Rebuilding database.");
            this.dropSchema();
            this.createSchema();
        }

        this.runSeedScript();
        this.writeSchemaVersion(SCHEMA_VERSION);
        Common.log("[AlchemyDataStore] Database built successfully (schema v" + SCHEMA_VERSION + ").");
    }

    private CompletableFuture<Integer> readSchemaVersion() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final ResultSet rs = this.query("SELECT version FROM schema_version LIMIT 1");
                final int version  = rs.next() ? rs.getInt("version") : -1;
                this.close(rs);
                return version;
            } catch (final Exception e) {
                // Table does not yet exist on a fresh build — treat as unversioned.
                return -1;
            }
        });
    }

    private void writeSchemaVersion(final int version) {
        this.update("DELETE FROM schema_version");
        this.update("INSERT INTO schema_version (version) VALUES (" + version + ")");
    }

    private void dropSchema() {
        this.update("DROP TABLE IF EXISTS representation_elements");
        this.update("DROP TABLE IF EXISTS base_representations");
        this.update("DROP TABLE IF EXISTS schema_version");
    }

    private void runSeedScript() {
        try (final InputStream in = AlchemyDataStorage.class.getResourceAsStream("/seed.sql")) {
            if (in == null) {
                Common.warning("[AlchemyDataStore] seed.sql not found in jar — base representations will be empty.");
                return;
            }

            final String script = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            for (final String raw : script.split(";")) {
                final String statement = raw.strip();
                if (!statement.isEmpty()) {
                    this.update(statement);
                } else {
                    Common.warning("[AlchemyDataStore] Empty statement in seed.sql — skipping.");
                }

            }
            Common.log("[AlchemyDataStore] seed.sql loaded successfully!");
        } catch (final IOException e) {
            Common.error(e, "[AlchemyDataStore] Failed to read seed.sql from plugin jar.");
        }
    }

    // -------------------------------------------------------------------------
    // Representation queries
    // -------------------------------------------------------------------------

    /**
     * Bulk-loads all base representations from the database in a single query.
     * Called by {@code RepresentationRegistry} at startup to populate its in-memory cache.
     *
     * @return an unmodifiable map of all seeded material representations
     */
    public CompletableFuture<Map<Material, AlchemicalRepresentation>> loadAllBaseRepresentations() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<Material, Map<Element, Integer>> rawData = new HashMap<>();
            try {
                final ResultSet rs = this.query(
                        "SELECT material, element, quantity FROM representation_elements");

                while (rs.next()) {
                    final Material material = Material.matchMaterial(rs.getString("material"));
                    if (material == null)
                        continue;

                    try {
                        final Element element  = Element.valueOf(rs.getString("element"));
                        final int quantity = rs.getInt("quantity");

                        rawData.computeIfAbsent(material, k -> new EnumMap<>(Element.class))
                                .put(element, quantity);

                    } catch (final IllegalArgumentException e) {
                        Common.warning("[AlchemyDataStore] Unknown element name in database row — skipping.");
                    }
                }

                this.close(rs);

            } catch (final Exception e) {
                Common.error(e, "[AlchemyDataStore] Failed to bulk-load base representations.");
            }

            final Map<Material, AlchemicalRepresentation> result = new EnumMap<>(Material.class);
            rawData.forEach((material, components) ->
                    result.put(material, new AlchemicalRepresentation(components)));
            Common.log("[AlchemyDataStore] Loaded " + result.size() + " base representations.");
            return Collections.unmodifiableMap(result);
        });
    }
}