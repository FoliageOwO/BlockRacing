package ml.windleaf.blockracing

import ml.windleaf.blockracing.commands.BTeamCommand
import ml.windleaf.blockracing.commands.BlockRacingCommand
import ml.windleaf.blockracing.configurations.ConfigManager
import ml.windleaf.blockracing.configurations.IConfiguration
import ml.windleaf.blockracing.logging.PluginLogger
import ml.windleaf.blockracing.team.TeamManager
import org.bukkit.plugin.java.JavaPlugin

class BlockRacing: JavaPlugin() {
  companion object {
    val pluginLogger: PluginLogger = PluginLogger("BlockRacing", "&9")
    lateinit var instance: BlockRacing
    lateinit var configManager: ConfigManager
    lateinit var teamManager: TeamManager

    val configInstances = mutableMapOf<String, IConfiguration>()
  }

  override fun onEnable() {
    pluginLogger.log("&f正在加载插件...")
    val startTime = System.currentTimeMillis()

    instance = this
    config.options().copyDefaults()
    saveDefaultConfig()

    configManager = ConfigManager()
    teamManager = TeamManager()
    registerCommands()

    val endTime = System.currentTimeMillis()
    pluginLogger.log("&a加载完成, 共耗时 ${endTime - startTime} 毫秒!")
  }

  private fun registerCommands() {
    val br = getCommand("blockracing")
    val brC = BlockRacingCommand()
    br?.setExecutor(brC)
    br?.tabCompleter = brC

    val bt = getCommand("bteam")
    val btC = BTeamCommand()
    bt?.setExecutor(btC)
    bt?.tabCompleter = btC
  }

  override fun onDisable() {
    super.onDisable()
  }
}