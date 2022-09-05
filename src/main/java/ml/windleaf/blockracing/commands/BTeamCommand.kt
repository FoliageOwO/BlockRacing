package ml.windleaf.blockracing.commands

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class BTeamCommand: CommandExecutor, TabCompleter {
  override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
    when (args.size) {
      0 -> getHelp(sender)
      1 -> {
        when (args[0]) {
          "help" -> getHelp(sender)
          "new" -> {
            val team = BlockRacing.teamManager.addRandomTeam()
            val message = if (team != null) "&a成功添加了一个新的队伍: ${team.color}${team.teamName}&a!" else "&c暂无可添加的队伍!"
            pluginLogger.send(sender, message)
          }
          "remove" -> {
            if (args.size != 2) {
              errorCommand(sender)
            } else {
              val name = args[1]
              val team = BlockRacing.teamManager.removeTeam(name)
              val message = if (team != null) "&a成功删除了 ${team.color}${team.teamName}&a!" else "&c无法找到该队伍!"
              pluginLogger.send(sender, message)
            }
          }
          "join" -> {
            if (args.size != 3) {
              errorCommand(sender)
            } else {
              val name = args[1]
              val player = Bukkit.getPlayer(args[2])
              if (player != null) {
                val team = BlockRacing.teamManager.joinTeam(name, player)
                val message = if (team != null) "&a成功使玩家 &6${name} &a加入 ${team.color}${team.teamName}&a!" else "&c无法找到该队伍!"
                pluginLogger.send(sender, message)
              } else pluginLogger.send(sender, "&c玩家 &6${name} &c不存在!")
            }
          }
          "randomize" -> {
            if (args.size != 2) {
              errorCommand(sender)
            } else {
              val size = args[1].toIntOrNull()
              if (size != null) {
                val result = BlockRacing.teamManager.randomizePlayers(size)
                val message = if (result) "&a分配完毕, 共添加了 &7${size} &a个队伍!" else "&c请输入正确的数字, 它不应该大于玩家数和可用队伍数!"
                pluginLogger.send(sender, message)
              } else pluginLogger.send(sender, "&c请输入一个正确的数字!")
            }
          }
        }
      }
    }
    return true
  }

  private fun errorCommand(sender: CommandSender) =
    pluginLogger.send(sender, "&c这是一个错误的命令, 请使用 &6/bt help &c来获取帮助!")

  private fun getHelp(sender: CommandSender) =
    listOf(
      "&a--- [BlockRacing Team Help] ---",
      "&2/bt [help] &f- &6查看此帮助",
      "&2/bt new &f- &6随机新建一个队伍",
      "&2/bt remove <name> &f- &6删除一个队伍",
      "&2/bt join <name> <player> &f- &6使玩家加入一个队伍",
      "&2/bt randomize <size> &f- &6随机分配指定数量队伍"
    ).forEach {
      BlockRacing.pluginLogger.send(sender, it)
    }

  override fun onTabComplete(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): MutableList<String>? {
    TODO("Not yet implemented")
  }
}