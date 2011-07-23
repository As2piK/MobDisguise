package me.desmin88.mobdisguise.listeners;

import me.desmin88.mobdisguise.MobDisguise;

import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class MDPlayerListener extends PlayerListener {
    private final MobDisguise plugin;

    public MDPlayerListener(MobDisguise instance) {
        this.plugin = instance;
    }

    public void onPlayerAnimation(PlayerAnimationEvent event) {
        if (plugin.disList.contains(event.getPlayer())) {
            // event.setCancelled(true);
            return;
        }
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (plugin.disList.contains(event.getPlayer())) {
            event.getPlayer().sendMessage("[MobDisguise] You haven been disguised because you died");
            plugin.pu.disguiseToAll(event.getPlayer());
        }
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.disList.contains(event.getPlayer())) {
            event.getPlayer().sendMessage("[MobDisguise] You haven been disguised");
            plugin.pu.disguiseToAll(event.getPlayer());
        }
    }
}
