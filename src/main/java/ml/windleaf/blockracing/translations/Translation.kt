package ml.windleaf.blockracing.translations

import de.leonhard.storage.Json
import ml.windleaf.blockracing.BlockRacing.Companion.instance

class Translation(val name: String) {
  private var translation: Json

  init {
    instance.saveResource("translations/${name}.json", true)
    translation = Json(name, "plugins/BlockRacing/translations")
  }

  fun getTranslation(key: String): String? = translation.data[key] as String?
}