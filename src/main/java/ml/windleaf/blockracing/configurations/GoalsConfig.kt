package ml.windleaf.blockracing.configurations

import de.leonhard.storage.Yaml
import ml.windleaf.blockracing.entity.GoalColumn
import ml.windleaf.blockracing.entity.Rating

class GoalsConfig: IConfiguration("goals") {
  lateinit var goals: Yaml

  override fun loadConfig() {
    goals = Yaml("goals", "plugins/BlockRacing")
  }

  fun getRatings(): ArrayList<Rating> {
    val result = arrayListOf<Rating>()
    goals.getStringList("goals").forEach { ratingKey ->
      val prefix = "goals.$ratingKey"
      val name = goals.getString("$prefix.name")
      val color = goals.getString("$prefix.color")
      result.add(Rating(ratingKey, name, color))
    }
    return result
  }

  fun getGoals(): ArrayList<GoalColumn> {
    val result = arrayListOf<GoalColumn>()
    val ratings = getRatings()
    ratings.forEach { rating ->
      val prefix = "goals.${rating.key}"
      val blocks = goals.getStringList("$prefix.blocks")
      result.add(GoalColumn(rating, blocks))
    }
    return result
  }
}