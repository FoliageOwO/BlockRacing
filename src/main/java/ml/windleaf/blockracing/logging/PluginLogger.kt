package ml.windleaf.blockracing.logging

import ml.windleaf.blockracing.utils.ChatUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class PluginLogger(name: String, color: String) {
  private val prefix = "$color[$name]&r "

  fun send(sender: CommandSender, vararg any: Any, withPrefix: Boolean = true) {
    val sb = StringBuilder()
    any.forEach { obj ->
      sb.append(" $obj ")
    }
    sender.sendMessage(ChatUtil.color("${if (withPrefix) prefix else ""}$sb"))
  }

  fun log(vararg any: Any, withPrefix: Boolean = true) =
    send(Bukkit.getConsoleSender(), *any, withPrefix = withPrefix)
}