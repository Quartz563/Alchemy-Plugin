package dev.quartz563.alchemy.structures;

import dev.quartz563.alchemy.registeries.StructureRegistry;
import dev.quartz563.alchemy.structures.impl.cauldron.AlchemicalCauldron;
import dev.quartz563.alchemy.structures.impl.workbench.AlchemicWorkbench;
import dev.quartz563.alchemy.structures.util.RelativeCoordinate;
import dev.quartz563.alchemy.structures.util.StructureBlock;
import dev.quartz563.alchemy.structures.util.StructureDefinition;
import org.bukkit.Material;

import java.util.Set;

/**
 * This class contains all the structure definitions for the plugin and a method used to bulk register them
 * when the plugin is first enabled on server startup.
 */
public final class AlchemyStructures {

    public static final StructureDefinition ALCHEMIC_WORKBENCH = new StructureDefinition(
            "alchemy:alchemic_workbench",
            "Alchemic Workbench",
            Material.NOTE_BLOCK,
            Set.of(
                    new StructureBlock(
                            new RelativeCoordinate(0, 1, 0),
                            Set.of(Material.BREWING_STAND)
                    )
            ),
            AlchemicWorkbench::new
    );

    public static final StructureDefinition CAULDRON = new StructureDefinition(
            "alchemy:alchemic_cauldron",
            "Alchemic Cauldron",
            Material.WATER_CAULDRON,
            Set.of(
                    new StructureBlock(
                            new RelativeCoordinate(0, -1, 0),
                            Set.of(Material.FIRE, Material.SOUL_FIRE)
                    )
            ),
            AlchemicalCauldron::new
    );

    public static void registerAll() {
        StructureRegistry.registerDefinition(ALCHEMIC_WORKBENCH);
        StructureRegistry.registerDefinition(CAULDRON);
    }
}
