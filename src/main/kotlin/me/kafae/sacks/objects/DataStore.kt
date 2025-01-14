package me.kafae.sacks.objects

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import java.io.File

object DataStore {

    @Serializable
    data class PlayerData(
        var energy: Int = 1,
        var shells: Int = 0,
        val equippedAbilities: Array<String?> = arrayOf(null, null),
        var abilities: ArrayList<String> = arrayListOf(),
        var signatureClass: String? = null
    )

    lateinit var player: MutableMap<String, PlayerData>
    var isHarvest: ArrayList<Player> = arrayListOf()
    var isFog: ArrayList<Player> = arrayListOf()

    private val jsonFormatter = Json { prettyPrint = true }

    fun save(): Boolean{
        val jsonf: String = jsonFormatter.encodeToString(player)
        File("plugins/Sacks/player.json").writeText(jsonf)
        return true
    }

    fun load(): Boolean {
        if (!File("plugins/Sacks").exists()) {
            File("plugins/Sacks").mkdir()
            return false
        }

        val jsonf: String = File("plugins/Sacks/player.json").readText()
        player = Json.decodeFromString(jsonf)
        return true
    }
}