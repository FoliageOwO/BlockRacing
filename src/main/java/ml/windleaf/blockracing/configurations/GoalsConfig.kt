package ml.windleaf.blockracing.configurations

import de.leonhard.storage.Yaml
import ml.windleaf.blockracing.BlockRacing.Companion.pluginLogger
import ml.windleaf.blockracing.entity.goals.GoalBlock
import ml.windleaf.blockracing.entity.goals.GoalColumn
import ml.windleaf.blockracing.entity.goals.Rating
import ml.windleaf.blockracing.translations.TranslationManager.Companion.getBlockTranslation
import org.bukkit.Material

class GoalsConfig: IConfiguration("goals") {
  private lateinit var goals: Yaml
  private val blocks = hashMapOf<String, Material>()

  override fun loadConfig() {
    goals = Yaml("goals", "plugins/BlockRacing")
  }

  fun getRatings(): ArrayList<Rating> {
    val result = arrayListOf<Rating>()
    (goals.get("goals") as LinkedHashMap<*, *>).forEach { entry ->
      val ratingKey = entry.key
      val prefix = "goals.$ratingKey"
      val name = goals.getString("$prefix.name")
      val color = goals.getString("$prefix.color")
      result.add(Rating(ratingKey as String, name, color))
    }
    return result
  }

  fun getGoals(): ArrayList<GoalColumn> {
    val result = arrayListOf<GoalColumn>()
    getRatings().forEach { rating ->
      val prefix = "goals.${rating.key}"
      val blocks = goals.getStringList("$prefix.blocks")
      val goalBlocks = arrayListOf<GoalBlock>()
      blocks.forEach { blockName ->
        val material = Material.getMaterial(blockName.uppercase()) ?: Material.AIR
        this.blocks[blockName] = material
        val translation = getBlockTranslation(blockName)
        goalBlocks.add(GoalBlock(blockName, translation, material, rating,
          "${rating.color}${rating.name} | $translation"))
      }
      result.add(GoalColumn(rating, goalBlocks))
    }
    return result
  }
}