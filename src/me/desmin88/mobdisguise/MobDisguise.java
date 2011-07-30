package me.desmin88.mobdisguise;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.desmin88.mobdisguise.commands.MDCommand;
import me.desmin88.mobdisguise.listeners.MDEntityListener;
import me.desmin88.mobdisguise.listeners.MDPlayerListener;
import me.desmin88.mobdisguise.listeners.PacketListener;
import me.desmin88.mobdisguise.utils.DisguiseTask;
import me.desmin88.mobdisguise.utils.MobIdEnum;
import me.desmin88.mobdisguise.utils.PacketUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class MobDisguise extends JavaPlugin {
    public static List<Player> disList = new ArrayList<Player>();
    public static List<Player> apiList = new ArrayList<Player>();
    public static Map<Player, Byte> playerMobId = new HashMap<Player, Byte>();
    public static Set<Integer> playerEntIds = new HashSet<Integer>();
    public static PacketUtils pu = new PacketUtils();
    public final PacketListener packetlistener = new PacketListener(this);
    public final MDPlayerListener playerlistener = new MDPlayerListener(this);
    public final MDEntityListener entitylistener = new MDEntityListener(this);
    public static final String pref = "[MobDisguise] ";
    public static Configuration cfg;
    public static boolean perm;
    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        System.out.println(pref + "version 1.0 disabled");
    }

    @Override
    public void onEnable() {
        // Begin config code
        if (!new File(getDataFolder(), "config.yml").exists()) {
            try {new File(getDataFolder(), "config.yml").createNewFile();} catch(Exception e) {}
        }
        cfg = this.getConfiguration(); // Get config
        if (cfg.getKeys().isEmpty()) { // Config hasn't been made
            System.out.println(pref + "config.yml not found, making with default values");
            cfg.setProperty("RealDrops.enabled", false);
            cfg.setProperty("Permissions.enabled", true);
            for (String mobtype : MobIdEnum.map.keySet()) {
                cfg.setHeader("#Setting a mobtype to false will not allow a player to disguise as that type");
                cfg.setProperty("Blacklist." + mobtype, true); // Just making
            }
            cfg.save();
        }
        perm = cfg.getBoolean("Permissions.enabled", false);
        PluginManager pm = getServer().getPluginManager();
        this.getCommand("md").setExecutor(new MDCommand(this));
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerlistener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerlistener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entitylistener, Priority.Normal, this);
        // this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ANIMATION,
        // new MDPlayerListener(this), Priority.Normal, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DisguiseTask(this), 1200, 1200);
        // Register packet listeners
        org.bukkitcontrib.packet.listener.Listeners.addListener(17, packetlistener);
        org.bukkitcontrib.packet.listener.Listeners.addListener(18, packetlistener);
        System.out.println(pref + "version 1.0 enabled");

    }

}