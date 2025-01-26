package me.kafae.sacks.listeners

import me.kafae.sacks.functions.removeItem
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
            CItems.sack.name -> {
                val p: Player = e.whoClicked as Player
                if (e.clickedInventory != e.whoClicked.inventory || e.click == ClickType.SHIFT_LEFT || e.click == ClickType.SHIFT_RIGHT) {
                    e.isCancelled = true
                }
                when (e.slot) {
                    11 -> e.isCancelled = true
                    13, 15 -> {
                        e.isCancelled = true
                        var index: Int = 0
                        if (e.slot == 15) {
                            index++
                        }
                        if (DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] != null) {
                            Abilities.getShard(DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index]!!).givePlayer(p, 1)
                            DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] = null
                            SacksGUI.main(p)
                        } else {
                            val item: ItemStack = e.cursor?: return
                            if (item.type != Material.AIR && item.hasItemMeta()) {
                                if (item.itemMeta!!.hasCustomModelData()) {
                                    when (item.itemMeta!!.customModelData) {
                                        5001 -> {
                                            DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] = "dragons_breath"
                                            p.sendMessage("§2You equipped ability ${CItems.Rarity.MYTHIC.s}Dragon's Breath §2to your ${index + 1}st slot!")
                                            SacksGUI.main(p)
                                            removeItem(p, CItems.dragonAbilityShard.getItem(1), 1)
                                        }
                                        5002 -> {
                                            DataStore.player["${p.uniqueId}"]!!.equippedAbilities[index] = "dash"
                                            p.sendMessage("§2You equipped ability ${CItems.Rarity.EPIC.s}Dash §2to your ${index + 1}st slot!")
                                            SacksGUI.main(p)
                                            removeItem(p, CItems.breezeAbilityShard.getItem(1), 1)
                                        }
                                        else -> return
                                    }
                                    removeItem(p, e.cursor!!, 1)
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