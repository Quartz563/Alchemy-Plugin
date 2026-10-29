package dev.quartz563.alchemy.structures.impl.workbench;

import dev.quartz563.alchemy.structures.IStructureState;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class WorkbenchState implements IStructureState {

    private ItemStack catalystItem;
    private final Location location;
    //create default map here for elements too, init it in new WorkbenchState

    public WorkbenchState(Location location) {
        this.location = location;
    }

    public void addCatalyst(ItemStack catalystItem) {

    }

    @Override
    public Location location() {
        return location;
    }

}
