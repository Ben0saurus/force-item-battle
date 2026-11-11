package me.benosaurus.forceItemBattle.listeners;

import me.benosaurus.forceItemBattle.ForceItemBattle;
import me.benosaurus.forceItemBattle.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

public class SkipListener implements Listener {

    private final ForceItemBattle plugin;
    private final GameManager gameManager;

    public SkipListener(ForceItemBattle plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void UseEvent(PlayerInteractEvent event) {

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        ItemStack item = event.getItem();

        if (item == null || item.getType().isAir()) return;
        if (!(item.getType() == Material.BARRIER)) return;


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            Player player = event.getPlayer();

            int currentAmount = item.getAmount();
            if (currentAmount > 1) {
                item.setAmount(currentAmount - 1);
            } else {
                player.getInventory().setItem(event.getHand(), null);
            }

            event.setCancelled(true);

            Team team = player.getScoreboard().getEntryTeam(player.getName());
            if (team == null) return;

            Material goalItem = gameManager.getCurrentItem(team);

            player.sendMessage(Component.text("Skipped Item: " + goalItem.name(), NamedTextColor.GOLD));
            ItemStack skippedItem = new ItemStack(goalItem);
            player.give(skippedItem);
            gameManager.assignNewGoal(team);
            player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);

        }

    }

}
