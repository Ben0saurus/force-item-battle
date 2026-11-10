package me.benosaurus.forceItemBattle.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SkipListener implements Listener {

    @EventHandler
    public void UseEvent(PlayerInteractEvent event) {

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        ItemStack item = event.getItem();

        if (item == null || item.getType().isAir()) return;
        if (!(item.getType() == Material.BARRIER)) return;


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            int currentAmount = item.getAmount();
            if (currentAmount > 1) {
                item.setAmount(currentAmount - 1);
            } else {
                event.getPlayer().getInventory().setItem(event.getHand(), null);
            }

            event.setCancelled(true);

            event.getPlayer().sendMessage("Skipped Item:" );

        }

    }

}
