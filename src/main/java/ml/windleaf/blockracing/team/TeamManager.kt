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
    if (size > players.size || size > TeamInfo.values().size) return false
    removeAllTeam()
    players.chunked(size) {
      val team = addRandomTeam()!!
      it.forEach { player ->
        joinTeam(team.teamName, player)
      }
    }
    return true
  }

  fun addRandomTeam(): TeamInfo? {
    return if (teams.size != TeamInfo.values().size) {
      var team: TeamInfo? = null
      while (team == null || teams.values.any { it.team == team }) {
        team = TeamInfo.getRandomTeam(expect = teams.values.map { it.team })
      }
      teams[team.teamName] = Team(team)
      TeamInfo.availableList.remove(team)
      team
    } else null
  }

  fun removeTeam(name: String): TeamInfo? {
    return if (teams.containsKey(name)) {
      val team = teams[name]!!
      team.reset()
      teams.remove(name)
      TeamInfo.availableList.add(team.team)
      team.team
    } else null
  }

  fun removeAllTeam() {
    teams.values.forEach(Team::reset)
    teams.clear()
    TeamInfo.availableList = arrayListOf(*TeamInfo.values())
  }

  fun joinTeam(teamName: String, player: Player): TeamInfo? {
    val uuid = player.uniqueId.toString()
    return if (teams.containsKey(teamName)) {
      teams.forEach { entry ->
        val team = entry.value
        if (team.players.containsKey(uuid)) {
          team.players.remove(uuid)
        }
      }
      val team = teams[teamName]!!
      team.players[uuid] = player
      playerNameColor.setPlayer(player, team.team)
      pluginLogger.send(player, "&a你已加入 ${team.team.color}${team.team.teamName}&a!")
      team.team
    } else null
  }
}