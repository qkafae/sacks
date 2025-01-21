package me.kafae.sacks.command.commands

import me.kafae.sacks.functions.removeItem
import me.kafae.sacks.objects.CItems
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun sackCommand(sender: CommandSender) {
    val p: Player = sender as Player

    for (item in p.inventory.contents) {
        if (item != null && item.hasItemMeta() && item.itemMeta?.hasCustomModelData() == true) {
            if (item.itemMeta!!.customModelData == CItems.sack.customModelData) {
                removeItem(p, item, item.amount)
            }
        }
    }

    CItems.sack.givePlayer(p, 1)
}