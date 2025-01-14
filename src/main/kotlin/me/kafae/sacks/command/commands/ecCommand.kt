package me.kafae.sacks.command.commands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun ecCommand(sender: CommandSender, args: Array<out String>) {
    if (sender is Player) {
        sender.openInventory(sender.enderChest)
    }
}