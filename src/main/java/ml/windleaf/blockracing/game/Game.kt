package ml.windleaf.blockracing.game

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.instance
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.BlockRacing.Companion.scoreManager
import ml.windleaf.blockracing.BlockRacing.Companion.teamManager
import ml.windleaf.blockracing.configurations.GoalsConfig
import ml.windleaf.blockracing.configurations.PluginConfig
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.game.listeners.PlayerCompleteGoal
import ml.windleaf.blockracing.team.Team
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class Game {
  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private val goals = BlockRacing.configInstances["goals"] as GoalsConfig
  private var score: Int = 0
  private var round: Int = 0
  private val unique = pluginConfig.get("game.unique-goals")!!.toBoolean()
  private val listenerTasks: HashMap<Player, BukkitTask> = hashMapOf()

  fun start(score: Int, round: Int) {
    this.score = score
    this.round = round

    log("&f检测队伍...")
    val teams = teamManager.getTeams()
    if (teams.size <= 1) {
      log("&e未检测到队伍, 开始随机分配队伍...")
      teamManager.randomizePlayers(pluginConfig.get("game.default-team-size")!!.toInt())
    }

    log("&f分配队伍目标...")
    var availableGoals = arrayListOf<GoalBlock>()
    this.goals.getGoals().forEach { column -> column.blocks.forEach { block -> availableGoals.add(block) } }
    val goals: HashMap<Team, ArrayList<GoalBlock>> = hashMapOf()
    teams.forEach { team ->
      availableGoals = ArrayList(availableGoals.shuffled())
      val g = if (availableGoals.size <= round) availableGoals else availableGoals.subList(0, round)
      goals[team] = ArrayList(g)
      if (!unique) availableGoals.removeAll(g.toSet())
    }

    log("&f配置计分板...")
    teams.forEach { team ->
      scoreManager.updateGoals(team, goals[team]!!)
      team.players.values.forEach { player ->
        val t = team.info
        pluginLogger.send(player, "&8[&a!&8]&a 游戏开始! 你的队伍是: ${t.color}${t.teamName}&a!", withPrefix = false)
        scoreManager.newBoard(player)
      }
    }
    scoreManager.renderScoreboard()

    log("&a注册监听器...")
    Bukkit.getOnlinePlayers().forEach { p ->
      val task = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, ListenPlayerGetItem(p), 0, 10)
      listenerTasks[p] = task
    }
    registerListeners()
  }

  fun stop() {
    scoreManager.reset()
    listenerTasks.values.forEach(BukkitTask::cancel)
    pluginLogger.broadcast("&8[&a!&8]&a 游戏已结束!")
  }

  private fun registerListeners() {
    val pm = instance.server.pluginManager
    pm.registerEvents(PlayerCompleteGoal(), instance)
  }

  private fun log(msg: String) = pluginLogger.log(msg)
}