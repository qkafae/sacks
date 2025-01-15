package me.kafae.sacks.objects

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

object Abilities {

    abstract class Ability(name: String) {
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
            p.sendMessage("§bUsed ability Dash §f| §e-50 ⚡")
        }

    }

    private val dash: Ability = Dash()

    fun getAbility(n: String): Ability {
        return when (n.lowercase()) {
            "dash" -> dash
            else -> dash
        }
    }

    fun getShard(n: String): CItems.Item {
        return when (n.lowercase()) {
            "dragons_breath" -> CItems.dragonAbilityShard
            "dash" -> CItems.breezeAbilityShard
            else -> CItems.breezeAbilityShard
        }
    }
}