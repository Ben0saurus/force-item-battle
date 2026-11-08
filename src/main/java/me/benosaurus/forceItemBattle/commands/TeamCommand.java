package me.benosaurus.forceItemBattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamCommand {

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("team")
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.literal("set")
                                .then(Commands.argument("color", ArgumentTypes.namedColor())
                                        .executes(context -> {

                                            PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                            Player target = targetResolver.resolve(context.getSource()).getFirst();
                                            NamedTextColor color = context.getArgument("color", NamedTextColor.class);
                                            Scoreboard score = target.getScoreboard();


                                            Team currentTeam = target.getScoreboard().getEntityTeam(target);

                                            if (currentTeam != null) {
                                                currentTeam.removeEntry(target.getName());
                                            }


                                            String teamName = "team_" + color.toString().toLowerCase();
                                            Team targetTeam = score.getTeam(teamName);

                                            if (targetTeam == null) {
                                                targetTeam = score.registerNewTeam(teamName);
                                                targetTeam.color(color);
                                            }


                                            World gameWorld = GameCommand.getNewWorld();
                                            if (gameWorld != null) {
                                                target.teleport(gameWorld.getSpawnLocation());
                                                target.setGameMode(GameMode.SURVIVAL);
                                            }


                                            targetTeam.addPlayer(target);
                                            Component message = Component.text("")
                                                    .append(Component.text(target.getName(), color, TextDecoration.BOLD))
                                                    .append(Component.text(" has successfully been added to team "))
                                                    .append(Component.text(color.toString().toLowerCase(), color, TextDecoration.BOLD));
                                            context.getSource().getSender().sendMessage(message);

                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(Commands.literal("clear")
                                .executes(context -> {

                                    PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    Player target = targetResolver.resolve(context.getSource()).getFirst();

                                    Team currentTeam = target.getScoreboard().getEntityTeam(target);

                                    if (currentTeam != null) {
                                        currentTeam.removeEntry(target.getName());
                                    }

                                    context.getSource().getSender().sendMessage("Successfully cleared players team.");

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
    }

}
