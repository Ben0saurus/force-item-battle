package me.benosaurus.forceItemBattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;

public class GameCommand {

    private static String newWorldName = null;
    private static World spawnWorld = null;
    private static World newWorld = null;

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("game")
                .then(Commands.literal("start")
                        .executes(context -> {

                            spawnWorld = context.getSource().getPlayerOrThrow().getWorld();

                            newWorldName = "world_" + System.currentTimeMillis();

                            WorldCreator creator = new WorldCreator(newWorldName);
                            creator.seed(new java.util.Random().nextLong());

                            newWorld = creator.createWorld();

                            newWorld.setTime(0);
                            newWorld.setStorm(false);
                            newWorld.setThundering(false);

                            Location spawnLoc = newWorld.getSpawnLocation();

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.teleport(spawnLoc);
                                player.setGameMode(GameMode.SURVIVAL);
                                player.getInventory().clear();
                                player.sendMessage("You have been teleported to a randomly generated new world!");
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("end")
                        .executes(context -> {

                            if (newWorldName == null) {
                                context.getSource().getSender().sendMessage("No game is currently running.");
                                return Command.SINGLE_SUCCESS;
                            } else {

                                Location spawnLoc = spawnWorld.getSpawnLocation();

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.teleport(spawnLoc);
                                    player.setGameMode(GameMode.ADVENTURE);
                                    player.getInventory().clear();
                                    player.sendMessage("The round has been ended and you have been teleported back to lobby!");
                                }

                                World worldToUnload = Bukkit.getWorld(newWorldName);

                                if (worldToUnload != null) {
                                    File worldFolder = worldToUnload.getWorldFolder();
                                    boolean unloaded = Bukkit.unloadWorld(worldToUnload, false);

                                    if (unloaded) {
                                        deleteWorldFolder(worldFolder);
                                    }
                                }

                                newWorldName = null;
                                newWorld = null;
                                spawnWorld = null;
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }

    private static boolean deleteWorldFolder(File path) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteWorldFolder(file);
                }
            }
        }
        return path.delete();
    }

    public static World getNewWorld() {
        return newWorld;
    }
}