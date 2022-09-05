package ml.windleaf.blockracing.team

import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.collections.HashMap

class TeamManager {
  private val teams: HashMap<String, Team> = hashMapOf()

  companion object {
    val playerNameColor = PlayerNameColor()
  }

  fun getTeams(): ArrayList<Team> = ArrayList(teams.values.toList())

  fun randomizePlayers(size: Int): Boolean {
    val players = Bukkit.getOnlinePlayers().toList()
    if (size > players.size || size > AvailableTeam.values().size) return false
    removeAllTeam()
    players.chunked(size) {
      val team = addRandomTeam()!!
      it.forEach { player ->
        joinTeam(team.teamName, player)
      }
    }
    return true
  }

  fun addRandomTeam(): AvailableTeam? {
    return if (teams.size != AvailableTeam.values().size) {
      var team: AvailableTeam? = null
      while (team == null || teams.values.any { it.team == team }) {
        team = AvailableTeam.getRandomTeam(expect = teams.values.map { it.team })
      }
      teams[team.teamName] = Team(team)
      AvailableTeam.availableList.remove(team)
      team
    } else null
  }

  fun removeTeam(name: String): AvailableTeam? {
    return if (teams.containsKey(name)) {
      val team = teams[name]!!
      team.reset()
      teams.remove(name)
      AvailableTeam.availableList.add(team.team)
      team.team
    } else null
  }

  fun removeAllTeam() {
    teams.values.forEach(Team::reset)
    teams.clear()
    AvailableTeam.availableList = arrayListOf(*AvailableTeam.values())
  }

  fun joinTeam(teamName: String, player: Player): AvailableTeam? {
    return if (teams.containsKey(teamName)) {
      teams.forEach { entry ->
        val team = entry.value
        if (team.playerList.contains(player)) {
          team.playerList.remove(player)
        }
      }
      val team = teams[teamName]!!
      team.playerList.add(player)
      playerNameColor.setPlayer(player, team.team)
      pluginLogger.send(player, "&a你已加入 ${team.team.color}${team.team.teamName}&a!")
      team.team
    } else null
  }

  fun openTeamStorage(player: Player): Boolean {
    val team = teams.values.find { it.playerList.contains(player) } ?: return false
    team.storage.open(player)
    return true
  }
}