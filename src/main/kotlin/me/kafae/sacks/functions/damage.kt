package me.kafae.sacks.functions

import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

fun damageEntity(e: LivingEntity, n: Double) {
    var h: Double = e.health
    h = (h - n).coerceAtLeast(0.0)
    e.world.playSound(e.location, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f)
    e.health = h
}

fun damagePlayer(e: Player, n: Double) {
    var h: Double = e.health
    h = (h - n).coerceAtLeast(0.0)
    e.world.playSound(e.location, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f)
    e.health = h
}