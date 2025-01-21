package me.kafae.sacks.listeners

import me.kafae.sacks.objects.CItems
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack

class CraftingListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onCraftItem(e: CraftItemEvent) {
        val mat: Array<ItemStack> = e.inventory.matrix

        when (e.recipe.result) {
            CItems.dragonAbilityShard.getItem(1) -> {
                val item: ItemStack = mat[4]

                if (item.type != Material.AIR) {
                    if (item.hasItemMeta()) {
                        if (item.itemMeta!!.hasCustomModelData()) {
                            if (item.itemMeta!!.customModelData != CItems.abilityShard.customModelData) {
                                e.isCancelled = true
                            }
                        } else {
                            e.isCancelled = true
                        }
                    } else {
                        e.isCancelled = true
                    }
                }

                for (i in 0..8) {
                    if (i != 4) {
                        val item: ItemStack = mat[i]

                        if (item.type != Material.AIR) {
                            if (item.hasItemMeta()) {
                                if (item.itemMeta!!.hasCustomModelData()) {
                                    if (item.itemMeta!!.customModelData != CItems.dragonHorn.customModelData) {
                                        e.isCancelled = true
                                    } else {
                                        if (mat[i].amount < 2) {
                                            mat[i].amount--
                                        }
                                    }
                                } else {
                                    e.isCancelled = true
                                }
                            } else {
                                e.isCancelled = true
                            }
                        }
                    }
                }
            }
            CItems.breezeAbilityShard.getItem(1) -> {
                val item: ItemStack = mat[4]

                if (item.type != Material.AIR) {
                    if (item.hasItemMeta()) {
                        if (item.itemMeta!!.hasCustomModelData()) {
                            if (item.itemMeta!!.customModelData != CItems.abilityShard.customModelData) {
                                e.isCancelled = true
                            }
                        } else {
                            e.isCancelled = true
                        }
                    } else {
                        e.isCancelled = true
                    }
                }

                for (i in 0..8) {
                    if (i != 4) {
                        if (mat[i].amount < 16) {
                            e.isCancelled = true
                        } else {
                            mat[i].amount -= 15
                        }
                    }
                }
            }
        }
    }
}