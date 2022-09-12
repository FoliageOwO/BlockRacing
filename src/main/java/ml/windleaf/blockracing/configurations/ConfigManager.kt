package ml.windleaf.blockracing.configurations

import ml.windleaf.blockracing.BlockRacing

class ConfigManager {
  var configClassList: List<Class<out IConfiguration>> = arrayListOf(
    GoalsConfig::class.java,
    PluginConfig::class.java
  )

  init {
    configClassList.forEach { config ->
      val instance = config.getDeclaredConstructor().newInstance()
      val name = instance.name
      BlockRacing.pluginLogger.log("&f正在加载配置文件 &3${name} &6=> &9${instance}")
      instance.loadConfig()
      BlockRacing.configInstances[name] = instance
    }
  }
}