package fr.franklin.potionstacker;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author @franklintra (362694)
 * @project PotionStacker
 */
public final class Msg {
    private static final String defaultPrefix = "&7[&6PotionStacker&7] ";
    public static void sendChat(Player player, String message) {
        sendChat(player, message, defaultPrefix);
    }

    public static void sendChat(Player player, String message, String  prefix) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, defaultPrefix);
    }

    public static void sendTitle(Player player, String title, String subtitle, String prefix) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', prefix + title), ChatColor.translateAlternateColorCodes('&', prefix + subtitle), 10, 70, 20);
    }
}
