package ml.windleaf.blockracing.configurations

import de.leonhard.storage.Yaml
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.entity.GoalColumn
import ml.windleaf.blockracing.entity.Rating
import org.bukkit.Material

class GoalsConfig: IConfiguration("goals") {
  private lateinit var goals: Yaml
  val blocks = hashMapOf<String, Material>()

  override fun loadConfig() {
    goals = Yaml("goals", "plugins/BlockRacing")

    getGoals().forEach { column ->
      column.blocks.forEach { block ->
        val material = Material.getMaterial(block.uppercase())
        if (material == null || !material.isBlock) pluginLogger.log("&l&c无法读取方块名 ${column.rating.key}.blocks.$block, 请正确输入方块名!")
        blocks[block] = material ?: Material.AIR
      }
    }
  }

  fun getRatings(): ArrayList<Rating> {
    val result = arrayListOf<Rating>()
    (goals.get("goals") as LinkedHashMap<String, Any>).forEach { entry ->
      val ratingKey = entry.key
      val prefix = "goals.$ratingKey"
      val name = goals.getString("$prefix.name")
      val color = goals.getString("$prefix.color")
      result.add(Rating(ratingKey, name, color))
    }
    return result
  }

  fun getGoals(): ArrayList<GoalColumn> {
    val result = arrayListOf<GoalColumn>()
      getRatings().forEach { rating ->
      val prefix = "goals.${rating.key}"
      val blocks = goals.getStringList("$prefix.blocks")
      result.add(GoalColumn(rating, blocks))
    }
    return result
  }
}