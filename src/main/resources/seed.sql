-- ============================================================
-- Alchemy Plugin — Base Alchemical Representations
-- ============================================================
-- Elements: FIRE, WATER, EARTH, AIR, LIFE, DEATH, MAGIC, MUNDANE
-- Quantities are integer-based, typically in the range 1–6.
-- This is a starting corpus. All vanilla materials will be
-- catalogued in future expansion passes.
-- ============================================================

-- ============================================================
-- 1. Register all materials (must precede element inserts)
-- ============================================================

INSERT OR IGNORE INTO base_representations (material) VALUES
    -- Earth & Stone
    ('STONE'),
    ('COBBLESTONE'),
    ('DIRT'),
    ('GRASS_BLOCK'),
    ('SAND'),
    ('GRAVEL'),
    ('CLAY'),
    ('CLAY_BALL'),
    ('OBSIDIAN'),
    ('BEDROCK'),

    -- Wood
    ('OAK_LOG'),
    ('OAK_PLANKS'),
    ('OAK_SAPLING'),
    ('OAK_LEAVES'),

    -- Water & Ice
    ('WATER'),
    ('WATER_BUCKET'),
    ('ICE'),
    ('SNOW_BLOCK'),
    ('SNOWBALL'),

    -- Fire & Lava
    ('LAVA'),
    ('LAVA_BUCKET'),

    -- Metals & Minerals
    ('IRON_ORE'),
    ('IRON_INGOT'),
    ('GOLD_ORE'),
    ('GOLD_INGOT'),
    ('DIAMOND'),
    ('EMERALD'),
    ('COAL'),
    ('CHARCOAL'),
    ('REDSTONE'),
    ('LAPIS_LAZULI'),
    ('QUARTZ'),
    ('NETHERITE_INGOT'),

    -- Nether
    ('NETHERRACK'),
    ('SOUL_SAND'),
    ('SOUL_SOIL'),
    ('MAGMA_BLOCK'),
    ('BLAZE_ROD'),
    ('BLAZE_POWDER'),
    ('NETHER_WART'),
    ('GLOWSTONE_DUST'),
    ('GLOWSTONE'),
    ('GHAST_TEAR'),

    -- The End
    ('ENDER_PEARL'),
    ('ENDER_EYE'),
    ('END_STONE'),
    ('DRAGON_BREATH'),
    ('CHORUS_FRUIT'),

    -- Biological drops
    ('BONE'),
    ('BONE_MEAL'),
    ('FEATHER'),
    ('GUNPOWDER'),
    ('SPIDER_EYE'),
    ('FERMENTED_SPIDER_EYE'),
    ('STRING'),
    ('SLIME_BALL'),
    ('ROTTEN_FLESH'),
    ('RABBIT_FOOT'),
    ('RABBIT_HIDE'),

    -- Plants & Food
    ('SUGAR'),
    ('WHEAT'),
    ('WHEAT_SEEDS'),
    ('CARROT'),
    ('POTATO'),
    ('APPLE'),
    ('GOLDEN_APPLE'),
    ('MELON'),
    ('PUMPKIN'),

    -- Potion ingredients
    ('GLISTERING_MELON_SLICE'),
    ('MAGMA_CREAM'),
    ('GOLDEN_CARROT'),
    ('PHANTOM_MEMBRANE'),
    ('SCUTE'),
    ('PUFFERFISH');

-- ============================================================
-- 2. Element components for each material
-- ============================================================

