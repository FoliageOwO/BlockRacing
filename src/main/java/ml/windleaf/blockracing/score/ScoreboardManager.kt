package ml.windleaf.blockracing.score

import fr.mrmicky.fastboard.FastBoard
import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.configurations.PluginConfig
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.team.Team
import ml.windleaf.blockracing.translations.TranslationManager.Companion.getBlockTranslation
import ml.windleaf.blockracing.utils.StringUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ScoreboardManager {
  private val boards: HashMap<UUID, FastBoard> = hashMapOf()
  private val goals: HashMap<Team, ArrayList<GoalBlock>> = hashMapOf()
  private val scores: HashMap<Team, Int> = hashMapOf()
  private val succeedGoal: HashMap<Team, GoalBlock> = hashMapOf()

  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private val title = pluginConfig.get("score.title") as String
  private val size = pluginConfig.get("score.size") as Int
  private val formatTeam = pluginConfig.get("score.format.team") as String
  private val formatGoalNormal = pluginConfig.get("score.format.goal.normal") as String
  private val formatGoalSuccess = pluginConfig.get("score.format.goal.success") as String

  fun updateGoals(team: Team, goals: ArrayList<GoalBlock>) {
    this.goals[team] = goals
  }

  fun getScore(team: Team) = scores[team]

  fun render() {
    goals.forEach { (team, goalsList) ->
      val t = team.team
      val list = arrayListOf<String>()
      val teamName = "name" to "${t.color}${t.teamName}"
      val teamScore = "score" to (getScore(team) ?: "")
      val gameMode = "mode" to "TODO" // todo
      val succeedGoal = succeedGoal[team]
      val succeedRating = succeedGoal?.rating
      list.add(StringUtil.map(formatTeam, hashMapOf(teamName, teamScore, gameMode)))
      if (succeedGoal != null) {
        val goalRating = "rating" to "${succeedRating?.color}${succeedRating?.name}"
        val succeedGoalName = "name" to succeedGoal.name
        list.add(StringUtil.map(formatGoalSuccess, hashMapOf(succeedGoalName, goalRating)))
      }
      (if (goalsList.size <= size) goalsList else goalsList.subList(0, size - 1)).forEach { goalBlock ->
        val name = "name" to goalBlock.translation
        val rating = "rating" to goalBlock.rating
        list.add(StringUtil.map(formatGoalNormal, hashMapOf(name, rating)))
      }
    }
  }

  fun newBoard(player: Player) {
    val board = FastBoard(player)
    board.updateTitle(title)
    boards[player.uniqueId] = board
  }

  fun removeBoard(player: Player) = boards.remove(player.uniqueId)?.delete()

  fun update(lines: ArrayList<String>, scope: (Player) -> Boolean) {
    boards.forEach { (uuid, board) ->
      if (Bukkit.getPlayer(uuid)?.let { scope.invoke(it) } == true) {
        board.updateLines(lines)
      }
    }
  }
}