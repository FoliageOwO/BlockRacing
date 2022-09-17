package ml.windleaf.blockracing.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class PlayerCompleteGoalEvent(who: Player): PlayerEvent(who) {
  companion object {
    private val handlers: HandlerList = HandlerList()

    fun getHandlers(): HandlerList = handlers
  }

  override fun getHandlers() = Companion.handlers
}