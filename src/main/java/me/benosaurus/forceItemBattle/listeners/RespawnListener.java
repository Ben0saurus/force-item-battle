package me.benosaurus.forceItemBattle.listeners;

import me.benosaurus.forceItemBattle.commands.GameCommand;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.net.http.WebSocket;

public class RespawnListener implements Listener {

    @EventHandler
    public void respawnListener(PlayerRespawnEvent event) {

        World activeGameWorld = GameCommand.getNewWorld();

        if (activeGameWorld != null) {

            event.setRespawnLocation(activeGameWorld.getSpawnLocation());

        }

    }

}
