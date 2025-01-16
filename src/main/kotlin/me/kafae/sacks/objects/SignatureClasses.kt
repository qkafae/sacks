package me.kafae.sacks.objects

import me.kafae.sacks.functions.damageEntity
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Entity
import org.bukkit.entity.Fireball
import org.bukkit.entity.IronGolem
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

object SignatureClasses {

    lateinit var plugin: JavaPlugin

    //abs class
    abstract class Signature(val name: String) {
        abstract fun passive(p: Player)
        abstract fun ability(p: Player)
        abstract val color: String
    }

    //water
    class Water: Signature("water") {
        override val color: String = "§9"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.WATER_BREATHING, 40, 0))
            p.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }
            p.sendMessage("§bUsed ability$color Tsunami §f| §e-70 ⚡")

            var duration: Int = (5 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(15)
            val center: Location = p.location.add(0.0, 2.0, 0.0)

            object : BukkitRunnable() {
                override fun run() {
                    if (duration <= 0) {
                        cancel()
                        return
                    }

                    for (angle in 0..360) {
                        val x = 5.0 * cos(Math.toRadians(angle.toDouble()))
                        val z = 5.0 * sin(Math.toRadians(angle.toDouble()))
                        val particleLocation: Location = center.clone().add(x, 0.0, z)
                        p.world.spawnParticle(Particle.DRIPPING_WATER, particleLocation, 1)
                    }

                    p.playSound(p, Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f)

                    val nearbyEntities = p.getNearbyEntities(5.0, 5.0, 5.0)
                    nearbyEntities?.forEach { e: Entity? ->
                        if (e is LivingEntity && e !is Villager && e != p) {
                            damageEntity(e, 2.0)
                            e.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 30, 2))
                            e.world.playSound(e, Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f)
                        }
                    }
                    duration--
                }
            }.runTaskTimer(plugin, 0, 20)
        }
    }

    //sky
    class Sky: Signature("sky") {
        override val color: String = "§7"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Fog §f| §e-70 ⚡")

            var duration: Int = (5 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(15)

            DataStore.isFog.add(p)
            p.playSound(p, Sound.BLOCK_ANVIL_FALL, 1.0f, 1.0f)
            object : BukkitRunnable() {
                override fun run() {
                    DataStore.isFog.remove(p)
                    p.sendMessage("§4Your ability Fog has expired")
                    p.playSound(p, Sound.BLOCK_SOUL_SAND_BREAK, 1.0f, 1.0f)
                }
            }.runTaskLater(plugin, duration * 20L)
        }
    }

    //earth
    class Earth: Signature("earth") {
        override val color: String = "§2"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Earthquake §f| §e-70 ⚡")

            val r: Int = (5 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(13)
            val dmg: Double = (5 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(13).toDouble()

            for (angle in 0..360) {
                val x = r * cos(Math.toRadians(angle.toDouble()))
                val z = r * sin(Math.toRadians(angle.toDouble()))
                val particleLocation: Location = p.location.clone().add(x, 1.0, z)
                p.world.spawnParticle(Particle.CRIT, particleLocation, 1)
            }

            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f)

            val nearbyEntities = p.getNearbyEntities(r.toDouble(), r.toDouble(), r.toDouble())
            nearbyEntities?.forEach { e: Entity? ->
                if (e is LivingEntity && e !is Villager && e != p) {
                    e.world.playSound(e, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f)
                    damageEntity(e, dmg)
                    e.velocity = Vector(0.0, 2.0, 0.0)
                }
            }
        }
    }

    //fire
    class Fire: Signature("fire") {
        override val color: String = "§6"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0))
            p.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Fireball §f| §e-70 ⚡")

            val count: Int = (3 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(10)

            object : BukkitRunnable() {
                var currentCount = 0

                override fun run() {
                    if (currentCount < count) {
                        val fireball: Fireball? = p.world?.spawn(p.eyeLocation, Fireball::class.java)
                        if (fireball != null) {
                            fireball.direction = p.eyeLocation.direction
                            fireball.setIsIncendiary(false)
                        }
                        currentCount++
                    } else {
                        this.cancel()
                    }
                }
            }.runTaskTimer(plugin, 0L, 10L)
        }
    }

    //undead
    class Undead: Signature("undead") {
        override val color: String = "§4"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Harvest §f| §e-70 ⚡")

            var duration: Int = (5 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(13)

            DataStore.isHarvest.add(p)
            p.playSound(p, Sound.ENTITY_BREEZE_SHOOT, 1.0f, 1.0f)
            object : BukkitRunnable() {
                override fun run() {
                    DataStore.isHarvest.remove(p)
                    p.sendMessage("§4Your ability Harvest has expired")
                    p.playSound(p, Sound.ENTITY_WITHER_DEATH, 0.5f, 1.0f)
                }
            }.runTaskLater(plugin, duration * 20L)
        }
    }

    //ender
    class Ender: Signature("ender") {
        override val color: String = "§5"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 40, 0))
            p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 40, 1))
            p.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 40, 0))
        }

        override fun ability(p: Player) {
            val cst: Int = (25 - (floor(((DataStore.player["${p.uniqueId}"]?.shells ?: 0) / 2).toDouble()).toInt() * 5)).coerceAtLeast(5)

            if (!Energy.subtract(cst, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Blink §f| §e-$cst ⚡")

            val pearl: EnderPearl = p.world.spawn(p.eyeLocation.clone(), EnderPearl::class.java)
            pearl.velocity = p.eyeLocation.direction.multiply(1.5)
            pearl.shooter = p
        }
    }

    //necromancer
    class Necromancer: Signature("necromancer") {
        override val color: String = "§8"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 40, 0))
            p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Poison Ivy §f| §e-70 ⚡")

            val n: Int = (3 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(13)

            p.world.playSound(p, Sound.ENTITY_VEX_CHARGE, 1.0f, 1.0f)

            for (angle in 0..360) {
                val x = n.toDouble() * cos(Math.toRadians(angle.toDouble()))
                val z = n.toDouble() * sin(Math.toRadians(angle.toDouble()))
                val particleLocation: Location = p.location.clone().add(x, 1.0, z)
                p.world.spawnParticle(Particle.SMOKE, particleLocation, 50)
            }

            for (e in p.getNearbyEntities(n.toDouble(), n.toDouble(), n.toDouble())) {
                if (e is LivingEntity && e !is Villager && e != p) {
                    e.world.playSound(e, Sound.ENTITY_WARDEN_ROAR, 0.8f, 1.0f)
                    e.addPotionEffect(PotionEffect(PotionEffectType.POISON, n * 20, 1))
                    e.addPotionEffect(PotionEffect(PotionEffectType.NAUSEA, n * 20, 0))
                }
            }
        }
    }

    //merchant
    class Merchant: Signature("merchant") {
        override val color: String = "§b"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 40, 11))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Support §f| §e-70 ⚡")

            val cnt: Int = (1 +((DataStore.player["${p.uniqueId}"]?.shells ?: 0) / 2)).coerceAtMost(5)
            for (i in 1..cnt) {
                val golem: IronGolem = p.world.spawn(p.location, IronGolem::class.java)
                try {
                    golem.target = p.lastDamageCause as Player
                } catch (e: Exception) {
                    //nothing
                }

                if (DataStore.player["${p.uniqueId}"]?.shells!! >= 10) {
                    golem.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, Int.MAX_VALUE, 0))
                    golem.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, Int.MAX_VALUE, 0))
                }
            }
        }
    }

    //thunder
    class Thunder: Signature("thunder") {
        override val color: String = "§c"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 40, 0))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(100, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Strike §f| §e-100 ⚡")

            val r: Double = (5 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(15).toDouble()

            for (angle in 0..360 step 10) {
                val x = r * cos(Math.toRadians(angle.toDouble()))
                val z = r * sin(Math.toRadians(angle.toDouble()))
                val particleLocation: Location = p.location.clone().add(x, 0.0, z)
                p.world.strikeLightningEffect(particleLocation)
            }

            for (e in p.getNearbyEntities(r, r, r)) {
                if (e is LivingEntity && e !is Villager && e != p) {
                    e.world.strikeLightning(e.location)
                    e.world.createExplosion(e.location, 2.5f, false)
                    e.removePotionEffect(PotionEffectType.FIRE_RESISTANCE)
                }
            }
        }
    }

    //warrior
    class Warrior: Signature("warrior") {
        override val color: String = "§f"

        override fun passive(p: Player) {
            p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 40, 1))
        }

        override fun ability(p: Player) {
            if (!Energy.subtract(70, p)) {
                p.sendMessage("§4You do not have enough Energy to use this ability!")
                return
            }

            p.sendMessage("§bUsed ability$color Assault §f| §e-70 ⚡")

            val particleLocation = p.location.clone().add(0.0, 2.0, 0.0)
            val n: Int = (3 + (DataStore.player["${p.uniqueId}"]?.shells ?: 0)).coerceAtMost(10)

            p.world.spawnParticle(Particle.ELECTRIC_SPARK, particleLocation, 5)
            p.playSound(p, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.8f, 1.0f)

            p.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, n * 20, 2))
            p.addPotionEffect(PotionEffect(PotionEffectType.HASTE, n * 20, 2))
            p.maxHealth = 10.0

            object : BukkitRunnable() {
                override fun run() {
                    p.maxHealth = 20.0
                    p.sendMessage("§4Your ability Assault has expired")
                    p.playSound(p, Sound.ENTITY_WITHER_DEATH, 0.5f, 1.0f)
                }
            }.runTaskLater(plugin, n * 20L)
        }
    }

    private val water = Water()
    val sky = Sky()
    private val earth = Earth()
    val fire = Fire()
    val undead = Undead()
    private val ender = Ender()
    private val necromancer = Necromancer()
    private val merchant = Merchant()
    val thunder = Thunder()
    private val warrior = Warrior()

    val list: Array<Signature> = arrayOf(water, sky, earth, fire, undead, ender, necromancer, merchant, thunder, warrior)

    fun getClass(p: Player): Signature {
        val cstring: String = DataStore.player["${p.uniqueId}"]!!.signatureClass!!

        return when (cstring.lowercase()) {
            "water" -> water
            "sky" -> sky
            "earth" -> earth
            "fire" -> fire
            "undead" -> undead
            "ender" -> ender
            "necromancer" -> necromancer
            "merchant" -> merchant
            "thunder" -> thunder
            "warrior" -> warrior
            else -> list.random()
        }
    }

    fun getClass(cstring: String): Signature {
        return when (cstring.lowercase()) {
            "water" -> water
            "sky" -> sky
            "earth" -> earth
            "fire" -> fire
            "undead" -> undead
            "ender" -> ender
            "necromancer" -> necromancer
            "merchant" -> merchant
            "thunder" -> thunder
            "warrior" -> warrior
            else -> list.random()
        }
    }
}