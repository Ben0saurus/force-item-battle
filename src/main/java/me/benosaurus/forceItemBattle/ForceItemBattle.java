package me.benosaurus.forceItemBattle;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.benosaurus.forceItemBattle.commands.GameCommand;
import me.benosaurus.forceItemBattle.commands.HealCommand;
import me.benosaurus.forceItemBattle.commands.TeamCommand;
import me.benosaurus.forceItemBattle.game.GameManager;
import me.benosaurus.forceItemBattle.listeners.ConnectionListener;
import me.benosaurus.forceItemBattle.listeners.RespawnListener;
import me.benosaurus.forceItemBattle.listeners.SkipListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class ForceItemBattle extends JavaPlugin {


    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new SkipListener(), this);

        registerCommand("heal", List.of("h"), new HealCommand());

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(TeamCommand.create());
            event.registrar().register(GameCommand.create(this));
        });
    }

    @Override
    public void onDisable() {
    }

    public ForceItemBattle getInstance() {
        return this;
    }

}
