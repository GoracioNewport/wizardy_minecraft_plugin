package goracionewport.spells_plugin;

import commands.mainCommands;
import listeners.mainListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Spells_plugin extends JavaPlugin {

    private static Spells_plugin instance;

    @Override
    public void onEnable() {

        System.out.println("Plugin started");

        instance = this;

        Objects.requireNonNull(getCommand("give_wizard_wand")).setExecutor(new mainCommands());
        getServer().getPluginManager().registerEvents(new mainListener(), this);

    }

    public static Spells_plugin getInstance() {
        return instance;
    }
}
