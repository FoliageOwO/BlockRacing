package ml.windleaf.blockracing.team

import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.game.ScoreManager
import org.bukkit.entity.Player

class Team(val info: TeamInfo) {
  val players: HashMap<String, Player> = hashMapOf()

  /**
   * 重置队伍
   */
  fun reset() {
    players.forEach {
      TeamManager.playerNameColor.reset(it.value)
    }
  }

  /**
   * 获取当前队伍的所有目标
   */
  fun getGoals(): ArrayList<GoalBlock> = ArrayList(ScoreManager.goals[this]!!)
}