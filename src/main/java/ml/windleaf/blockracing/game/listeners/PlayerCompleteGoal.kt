package ml.windleaf.blockracing.game.listeners

import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.BlockRacing.Companion.scoreManager
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.events.PlayerCompleteGoalEvent
import ml.windleaf.blockracing.team.Team
import ml.windleaf.blockracing.team.TeamInfo
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class PlayerCompleteGoal: Listener {
  @EventHandler(priority = EventPriority.HIGHEST)
  fun onPlayerCompleteGoalEvent(e: PlayerCompleteGoalEvent) {
    val player: Player = e.player
    val team: Team = e.team
    val info: TeamInfo = team.info
    val goal: GoalBlock = e.goal
    val goals: ArrayList<GoalBlock> = ArrayList(team.getGoals())
    goals.remove(goal)
    scoreManager.updateGoals(team, goals)
    scoreManager.renderScoreboard()
    pluginLogger.broadcast(
      "&8[&e√&8]&8 ${info.color}${info.teamName}&8队员 [${player.displayName}&8] &a完成&8目标 [${goal.display}&8]!", withPrefix = false
    )
  }
}