package ml.windleaf.blockracing.listeners

import ml.windleaf.blockracing.BlockRacing.Companion.instance
import ml.windleaf.blockracing.BlockRacing.Companion.pluginManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ListenPlayerJoin: Listener {
  @EventHandler
  fun onPlayerJoin(e: PlayerJoinEvent) = pluginManager.registerEvents(ListenPlayerGetItem(e.player), instance)
}