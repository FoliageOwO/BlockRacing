package ml.windleaf.blockracing.logging

import ml.windleaf.blockracing.utils.StringUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class PluginLogger(name: String, color: String) {
  private val prefix = "$color[$name]&r "

  /**
   * 向对象发送消息
   * @param sender 对象, 可以是玩家或控制台
   * @param any 消息对象, 可以是任何 [Object]
   * @param withPrefix 是否带上插件名称前缀, 默认为 `true`
   */
  fun send(sender: CommandSender, vararg any: Any, withPrefix: Boolean = true) {
    val sb = StringBuilder()
    any.forEach { obj ->
      sb.append("$obj ")
    }
    sender.sendMessage(StringUtil.color("${if (withPrefix) prefix else ""}$sb"))
  }

  /**
   * 同 [send], 不过可以一次性发送消息到很多对象
   */
  fun send(sender: List<CommandSender>, vararg any: Any, withPrefix: Boolean = true) =
    sender.forEach { send(it, any, withPrefix = withPrefix) }

  /**
   * 向控制台发送消息
   * @param any 消息对象, 可以是任何 [Object]
   * @param withPrefix 是否带上插件名称前缀, 默认为 `true`
   */
  fun log(vararg any: Any, withPrefix: Boolean = true) =
    send(Bukkit.getConsoleSender(), *any, withPrefix = withPrefix)
}