package me.benosaurus.forceItemBattle;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.benosaurus.forceItemBattle.commands.HealCommand;
import me.benosaurus.forceItemBattle.commands.TeamCommand;
import me.benosaurus.forceItemBattle.listeners.ConnectionListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class ForceItemBattle extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);

        registerCommand("heal", List.of("h"), new HealCommand());
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(TeamCommand.create());
        });
    }

    @Override
    public void onDisable() {
    }
}
