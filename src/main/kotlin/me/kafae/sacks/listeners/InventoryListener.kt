package me.kafae.sacks.listeners

import me.kafae.sacks.gui.SacksGUI
import me.kafae.sacks.objects.CItems
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class InventoryListener: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private fun onInventoryClick(e: InventoryClickEvent) {
        when (e.view.title) {
            CItems.sack.name -> { //sacks gui main
                e.isCancelled = true
                when (e.slot) {
                    11 -> return //SacksGUI.craft.main(e.whoClicked as Player)
                    15 -> return //SacksGUI.ability(e.whoClicked as Player)
                }
            }
        }
    }

}