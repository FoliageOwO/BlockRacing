package ml.windleaf.blockracing.game

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.BlockRacing.Companion.scoreboardManager
import ml.windleaf.blockracing.BlockRacing.Companion.teamManager
import ml.windleaf.blockracing.configurations.GoalsConfig
import ml.windleaf.blockracing.configurations.PluginConfig
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.entity.goals.Rating
import ml.windleaf.blockracing.team.Team

class Game {
  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private val goals = BlockRacing.configInstances["goals"] as GoalsConfig
  private var score: Int = 0
  private var round: Int = 0
  private val unique = pluginConfig.get("game.unique-goals") as Boolean

  fun start(score: Int, round: Int) {
    this.score = score
    this.round = round

    log("&f检测队伍...")
    val teams = teamManager.getTeams()
    if (teams.size <= 1) {
      log("&e未检测到队伍, 开始随机分配队伍...")
      teamManager.randomizePlayers(pluginConfig.get("game.default-team-size") as Int)
    }

    log("&f分配队伍目标...")
    var availableGoals = arrayListOf<GoalBlock>()
    this.goals.getGoals().forEach { column ->
      column.blocks.forEach { block -> availableGoals.add(block) }
    }
    val goals: HashMap<Team, ArrayList<GoalBlock>> = hashMapOf()
    teams.forEach { team ->
      availableGoals = ArrayList(availableGoals.shuffled())
      val g = if (availableGoals.size <= round) availableGoals else availableGoals.subList(0, round - 1)
      goals[team] = ArrayList(g)
      if (!unique) availableGoals.removeAll(g.toSet())
    }

    log("&f配置计分板...")
    teams.forEach { team ->
      scoreboardManager.updateGoals(team, goals[team]!!)
      team.players.values.forEach { player ->
        val t = team.info
        pluginLogger.send(player, "&a游戏即将开始, 你的队伍是: ${t.color}${t.teamName}&a!")
        scoreboardManager.newBoard(player)
      }
    }
    scoreboardManager.render()
  }

  private fun log(msg: String) = pluginLogger.log(msg)
}