INSERT OR IGNORE INTO representation_elements (material, element, quantity) VALUES

    -- --------------------------------------------------------
    -- Earth & Stone
    -- --------------------------------------------------------
    -- Stone: dense, compressed earth with a mundane quality
    ('STONE',       'EARTH',   4),
    ('STONE',       'MUNDANE', 1),

    -- Cobblestone: broken stone, slightly less refined
    ('COBBLESTONE', 'EARTH',   3),
    ('COBBLESTONE', 'MUNDANE', 1),

    -- Dirt: earth mingled with traces of organic life
    ('DIRT',        'EARTH',   3),
    ('DIRT',        'LIFE',    1),

    -- Grass block: living earth, air circulates through it
    ('GRASS_BLOCK', 'EARTH',   2),
    ('GRASS_BLOCK', 'LIFE',    2),
    ('GRASS_BLOCK', 'AIR',     1),

    -- Sand: earth broken down by wind and water into fine particles
    ('SAND',        'EARTH',   2),
    ('SAND',        'AIR',     3),

    -- Gravel: coarser than sand, more earth than air
    ('GRAVEL',      'EARTH',   3),
    ('GRAVEL',      'AIR',     2),

    -- Clay (block): earth heavily saturated with water
    ('CLAY',        'EARTH',   2),
    ('CLAY',        'WATER',   3),

    -- Clay ball: same composition, singular unit
    ('CLAY_BALL',   'EARTH',   1),
    ('CLAY_BALL',   'WATER',   2),

    -- Obsidian: the violent meeting of fire and water locked into earth
    ('OBSIDIAN',    'EARTH',   3),
    ('OBSIDIAN',    'FIRE',    2),
    ('OBSIDIAN',    'WATER',   2),

    -- Bedrock: the absolute mundane, immovable and without refinement
    ('BEDROCK',     'EARTH',   6),
    ('BEDROCK',     'MUNDANE', 4),

    -- --------------------------------------------------------
    -- Wood
    -- --------------------------------------------------------
    -- Oak log: life given form through earthen roots
    ('OAK_LOG',     'LIFE',    3),
    ('OAK_LOG',     'EARTH',   2),

    -- Oak planks: processed wood, life diminished by mundane craft
    ('OAK_PLANKS',  'LIFE',    2),
    ('OAK_PLANKS',  'EARTH',   1),
    ('OAK_PLANKS',  'MUNDANE', 2),

    -- Oak sapling: concentrated life potential, reaching for air
    ('OAK_SAPLING', 'LIFE',    5),
    ('OAK_SAPLING', 'AIR',     1),

    -- Oak leaves: life expressed through air
    ('OAK_LEAVES',  'LIFE',    3),
    ('OAK_LEAVES',  'AIR',     2),

    -- --------------------------------------------------------
    -- Water & Ice
    -- --------------------------------------------------------
    -- Water (block): pure water element
    ('WATER',        'WATER',  6),

    -- Water bucket: same as the block form when held
    ('WATER_BUCKET', 'WATER',  6),

    -- Ice: water with air trapped within its crystalline structure
    ('ICE',          'WATER',  4),
    ('ICE',          'AIR',    1),

    -- Snow block: water dispersed through air, compressed
    ('SNOW_BLOCK',   'WATER',  2),
    ('SNOW_BLOCK',   'AIR',    3),

    -- Snowball: a handful of the same
    ('SNOWBALL',     'WATER',  1),
    ('SNOWBALL',     'AIR',    2),

    -- --------------------------------------------------------
    -- Fire & Lava
    -- --------------------------------------------------------
    -- Lava (block): fire that has absorbed earth
    ('LAVA',         'FIRE',   4),
    ('LAVA',         'EARTH',  1),

    -- Lava bucket: the constrained form carries more earth
    ('LAVA_BUCKET',  'FIRE',   4),
    ('LAVA_BUCKET',  'EARTH',  2),

    -- --------------------------------------------------------
    -- Metals & Minerals
    -- --------------------------------------------------------
    -- Iron ore: earth densely packed with mundane potential
    ('IRON_ORE',     'EARTH',   3),
    ('IRON_ORE',     'MUNDANE', 2),

    -- Iron ingot: refined, mundane quality concentrated
    ('IRON_INGOT',   'EARTH',   2),
    ('IRON_INGOT',   'MUNDANE', 4),

    -- Gold ore: earth concealing latent magic
    ('GOLD_ORE',     'EARTH',   2),
    ('GOLD_ORE',     'MAGIC',   2),
    ('GOLD_ORE',     'MUNDANE', 1),

    -- Gold ingot: earth shed, magic expressed
    ('GOLD_INGOT',   'EARTH',   1),
    ('GOLD_INGOT',   'MAGIC',   4),

    -- Diamond: earth compressed until only magic remains
    ('DIAMOND',      'EARTH',   2),
    ('DIAMOND',      'MAGIC',   5),

    -- Emerald: magic tinged with life, rare in its harmony
    ('EMERALD',      'EARTH',   1),
    ('EMERALD',      'LIFE',    2),
    ('EMERALD',      'MAGIC',   3),

    -- Coal: partially burned organic matter, fire and death within earth
    ('COAL',         'FIRE',    2),
    ('COAL',         'EARTH',   2),
    ('COAL',         'DEATH',   1),

    -- Charcoal: more thoroughly burned, fire dominant
    ('CHARCOAL',     'FIRE',    3),
    ('CHARCOAL',     'EARTH',   1),
    ('CHARCOAL',     'DEATH',   1),

    -- Redstone: earth animated by magic, mundane in origin but responsive
    ('REDSTONE',     'EARTH',   2),
    ('REDSTONE',     'MAGIC',   3),
    ('REDSTONE',     'MUNDANE', 1),

    -- Lapis lazuli: earth soaked in water and magic
    ('LAPIS_LAZULI', 'EARTH',   2),
    ('LAPIS_LAZULI', 'MAGIC',   2),
    ('LAPIS_LAZULI', 'WATER',   2),

    -- Quartz: earth refined by air and magic in the nether
    ('QUARTZ',       'EARTH',   2),
    ('QUARTZ',       'MAGIC',   2),
    ('QUARTZ',       'AIR',     1),

    -- Netherite ingot: ancient, fire-forged, bearing death and magic
    ('NETHERITE_INGOT', 'EARTH',  2),
    ('NETHERITE_INGOT', 'FIRE',   3),
    ('NETHERITE_INGOT', 'DEATH',  2),
    ('NETHERITE_INGOT', 'MAGIC',  2),

    -- --------------------------------------------------------
    -- Nether
    -- --------------------------------------------------------
    -- Netherrack: fire-saturated earth, tainted with death
    ('NETHERRACK',   'FIRE',    3),
    ('NETHERRACK',   'EARTH',   2),
    ('NETHERRACK',   'DEATH',   2),

    -- Soul sand: death so concentrated it traps the living
    ('SOUL_SAND',    'DEATH',   5),
    ('SOUL_SAND',    'EARTH',   2),

    -- Soul soil: death bound into firmer earth
    ('SOUL_SOIL',    'DEATH',   4),
    ('SOUL_SOIL',    'EARTH',   3),

    -- Magma block: fire and earth fused into a stable, radiating form
    ('MAGMA_BLOCK',  'FIRE',    4),
    ('MAGMA_BLOCK',  'EARTH',   2),

    -- Blaze rod: pure channelled fire with a sliver of magic
    ('BLAZE_ROD',    'FIRE',    5),
    ('BLAZE_ROD',    'MAGIC',   1),

    -- Blaze powder: blaze rod ground down, fire and magic released
    ('BLAZE_POWDER', 'FIRE',    3),
    ('BLAZE_POWDER', 'MAGIC',   2),

    -- Nether wart: the only life in the nether, shaped by death and magic
    ('NETHER_WART',  'DEATH',   3),
    ('NETHER_WART',  'LIFE',    1),
    ('NETHER_WART',  'MAGIC',   1),

    -- Glowstone dust: fire and magic expressed as light through air
    ('GLOWSTONE_DUST', 'FIRE',  2),
    ('GLOWSTONE_DUST', 'MAGIC', 3),
    ('GLOWSTONE_DUST', 'AIR',   1),

    -- Glowstone: the compressed block form, magic slightly amplified
    ('GLOWSTONE',    'FIRE',    2),
    ('GLOWSTONE',    'MAGIC',   4),
    ('GLOWSTONE',    'AIR',     2),

    -- Ghast tear: grief distilled — death and water mingled with magic
    ('GHAST_TEAR',   'MAGIC',   2),
    ('GHAST_TEAR',   'DEATH',   2),
    ('GHAST_TEAR',   'WATER',   3),

    -- --------------------------------------------------------
    -- The End
    -- --------------------------------------------------------
    -- Ender pearl: magic folded through air with a whisper of death
    ('ENDER_PEARL',  'MAGIC',   4),
    ('ENDER_PEARL',  'AIR',     2),
    ('ENDER_PEARL',  'DEATH',   1),

    -- Ender eye: the pearl awakened, magic intensified
    ('ENDER_EYE',    'MAGIC',   5),
    ('ENDER_EYE',    'AIR',     2),
    ('ENDER_EYE',    'DEATH',   1),

    -- End stone: earth touched by void-magic and suffused with air
    ('END_STONE',    'EARTH',   3),
    ('END_STONE',    'MAGIC',   2),
    ('END_STONE',    'AIR',     1),

    -- Dragon breath: the concentrated essence of the dragon — fire, magic, death, and air
    ('DRAGON_BREATH','FIRE',    2),
    ('DRAGON_BREATH','MAGIC',   4),
    ('DRAGON_BREATH','DEATH',   3),
    ('DRAGON_BREATH','AIR',     2),

    -- Chorus fruit: life sustained in the void through magic and air
    ('CHORUS_FRUIT', 'LIFE',    2),
    ('CHORUS_FRUIT', 'MAGIC',   4),
    ('CHORUS_FRUIT', 'AIR',     2),

    -- --------------------------------------------------------
    -- Biological drops
    -- --------------------------------------------------------
    -- Bone: the framework of life reduced to death and earth
    ('BONE',         'DEATH',   4),
    ('BONE',         'EARTH',   1),

    -- Bone meal: bone ground down, releasing stored life into death
    ('BONE_MEAL',    'DEATH',   2),
    ('BONE_MEAL',    'EARTH',   1),
    ('BONE_MEAL',    'LIFE',    2),

    -- Feather: air made tangible through life
    ('FEATHER',      'AIR',     4),
    ('FEATHER',      'LIFE',    2),

    -- Gunpowder: fire, air, and death compressed into mundane powder
    ('GUNPOWDER',    'FIRE',    3),
    ('GUNPOWDER',    'AIR',     2),
    ('GUNPOWDER',    'DEATH',   1),

    -- Spider eye: a small death with a trace of life and magic venom
    ('SPIDER_EYE',   'DEATH',   2),
    ('SPIDER_EYE',   'LIFE',    1),
    ('SPIDER_EYE',   'MAGIC',   1),

    -- Fermented spider eye: death dominant, magic altered through mundane corruption
    ('FERMENTED_SPIDER_EYE', 'DEATH',   3),
    ('FERMENTED_SPIDER_EYE', 'MAGIC',   2),
    ('FERMENTED_SPIDER_EYE', 'MUNDANE', 1),

    -- String: life and air woven into mundane utility
    ('STRING',       'LIFE',    2),
    ('STRING',       'AIR',     2),
    ('STRING',       'MUNDANE', 1),

    -- Slime ball: water-dominant life with an earthen core
    ('SLIME_BALL',   'WATER',   3),
    ('SLIME_BALL',   'LIFE',    2),
    ('SLIME_BALL',   'EARTH',   1),

    -- Rotten flesh: life turned to death, mundane in its decay
    ('ROTTEN_FLESH', 'DEATH',   3),
    ('ROTTEN_FLESH', 'LIFE',    1),
    ('ROTTEN_FLESH', 'MUNDANE', 1),

    -- Rabbit foot: life with agility — air and a touch of magic
    ('RABBIT_FOOT',  'LIFE',    3),
    ('RABBIT_FOOT',  'AIR',     2),
    ('RABBIT_FOOT',  'MAGIC',   1),

    -- Rabbit hide: life and earth bound together, tinged with death
    ('RABBIT_HIDE',  'LIFE',    2),
    ('RABBIT_HIDE',  'EARTH',   1),
    ('RABBIT_HIDE',  'DEATH',   1),

    -- --------------------------------------------------------
    -- Plants & Food
    -- --------------------------------------------------------
    -- Sugar: distilled life, sweet and mundane
    ('SUGAR',        'LIFE',    3),
    ('SUGAR',        'MUNDANE', 2),

    -- Wheat: life grown from earth and air
    ('WHEAT',        'LIFE',    4),
    ('WHEAT',        'EARTH',   1),
    ('WHEAT',        'AIR',     1),

    -- Wheat seeds: concentrated life potential in earthen form
    ('WHEAT_SEEDS',  'LIFE',    3),
    ('WHEAT_SEEDS',  'EARTH',   1),

    -- Carrot: life rooted deeply in earth
    ('CARROT',       'LIFE',    4),
    ('CARROT',       'EARTH',   1),

    -- Potato: denser life, more earth than the carrot
    ('POTATO',       'LIFE',    3),
    ('POTATO',       'EARTH',   2),

    -- Apple: life carried on air, fruit of the sky-reaching tree
    ('APPLE',        'LIFE',    4),
    ('APPLE',        'AIR',     1),

    -- Golden apple: life elevated to magic through gold
    ('GOLDEN_APPLE', 'LIFE',    4),
    ('GOLDEN_APPLE', 'MAGIC',   3),
    ('GOLDEN_APPLE', 'AIR',     1),

    -- Melon: life swelled with water
    ('MELON',        'LIFE',    4),
    ('MELON',        'WATER',   2),

    -- Pumpkin: earthbound life, hollow and air-filled
    ('PUMPKIN',      'LIFE',    3),
    ('PUMPKIN',      'EARTH',   2),
    ('PUMPKIN',      'AIR',     1),

    -- --------------------------------------------------------
    -- Potion ingredients
    -- --------------------------------------------------------
    -- Glistering melon slice: life and water elevated by gold-magic
    ('GLISTERING_MELON_SLICE', 'LIFE',  4),
    ('GLISTERING_MELON_SLICE', 'MAGIC', 2),
    ('GLISTERING_MELON_SLICE', 'WATER', 1),

    -- Magma cream: fire and earth fused with a sliver of life
    ('MAGMA_CREAM',  'FIRE',    3),
    ('MAGMA_CREAM',  'EARTH',   1),
    ('MAGMA_CREAM',  'LIFE',    1),

    -- Golden carrot: life and earth enriched with gold-magic
    ('GOLDEN_CARROT','LIFE',    4),
    ('GOLDEN_CARROT','MAGIC',   2),
    ('GOLDEN_CARROT','EARTH',   1),

    -- Phantom membrane: death and air given substance, with magic resonance
    ('PHANTOM_MEMBRANE', 'DEATH',  3),
    ('PHANTOM_MEMBRANE', 'AIR',    3),
    ('PHANTOM_MEMBRANE', 'MAGIC',  1),

    -- Scute: life armoured by water and earth
    ('SCUTE',        'LIFE',    3),
    ('SCUTE',        'WATER',   3),
    ('SCUTE',        'EARTH',   1),

    -- Pufferfish: water-dominant life carrying a trace of death-venom
    ('PUFFERFISH',   'WATER',   4),
    ('PUFFERFISH',   'LIFE',    2),
    ('PUFFERFISH',   'DEATH',   1)
