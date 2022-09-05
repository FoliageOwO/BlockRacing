package ml.windleaf.blockracing.team.storage

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import ml.windleaf.blockracing.team.AvailableTeam
import org.bukkit.entity.Player

/**
 * 团队箱子对象, 储存的 [ChestGui] 为箱子内部的物品等
 * @see ChestGui
 * @see AvailableTeam
 */
class TeamStorage(team: AvailableTeam) {
  var storage: ChestGui? = null

  init {
    storage = ChestGui(6, "&b方块竞速 &a- &d团队箱子 &a- ${team.color}${team.teamName}")
  }

  fun open(player: Player) = storage?.show(player)
}