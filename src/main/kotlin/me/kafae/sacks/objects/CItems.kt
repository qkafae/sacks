package me.kafae.sacks.objects

import com.sun.org.apache.xpath.internal.operations.Bool
import me.kafae.sacks.functions.removeItem
import me.kafae.sacks.gui.SacksGUI
import org.bukkit.Material
import org.bukkit.Sound
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
                p.sendMessage("§4You are too weak to use abilities")
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
                p.sendMessage("§6You have gained +1 Energy Shell, you are now at ${DataStore.player["${p.uniqueId}"]!!.shells} shells")
                val item: ItemStack = p.inventory.itemInMainHand
                removeItem(p, item, 1)
                p.world.strikeLightningEffect(p.location)
                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
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

    class AbilityShard: Item("ability_shard") {
        override val name: String = "Ability Shard (Unrefined)"
        override val material: Material = Material.PRISMARINE_SHARD
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

    class DragonAbilityShard: Item("dragon_ability_shard") {
        override val name: String = "Ability Shard (Dragon)"
        override val material: Material = Material.PRISMARINE_SHARD
        override val lore: List<String> = listOf("A §erefined §7ability shard, housing", "the ability ${Rarity.MYTHIC.s}Dragon's Breath")
        override val rarity: Rarity = Rarity.MYTHIC
        override val hasGlint: Boolean = true
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

    class BreezeAbilityShard: Item("breeze_ability_shard") {
        override val name: String = "Ability Shard (Breeze)"
        override val material: Material = Material.PRISMARINE_SHARD
        override val lore: List<String> = listOf("A §erefined §7ability shard, housing", "the ability ${Rarity.EPIC.s}Dash")
        override val rarity: Rarity = Rarity.EPIC
        override val hasGlint: Boolean = true
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

    class RerollBook: Item("reroll_book") {
        override val name: String = "Reroll Book"
        override val material: Material = Material.BOOK
        override val lore: List<String> = listOf("§6RIGHT CLICK §7to reroll your", "§dSignature Class")
        override val rarity: Rarity = Rarity.EPIC
        override val hasGlint: Boolean = true
        override val customModelData: Int = 1002

        override fun onRightClick(p: Player) {
            removeItem(p, CItems.rerollBook.getItem(1), 1)
            val c: SignatureClasses.Signature = SignatureClasses.list.random()
            DataStore.player["${p.uniqueId}"]!!.signatureClass = c.name
            p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            p.sendMessage("§aYou got class: " + c.color + c.name.replaceFirstChar { it.titlecase() })
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

    class DragonHorn: Item("dragon_horn") {
        override val name: String = "Dragon Horn"
        override val material: Material = Material.ECHO_SHARD
        override val lore: List<String> = listOf("The horn of a mighty dragon that once", "roamed the sky")
        override val rarity: Rarity = Rarity.LEGENDARY
        override val hasGlint: Boolean = false
        override val customModelData: Int = 1003

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
    val rerollBook: Item = RerollBook()
    val dragonHorn: Item = DragonHorn()

    fun getItem(s: String): Item {
        return when (s.lowercase()) {
            sack.id -> sack
            shell.id -> shell
            abilityShard.id -> abilityShard
            dragonAbilityShard.id -> dragonAbilityShard
            breezeAbilityShard.id -> breezeAbilityShard
            dragonHorn.id -> dragonHorn
            rerollBook.id -> rerollBook
            else -> sack
        }
    }
}