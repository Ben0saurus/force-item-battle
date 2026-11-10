package me.benosaurus.forceItemBattle.listeners;

import me.benosaurus.forceItemBattle.commands.GameCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage((Component.text("Welcome to the Server " + player.getName() + "!")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)));


        World gameWorld = GameCommand.getNewWorld();

        if (gameWorld != null) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().teleport(gameWorld.getSpawnLocation());
        } else {
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);

            World world = event.getPlayer().getWorld();
            world.setDifficulty(Difficulty.PEACEFUL);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage((Component.text(player.getName() + "  left the server!")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)));
    }

}