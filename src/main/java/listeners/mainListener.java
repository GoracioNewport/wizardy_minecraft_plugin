package listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import utils.mainUtils;

public class mainListener implements Listener {


    @EventHandler
    public void onWandClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null || event.getItem().getType() != Material.STICK)
            return;

        if (!player.hasPermission("spells.main"))
            return;

        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            mainUtils.castSpell(player);

            event.setCancelled(true);
        }


        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
            mainUtils.spawnSpellMenu(player);
    }

    @EventHandler
    public void onMenuSelection(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("Select a spell"))
            return;

        Player player = (Player) event.getWhoClicked();

        mainUtils.setSpellByInventoryId(player, event.getSlot());

        player.closeInventory();
        event.setCancelled(true);

    }

}
