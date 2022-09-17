package ml.windleaf.blockracing.team

/**
 * 所有内置可用的队伍枚举
 * @param teamName 队伍名
 * @param color 队伍颜色
 * @see TeamManager.addRandomTeam
 */
enum class TeamInfo(val teamName: String, val color: String) {
  WHITE("白队", "&f"),
  RED("红队", "&c"),
  YELLOW("黄队", "&e"),
  AQUA("青队", "&b"),
  GREEN("绿队", "&a"),
  PURPLE("紫队", "&d"),
  BLUE("蓝队", "&9"),
  GRAY("灰队", "&7");

  companion object {
    var availableList: ArrayList<TeamInfo> = arrayListOf(*values())

    /**
     * 获取一个随机的队伍
     * @param expect 获取除了这个列表中的队伍
     * @return 可用的队伍
     * @see TeamInfo
     */
    fun getRandomTeam(expect: Collection<TeamInfo>): TeamInfo {
      val size = availableList.size
      val randomIndex = (0 until size).random()
      var team: TeamInfo? = null
      while (team == null || expect.contains(team)) {
        team = availableList[randomIndex]
      }
      return team
    }
  }
}