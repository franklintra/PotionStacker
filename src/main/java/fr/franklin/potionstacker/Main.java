package fr.franklin.potionstacker;

import fr.franklin.potionstacker.Commands.Pot;
import fr.franklin.potionstacker.listeners.DeathHandler;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Pot potHandler = new Pot();
        // Plugin startup logic
        //register all Commands in Commands package
        //register all Listeners in Listeners package

        //registering pot command
        Objects.requireNonNull(getCommand("pot")).setExecutor(potHandler);
        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
