package me.kafae.sacks.recipes

import me.kafae.sacks.objects.CItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

fun shardRecipe(plugin: JavaPlugin) {
    val recipe: ShapedRecipe = ShapedRecipe(NamespacedKey(plugin, "ability_shard_recipe"), CItems.abilityShard.getItem(1)).apply {
            shape("ene", "ntn", "ene")
            setIngredient('e', Material.END_CRYSTAL)
            setIngredient('n', Material.NETHER_STAR)
            setIngredient('t', Material.TOTEM_OF_UNDYING)
        }

    plugin.server.addRecipe(recipe)
}