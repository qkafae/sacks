package me.kafae.sacks.listeners

import me.kafae.sacks.objects.DataStore
import me.kafae.sacks.objects.CItems
import me.kafae.sacks.objects.SignatureClasses
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onPlayerJoin(e: PlayerJoinEvent) {
        val p: Player = e.player

        if (!DataStore.player.containsKey("${p.uniqueId}")) {
            DataStore.player["${p.uniqueId}"] = DataStore.PlayerData()

            val c: SignatureClasses.Signature = SignatureClasses.list.random()
            DataStore.player["${p.uniqueId}"]!!.signatureClass = c.name
            p.sendMessage("§aYou got class: " + c.color + c.name.replaceFirstChar { it.titlecase() })

            CItems.sack.givePlayer(p, 1)
        }
    }

}