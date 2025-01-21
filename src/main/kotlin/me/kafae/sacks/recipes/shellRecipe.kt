package me.kafae.sacks.recipes

import me.kafae.sacks.objects.CItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

fun shellRecipe(plugin: JavaPlugin) {
    val recipe: ShapedRecipe = ShapedRecipe(NamespacedKey(plugin, "energy_shell_recipe"), CItems.shell.getItem(1)).apply {
        shape("ana", "nsn", "ana")
        setIngredient('a', Material.AMETHYST_SHARD)
        setIngredient('n', Material.NETHERITE_INGOT)
        setIngredient('s', Material.NETHER_STAR)
    }

    plugin.server.addRecipe(recipe)
}