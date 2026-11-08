package me.benosaurus.forceItemBattle.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage((Component.text(player.getName() + "  left the server!")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)));
    }

}
