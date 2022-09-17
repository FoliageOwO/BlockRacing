package ml.windleaf.blockracing.team

import ml.windleaf.blockracing.entity.TeamPlayer

class Team(val team: AvailableTeam) {
  val players: HashMap<String, TeamPlayer> = hashMapOf()

  /**
   * 重置队伍
   */
  fun reset() {
    players.forEach {
      TeamManager.playerNameColor.reset(it.value)
    }
  }
}