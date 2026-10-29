package dev.quartz563.alchemy.tools;

import dev.quartz563.alchemy.menus.AlmanacMainMenu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;


public final class AlmanacTool extends Tool {

    private AlmanacTool(){}

    @Getter
    private static final AlmanacTool instance = new AlmanacTool();

    @Override
    public ItemStack getItem() {
        return ItemCreator.of(CompMaterial.BOOK)
                .name("&6Alchemic Almanac")
                .lore(
                        "&7A record of your alchemical discoveries."
                )
                .glow(true)
                .make();
    }

    @Override
    protected void onBlockClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        final Player player = event.getPlayer();
        new AlmanacMainMenu(player).displayTo(player);
    }

    @Override
    protected boolean ignoreCancelled() {
        return false;
    }
}
