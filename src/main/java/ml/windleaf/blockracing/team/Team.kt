package ml.windleaf.blockracing.team

import org.bukkit.entity.Player

class Team(val team: TeamInfo) {
  val players: HashMap<String, Player> = hashMapOf()

  /**
   * 重置队伍
   */
  fun reset() {
    players.forEach {
      TeamManager.playerNameColor.reset(it.value)
    }
  }
}