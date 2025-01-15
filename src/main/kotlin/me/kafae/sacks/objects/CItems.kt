package me.kafae.sacks.objects

import com.sun.org.apache.xpath.internal.operations.Bool
import me.kafae.sacks.gui.SacksGUI
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object CItems {

    enum class Rarity(val s: String, val n: String) {
        COMMON("§f", "Common"),
        UNCOMMON("§a", "Uncommon"),
        RARE("§9", "Rare"),
        EPIC("§5", "Epic"),
        LEGENDARY("§6", "Legendary"),
        MYTHIC("§d", "Mythic"),
        SPECIAL("§e", "Special")
    }

    abstract class Item(val id: String) {
        abstract val name: String
        abstract val material: Material
        abstract val lore: List<String>
        abstract val rarity: Rarity
        abstract val hasGlint: Boolean
        abstract val customModelData: Int

        abstract fun onLeftClick(p: Player)
        abstract fun onRightClick(p: Player)
        abstract fun onShiftLeftClick(p: Player)
        abstract fun onShiftRightClick(p: Player)

        fun getItem(n: Int): ItemStack {
            val itemstack: ItemStack = ItemStack(material, n)
            val itemmeta: ItemMeta? = itemstack.itemMeta
            val temp: List<String> = listOf("", rarity.s + "§l" + rarity.n.uppercase() + " " + "ITEM")

            itemmeta?.let {
                it.setDisplayName(rarity.s + name)
                it.lore = (listOf("") + lore + temp).map { index -> "§7$index" }
                it.setCustomModelData(customModelData)
                if (hasGlint) {
                    it.addEnchant(Enchantment.MENDING, 1, true)
                }
                it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                it.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)

                itemstack.itemMeta = itemmeta
            }

            return itemstack
        }

        fun givePlayer(p: Player, n: Int = 1) {
            p.inventory.addItem(getItem(n))
        }
    }

    class Sack: Item("sack") {
        override val name: String = "Sack"
        override val material: Material = Material.ECHO_SHARD
        override val lore: List<String> = listOf("A magical sack that grants the user", "magical abilities!", "", "§6RIGHT CLICK §7: Signature Ability", "§6LEFT CLICK §7: Ability 1", "§6SHIFT RIGHT CLICK §7: Ability 2", "§6SHIFT LEFT CLICK §7: Menu")
        override val rarity: Rarity = Rarity.SPECIAL
        override val hasGlint: Boolean = false
        override val customModelData: Int = 1000

        override fun onRightClick(p: Player) {
            if (DataStore.player["${p.uniqueId}"]!!.shells < 0) {
                p.sendMessage("§You are too weak to use abilities")
                return
            }

            val c: SignatureClasses.Signature = SignatureClasses.getClass(p)
            c.ability(p)
        }

        override fun onLeftClick(p: Player) {
            if (DataStore.player["${p.uniqueId}"]!!.equippedAbilities[0] == null) {
                return
            }

            if (DataStore.player["${p.uniqueId}"]!!.shells < 0) {
                p.sendMessage("§You are too weak to use abilities")
                return
            }

            val ability: Abilities.Ability = Abilities.getAbility(DataStore.player["${p.uniqueId}"]!!.equippedAbilities[0]!!)
            ability.useAbility(p)
        }

        override fun onShiftRightClick(p: Player) {
            if (DataStore.player["${p.uniqueId}"]!!.equippedAbilities[1] == null) {
                return
            }

            if (DataStore.player["${p.uniqueId}"]!!.shells < 0) {
                p.sendMessage("§You are too weak to use abilities")
                return
            }

            val ability: Abilities.Ability = Abilities.getAbility(DataStore.player["${p.uniqueId}"]!!.equippedAbilities[1]!!)
            ability.useAbility(p)
        }

        override fun onShiftLeftClick(p: Player) {
            SacksGUI.main(p)
        }
    }

    class Shell: Item("shell") {
        override val name: String = "Energy Shell"
        override val material: Material = Material.NETHER_STAR
        override val lore: List<String> = listOf("Powerful artifacts to power", "", "§6RIGHT CLICK §7to consume, granting", "+1 energy shell.")
        override val rarity: Rarity = Rarity.SPECIAL
        override val hasGlint: Boolean = true
        override val customModelData: Int = 1001

        override fun onRightClick(p: Player) {
            if (DataStore.player["${p.uniqueId}"]!!.shells < 10) {
                DataStore.player["${p.uniqueId}"]!!.shells++
                p.sendMessage("§6You have gain +1 Energy Shell")
                val item: ItemStack = p.inventory.itemInMainHand
                item.amount = 1
                p.inventory.removeItem(item)
                p.world.strikeLightningEffect(p.location)
            } else {
                p.sendMessage("§4You already have the maximum amount of energy shells!")
            }
        }

        override fun onLeftClick(p: Player) {
            return
        }

        override fun onShiftRightClick(p: Player) {
            onRightClick(p)
        }

        override fun onShiftLeftClick(p: Player) {
            return
        }
    }

    class AbilityShard: Item("ability_shard_unrefined") {
        override val name: String = "Ability Shard (Unrefined)"
        override val material: Material = Material.IRON_INGOT
        override val lore: List<String> = listOf("The key to abilities", "§eRefine §7it inside the menu", "of your Sack")
        override val rarity: Rarity = Rarity.RARE
        override val hasGlint: Boolean = false
        override val customModelData: Int = 5000

        override fun onRightClick(p: Player) {
            return
        }

        override fun onLeftClick(p: Player) {
            return
        }

        override fun onShiftRightClick(p: Player) {
            return
        }

        override fun onShiftLeftClick(p: Player) {
            return
        }
    }

    class DragonAbilityShard: Item("ability_shard_dragon") {
        override val name: String = "Ability Shard (Dragon)"
        override val material: Material = Material.IRON_INGOT
        override val lore: List<String> = listOf("A §erefined §7ability shard, housing", "the ability ${Rarity.MYTHIC.s}Dragon's Breath")
        override val rarity: Rarity = Rarity.MYTHIC
        override val hasGlint: Boolean = false
        override val customModelData: Int = 5001

        override fun onRightClick(p: Player) {
            return
        }

        override fun onLeftClick(p: Player) {
            return
        }

        override fun onShiftRightClick(p: Player) {
            return
        }

        override fun onShiftLeftClick(p: Player) {
            return
        }
    }

    class BreezeAbilityShard: Item("ability_shard_breeze") {
        override val name: String = "Ability Shard (Breeze)"
        override val material: Material = Material.IRON_INGOT
        override val lore: List<String> = listOf("A §erefined §7ability shard, housing", "the ability ${Rarity.EPIC.s}Dash")
        override val rarity: Rarity = Rarity.EPIC
        override val hasGlint: Boolean = false
        override val customModelData: Int = 5002

        override fun onRightClick(p: Player) {
            return
        }

        override fun onLeftClick(p: Player) {
            return
        }

        override fun onShiftRightClick(p: Player) {
            return
        }

        override fun onShiftLeftClick(p: Player) {
            return
        }
    }


    val sack: Item = Sack()
    val shell: Item = Shell()
    val abilityShard: Item = AbilityShard()
    val dragonAbilityShard: Item = DragonAbilityShard()
    val breezeAbilityShard: Item = BreezeAbilityShard()

    fun getItem(s: String): Item {
        return when (s.lowercase()) {
            sack.id -> sack
            shell.id -> shell
            abilityShard.id -> abilityShard
            dragonAbilityShard.id -> dragonAbilityShard
            breezeAbilityShard.id -> dragonAbilityShard
            else -> sack
        }
    }
}