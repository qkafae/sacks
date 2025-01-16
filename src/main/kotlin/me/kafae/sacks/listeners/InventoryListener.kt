package me.kafae.sacks.listeners

import me.kafae.sacks.gui.SacksGUI
import me.kafae.sacks.objects.Abilities
import me.kafae.sacks.objects.CItems
import me.kafae.sacks.objects.DataStore
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class InventoryListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onInventoryClick(e: InventoryClickEvent) {
        when (e.view.title) {
            CItems.sack.name -> { //sacks gui main
                e.isCancelled = true
                when (e.slot) {
                    11 -> return //SacksGUI.craft.main(e.whoClicked as Player)
                    15 -> SacksGUI.ability(e.whoClicked as Player)
                }
            }
            "§eAbility Slots" -> {
                val p: Player = e.whoClicked as Player
                if (e.clickedInventory != e.whoClicked.inventory || e.click == ClickType.SHIFT_LEFT || e.click == ClickType.SHIFT_RIGHT) {
                    e.isCancelled = true
                }
                when (e.slot) {
                    11 -> return
                    13, 15 -> {
                        var index: Int = 0
                        if (e.slot == 15) {
                            index++
                        }
                        if (DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] != null) {
                            Abilities.getShard(DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index]!!).givePlayer(p, 1)
                            DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] = null
                            SacksGUI.ability(p)
                        } else {
                            val item: ItemStack = e.cursor?: return
                            if (item.type != Material.AIR && item.hasItemMeta()) {
                                if (item.itemMeta!!.hasCustomModelData()) {
                                    when (item.itemMeta!!.customModelData) {
                                        5001 -> {
                                            DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] = "dragons_breathe"
                                            p.sendMessage("§2You equipped ability ${CItems.Rarity.MYTHIC.s}Dragon's Breath §2to your 1st slot!")
                                            SacksGUI.ability(p)
                                        }
                                        5002 -> {
                                            DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] = "dragons_breathe"
                                            p.sendMessage("§2You equipped ability ${CItems.Rarity.EPIC.s}Dragon's Breath §2to your 1st slot!")
                                            SacksGUI.ability(p)
                                        }
                                        else -> return
                                    }
                                    p.inventory.removeItem(e.cursor)
                                }
                            }
                        }
                    }
                    else -> return
                }
            }
        }
    }

}