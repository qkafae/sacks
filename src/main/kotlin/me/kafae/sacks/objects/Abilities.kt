package me.kafae.sacks.objects

import org.bukkit.entity.Player
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
            p.velocity = unitVector.multiply(2.0)
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
}