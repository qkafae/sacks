package me.kafae.sacks.functions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun removeItem(p: Player, i: ItemStack, n: Int): Boolean {
    val customModelData = i.itemMeta?.customModelData ?: return false
    var remainingAmount = n
    var totalAmount = 0


    for (item in p.inventory.contents) {
        if (item != null && item.hasItemMeta() && item.itemMeta?.hasCustomModelData() == true) {
            if (item.itemMeta!!.customModelData == customModelData) {
                totalAmount += item.amount
            }
        }
    }

    if (totalAmount < n) {
        return false
    }

    for (item in p.inventory.contents) {
        if (item != null && item.hasItemMeta() && item.itemMeta?.hasCustomModelData() == true) {
            if (item.itemMeta!!.customModelData == customModelData) {
                val stackSize = item.amount
                if (stackSize > remainingAmount) {
                    item.amount = stackSize - remainingAmount
                    break
                } else {
                    remainingAmount -= stackSize
                    p.inventory.remove(item)
                }
            }
        }
    }

    return true
}