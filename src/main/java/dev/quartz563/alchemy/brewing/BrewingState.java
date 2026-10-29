package dev.quartz563.alchemy.brewing;

import dev.quartz563.alchemy.structures.IStructureState;
import dev.quartz563.alchemy.structures.impl.cauldron.AlchemicalCauldron;
import dev.quartz563.alchemy.brewing.potion.PotionIngredient;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.nbt.NBT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//maybe this needs to be moved out of the brewing subfolder and into the structure one for states!

/**
 * Tracks the current state of an in-progress brew inside an {@link AlchemicalCauldron}.
 * <p>
 * All player actions — adding ingredients, stirring, adjusting heat — are recorded
 * as a flat ordered list of {@link BrewingStep}s for later comparison against a recipe.
 * This also includes the current temperature of the cauldron to be reflected in the
 * brewing minigame menu
 */
public final class BrewingState implements IStructureState {

    private final List<BrewingStep> steps = new ArrayList<>();
    private final Location location;

    public BrewingState(Location location) {
        this.location = location;
    }

    //default state is always warm - increase and decrease via state
    private TemperatureState currentTemp = TemperatureState.WARM;

    /** Records one unit of the given ingredient being added to the cauldron. */
    public void addIngredient(ItemStack item) {
        ProcessingState state = NBT.get(item, nbt -> {
           if(nbt.getString("AlchemyProcessState") != null)
               return ProcessingState.valueOf(nbt.getString("AlchemyProcessState"));
           else
               return ProcessingState.NONE;
        });
        steps.add(new BrewingStep.IngredientStep(new PotionIngredient(item.getType(), state, currentTemp)));
    }

    /** Records a non-ingredient player action such as stirring or adjusting heat. */
    public void addAction(BrewingAction action) {
        steps.add(new BrewingStep.ActionStep(action));
    }

    /** Returns the ordered list of all steps taken so far. */
    public List<BrewingStep> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public void increaseTemp() {
        currentTemp = currentTemp.next();
    }

    public void decreaseTemp() {
        currentTemp = currentTemp.previous();
    }

    /** Returns true if no steps have been recorded yet. */
    public boolean isEmpty() {
        return steps.isEmpty();
    }

    /** Resets the brewing state, clearing all recorded steps. */
    public void reset() {
        steps.clear();
    }

    public TemperatureState getCurrentTemp() {
        return currentTemp;
    }

    @Override
    public Location location() {
        return location;
    }
}
