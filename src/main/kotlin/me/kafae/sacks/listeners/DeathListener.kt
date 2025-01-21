package me.kafae.sacks.listeners

import me.kafae.sacks.objects.Abilities
import me.kafae.sacks.objects.CItems
import me.kafae.sacks.objects.DataStore
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathListener: Listener {

    private val dropRarity: Array<CItems.Rarity> = arrayOf(CItems.Rarity.LEGENDARY, CItems.Rarity.MYTHIC)

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onPlayerDeath(e: PlayerDeathEvent) {
        var drop: Int
        if (DataStore.player["${e.entity.uniqueId}"]!!.shells == -3) {
            drop = 0
        } else {
            drop = 1
            e.drops.add(CItems.shell.getItem(drop))
        }
        DataStore.player["${e.entity.uniqueId}"]!!.shells = (DataStore.player["${e.entity.uniqueId}"]!!.shells - drop).coerceAtLeast(-3)
        for ((i, ability) in DataStore.player["${e.entity.uniqueId}"]!!.equippedAbilities.withIndex()) {
            if (ability != null && Abilities.getShard(ability).rarity in dropRarity) {
                e.drops.add(Abilities.getShard(ability).getItem(1))
                DataStore.player["${e.entity.uniqueId}"]!!.equippedAbilities[i] = null
                e.entity.sendMessage("§4You lost ability $ability")
            }
        }
        e.entity.sendMessage("§4You lost $drop shell, you are now at ${DataStore.player["${e.entity.uniqueId}"]!!.shells} shells")
        e.drops.remove(CItems.sack.getItem(1))
    }

}