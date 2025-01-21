package me.kafae.sacks.recipes

import me.kafae.sacks.objects.CItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

fun breezeRecipe(plugin: JavaPlugin) {
    val recipe: ShapedRecipe = ShapedRecipe(NamespacedKey(plugin, "breeze_ability_shard_recipe"), CItems.breezeAbilityShard.getItem(1)).apply {
        shape("ddd", "dsd", "ddd")
        setIngredient('d', Material.BREEZE_ROD)
        setIngredient('s', CItems.abilityShard.material)
    }

    plugin.server.addRecipe(recipe)
}