package ml.windleaf.blockracing.commands

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.configurations.GoalsConfig
import ml.windleaf.blockracing.entity.GoalColumn
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class BlockRacingCommand: CommandExecutor, TabCompleter {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    when (args.size) {
      // 发送帮助
      0 -> getHelp(sender)
      1 -> {
        when (args[0]) {
          "help" -> getHelp(sender)
          "goals" -> getGoals(sender)
        }
      }
      else -> errorCommand(sender)
    }

    return true
  }

  private fun errorCommand(sender: CommandSender) =
    pluginLogger.send(sender, "&c这是一个错误的命令, 请使用 &6/br help &c来获取帮助!")

  private fun getHelp(sender: CommandSender) =
    listOf(
      "&a--- [BlockRacing Help] ---",
      "&2/br [help] &f- &6查看此帮助",
      "&2/br goals &f- &6查看所有可用目标",
    ).forEach {
      pluginLogger.send(sender, it)
    }

  /**
   * 获取所有可用目标, 以下是例子
   *
   * --- 所有可用目标如下 ---
   * 简单|easy:
   *  - 工作台
   * 普通|normal:
   *  - 铁镐
   */
  private fun getGoals(sender: CommandSender) {
    val config = BlockRacing.configInstances["goals"] as GoalsConfig
    val goals = config.getGoals()
    val ratings = config.getRatings()
    pluginLogger.send(sender, "--- 所有可用目标如下 ---")

    data class ColumnsHelp(
      /**
       * 难度标题
       */
      val title: String,

      /**
       * 目标栏目
       */
      val column: GoalColumn,

      /**
       * 颜色
       */
      val color: String
    )

    val messages = arrayListOf<ColumnsHelp>()
    ratings.forEach { rating ->
      messages.add(ColumnsHelp("${rating.key}|${rating.name}", goals.first { it.rating == rating }, rating.color))
    }

    messages.forEach { help ->
      val title = help.title
      val column = help.column
      val color = help.color

      pluginLogger.send(sender, "$color$title:")
      column.blocks.forEach { blockName ->
        pluginLogger.send(sender, "$color - ${getTranslation(blockName)}")
      }
    }
  }

  private fun getTranslation(blockName: String) = TranslatableComponent("block.minecraft.$blockName").translate

  override fun onTabComplete(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): MutableList<String>? {
    TODO("Not yet implemented")
  }
}