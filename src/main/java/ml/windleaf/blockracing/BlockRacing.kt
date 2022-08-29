package ml.windleaf.blockracing

import ml.windleaf.blockracing.commands.BlockRacingCommand
import ml.windleaf.blockracing.configurations.ConfigManager
import ml.windleaf.blockracing.configurations.IConfiguration
import ml.windleaf.blockracing.logging.PluginLogger
import org.bukkit.plugin.java.JavaPlugin

class BlockRacing: JavaPlugin() {
  companion object {
    val pluginLogger: PluginLogger = PluginLogger("BlockRacing", "&9")
    lateinit var configManager: ConfigManager

    val configInstances = mutableMapOf<String, IConfiguration>()
  }

  override fun onEnable() {
    pluginLogger.log("&f正在加载插件...")
    val startTime = System.currentTimeMillis()

    config.options().copyDefaults()
    saveDefaultConfig()

    registerCommands()
    configManager = ConfigManager()

    val endTime = System.currentTimeMillis()
    pluginLogger.log("&a加载完成, 共耗时 ${endTime - startTime} 毫秒!")
  }

  private fun registerCommands() {
    val br = getCommand("blockracing")
    br?.setExecutor(BlockRacingCommand())
    br?.tabCompleter = BlockRacingCommand()
  }

  override fun onDisable() {
    super.onDisable()
  }
}