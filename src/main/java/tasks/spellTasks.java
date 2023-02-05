package tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class spellTasks extends BukkitRunnable {

    private Player player;
    private Block block;

    public spellTasks(Player player) {
        this.player = player;
        this.block = null;
    }

    @Override
    public void run() {
        if (block != null && block.getType() == Material.LIGHT)
            block.setType(Material.AIR);

        if (!player.isOnline()) {
            cancel();
            return;
        }

        ItemStack itemHeld = player.getInventory().getItemInMainHand();

        if (itemHeld.getType() != Material.STICK) {
            cancel();
            return;
        }

        block = player.getLocation().getBlock();
        block.setType(Material.LIGHT);
    }
}
