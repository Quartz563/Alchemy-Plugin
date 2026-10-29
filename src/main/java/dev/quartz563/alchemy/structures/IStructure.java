package dev.quartz563.alchemy.structures;

import dev.quartz563.alchemy.structures.util.StructureDefinition;
import dev.quartz563.alchemy.structures.util.StructureDetector;
import org.bukkit.Location;

public interface IStructure {
     Location location();

     StructureDefinition definition();

     default boolean isValid() {
          return StructureDetector.detect(location().getBlock(), definition());
     }

     default String structureName() {
          return definition().name();
     }

     default String structureId() {
          return definition().id();
     }

     default IStructureState state() {
          return null;
     }

     default boolean hasState() {
          return state() != null;
     }

     String string();
}
