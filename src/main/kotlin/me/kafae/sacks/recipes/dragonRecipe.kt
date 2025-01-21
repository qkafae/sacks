package me.kafae.sacks.recipes

import me.kafae.sacks.objects.CItems
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

fun dragonRecipe(plugin: JavaPlugin) {
    val recipe: ShapedRecipe = ShapedRecipe(NamespacedKey(plugin, "dragon_ability_shard_recipe"), CItems.dragonAbilityShard.getItem(1)).apply {
        shape("ddd", "dsd", "ddd")
        setIngredient('d', CItems.dragonHorn.material)
        setIngredient('s', CItems.abilityShard.material)
    }

    plugin.server.addRecipe(recipe)
}