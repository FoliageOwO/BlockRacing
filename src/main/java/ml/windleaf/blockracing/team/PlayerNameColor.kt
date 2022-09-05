package ml.windleaf.blockracing.team

import ml.windleaf.blockracing.utils.ChatUtil
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

/**
 * 储存玩家原始显示名, 配置玩家显示名颜色
 * @see Player.setDisplayName
 */
class PlayerNameColor {
  private val dataBackup: HashMap<UUID, String> = hashMapOf()

  /**
   * 将玩家的显示名设置为队伍对应颜色
   * @param player 玩家
   * @param team 队伍
   * @see Player.setDisplayName
   */
  fun setPlayer(player: Player, team: AvailableTeam) {
    val originName = player.displayName
    dataBackup[player.uniqueId] = originName
    player.setDisplayName(ChatUtil.color(team.color + originName))
  }

  /**
   * 重置玩家的显示名颜色
   * @param player 玩家
   * @see Player.setDisplayName
   */
  fun reset(player: Player) = player.setDisplayName(ChatUtil.color(dataBackup[player.uniqueId]!!))
}