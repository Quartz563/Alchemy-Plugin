package dev.quartz563.alchemy.configs;

import org.mineacademy.fo.settings.SimpleSettings;

/**
 * Main plugin configuration, loaded from settings.yml.
 */
public final class AlchemySettings extends SimpleSettings {

    // -------------------------------------------------------------------------
    // Brewing
    // -------------------------------------------------------------------------

    public static class Brewing {

        /**
         * Minimum quality score [0.0 - 1.0] required for a brew to succeed.
         * A score below this threshold results in a failed brew.
         */
        public static Double MINIMUM_QUALITY;

        private static void init() {
            setPathPrefix("Brewing");
            MINIMUM_QUALITY = getDouble("Minimum_Quality");
        }
    }

    // -------------------------------------------------------------------------
    // Elements
    // -------------------------------------------------------------------------

    public static class Elements {

        /** Display a name for each element, configurable by server administrators. */
        public static String FIRE;
        public static String WATER;
        public static String EARTH;
        public static String AIR;
        public static String LIFE;
        public static String DEATH;
        public static String MAGIC;
        public static String MUNDANE;

        private static void init() {
            setPathPrefix("Elements");
            FIRE = getString("Fire");
            WATER = getString("Water");
            EARTH = getString("Earth");
            AIR = getString("Air");
            LIFE = getString("Life");
            DEATH = getString("Death");
            MAGIC = getString("Magic");
            MUNDANE = getString("Mundane");
        }
    }

    @Override
    protected int getConfigVersion() {
        return 1;
    }
}
