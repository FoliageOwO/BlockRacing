package ml.windleaf.blockracing.commands

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.game
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.configurations.GoalsConfig
import ml.windleaf.blockracing.configurations.PluginConfig
import ml.windleaf.blockracing.entity.goals.GoalColumn
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import kotlin.properties.Delegates

class BlockRacingCommand: CommandExecutor, TabCompleter {
  private val config = BlockRacing.configInstances["goals"] as GoalsConfig
  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private lateinit var sender: CommandSender
  private var score: Int = 0
  private var round: Int = 0

  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    this.sender = sender
    val first = args.getOrElse(0) { "" }
    when (args.size) {
      0 -> getHelp()
      1 -> {
        when (first) {
          "help" -> getHelp()
          "goals" -> getGoals()
          "start" -> startGame(score, round)
          "start-force" -> startGame(score, round, true)
          "stop" -> stopGame()
          else -> errorCommand()
        }
      }
      2 -> {
        when (args[0]) {
          "score" -> try { score(args[1].toInt()) } catch (e: NumberFormatException) { errorArgs() }
          "round" -> try { round(args[1].toInt()) } catch (e: NumberFormatException) { errorArgs() }
          else -> errorCommand()
        }
      }
      3 -> {
        when (args[0]) {
          "start" -> try { startGame(args[1].toInt(), args[2].toInt()) } catch (e: NumberFormatException) { errorArgs() }
          else -> errorCommand()
        }
      }
      else -> errorCommand()
    }
    return true
  }

  private fun errorCommand() =
    pluginLogger.send(sender, "&c这是一个错误的命令, 请使用 &6/br help &c来获取帮助!")

  private fun errorArgs() =
    pluginLogger.send(sender, "&c参数错误, 请正确输入参数!")

  private fun getHelp() =
    listOf(
      "&a--- [BlockRacing Help] ---",
      "&2/br [help] &f- &6查看此帮助",
      "&2/br goals &f- &6查看所有可用目标",
      "&2/br start &f- &6使用已有设置开始游戏",
      "&2/br start <score> <round> &f- &6自定义目标分数和轮数开始游戏",
      "&2/br start-force &f- &6强制开始游戏",
      "&2/br score <value> &f- &6自定义游戏分数",
      "&2/br round <value> &f- &6自定义游戏轮数",
      "&2/br stop &f- &6强制结束游戏"
    ).forEach {
      pluginLogger.send(sender, it, withPrefix = false)
    }

  private fun score(score: Int) { this.score = score }

  private fun round(round: Int) { this.round = round }

  private fun startGame(score: Int?, round: Int?, ignorePlayerSize: Boolean = false) {
    val start = { game.start(score ?: pluginConfig.get("game.default-score")!!.toInt(),
      round ?: pluginConfig.get("game.default-round")!!.toInt()) }
    if (ignorePlayerSize) {
      start.invoke()
    } else {
      if (Bukkit.getOnlinePlayers().size >= 2) start.invoke()
      else pluginLogger.send(sender, "&c必须要有两个及以上玩家才可以开始游戏!")
    }
  }

  private fun stopGame() = game.stop()

  private fun getGoals() {
    val goals = config.getGoals()
    val ratings = config.getRatings()
    pluginLogger.send(sender, "--- 所有可用目标如下 ---", withPrefix = false)

    data class ColumnsHelp(
      val title: String,
      val column: GoalColumn,
      val color: String
    )

    val messages = arrayListOf<ColumnsHelp>()
    ratings.forEach { rating ->
      messages.add(ColumnsHelp("[${rating.key}] ${rating.name}", goals.first { it.rating == rating }, rating.color))
    }

    messages.forEach { help ->
      val title = help.title
      val column = help.column
      val color = help.color

      pluginLogger.send(sender, "$color$title:", withPrefix = false)
      column.blocks.forEach { block ->
        pluginLogger.send(sender, "$color - ${block.translation}", withPrefix = false)
      }
    }
  }

  override fun onTabComplete(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): MutableList<String>? {
    return mutableListOf()
  }
}