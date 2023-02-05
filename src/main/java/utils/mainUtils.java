package utils;

import goracionewport.spells_plugin.Spells_plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class mainUtils {
private static final HashMap<Player, Integer> selectedSpell = new HashMap<>();

    private static final ArrayList<Spell> spellList = new ArrayList<Spell>() {{
        add(new Spell(0, Material.TNT, "Explosion", "Bombarda!", Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, spellCastFunctions::castExplosion, "Summons a destructive explosion"));
        add(new Spell(1, Material.BLUE_ICE, "Ice Peak", "Spikus!", Sound.ENTITY_ILLUSIONER_CAST_SPELL, spellCastFunctions::castIcePeak, "Tosses up a caster and creates an ice spike beneath him"));
        add(new Spell(8, Material.SKELETON_SKULL, "Death", "Avada kedavra!", Sound.ENTITY_WITHER_DEATH, spellCastFunctions::castInstantDeath, "Spell needs no introduction"));
        add(new Spell(2, Material.LIGHTNING_ROD, "Lightning", "Baubillious!", Sound.ENTITY_LIGHTNING_BOLT_IMPACT, spellCastFunctions::castLightning, "Summons a beam of lightning"));
        add(new Spell(3,  Material.STICKY_PISTON, "Attraction", "Accio...", Sound.BLOCK_BEACON_ACTIVATE, spellCastFunctions::castAttract, "Pulls a pointed entity towards a caster"));
        add(new Spell(4, Material.LANTERN, "Light", "Lumus", Sound.ENTITY_GLOW_SQUID_SQUIRT, spellCastFunctions::castLight, "Summons an infinite light source at the tip of your wand"));
        add(new Spell(5, Material.ENDER_PEARL, "Transgression", null, Sound.ENTITY_ENDERMAN_TELEPORT, spellCastFunctions::castTeleport, "Instantly teleports caster to a pointed location"));
        add(new Spell(6, Material.CREEPER_HEAD, "Transfiguration", null, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, spellCastFunctions::castMutation, "Randomly transforms a pointed entity"));
    }};

    public static void giveWizardWand(Player player) {

        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta wandMeta = wand.getItemMeta();

        assert wandMeta != null;
        wandMeta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
        wandMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Magic Wand");

        wand.setItemMeta(wandMeta);
        player.getInventory().addItem(wand);

    }

    public static void setSpellByInventoryId(Player player, int inventoryId) {
        for (int i = 0; i < spellList.size(); ++i) {
            if (spellList.get(i).id == inventoryId) {
                selectedSpell.put(player, i);
            }
        }
    }


    public static void castSpell(Player player) {

        if (!selectedSpell.containsKey(player)) {
            setSpellByInventoryId(player, 0);
        }

        Spell spellToCast = spellList.get(selectedSpell.get(player));

        boolean castSuccess = spellToCast.cast.cast(Spells_plugin.getInstance(), player);


        if (castSuccess) {
            String magicWords = spellToCast.magicWords;

            double limitDistance = 16 * 8;

            for (Player castToPlayer : Bukkit.getOnlinePlayers()) {
                double distance = player.getLocation().distance(castToPlayer.getLocation());
                if (distance > limitDistance)
                    continue;

                castToPlayer.playSound(player.getLocation(), spellToCast.sound, (float) (1.0f * ((limitDistance - distance) / limitDistance)), 1.0f);
            }

            if (magicWords != null)
                player.chat(magicWords);
        }
    }

    public static void spawnSpellMenu(Player player) {
        Inventory menu = Bukkit.createInventory(player, 9, "Select a spell");

        for (Spell spell : spellList) {
            ItemStack spellItem = new ItemStack(spell.icon);
            ItemMeta spellItemMeta = spellItem.getItemMeta();
            assert spellItemMeta != null;
            spellItemMeta.setDisplayName(ChatColor.YELLOW + spell.name);

            ArrayList <String> lore = new ArrayList<>();
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + spell.description);

            if (spell.magicWords != null) {
                lore.add("");
                lore.add("'" + spell.magicWords + "'");
            }

            spellItemMeta.setLore(lore);
            spellItem.setItemMeta(spellItemMeta);

            menu.setItem(spell.id, spellItem);
        }

        player.openInventory(menu);
    }

}
