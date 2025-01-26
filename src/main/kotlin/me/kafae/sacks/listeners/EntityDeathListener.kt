package me.kafae.sacks.listeners

import me.kafae.sacks.objects.CItems
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.random.Random

class EntityDeathListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onEntityDeath(e: EntityDeathEvent) {
        when (e.entityType) {
            EntityType.ENDER_DRAGON -> {
                val rng: Int = Random.nextInt(1, 3)
                e.drops.add(CItems.dragonHorn.getItem(rng))
            }
            else -> return
        }
    }

}