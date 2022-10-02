package ml.windleaf.blockracing.commands

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.game.ScoreManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class BTeamCommand: CommandExecutor, TabCompleter {
  private lateinit var sender: CommandSender

  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    this.sender = sender
    val first = args.getOrElse(0) { "" }
    when (args.size) {
      0 -> getHelp()
      1 -> {
        when (first) {
          "help" -> getHelp()
          "new" -> newTeam()
          "clear" -> removeAllTeam()
          "list" -> getList()
          else -> errorCommand()
        }
      }
      2 -> {
        when (first) {
          "remove" -> removeTeam(args[1])
          "randomize" -> randomizeTeam(args[1].toIntOrNull())
          "goals" -> getGoals(args[1])
          else -> errorCommand()
        }
      }
      3 -> {
        when (first) {
          "join" -> joinTeam(args[1], Bukkit.getPlayer(args[2]), args[2])
          else -> errorCommand()
        }
      }
      else -> errorCommand()
    }
    return true
  }

  private fun newTeam() {
    val team = BlockRacing.teamManager.addRandomTeam()
    val message = if (team != null) "&a成功添加了一个新的队伍: ${team.color}${team.teamName}&a!" else "&c暂无可添加的队伍!"
    pluginLogger.send(sender, message)
  }

  private fun removeTeam(name: String) {
    val team = BlockRacing.teamManager.removeTeam(name)
    val message = if (team != null) "&a成功删除了 ${team.color}${team.teamName}&a!" else "&c无法找到该队伍!"
    pluginLogger.send(sender, message)
  }

  private fun randomizeTeam(size: Int?) {
    if (size != null) {
      val result = BlockRacing.teamManager.randomizePlayers(size)
      val message = if (result) "&a分配完毕, 共添加了 &7${size} &a个队伍!" else "&c请输入正确的数字, 它不应该大于玩家数和可用队伍数!"
      pluginLogger.send(sender, message)
    } else pluginLogger.send(sender, "&c请输入一个正确的数字!")
  }

  private fun joinTeam(name: String, player: Player?, playerName: String) {
    if (player != null) {
      val team = BlockRacing.teamManager.joinTeam(name, player)
      val message = if (team != null) "&a成功使玩家 &6${playerName} &a加入 ${team.color}${team.teamName}&a!" else "&c无法找到该队伍!"
      pluginLogger.send(sender, message)
    } else pluginLogger.send(sender, "&c玩家 &6${playerName} &c不存在!")
  }

  private fun removeAllTeam() {
    BlockRacing.teamManager.removeAllTeam()
    pluginLogger.send(sender, "&a成功清除所有队伍!")
  }

  private fun getList() {
    pluginLogger.send(sender, "--- 所有队伍如下 ---")
    val teams = BlockRacing.teamManager.getTeams()
    teams.forEach {
      val team = it.info
      pluginLogger.send(sender, "${team.color}${team.teamName}")
      it.players.forEach { entry ->
        pluginLogger.send(sender, "${team.color} - ${entry.value.displayName}")
      }
    }
  }

  private fun getGoals(teamName: String) {
    val team = BlockRacing.teamManager.getTeams().find { it.info.teamName == teamName }
    if (team != null) {
      val info = team.info
      val name = "${info.color}${info.teamName}"
      pluginLogger.send(sender, "--- ${name}&r对应的目标如下 ---")
      ScoreManager.goals.forEach { pair ->
        if (pair.key == team) {
          pair.value.forEach { goal -> pluginLogger.send(sender, goal.display) }
        }
      }
    } else pluginLogger.send(sender, "&c无法找到名为 [&f$teamName&c] 的队伍!")
  }

  private fun errorCommand() =
    pluginLogger.send(sender, "&c这是一个错误的命令, 请使用 &6/bt help &c来获取帮助!")

  private fun getHelp() =
    listOf(
      "&a--- [BlockRacing Team Help] ---",
      "&2/bt [help] &f- &6查看此帮助",
      "&2/bt new &f- &6随机新建一个队伍",
      "&2/bt clear &f- &6清空所有队伍",
      "&2/bt list &f- &6查看所有队伍",
      "&2/bt remove <name> &f- &6删除一个队伍",
      "&2/bt join <name> <player> &f- &6使玩家加入一个队伍",
      "&2/bt randomize <size> &f- &6随机分配指定数量队伍",
      "&2/bt goals <name> &f- &6查看队伍的所有目标"
    ).forEach {
      pluginLogger.send(sender, it, withPrefix = false)
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