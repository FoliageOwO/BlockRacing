package ml.windleaf.blockracing.utils

object StringUtil {
  fun color(string: String) = string.replace('&', '§')

  fun map(string: String, map: HashMap<String, Any>): String {
    var replaced = string
    map.forEach { (key, value) -> replaced = replaced.replace("{$key}", value.toString()) }
    return replaced
  }
}