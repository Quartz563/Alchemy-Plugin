package dev.quartz563.alchemy.brewing.potion;

import dev.quartz563.alchemy.brewing.BrewingAction;
import dev.quartz563.alchemy.brewing.BrewingStep;
import dev.quartz563.alchemy.brewing.ProcessingState;
import dev.quartz563.alchemy.brewing.TemperatureState;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Score weights: ingredient order 40%, ingredient state 30%,
 * temperature 20%, stirring 10%.
 */
class BrewQualityScorerTest {

    private static final double DELTA = 1e-9;

    private static BrewingStep ingredient(Material material, ProcessingState state, TemperatureState temperature) {
        return new BrewingStep.IngredientStep(new PotionIngredient(material, state, temperature));
    }

    private static BrewingStep stir(BrewingAction action) {
        return new BrewingStep.ActionStep(action);
    }

    private static final BrewingStep CRUSHED_REDSTONE_COLD =
            ingredient(Material.REDSTONE, ProcessingState.CRUSHED, TemperatureState.COLD);
    private static final BrewingStep POWDERED_BONE_HOT =
            ingredient(Material.BONE_MEAL, ProcessingState.POWDERED, TemperatureState.HOT);

    @Test
    void exactMatchScoresOne() {
        List<BrewingStep> recipe = List.of(CRUSHED_REDSTONE_COLD, POWDERED_BONE_HOT, stir(BrewingAction.STIR_CLOCKWISE));

        assertEquals(1.0, BrewQualityScorer.score(recipe, recipe), DELTA);
    }

    @Test
    void emptyRecipeAndEmptyBrewScoreOne() {
        assertEquals(1.0, BrewQualityScorer.score(List.of(), List.of()), DELTA);
    }

    @Test
    void wrongTemperatureOnOneOfTwoIngredientsLosesHalfTheTemperatureWeight() {
        List<BrewingStep> recipe = List.of(CRUSHED_REDSTONE_COLD, POWDERED_BONE_HOT);
        List<BrewingStep> actual = List.of(CRUSHED_REDSTONE_COLD,
                ingredient(Material.BONE_MEAL, ProcessingState.POWDERED, TemperatureState.WARM));

        // order 1.0, state 1.0, temperature 0.5, stirring 1.0 (none expected, none done)
        assertEquals(0.4 + 0.3 + 0.1 + 0.1, BrewQualityScorer.score(recipe, actual), DELTA);
    }

    @Test
    void swappedIngredientOrderLosesOrderWeightOnly() {
        BrewingStep redstone = ingredient(Material.REDSTONE, ProcessingState.NONE, TemperatureState.COLD);
        BrewingStep bone = ingredient(Material.BONE_MEAL, ProcessingState.NONE, TemperatureState.COLD);

        // Same state and temperature at each position, but the materials are swapped.
        double score = BrewQualityScorer.score(List.of(redstone, bone), List.of(bone, redstone));

        assertEquals(0.0 + 0.3 + 0.2 + 0.1, score, DELTA);
    }

    @Test
    void extraIngredientIsPenalisedByLongerSequence() {
        List<BrewingStep> recipe = List.of(CRUSHED_REDSTONE_COLD);
        List<BrewingStep> actual = List.of(CRUSHED_REDSTONE_COLD, CRUSHED_REDSTONE_COLD);

        // order, state and temperature each 1 of 2 matched; stirring unaffected
        assertEquals(0.5 * 0.4 + 0.5 * 0.3 + 0.5 * 0.2 + 0.1, BrewQualityScorer.score(recipe, actual), DELTA);
    }

    @Test
    void missingRequiredStirLosesStirringWeight() {
        List<BrewingStep> recipe = List.of(CRUSHED_REDSTONE_COLD, stir(BrewingAction.STIR_CLOCKWISE));
        List<BrewingStep> actual = List.of(CRUSHED_REDSTONE_COLD);

        assertEquals(0.9, BrewQualityScorer.score(recipe, actual), DELTA);
    }

    @Test
    void stirringInTheWrongDirectionCountsAsAMismatch() {
        List<BrewingStep> recipe = List.of(CRUSHED_REDSTONE_COLD, stir(BrewingAction.STIR_CLOCKWISE));
        List<BrewingStep> actual = List.of(CRUSHED_REDSTONE_COLD, stir(BrewingAction.STIR_COUNTERCLOCKWISE));

        assertEquals(0.9, BrewQualityScorer.score(recipe, actual), DELTA);
    }

    @Test
    void stirringWhenRecipeHasNoneLosesStirringWeight() {
        List<BrewingStep> recipe = List.of(CRUSHED_REDSTONE_COLD);
        List<BrewingStep> actual = List.of(CRUSHED_REDSTONE_COLD, stir(BrewingAction.STIR_CLOCKWISE));

        assertEquals(0.9, BrewQualityScorer.score(recipe, actual), DELTA);
    }
}
