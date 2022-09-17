package ml.windleaf.blockracing.entity.goals

import org.bukkit.Material

data class GoalBlock(
  val name: String,
  val translation: String,
  val material: Material,
  val rating: Rating
)