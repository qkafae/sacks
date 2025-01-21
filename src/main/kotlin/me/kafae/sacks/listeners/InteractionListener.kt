package me.kafae.sacks.listeners

import me.kafae.sacks.objects.CItems
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class InteractionListener: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private fun onInteract(e: PlayerInteractEvent) {
        val item: ItemStack = e.player.inventory.itemInMainHand

        try {
            if (item.type != Material.AIR && e.item!!.hasItemMeta()) {
                if (item.itemMeta!!.hasCustomModelData()) {
                    when (e.action) {
                        Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                            if (e.player.isSneaking) {
                                when (e.item!!.itemMeta!!.customModelData) { //shift right click
                                    CItems.sack.customModelData -> CItems.sack.onShiftRightClick(e.player)
                                    CItems.shell.customModelData -> CItems.shell.onShiftRightClick(e.player)
                                    CItems.rerollBook.customModelData -> CItems.rerollBook.onShiftRightClick(e.player)
                                }
                            } else {
                                when (e.item!!.itemMeta!!.customModelData) { //right click
                                    CItems.sack.customModelData -> CItems.sack.onRightClick(e.player)
                                    CItems.shell.customModelData -> CItems.shell.onRightClick(e.player)
                                    CItems.rerollBook.customModelData -> CItems.rerollBook.onRightClick(e.player)
                                }
                            }
                        }
                        Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                            if (e.player.isSneaking) {
                                when (e.item!!.itemMeta!!.customModelData) { //shift left click
                                    CItems.sack.customModelData -> CItems.sack.onShiftLeftClick(e.player)
                                }
                            } else {
                                when (e.item!!.itemMeta!!.customModelData) { //left click
                                    CItems.sack.customModelData -> CItems.sack.onLeftClick(e.player)
                                }
                            }
                        }
                        else -> return
                    }
                }
            }
        } catch (e: Exception) {
            return
        }
    }
}