package ml.windleaf.blockracing.configurations

import de.leonhard.storage.Yaml

class PluginConfig: IConfiguration("config") {
  private lateinit var config: Yaml

  override fun loadConfig() {
    config = Yaml("config", "plugins/BlockRacing")
  }

  fun get(path: String): Any? = config.get(path)
}