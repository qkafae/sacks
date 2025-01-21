package me.kafae.sacks.recipes

import me.kafae.sacks.objects.CItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

fun rerollRecipe(plugin: JavaPlugin) {
    val recipe: ShapedRecipe = ShapedRecipe(NamespacedKey(plugin, "reroll_book_recipe"), CItems.rerollBook.getItem(1)).apply {
        shape("dnd", "nbn", "dnd")
        setIngredient('d', Material.DIAMOND_BLOCK)
        setIngredient('n', Material.NETHERITE_INGOT)
        setIngredient('b', Material.BOOK)
    }

    plugin.server.addRecipe(recipe)
}