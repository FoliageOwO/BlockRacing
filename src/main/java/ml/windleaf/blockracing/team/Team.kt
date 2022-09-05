package ml.windleaf.blockracing.team

import ml.windleaf.blockracing.team.storage.TeamStorage
import org.bukkit.entity.Player

class Team(val team: AvailableTeam) {
  val players: HashMap<String, Player> = hashMapOf()
  var storage: TeamStorage = TeamStorage(this.team)

  /**
   * 重置队伍
   */
  fun reset() {
    players.forEach {
      TeamManager.playerNameColor.reset(it.value)
    }
  }
}