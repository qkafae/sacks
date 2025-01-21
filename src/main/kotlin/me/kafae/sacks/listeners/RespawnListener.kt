package me.kafae.sacks.listeners

import me.kafae.sacks.objects.CItems
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RespawnListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onPlayerRespawn(e: PlayerRespawnEvent) {
        CItems.sack.givePlayer(e.player)
    }

}