package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.mainUtils;

public class mainCommands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player)sender;

            if (!player.hasPermission("spells.main"))
                return true;

            mainUtils.giveWizardWand(player);
            player.sendMessage(ChatColor.DARK_PURPLE + "As you wish...");
        }

        return true;
    }
}
