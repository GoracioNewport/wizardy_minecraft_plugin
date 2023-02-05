package utils;

import goracionewport.spells_plugin.Spells_plugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import tasks.spellTasks;

import java.util.Random;

public class spellCastFunctions {

    private static void createParticleTrail(World world, Location begin, Location end, Particle particle, double particleSpread, double multiplier) {

        Location currentLocation = begin;

        int count = 0;
        int particleLimit = 500;

        Random generator = new Random();

        while (begin.distance(currentLocation) < begin.distance(end)) {
            world.spawnParticle(particle, currentLocation, 1);
            currentLocation = currentLocation.add(end.toVector().subtract(begin.toVector()).normalize().multiply(particleSpread));

            double delta_x = generator.nextDouble() * multiplier - multiplier / 2;
            double delta_y = generator.nextDouble() * multiplier - multiplier / 2;
            double delta_z = generator.nextDouble() * multiplier - multiplier / 2;

            currentLocation = currentLocation.add(delta_x, delta_y, delta_z);


            if (count++ > particleLimit)
                break;
        }

    }

    public static boolean castIcePeak(Spells_plugin plugin, Player player) {

        player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));

        new BukkitRunnable() {

            private int state = 0;
            private int max_state = 15;

            @Override
            public void run() {
                if (state < max_state) {

                    Location location = player.getLocation();

                    if (player.getVelocity().getY() <= 0)
                        state = max_state;

                    Block bottomBlock = location.getBlock().getRelative(BlockFace.DOWN);
                    Block oBlock = bottomBlock.getRelative(BlockFace.SOUTH);
                    Block tBlock = bottomBlock.getRelative(BlockFace.NORTH);
                    Block rBlock = bottomBlock.getRelative(BlockFace.EAST);
                    Block fBlock = bottomBlock.getRelative(BlockFace.WEST);

                    bottomBlock.setType(Material.BLUE_ICE);
                    oBlock.setType(Material.BLUE_ICE);
                    tBlock.setType(Material.BLUE_ICE);
                    rBlock.setType(Material.BLUE_ICE);
                    fBlock.setType(Material.BLUE_ICE);

                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 30, 1, 1, 1);


                    state++;

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);

        return true;
    }

    public static boolean castExplosion(Spells_plugin plugin, Player player) {
        RayTraceResult target = player.rayTraceBlocks(16 * 12);

        if (target == null)
            return false;

        World world = player.getWorld();

        createParticleTrail(player.getWorld(), player.getLocation(), target.getHitPosition().toLocation(player.getWorld()), Particle.CLOUD, 0.1, 0);
        world.createExplosion(target.getHitPosition().toLocation(world), 3);



        return true;
    }

    public static boolean castInstantDeath(Spells_plugin plugin, Player player) {
        RayTraceResult target = player.getWorld().rayTraceEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize()), player.getEyeLocation().getDirection(), 16 * 12);

        if (target == null || target.getHitEntity() == null)
            return false;

        if (!(target.getHitEntity() instanceof LivingEntity))
            return false;

        if (target.getHitEntity() == player)
            return false;


        LivingEntity targetEntity = (LivingEntity) target.getHitEntity();

        createParticleTrail(player.getWorld(), player.getEyeLocation(), target.getHitEntity().getLocation(), Particle.COMPOSTER, 0.1, 0.5);

        targetEntity.setHealth(0.0);

        return true;
    }

    public static boolean castLightning(Spells_plugin plugin, Player player) {
        RayTraceResult target = player.rayTraceBlocks(16 * 12);

        if (target == null || target.getHitBlock() == null)
            return false;

        World world = player.getWorld();
        world.strikeLightning(target.getHitBlock().getLocation());

        return true;


    }

    public static boolean castAttract(Spells_plugin plugin, Player player) {
        RayTraceResult target = player.getWorld().rayTraceEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize()), player.getEyeLocation().getDirection(), 16 * 12);

        if (target == null || target.getHitEntity() == null)
            return false;

        if (target.getHitEntity() == player)
            return false;

        target.getHitEntity().setVelocity(player.getEyeLocation().getDirection().normalize().multiply(-1));


        return true;
    }

    public static boolean castLight(Spells_plugin plugin, Player player) {

        BukkitTask lightTask = new spellTasks(player).runTaskTimer(plugin, 0l, 1l);

        return true;
    }

    public static boolean castTeleport(Spells_plugin plugin, Player player) {

        RayTraceResult target = player.rayTraceBlocks(16 * 12);

        if (target == null || target.getHitBlock() == null)
            return false;

        Location location = target.getHitBlock().getLocation();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();

        player.teleport(new Location(player.getWorld(), location.getX(), location.getY() + 1, location.getZ(), yaw, pitch));

        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 50);

        return true;
    }

    public static boolean castMutation(Spells_plugin plugin, Player player) {
        RayTraceResult target = player.getWorld().rayTraceEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize()), player.getEyeLocation().getDirection(), 16 * 12);

        if (target == null || target.getHitEntity() == null)
            return false;

        if (target.getHitEntity() == player)
            return false;

        Random generator = new Random();

        if (target.getHitEntity() instanceof Item) {
            Item droppedItem = (Item) target.getHitEntity();

            Material newMaterial = Material.values()[generator.nextInt(Material.values().length)];

            droppedItem.setItemStack(new ItemStack(newMaterial));
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH, droppedItem.getLocation(), 100);
            droppedItem.setVelocity(new Vector(0, 0.2, 0));

        }

        else {

            Location mobLocation = target.getHitEntity().getLocation();
            target.getHitEntity().remove();

            EntityType newEntity = EntityType.values()[generator.nextInt(EntityType.values().length)];

            player.getWorld().spawnEntity(mobLocation, newEntity);
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH,  mobLocation, 100);

        }
        return true;

    }


    public static boolean castNothing(Spells_plugin plugin, Player player) { return true; };

}
