package ml.windleaf.blockracing.listeners

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.BlockRacing.Companion.instance
import ml.windleaf.blockracing.BlockRacing.Companion.pluginManager
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.events.PlayerCompleteGoalEvent
import ml.windleaf.blockracing.score.ScoreboardManager
import ml.windleaf.blockracing.team.Team
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.PlayerInventory

class ListenPlayerGetItem(private val who: Player): Listener {
  private fun checkAndCall(material: Material?) {
    val team = BlockRacing.teamManager.getTeams().find { it.players.containsValue(who) }
    if (team != null) {
      if (team.getGoals().any { it.material == material }) {
        val event = PlayerCompleteGoalEvent(who)
        Bukkit.getScheduler().runTask(instance) { _ -> pluginManager.callEvent(event) }
      }
    }
  }

  @EventHandler
  fun onInventoryMoveItem(e: InventoryMoveItemEvent) {
    val destination = e.destination
    if (destination.type == InventoryType.PLAYER && who.inventory == destination as PlayerInventory) {
      if (who.inventory.holder == e.destination.holder) {
        checkAndCall(e.item.type)
      }
    }
  }

  @EventHandler
  fun onEntityPickupItem(e: EntityPickupItemEvent) {
    if (e.entityType == EntityType.PLAYER) {
      if (who == e.entity as Player) {
        checkAndCall(e.item.itemStack.type)
      }
    }
  }

  @EventHandler
  fun onCraftItem(e: CraftItemEvent) {
    if (who == e.whoClicked as Player) {
      checkAndCall(e.currentItem?.type)
    }
  }

  private fun Team.getGoals(): ArrayList<GoalBlock> {
    return ArrayList(ScoreboardManager.goals[this]!!)
  }
}