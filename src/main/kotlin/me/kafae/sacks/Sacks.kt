package me.kafae.sacks

import me.kafae.sacks.command.Commands
import me.kafae.sacks.listeners.*
import me.kafae.sacks.objects.*
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Sacks : JavaPlugin() {

    //energy regen
    private fun regen() {
        object: BukkitRunnable() {
            private var n: Boolean = false
            override fun run() {
                for (p in Bukkit.getOnlinePlayers()) {
                    var playerData: DataStore.PlayerData
                    if (!n) {
                        playerData = Energy.add(50, p)!!
                        n = true
                    } else {
                        playerData = DataStore.player["${p.uniqueId}"] as DataStore.PlayerData
                        n = false
                    }
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§f${playerData.energy}/100§e ⚡"))
                    if (DataStore.player["${p.uniqueId}"]!!.shells >= 0) {
                        val c: SignatureClasses.Signature = SignatureClasses.getClass(p)
                        c.passive(p)
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
        this.getCommand("enderchest")?.setExecutor(Commands())
        this.getCommand("cgive")?.setExecutor(Commands())
        logger.info("Registered all commands")

        server.pluginManager.registerEvents(JoinListener(), this) //player join event listener
        server.pluginManager.registerEvents(DamageListenter(), this) //damage listener
        server.pluginManager.registerEvents(TeleportListener(), this) //food consume listener
        server.pluginManager.registerEvents(InteractionListener(), this)  //interaction listener
        server.pluginManager.registerEvents(InventoryListener(), this) //inv listener
        logger.info("Registered all events!")
        regen()
        logger.info("Running tasks in the background!")
        logger.info("Sacks Loaded!")
    }

    override fun onDisable() {
        DataStore.save()
        logger.info("Saved all data!")
        logger.info("Sacks disabled!")
    }
}
