package me.kafae.sacks.functions

import org.bukkit.Sound
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

fun damageEntity(e: LivingEntity, n: Double, p: Player) {
    var h: Double = e.health
    h = (h - n).coerceAtLeast(0.0)
    e.world.playSound(e.location, Sound.ENTITY_GENERIC_HURT, 1.0f, 1.0f)
    e.health = h
    e.damage(0.01)
}

fun damagePlayer(e: Player, n: Double, p: Player) {
    var h: Double = e.health
    h = (h - n).coerceAtLeast(0.0)
    e.world.playSound(e.location, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f)
    e.health = h
    e.damage(0.01)
}