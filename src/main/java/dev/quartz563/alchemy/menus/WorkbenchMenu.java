package dev.quartz563.alchemy.menus;

import dev.quartz563.alchemy.registeries.StructureRegistry;
import dev.quartz563.alchemy.structures.impl.workbench.AlchemicWorkbench;
import dev.quartz563.alchemy.structures.impl.workbench.WorkbenchState;
import org.mineacademy.fo.menu.Menu;

public class WorkbenchMenu extends Menu {

    private final AlchemicWorkbench workbench;
    private final WorkbenchState state;

    public WorkbenchMenu(AlchemicWorkbench workbench) {
        setTitle("Alchemic Workbench");
        setSize(4*9);
        this.workbench = workbench;
        state = (WorkbenchState) StructureRegistry.getState(workbench).orElse(null);
        if(state == null) {
            throw new IllegalStateException("Cannot get state of Workbench!");
        }
        //todo set buttons
    }

}
