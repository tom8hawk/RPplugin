package ru.siaw.free.rpplugin.function

import ru.siaw.free.rpplugin.utility.RpSender
import org.bukkit.entity.Player

class Me {
    private val msgSender = RpSender()
    fun send(sender: Player?, msg: String?) {
        msgSender.use(sender!!, msg!!)
    }

    fun setMeEnabled(value: Boolean) {
        msgSender.setMeEnabled(value)
    }

    fun setMeGlobal(value: Boolean) {
        msgSender.setMeGlobal(value)
    }

    fun setMeRadius(value: Int) {
        msgSender.setMeRadius(value.toDouble())
    }

    fun setMeMsg(value: String?) {
        msgSender.setMeMsg(value!!)
    }
}