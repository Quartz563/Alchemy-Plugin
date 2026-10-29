package dev.quartz563.alchemy.transmutation;

import dev.quartz563.alchemy.element.AlchemicalRepresentation;
import dev.quartz563.alchemy.element.Element;
import org.bukkit.Material;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.EnumMap;
import java.util.Map;

/**
 * The four preparation process types a material may undergo before transmutation.
 *
 * <p>Each type defines one {@link ElementOperation} per element, encoding how that
 * process transforms an {@link AlchemicalRepresentation}. Operations are applied
 * per-element when {@link #apply} is called; elements with no explicit operation
 * default to {@link ElementOperation#neutral()}.
 *
 * <p>The physical item produced by each type is fixed — the original material identity
 * and current representation are preserved in the item's NBT while the renamed item
 * communicates the prepared state to the player.
 *
 * <p>Output materials are currently proxies and are expected to be replaced with
 * custom items in a future pass.
 */
public enum TransformationType {

    /**
     * Reduction of a material to its base essence through burning or decay.
     *
     * <p>Strips volatile elements (Air), halves Water and Life, reveals the hidden
     * Death within the material, and always leaves a trace of Fire from the burning.
     * The crude heat slightly diminishes magical quality while making things more base.
     *
     * <p>Covers: Calcination, Putrefaction, Mortification, Separation.
     */
    REDUCTION(
            "Reduction",
            CompMaterial.GUNPOWDER.getMaterial(),
            Map.of(
                    Element.FIRE, ElementOperation.addFlat(1),
                    Element.WATER, ElementOperation.scale(0.5),
                    Element.EARTH, ElementOperation.neutral(),
                    Element.AIR, ElementOperation.strip(),
                    Element.LIFE, ElementOperation.scale(0.5),
                    Element.DEATH, ElementOperation.scale(1.5),
                    Element.MAGIC, ElementOperation.shift(-1),
                    Element.MUNDANE, ElementOperation.shift(1)
            )
    ),

    /**
     * Dissolution of a material in solvent, shifting it toward fluidity.
     *
     * <p>Extinguishes Fire entirely, strongly amplifies Water as the medium,
     * halves Earth as solid structure dissolves, and cleanses Mundane quality.
     * A slight stirring introduces Air and nudges Life awake.
     *
     * <p>Covers: Dissolution, Ablution.
     */
    DISSOLUTION(
            "Dissolution",
            CompMaterial.POTION.getMaterial(),
            Map.of(
                    Element.FIRE, ElementOperation.strip(),
                    Element.WATER, ElementOperation.scale(2.0),
                    Element.EARTH, ElementOperation.scale(0.5),
                    Element.AIR, ElementOperation.shift(1),
                    Element.LIFE, ElementOperation.shift(1),
                    Element.DEATH, ElementOperation.neutral(),
                    Element.MAGIC, ElementOperation.neutral(),
                    Element.MUNDANE, ElementOperation.shift(-1)
            )
    ),

    /**
     * Recombination of separated elements through fermentation and conjunction.
     *
     * <p>Amplifies Life as the dominant principle of fermentation and rebirth.
     * Death recedes as life reasserts itself. Warmth, settling, and gas production
     * offer slight boosts to Fire, Earth, Air, and Magic.
     *
     * <p>Covers: Conjunction, Fermentation, Digestion.
     */
    RECOMBINATION(
            "Recombination",
            CompMaterial.FERMENTED_SPIDER_EYE.getMaterial(),
            Map.of(
                    Element.FIRE, ElementOperation.shift(1),
                    Element.WATER, ElementOperation.neutral(),
                    Element.EARTH, ElementOperation.shift(1),
                    Element.AIR, ElementOperation.shift(1),
                    Element.LIFE, ElementOperation.scale(1.5),
                    Element.DEATH, ElementOperation.scale(0.5),
                    Element.MAGIC, ElementOperation.shift(1),
                    Element.MUNDANE, ElementOperation.neutral()
            )
    ),

    /**
     * Purification and elevation of a material toward its highest quality.
     *
     * <p>Strongly amplifies Magic as the spiritual principle, amplifies Air through
     * sublimation, and strips Mundane entirely. Water evaporates, concentrating
     * what remains. Death taint is gently refined away.
     *
     * <p>Covers: Distillation, Sublimation, Exaltation, Multiplication.
     */
    REFINEMENT(
            "Refinement",
            CompMaterial.BLAZE_POWDER.getMaterial(),
            Map.of(
                    Element.FIRE, ElementOperation.shift(1),
                    Element.WATER, ElementOperation.shift(-1),
                    Element.EARTH, ElementOperation.neutral(),
                    Element.AIR, ElementOperation.scale(1.5),
                    Element.LIFE, ElementOperation.neutral(),
                    Element.DEATH, ElementOperation.shift(-1),
                    Element.MAGIC, ElementOperation.scale(2.0),
                    Element.MUNDANE, ElementOperation.strip()
            )
    );

    private final String displayName;
    private final Material outputMaterial;
    private final Map<Element, ElementOperation> operations;

    TransformationType(final String displayName, final Material material, final Map<Element, ElementOperation> operations){
        this.displayName = displayName;
        this.outputMaterial = material;
        this.operations = operations;
    }

    public AlchemicalRepresentation apply(final AlchemicalRepresentation input) {
        final Map<Element, Integer> result = new EnumMap<>(Element.class);
        for(final Element element : Element.values()) {
            final int current = input.elements().getOrDefault(element, 0);
            final int quantity = this.operations.getOrDefault(element, ElementOperation.neutral()).apply(current);
            if(quantity > 0) {
                result.put(element, quantity);
            }
        }
        return new AlchemicalRepresentation(result);
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getOutputMaterial() {
        return outputMaterial;
    }

}
