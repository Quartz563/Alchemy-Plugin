package dev.quartz563.alchemy.brewing;

import dev.quartz563.alchemy.brewing.potion.PotionIngredient;

public sealed interface BrewingStep permits BrewingStep.IngredientStep, BrewingStep.ActionStep {

    record IngredientStep(PotionIngredient ingredient) implements BrewingStep {}

    record ActionStep(BrewingAction action) implements BrewingStep {}

}
