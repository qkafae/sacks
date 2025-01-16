package me.kafae.sacks.gui

import me.kafae.sacks.objects.Abilities
import me.kafae.sacks.objects.CItems
import me.kafae.sacks.objects.DataStore
import me.kafae.sacks.objects.SignatureClasses
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object SacksGUI {

    fun main(p: Player) {
        val gui: Inventory = Bukkit.createInventory(p, 27, CItems.sack.name)

        val craft: ItemStack = ItemStack(Material.CRAFTING_TABLE)
        val crmeta: ItemMeta = craft.itemMeta!!
        crmeta!!.setDisplayName("§eCrafting")
        craft.itemMeta = crmeta

        val abt: ItemStack = ItemStack(Material.NETHER_STAR)
        val ameta: ItemMeta = craft.itemMeta!!
        ameta!!.setDisplayName("§eAbility Slots")
        abt.itemMeta = ameta

        gui.setItem(11, craft)
        gui.setItem(15, abt)

        p.openInventory(gui)
    }

    object craft {

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
        val pcls: SignatureClasses.Signature = SignatureClasses.getClass(p)

        val gui: Inventory = Bukkit.createInventory(p, 27, "§eAbility Slots")

        val cls: ItemStack = CItems.sack.getItem(1)
        val clmeta: ItemMeta = cls.itemMeta!!
        clmeta.lore = listOf("", "§eShell§f: ${DataStore.player["${p.uniqueId}"]!!.shells}")
        clmeta.setDisplayName("§dClass§f: ${pcls.color + pcls.name.replaceFirstChar { it.titlecase() }}")
        cls.itemMeta = clmeta

        val s1: ItemStack
        val s2: ItemStack

        if (DataStore.player["${p.uniqueId}"]!!.equippedAbilities[0] != null) {
            s1 = Abilities.getShard(DataStore.player["${p.uniqueId}"]!!.equippedAbilities[0]!!).getItem(1)
        } else {
            s1 = ItemStack(Material.RED_STAINED_GLASS_PANE)
            val s1meta: ItemMeta = s1.itemMeta!!
            s1meta.setDisplayName("§l§cCLICK WITH ABILITY SHARD TO EQUIP IT")
            s1.itemMeta = s1meta
        }

        if (DataStore.player["${p.uniqueId}"]!!.equippedAbilities[1] != null) {
            s2 = Abilities.getShard(DataStore.player["${p.uniqueId}"]!!.equippedAbilities[1]!!).getItem(1)
        } else {
            s2 = ItemStack(Material.RED_STAINED_GLASS_PANE)
            val s2meta: ItemMeta = s2.itemMeta!!
            s2meta.setDisplayName("§l§cCLICK WITH ABILITY SHARD TO EQUIP IT")
            s2.itemMeta = s2meta
        }

        gui.setItem(11, cls)
        gui.setItem(13, s1)
        gui.setItem(15, s2)

        p.openInventory(gui)
    }

}