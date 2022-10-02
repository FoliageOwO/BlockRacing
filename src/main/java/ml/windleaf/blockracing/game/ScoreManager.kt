package ml.windleaf.blockracing.game

import fr.mrmicky.fastboard.FastBoard
import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.configurations.PluginConfig
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.team.Team
import ml.windleaf.blockracing.utils.StringUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ScoreManager {
  companion object {
    private val boards: HashMap<UUID, FastBoard> = hashMapOf()
    val goals: HashMap<Team, ArrayList<GoalBlock>> = hashMapOf()
    private val scores: HashMap<Team, Int> = hashMapOf()
    private val succeedGoal: HashMap<Team, GoalBlock> = hashMapOf()
  }

  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private val title = StringUtil.color(pluginConfig.get("score.title")!!)
  private val size = pluginConfig.get("score.size")!!.toInt()
  private val formatTeam = pluginConfig.get("score.format.team")!!
  private val formatGoalNormal = pluginConfig.get("score.format.goal.normal")!!
  private val formatGoalSuccess = pluginConfig.get("score.format.goal.success")!!

  fun updateGoals(team: Team, goals: ArrayList<GoalBlock>) {
    Companion.goals[team] = goals
  }

  fun getScore(team: Team) = scores[team]

  fun renderScoreboard() {
    var list = arrayListOf<String>()
    goals.forEach { (team, goalsList) ->
      val t = team.info
      val teamName = "name" to "${t.color}${t.teamName}&r"
      val teamScore = "score" to (getScore(team) ?: 0)
      val gameMode = "mode" to "TODO" // todo
      val succeedGoal = succeedGoal[team]
      val succeedRating = succeedGoal?.rating
      list.add(StringUtil.map(formatTeam, hashMapOf(teamName, teamScore, gameMode)))
      if (succeedGoal != null) {
        val goalRating = "rating" to "${succeedRating?.color}${succeedRating?.name}&r"
        val succeedGoalName = "name" to succeedGoal.name
        list.add(StringUtil.map(formatGoalSuccess, hashMapOf(succeedGoalName, goalRating)))
      }
      (if (goalsList.size <= size) goalsList else goalsList.subList(0, size)).forEach { goalBlock ->
        val name = "name" to goalBlock.translation
        val rating = "rating" to "${goalBlock.rating.color}${goalBlock.rating.name}&r"
        list.add(StringUtil.map(formatGoalNormal, hashMapOf(name, rating)))
      }
      list.add("&7--------------")
    }
    list = ArrayList(list.subList(0, list.size - 1))
    boards.values.forEach { board -> board.updateLines(list.map { StringUtil.color(it) }) }
  }

  fun newBoard(player: Player) {
    val board = FastBoard(player)
    board.updateTitle(title)
    boards[player.uniqueId] = board
  }

  fun removeBoard(player: Player) = boards.remove(player.uniqueId)?.delete()

  fun reset() {
    boards.values.forEach(FastBoard::delete)
    boards.clear()
    goals.clear()
    scores.clear()
    succeedGoal.clear()
  }

  fun update(lines: ArrayList<String>, scope: (Player) -> Boolean) {
    boards.forEach { (uuid, board) ->
      if (Bukkit.getPlayer(uuid)?.let { scope.invoke(it) } == true) {
        board.updateLines(lines)
      }
    }
  }
}