package me.kafae.sacks

import me.kafae.sacks.command.Commands
import me.kafae.sacks.functions.enchantCheck
import me.kafae.sacks.listeners.*
import me.kafae.sacks.objects.*
import me.kafae.sacks.recipes.*
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class Sacks : JavaPlugin() {

    //energy regen
    private fun regen() {
        object: BukkitRunnable() {
            private var n: Boolean = false
            override fun run() {
                for (p in Bukkit.getOnlinePlayers()) {
                    enchantCheck(p)
                    var playerData: DataStore.PlayerData
                    if (!n) {
                        playerData = Energy.add(50, p)
                        n = true
                    } else {
                        playerData = DataStore.player["${p.uniqueId}"] as DataStore.PlayerData
                        n = false
                    }
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§f${playerData.energy}%§e ⚡"))
                    if (DataStore.player["${p.uniqueId}"]!!.shells >= 0) {
                        val c: SignatureClasses.Signature = SignatureClasses.getClass(p)
                        c.passive(p)
                    }
                    when (DataStore.player["${p.uniqueId}"]!!.shells) {
                        -1 -> p.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 40 ,0))
                        -2 -> {
                            p.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 40, 1))
                            p.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 40, 0))
                        }
                        -3 -> {
                            p.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 40, 2))
                            p.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 40, 1))
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L)
    }

    //checks
    private fun checks() {
        object: BukkitRunnable() {
            override fun run() {
                for (p in Bukkit.getOnlinePlayers()) {
                    when (SignatureClasses.getClass(p)) {
                        SignatureClasses.water -> {
                            if (p.isInWater) {
                                p.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 40 ,0))
                                p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 40 ,0))
                            }
                        }
                        SignatureClasses.fire -> {
                            if (p.isVisualFire) {
                                p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 40 ,0))
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L)
    }

    override fun onEnable() {
        SignatureClasses.plugin = this
        if (!DataStore.load()) {
            logger.warning("Data file not found! Creating ono")
            DataStore.player = mutableMapOf()
        } else {
            logger.info("Loaded all saved data!")
        }

        this.getCommand("ability")?.setExecutor(Commands())
        this.getCommand("cgive")?.setExecutor(Commands())
        this.getCommand("sack")?.setExecutor(Commands())
        logger.info("Registered all commands")

        server.pluginManager.registerEvents(JoinListener(), this) //player join event listener
        server.pluginManager.registerEvents(DamageListenter(), this) //damage listener
        server.pluginManager.registerEvents(TeleportListener(), this) //food consume listener
        server.pluginManager.registerEvents(InteractionListener(), this)  //interaction listener
        server.pluginManager.registerEvents(InventoryListener(), this) //inv listener
        server.pluginManager.registerEvents(DeathListener(), this) //death listener
        server.pluginManager.registerEvents(CraftingListener(), this) //crafting listener
        server.pluginManager.registerEvents(RespawnListener(), this) //respawn listener
        logger.info("Registered all events!")

        shellRecipe(this) //energy shell recipe
        rerollRecipe(this) //reroll recipe
        shardRecipe(this) //shard recipe
        breezeRecipe(this) //breeze recipe
        dragonRecipe(this) //dragon recipe
        logger.info("Registered all recipes")

        regen() //energy regen
        checks() //checks
        logger.info("Running tasks in the background!")

        logger.info("Sacks Loaded!")
    }

    override fun onDisable() {
        DataStore.save()
        logger.info("Saved all data!")

        logger.info("Sacks disabled!")
    }
}
