package ml.windleaf.blockracing.entity

import ml.windleaf.blockracing.BlockRacing.Companion.teamManager
import ml.windleaf.blockracing.team.Team
import org.bukkit.entity.Player

abstract class TeamPlayer: Player {
  private lateinit var team: Team

  fun setTeam(team: Team) {
    this.team = team
  }

  fun getTeam(): Team {
    return this.team
  }
}

fun Player.toTeamPlayer(): TeamPlayer? {
  val team = teamManager.getTeams().find { it.players.values.contains(this) }
  return if (team != null) {
    (this as TeamPlayer).setTeam(team)
    this
  } else null
}