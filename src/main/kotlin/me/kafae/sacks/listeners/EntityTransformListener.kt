package me.kafae.sacks.listeners

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTransformEvent

class EntityTransformListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onEntityTransform(e: EntityTransformEvent) {
        if (e.transformReason == EntityTransformEvent.TransformReason.INFECTION || e.transformReason == EntityTransformEvent.TransformReason.CURED) {
            (e.entity as LivingEntity).damage(20.0)
            e.isCancelled = true
        }
    }

}