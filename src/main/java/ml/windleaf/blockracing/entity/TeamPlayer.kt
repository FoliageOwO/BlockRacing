package ml.windleaf.blockracing.entity

import ml.windleaf.blockracing.team.Team
import org.bukkit.entity.Player

abstract class TeamPlayer(val team: Team): Player {
}