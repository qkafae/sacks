package me.kafae.sacks.command.commands

import me.kafae.sacks.objects.CItems
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun giveCommand(sender: CommandSender, args: Array<out String>) {
    if (args.size < 2) {
        sender.sendMessage("§4Invalid Command Arguments! /cigve <item_id> <count>")
    } else {
        CItems.getItem(args[0]).givePlayer(sender as Player, args[1].toInt())
    }
}