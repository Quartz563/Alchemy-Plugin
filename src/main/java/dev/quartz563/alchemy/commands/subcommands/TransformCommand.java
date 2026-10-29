package dev.quartz563.alchemy.commands.subcommands;

import dev.quartz563.alchemy.element.AlchemicalRepresentation;
import dev.quartz563.alchemy.registeries.RepresentationRegistry;
import dev.quartz563.alchemy.transmutation.PreparedItem;
import dev.quartz563.alchemy.transmutation.TransformationType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public class TransformCommand extends SimpleSubCommand {

    public TransformCommand(SimpleCommandGroup parent) {
        super(parent, "transform");
        setDescription("Preforms a transformation on the object held");
        setUsage("<reduce|dissolve|recombine|refine>");
        setMinArguments(1);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        TransformationType transform = switch (args[0]) {
            case "reduce" -> TransformationType.REDUCTION;
            case "dissolve" -> TransformationType.DISSOLUTION;
            case "recombine" -> TransformationType.RECOMBINATION;
            case "refine" -> TransformationType.REFINEMENT;
            default -> throw new IllegalStateException("Unexpected value: " + args[0]);
        };

        //theoretically, this *should* be how transformations occur outside commands too
        //just, well, more set. I think?

        final Player player = getPlayer();
        final ItemStack heldItem = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        final Material heldItemType = heldItem.getType();
        final AlchemicalRepresentation heldItemRepresentation = RepresentationRegistry.get(heldItemType)
                .orElse(null);
        final ItemStack output = PreparedItem.prepare(heldItem, transform, heldItemRepresentation);
        player.setItemOnCursor(output);
        tell("Alchemic transformation applied: " + args[0]);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            return completeLastWord(
                    "reduce",
                    "dissolve",
                    "recombine",
                    "refine");
        }
        return NO_COMPLETE;
    }
}
