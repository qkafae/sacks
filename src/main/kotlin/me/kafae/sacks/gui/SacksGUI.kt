package me.kafae.sacks.gui

import me.kafae.sacks.objects.CItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object SacksGUI {

    fun main(p: Player) {
        val gui: Inventory = Bukkit.createInventory(p, 27, CItems.sack.name)
        gui.setItem(11, ItemStack(Material.CRAFTING_TABLE))
        gui.setItem(15, ItemStack(Material.NETHER_STAR))

        p.openInventory(gui)
    }

    object craft {
        fun main(p: Player) {
            TODO()
        }

        fun abilityShard() {
            TODO()
        }

        fun dragon() {
            TODO()
        }

        fun breeze() {
            TODO()
        }

        fun shell() {
            TODO()
        }

        fun rrbook() {
            TODO()
        }
    }

    fun ability(p: Player) {
        TODO()
    }

}