package ml.windleaf.blockracing.events

import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.team.Team
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class PlayerCompleteGoalEvent(who: Player, val team: Team, val goal: GoalBlock): PlayerEvent(who) {
  companion object {
    private val handlers: HandlerList = HandlerList()

    fun getHandlers(): HandlerList = handlers
  }

  override fun getHandlers() = Companion.handlers
}