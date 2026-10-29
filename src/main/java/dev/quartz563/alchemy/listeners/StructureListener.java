package dev.quartz563.alchemy.listeners;

import dev.quartz563.alchemy.menus.CauldronMenu;
import dev.quartz563.alchemy.menus.WorkbenchMenu;
import dev.quartz563.alchemy.structures.events.StructureInteractEvent;
import dev.quartz563.alchemy.structures.impl.cauldron.AlchemicalCauldron;
import dev.quartz563.alchemy.structures.impl.cauldron.CauldronController;
import dev.quartz563.alchemy.structures.impl.workbench.AlchemicWorkbench;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.fo.Common;

public class StructureListener implements Listener {

    //this is where the main logic of interacting -> shunt to correct structure to process the interaction

    @EventHandler //do I need to adjust this at all? maybe.
    public void onCauldronInteract(StructureInteractEvent event) {
        if(!event.getStructureId().equals("alchemy:alchemic_cauldron")){
            return;
        }
        final AlchemicalCauldron cauldron = (AlchemicalCauldron) event.getStructure();
        final Player player = event.getPlayer();
        final boolean emptyHand = event.getItemInPlayerHand().isEmpty();
        if(emptyHand) {
            new CauldronMenu(cauldron).displayTo(player);
        } else {
            CauldronController.addIngredient(player, cauldron);
        }
    }

    @EventHandler
    public void onWorkbenchInteract(StructureInteractEvent event) {
        if(!event.getStructureId().equals("alchemy:alchemic_workbench")){
            return;
        }
        final AlchemicWorkbench workbench = (AlchemicWorkbench) event.getStructure();
        final Player player = event.getPlayer();
        //todo configure this later so players can add catalyst item to the workbench to transcribe
        Common.tell(player, "You interacted with this workbench!");
        //new WorkbenchMenu(workbench).displayTo(player);
    }

}
