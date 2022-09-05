package ml.windleaf.blockracing.team

import ml.windleaf.blockracing.team.storage.TeamStorage
import org.bukkit.entity.Player

class Team(val team: AvailableTeam) {
  val playerList: ArrayList<Player> = arrayListOf()
  var storage: TeamStorage = TeamStorage(this.team)

  /**
   * 重置队伍
   */
  fun reset() {
    playerList.forEach {
      TeamManager.playerNameColor.reset(it)
    }
  }
}