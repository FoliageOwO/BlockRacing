package ml.windleaf.blockracing.events

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

/**
 * 用来监听玩家 `shift+f` 的动作, 以此打开团队背包
 */
class TeamStorageListener: Listener {
  /**
   * 监听玩家按 `f`, 即切换主副手
   */
  @EventHandler
  fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {
    val player = event.player
    if (player.isSneaking) {
      val result = BlockRacing.teamManager.openTeamStorage(player)
      if (!result) return
      event.isCancelled = true
      pluginLogger.send(player, "&f你打开了团队背包!")
    }
  }
}