package dev.quartz563.alchemy.transmutation;

import dev.quartz563.alchemy.element.AlchemicalRepresentation;
import dev.quartz563.alchemy.element.Element;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.nbt.NBT;
import org.mineacademy.fo.remain.nbt.NBTCompound;
import org.mineacademy.fo.remain.nbt.NBTList;

/**
 * Manages the NBT schema for items that have undergone alchemical preparation.
 *
 * <p>A prepared item is a physical item whose material has changed to reflect
 * its most recent transformation stage. Its original material identity, the ordered
 * list of applied stages, and its current alchemical representation are all
 * stored in NBT under a namespaced root.
 *
 * <p>NBT structure:
 * <pre>
 * Alchemy: {
 *     Prepared: {
 *         OriginalMaterial: "DIRT"
 *         Stages: ["REDUCTION", "DISSOLUTION"]
 *         Representation: {
 *             EARTH: 3
 *             FIRE: 1
 *             WATER: 2
 *         }
 *     }
 * }
 * </pre>
 *
 * <p>All methods that produce items return new {@link ItemStack} instances —
 * the original item is never mutated.
 */
public final class PreparedItem {

    private static final String KEY_ROOT = "Alchemy";
    private static final String KEY_PREPARED = "Prepared";
    private static final String KEY_ORIGINAL = "OriginalMaterial";
    private static final String KEY_STAGES = "Stages";
    private static final String KEY_REPR = "Representation";

    private PreparedItem() {}

    public static boolean isPrepared(final ItemStack item) {
        if(item == null || item.getType().isAir())
            return false;
        return NBT.get(item, nbt -> {
            final NBTCompound root = (NBTCompound) nbt.getCompound(KEY_ROOT);
            return root != null && root.hasTag(KEY_PREPARED);
        });
    }

    /***
     * Creates a prepared {@link ItemStack} of a specific {@link TransformationType} and {@link AlchemicalRepresentation}.
     * Output item will have custom NBT values to dictate what the correct values are, and will include the following
     * - Original item
     * - All alchemical stages
     * - Current alchemical representation after transformations
     * Refer to documentation in {@link PreparedItem} for correct NBT structure
     * @param source Original {@link ItemStack} before item transformation
     * @param stage Stage of alchemic working from {@link TransformationType}
     * @param inputRepresentation {@link AlchemicalRepresentation} of input item
     * @return {@link ItemStack} Prepared item with NBT structure
     */
    public static ItemStack prepare(final ItemStack source, final TransformationType stage, final AlchemicalRepresentation inputRepresentation) {
        final Material originalMaterial = source.getType();
        final AlchemicalRepresentation result = stage.apply(inputRepresentation);

        //this may need more work to make it kinda nicer looking; me thinks
        //this works! huzzah! I just need to modify this to add in a tag that means it cannot be used in crafting or normal brewing
        //todo currently this does not work for stacked processes, as the input item will sorta error.
        ItemStack output = ItemCreator.of(CompMaterial.valueOf(stage.getOutputMaterial().name()))
                .name(stage.getDisplayName() + " of " + ItemUtil.bountifyCapitalized(originalMaterial.name()))
                .glow(true)
                .lore(
                        "An alchemical " + stage.getDisplayName() + " of " + ItemUtil.bountifyCapitalized(originalMaterial.name()),
                        "Alchemical Representation: " + ItemUtil.bountifyCapitalized(result.toString()))
                .make();
        NBT.modify(output, (nbt) -> {
            NBTCompound root = (NBTCompound) nbt.getOrCreateCompound(KEY_ROOT);
            NBTCompound prepared = root.addCompound(KEY_PREPARED);
            prepared.setString(KEY_ORIGINAL, originalMaterial.name());
            NBTList<String> stages = prepared.getStringList(KEY_STAGES);
            stages.add(stage.getDisplayName());
            NBTCompound representations = prepared.addCompound(KEY_REPR);
            for(Element e : result.elements().keySet()){
                //for the love of the gods, do NOT change the e.name() to display name.
                representations.setInteger(e.name(), result.getQuantity(e));
            }
        });
        return output;
    }

}
