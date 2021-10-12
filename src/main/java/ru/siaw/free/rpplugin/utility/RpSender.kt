package ru.siaw.free.rpplugin.utility

import org.bukkit.entity.Player
import java.lang.StringBuilder

class RpSender {
    private var enabled = false
    private var global = false
    private var radius: Double = 0.0
    private var message: String = ""

    fun use(sender: Player, msg: String) {
        if (enabled) {
            val toSend = replace(sender.displayName, msg).trim { it <= ' ' }
            if (global) Print.toPlayers(toSend) else for (entity in sender.world.getNearbyEntities(sender.location, radius, radius, radius)) if (entity is Player) Print.toPlayer(entity, toSend)
        }
    }

    private fun replace(playerName: String, playerMsg: String): String {
        val newLine = StringBuilder(message)
        var word = "%player"
        if (message.contains(word)) {
            val index = newLine.indexOf(word)
            newLine.replace(index, index + 7, playerName)
        }
        word = "%message"
        if (message.contains(word)) {
            val index = newLine.indexOf(word)
            newLine.replace(index, index + 8, playerMsg)
        }
        return newLine.toString()
    }

    fun setMeEnabled(value: Boolean) { enabled = value }

    fun setMeGlobal(value: Boolean) { global = value }

    fun setMeRadius(value: Double) { radius = value }

    fun setMeMsg(value: String) { message = value }
}