package me.benosaurus.forceItemBattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.benosaurus.forceItemBattle.ForceItemBattle;
import me.benosaurus.forceItemBattle.UI.Countdown;
import me.benosaurus.forceItemBattle.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.Collection;
import java.util.Random;

public class GameCommand {

    private static Countdown gameTimer;

    private static String newWorldName = null;
    private static World spawnWorld = null;
    private static World newWorld = null;

    public static LiteralCommandNode<CommandSourceStack> create(ForceItemBattle plugin) {
        return Commands.literal("game")
                .then(Commands.literal("start")
                        .executes(context -> {

                            spawnWorld = context.getSource().getPlayerOrThrow().getWorld();
                            GameManager gameManager = plugin.getGameManager();

                            Collection<Team> teams = Bukkit.getScoreboardManager().getMainScoreboard().getTeams();
                            gameManager.startGame(teams);

                            ItemStack skips = new ItemStack(Material.BARRIER, 3);
                            ItemMeta meta = skips.getItemMeta();
                            meta.displayName(Component.text("Skip Item", NamedTextColor.RED));
                            skips.setItemMeta(meta);

                            newWorldName = "world_" + System.currentTimeMillis();

                            WorldCreator creator = new WorldCreator(newWorldName);
                            creator.seed(new Random().nextLong());

                            newWorld = creator.createWorld();

                            newWorld.setTime(0);
                            newWorld.setStorm(false);
                            newWorld.setThundering(false);
                            newWorld.setDifficulty(Difficulty.EASY);

                            Location spawnLoc = newWorld.getSpawnLocation();

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.setHealth(20);
                                player.setSaturation(20);
                                player.setFoodLevel(20);

                                player.teleport(spawnLoc);
                                player.setGameMode(GameMode.SURVIVAL);
                                player.getInventory().clear();
                                player.getInventory().addItem(skips);
                                player.setFallDistance(0);
                                player.sendMessage("You have been teleported to a randomly generated new world!");
                            }

                            gameTimer = new Countdown(plugin, 3600);
                            gameTimer.runTaskTimer(plugin, 0L, 20L);
                            gameTimer.setRunning(true);

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
                                spawnWorld.setDifficulty(Difficulty.PEACEFUL);

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.setHealth(20);
                                    player.setSaturation(20);
                                    player.setFoodLevel(20);

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

                                gameTimer.setRunning(false);
                                gameTimer.cancel();

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