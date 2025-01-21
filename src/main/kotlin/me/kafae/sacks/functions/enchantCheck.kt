package me.kafae.sacks.functions

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun enchantCheck(p: Player) {
    val armors: Array<ItemStack> = p.inventory.armorContents

    for (armor in armors) {
        if (armor != null && armor.type != Material.AIR && armor.containsEnchantment(Enchantment.THORNS)) {
            armor.removeEnchantment(Enchantment.THORNS)
        }
    }
}