package me.kafae.sacks.command

import me.kafae.sacks.command.commands.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Commands: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return when (command.name.lowercase()) {
            "ability" -> {
                abilityCommand(sender, args)
                true
            }
            "cgive" -> {
                giveCommand(sender, args)
                true
            }
            "sack" -> {
                sackCommand(sender)
                true
            }
            else -> false
        }
    }
}