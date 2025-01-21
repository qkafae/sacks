package me.kafae.sacks.objects

import me.kafae.sacks.functions.damageEntity
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

object Abilities {

    abstract class Ability(val id: String) {
        abstract val cost: Int
        abstract fun useAbility(p: Player)
    }

    class Dash: Ability("dash") {
        override val cost = 50

        override fun useAbility(p: Player) {
            if (!Energy.subtract(cost, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            val unitVector = Vector(p.location.direction.x, 0.0, p.location.direction.z)
            val location: Location = p.location.add(p.location.direction.multiply(-0.5)).add(0.0, 1.5, 0.0)
            p.world.spawnParticle(Particle.CLOUD, location, 10, 0.2, 0.2, 0.2, 0.05)
            p.velocity = unitVector.multiply(3.0)
            p.sendMessage("§bUsed ability ${CItems.breezeAbilityShard.rarity.s}Dash §f| §e-50 ⚡")
        }
    }

    class DragonsBreath: Ability("dragons_breath") {
        override val cost = 100

        override fun useAbility(p: Player) {
            if (!Energy.subtract(cost, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            val entites: MutableList<Entity> = mutableListOf()

            for (e in p.world.getNearbyEntities(p.location, 5.0, 5.0, 5.0)) {
                if (e is LivingEntity && e != p) {
                    val dot: Double = p.eyeLocation.direction.dot(e.location.subtract(p.location).toVector().normalize())
                    if (dot <= 90) {
                        entites.add(e)
                    }
                }
            }

            for (i in 0..10) {
                val distance: Double = 5 * (i / 10.0)
                for (j in 0..40) {
                    val angle = j * (2 * Math.PI / 40)
                    val x = cos(angle) * distance
                    val z = sin(angle) * distance
                    for (k in -distance.toInt()..distance.toInt()) {
                        val particleLocation = p.eyeLocation.clone().add(p.eyeLocation.direction.clone().multiply(distance)).add(x, k.toDouble(), z)
                        p.world.spawnParticle(Particle.DRAGON_BREATH, particleLocation, 5, 0.0, 0.0, 0.0, 0.0)
                    }
                }
            }



            for (e in entites) {
                e as LivingEntity
                damageEntity(e, 10.0)
                e.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 100 ,2))
                e.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 100, 0))
                e.velocity = e.eyeLocation.direction.multiply(-5)
            }

            p.sendMessage("§bUsed ability ${CItems.dragonAbilityShard.rarity.s}Dragons's Breath §f| §e-100 ⚡")
        }
    }

    private val dash: Ability = Dash()
    private val dragonsBreath: Ability = DragonsBreath()

    fun getAbility(n: String): Ability {
        return when (n.lowercase()) {
            "dash" -> dash
            "dragons_breath" -> dragonsBreath
            else -> dash
        }
    }

    fun getShard(n: String): CItems.Item {
        return when (n.lowercase()) {
            dragonsBreath.id -> CItems.dragonAbilityShard
            dash.id -> CItems.breezeAbilityShard
            else -> CItems.breezeAbilityShard
        }
    }
}