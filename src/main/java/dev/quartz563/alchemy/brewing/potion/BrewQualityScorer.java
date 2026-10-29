package dev.quartz563.alchemy.brewing.potion;

import dev.quartz563.alchemy.brewing.BrewingAction;
import dev.quartz563.alchemy.brewing.BrewingStep;

import java.util.List;

/**
 * Scores an actual brewing sequence against a recipe using weighted fuzzy matching.
 * <p>
 * Weights:
 *   Ingredient order : 40%
 *   Ingredient state : 30%
 *   Temperature      : 20%
 *   Stirring         : 10%
 * <p>
 * All individual scores are in the range [0.0, 1.0].
 * The final score is their weighted sum, also in [0.0, 1.0].
 * <p>
 * When counts differ, the penalty is: matched / max(recipe count, actual count).
 */
public class BrewQualityScorer {

    private static final double WEIGHT_INGREDIENT_ORDER = 0.40;
    private static final double WEIGHT_INGREDIENT_STATE = 0.30;
    private static final double WEIGHT_TEMPERATURE = 0.20;
    private static final double WEIGHT_STIRRING = 0.10;

    private BrewQualityScorer() {}

    /**
     * Returns a quality score in [0.0, 1.0] representing how closely the actual
     * brewing steps match the recipe steps.
     *
     * @param recipe the expected step sequence from the recipe definition
     * @param actual the step sequence recorded from the player's actions
     */
    public static double score(List<BrewingStep> recipe, List<BrewingStep> actual) {
        final List<BrewingStep.IngredientStep> recipeIngredients = extractIngredients(recipe);
        final List<BrewingStep.IngredientStep> actualIngredients = extractIngredients(actual);
        final double ingredientOrderScore = scoreIngredientOrder(recipeIngredients, actualIngredients);
        final double ingredientStateScore = scoreIngredientState(recipeIngredients, actualIngredients);
        final double temperatureScore = scoreIngredientTemperature(recipeIngredients, actualIngredients);
        final double stirringScore = scoreActions(extractStirring(recipe), extractStirring(actual));

        return (ingredientOrderScore * WEIGHT_INGREDIENT_ORDER)
                + (ingredientStateScore * WEIGHT_INGREDIENT_STATE)
                + (temperatureScore * WEIGHT_TEMPERATURE)
                + (stirringScore * WEIGHT_STIRRING);

    }

    private static double scoreIngredientOrder(List<BrewingStep.IngredientStep> recipe, List<BrewingStep.IngredientStep> actual) {
        if(recipe.isEmpty() && actual.isEmpty())
            return 1.0;
        final int maxLen = Math.max(recipe.size(), actual.size());
        final int minLen = Math.min(recipe.size(), actual.size());
        int matched = 0;
        for(int i = 0; i < minLen; i++) {
            if(recipe.get(i).ingredient().material() == actual.get(i).ingredient().material())
                matched ++;
        }
        return (double) matched / maxLen;
    }

    private static double scoreIngredientState(List<BrewingStep.IngredientStep> recipe, List<BrewingStep.IngredientStep> actual) {
        if(recipe.isEmpty() && actual.isEmpty())
            return 1.0;
        final int maxLen = Math.max(recipe.size(), actual.size());
        final int minLen = Math.min(recipe.size(), actual.size());
        int matched = 0;
        for(int i = 0; i < minLen; i++) {
            if(recipe.get(i).ingredient().state() == actual.get(i).ingredient().state())
                matched ++;
        }
        return (double) matched / maxLen;
    }

    private static double scoreIngredientTemperature(List<BrewingStep.IngredientStep> recipe, List<BrewingStep.IngredientStep> actual) {
        if(recipe.isEmpty() && actual.isEmpty())
            return 1.0;
        final int maxLen = Math.max(recipe.size(), actual.size());
        final int minLen = Math.min(recipe.size(), actual.size());
        int matched = 0;
        for(int i = 0; i < minLen; i++) {
            if(recipe.get(i).ingredient().temperature() == actual.get(i).ingredient().temperature())
                matched ++;
        }
        return (double) matched / maxLen;
    }

    private static double scoreActions(List<BrewingAction> recipe, List<BrewingAction> actual) {
        if(recipe.isEmpty() && actual.isEmpty())
            return 1.0;
        if(recipe.isEmpty())
            return 0.0;
        final int maxLen = Math.max(recipe.size(), actual.size());
        final int minLen = Math.min(recipe.size(), actual.size());
        int matched = 0;
        for(int i = 0; i < minLen; i++) {
            if(recipe.get(i) == actual.get(i))
                matched++;
        }
        return (double) matched / maxLen;
    }

    private static List<BrewingStep.IngredientStep> extractIngredients(List<BrewingStep> steps) {
        return steps.stream()
                .filter(s -> s instanceof BrewingStep.IngredientStep)
                .map(s -> (BrewingStep.IngredientStep)s)
                .toList();
    }


    private static List<BrewingAction> extractStirring(List<BrewingStep> steps) {
        return steps.stream()
                .filter(s -> s instanceof BrewingStep.ActionStep action && (action.action() == BrewingAction.STIR_CLOCKWISE || action.action() == BrewingAction.STIR_COUNTERCLOCKWISE))
                .map(s -> ((BrewingStep.ActionStep) s).action())
                .toList();
    }
}
