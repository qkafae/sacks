package me.kafae.sacks.listeners

import me.kafae.sacks.functions.damagePlayer
import me.kafae.sacks.objects.DataStore
import me.kafae.sacks.objects.Energy
import me.kafae.sacks.objects.SignatureClasses
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class DamageListenter: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is Player) {
            val dclass: SignatureClasses.Signature = SignatureClasses.getClass(e.damager as Player)
            val eclass: SignatureClasses.Signature = SignatureClasses.getClass(e.entity as Player)
            when (dclass) {
                SignatureClasses.fire -> {
                    val p: Player = e.damager as Player
                    val chance: Int = (50 + ((DataStore.player["${p.uniqueId}"]?.shells ?: 0) * 5)).coerceAtMost(100)
                    val rng: Int = Random.nextInt(1 , 100)
                    if (rng in 1..chance) {
                        damagePlayer((e.entity as Player), 1.0)
                    }
                }
                SignatureClasses.thunder -> {
                    val p: Player = e.damager as Player
                    if (Random.nextInt(1, 4) == 2) {
                        if (p.saturation > 1) {
                            p.saturation--
                            Energy.add(1, p)
                        }
                    } else {
                        Energy.add(1, p)
                    }
                }
                SignatureClasses.undead -> {
                    val p: Player = e.damager as Player
                    if (p in DataStore.isHarvest) {
                        p.health = (p.health + (e.damage / 10)).coerceAtMost(20.0)
                    }
                }
                else -> return
            }
            when (eclass) {
                SignatureClasses.sky -> {
                    if (e.entity as Player in DataStore.isFog) {
                        e.isCancelled = true
                    }
                }
                else -> return
            }
        }
    }
}