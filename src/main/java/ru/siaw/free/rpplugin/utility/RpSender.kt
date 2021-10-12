package ru.siaw.free.rpplugin.utility

import org.bukkit.entity.Player
import java.lang.StringBuilder

class RpSender {
    private var global = false
    private var radius: Double = 0.0

    fun use(sender: Player, msg: String, original: String) {
        val toSend = replace(sender.displayName, msg, original).trim()
        if (global) Print.toPlayers(toSend) else for (entity in sender.world.getNearbyEntities(sender.location, radius, radius, radius)) if (entity is Player) Print.toPlayer(entity, toSend)
    }

    private fun replace(playerName: String, playerMsg: String, original: String): String {
        val newLine = StringBuilder(original)

        var word = "%player"
        if (original.contains(word)) {
            val index = newLine.indexOf(word)
            newLine.replace(index, index + 7, playerName)
        }

        word = "%message"
        if (original.contains(word)) {
            val index = newLine.indexOf(word)
            newLine.replace(index, index + 8, playerMsg)
        }

        return newLine.toString()
    }

    fun setGlobal(value: Boolean) { global = value }
    fun setRadius(value: Double) { radius = value }
}