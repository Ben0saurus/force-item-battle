package me.benosaurus.forceItemBattle.UI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private final JavaPlugin plugin;
    public int secondsLeft;
    public int orginialTime;

    public boolean isRunning = false;

    public Countdown(JavaPlugin plugin, int timeLeft) {
        this.plugin = plugin;
        secondsLeft = timeLeft;
        orginialTime = secondsLeft;
    }

    @Override
    public void run() {

        if (isRunning) {
            Component countdown;

            int minutesLeft = secondsLeft / 60;
            int afterMinutesSeconds = secondsLeft % 60;

            countdown = Component.text(minutesLeft + ":" + afterMinutesSeconds, NamedTextColor.YELLOW);

            if (secondsLeft > 0) {
                secondsLeft--;
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(countdown);
            }

        } else {
            Component countdown;

            int minutesLeft = orginialTime / 60;
            int afterMinutesSeconds = orginialTime % 60;
            countdown = Component.text(minutesLeft + ":" + afterMinutesSeconds, NamedTextColor.YELLOW);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(countdown);
            }
        }

    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
