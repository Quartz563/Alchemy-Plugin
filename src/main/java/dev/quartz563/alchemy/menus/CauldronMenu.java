package dev.quartz563.alchemy.menus;

import dev.quartz563.alchemy.registeries.StructureRegistry;
import dev.quartz563.alchemy.structures.impl.cauldron.AlchemicalCauldron;
import dev.quartz563.alchemy.structures.impl.cauldron.CauldronController;
import dev.quartz563.alchemy.brewing.BrewingAction;
import dev.quartz563.alchemy.brewing.BrewingState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public final class CauldronMenu extends Menu {

    //maybe the cauldron should keep track of its own temperature? Perhaps.
    private final AlchemicalCauldron cauldron;
    private final BrewingState state;

    @Position(34)
    private final Button stirClockwiseButton;
    @Position(28)
    private final Button stirCounterClockwiseButton;
    @Position(4)
    private final Button finishBrewButton;
    @Position(48)
    private final Button increaseTempButton;
    @Position(50)
    private final Button decreaseTempButton;

    public CauldronMenu(AlchemicalCauldron cauldron) {
        setTitle("Cauldron Menu");
        setSize(9*6);
        this.cauldron = cauldron;
        state = (BrewingState) StructureRegistry.getState(cauldron).orElse(null);
        if(state == null) {
            throw new IllegalStateException("Cannot get state of Cauldron!");
        }
        stirClockwiseButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                state.addAction(BrewingAction.STIR_CLOCKWISE);
                tellSuccess("You stirred the cauldron once clockwise");
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.STICK)
                        .name("Str Clockwise")
                        .lore("Str the potion once clockwise")
                        .makeMenuTool();
            }
        };

        stirCounterClockwiseButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                state.addAction(BrewingAction.STIR_COUNTERCLOCKWISE);
                tellSuccess( "You stirred the cauldron once counter-clockwise");
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.STICK)
                        .name("Stir Counter Clockwise")
                        .lore("Stir the potion once counter-clockwise")
                        .makeMenuTool();
            }
        };

        finishBrewButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                CauldronController.finishBrew(player, cauldron);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.POTION)
                        .name("Finish Brewing")
                        .lore("Click here to finish", "when you're done")
                        .makeMenuTool();
            }
        };
        increaseTempButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                tellSuccess("You increased the temperature for the potion");
                state.increaseTemp();
                restartMenu();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.FIRE_CHARGE)
                        .name("Heat Up")
                        .lore("Heat the mixture up")
                        .makeMenuTool();
            }
        };
        decreaseTempButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                tellSuccess("You decreased the temperature for the potion");
                state.decreaseTemp();
                restartMenu();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SNOWBALL)
                        .name("Cool Down")
                        .lore("Cool the mixture down")
                        .makeMenuTool();
            }
        };
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if(slot == 21 || slot == 22 || slot == 23 ||
            slot == 30 || slot == 31 || slot == 32 ||
                slot == 39 || slot == 40 || slot == 41)
        {
            return getCauldronTemperatureItem();
        }
        return NO_ITEM;
    }

    private ItemStack getCauldronTemperatureItem() {
        return switch(state.getCurrentTemp()) {
            case BOILING ->
                 ItemCreator.of(CompMaterial.GHAST_TEAR).name("Boiling").make();
            case HOT ->
                 ItemCreator.of(CompMaterial.LAVA_BUCKET).name("Hot").make();
            case WARM ->
                 ItemCreator.of(CompMaterial.WATER_BUCKET).name("Warm").make();
            case COLD ->
                 ItemCreator.of(CompMaterial.ICE).name("Cold").make();
            case FROZEN ->
                ItemCreator.of(CompMaterial.PACKED_ICE).name("Frozen").make();
        };
    }
}
