package ml.windleaf.blockracing.translations

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.configurations.PluginConfig

class TranslationManager {
  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private var translations: HashMap<String, Translation> = hashMapOf(
    "zh_cn" to Translation("zh_cn")
  )

  fun getTranslationInstance() = translations[pluginConfig.get("translation")]
}