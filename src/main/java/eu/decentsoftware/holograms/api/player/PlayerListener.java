package eu.decentsoftware.holograms.api.player;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import space.arim.morepaperlib.scheduling.GracefulScheduling;

public class PlayerListener implements Listener {

    private static final DecentHolograms DH = DecentHologramsAPI.get();
    private static final GracefulScheduling scheduler = DecentHologramsAPI.getMorePaperLib().scheduling();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        scheduler.regionSpecificScheduler(player.getLocation()).run(() -> DH.getHologramManager().updateVisibility(player));
        scheduler.regionSpecificScheduler(player.getLocation()).run(() -> DH.getPacketListener().hook(player));
        if (DH.isUpdateAvailable() && player.hasPermission("dh.admin")) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        scheduler.regionSpecificScheduler(player.getLocation()).run(() -> DH.getHologramManager().onQuit(player));
        DH.getPacketListener().unhook(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        scheduler.regionSpecificScheduler(player.getLocation()).run(() -> DH.getHologramManager().hideAll(player));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        scheduler.regionSpecificScheduler(player.getLocation()).run(() -> DH.getHologramManager().hideAll(player));
    }

}
