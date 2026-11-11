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
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.scoreboard.Team;

public class ItemListener implements Listener {

    private final ForceItemBattle plugin;
    private final GameManager gameManager;

    public ItemListener(ForceItemBattle plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void itemListener(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        Team team = player.getScoreboard().getEntryTeam(player.getName());
        if (team == null) return;

        Material item = event.getItem().getItemStack().getType();
        Material goalItem = gameManager.getCurrentItem(team);

        if (!(item == goalItem)) return;

        player.sendMessage(Component.text("Found item: " + goalItem.name(), NamedTextColor.GREEN));
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        gameManager.assignNewGoal(team);
        Material newGoal = gameManager.getCurrentItem(team);

    }

}
