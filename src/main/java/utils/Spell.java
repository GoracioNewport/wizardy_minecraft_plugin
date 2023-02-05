package utils;

import goracionewport.spells_plugin.Spells_plugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

interface castFunction {
    public boolean cast(Spells_plugin plugin, Player player);
}
public class Spell {
    public Integer id;
    public Material icon;
    public String name;

    public String magicWords;

    public Sound sound;

    public castFunction cast;

    public String description;

    Spell (Integer id, Material icon, String name, String magicWords, Sound sound, castFunction cast, String description) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.magicWords = magicWords;
        this.sound = sound;
        this.cast = cast;
        this.description = description;
    }

}
