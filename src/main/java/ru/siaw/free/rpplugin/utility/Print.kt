package ru.siaw.free.rpplugin.utility

import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object Print {
    @JvmStatic
    fun msgToConsole(msg: String) {
        println(ChatColor.GREEN.toString() + msg)
    }

    @JvmStatic
    fun infoToConsole(msg: String) {
        println(ChatColor.YELLOW.toString() + msg)
    }

    @JvmStatic
    fun errToConsole(msg: String) {
        println(ChatColor.RED.toString() + msg)
    }

    fun toPlayer(p: Player, msg: String?) {
        p.sendMessage(msg)
    }

    fun toPlayers(msg: String?) {
        for (p in Bukkit.getOnlinePlayers()) toPlayer(p, msg)
    }
}