package me.kafae.sacks.objects

import org.bukkit.entity.Player

object Energy {

    fun add(n: Int, p: Player): DataStore.PlayerData {
        DataStore.player["${p.uniqueId}"]!!.energy = (DataStore.player["${p.uniqueId}"]!!.energy + n).coerceAtMost(100)
        return DataStore.player["${p.uniqueId}"]!!
    }

    fun subtract(n: Int, p: Player): Boolean {
        return if (DataStore.player["${p.uniqueId}"]!!.energy >= n) {
            DataStore.player["${p.uniqueId}"]!!.energy = (DataStore.player["${p.uniqueId}"]!!.energy - n)
            true
        } else {
            false
        }
    }
}