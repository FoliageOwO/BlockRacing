package ml.windleaf.blockracing.translations

import ml.windleaf.blockracing.BlockRacing
import ml.windleaf.blockracing.configurations.PluginConfig

class TranslationManager {
  private val pluginConfig = BlockRacing.configInstances["config"] as PluginConfig
  private var translations: HashMap<String, Translation> = hashMapOf(
    "zh_cn" to Translation("zh_cn")
  )

  companion object {
    fun getBlockTranslation(blockName: String): String {
      val key = "block.minecraft.${blockName}"
      return BlockRacing.translationManager.getTranslationInstance()?.getTranslation(key) ?: "&o${blockName}"
    }
  }

  fun getTranslationInstance() = translations[pluginConfig.get("translation")!!]
}