package me.kafae.sacks.command.commands

import me.kafae.sacks.objects.SignatureClasses
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun abilityCommand(sender: CommandSender, args: Array<out String>) {
    val p: Player = sender as Player
    if (args.isEmpty()) {
        p.sendMessage("§4Invalid Command Arguments! /ability <ability>")
    } else {
        try {
            val c = SignatureClasses.getClass(args[0])
            c.ability(sender)
        } catch (e: Exception) {
            p.sendMessage(e.toString())
        }
    }
}