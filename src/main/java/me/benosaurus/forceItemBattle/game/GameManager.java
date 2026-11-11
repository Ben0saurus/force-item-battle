package me.benosaurus.forceItemBattle.game;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class GameManager {

    private boolean isRunning = false;

    private final Map<Team, Material> teamGoals = new HashMap<>();
    private final Map<Team, Integer> teamScores = new HashMap<>();
    private final List<Material> itemPool = new ArrayList<>();

    private final Random random = new Random();

    JavaPlugin plugin;

    public GameManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void startGame(Collection<Team> activeTeams) {

        isRunning = true;

        teamGoals.clear();
        teamScores.clear();;

        for (Team team : activeTeams) {
            teamScores.put(team, 0);
            assignNewGoal(team);
        }

    }


    public void assignNewGoal(Team team) {

        if (itemPool.isEmpty()) return;

        Material randomMaterial = itemPool.get(random.nextInt(itemPool.size()));
        teamGoals.put(team, randomMaterial);

        for (org.bukkit.OfflinePlayer offlinePlayer : team.getPlayers()) {
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                player.sendMessage("Next item: " + randomMaterial.name());
                String name = player.getName();
                player.setPlayerListName(name + " [" + randomMaterial.name() + "]");
            }
        }

    }

    public void clearGoal(Team team) {
        teamGoals.clear();
        teamScores.clear();
    }


    public Material getCurrentItem(Team team) {
        return teamGoals.get(team);
    }


    public void loadItemPool() {
        List<String> itemNames = plugin.getConfig().getStringList("item-pool");

        itemPool.clear();
        for (String name : itemNames) {
            try {
                Material mat = Material.valueOf(name);
                itemPool.add(mat);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Couldn't find item " + name);
            }
        }
    }


}