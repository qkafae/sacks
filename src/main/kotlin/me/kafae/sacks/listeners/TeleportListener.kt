package me.kafae.sacks.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TeleportListener: Listener {

    private val reasons: Array<PlayerTeleportEvent.TeleportCause> = arrayOf(
        PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT,
        PlayerTeleportEvent.TeleportCause.ENDER_PEARL
    )

    @EventHandler(priority = EventPriority.HIGH)
    private fun onPlayerTeleport(e: PlayerTeleportEvent) {
        if (e.cause in reasons) {
            val p: Player = e.player
            p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 60, 2))
            p.sendMessage("§6Passive: §dAetheric Surge§f, gave you §cRegeneration 3§f for 3 seconds!")
        }
    }

}