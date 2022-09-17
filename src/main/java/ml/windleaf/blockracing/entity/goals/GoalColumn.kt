package ml.windleaf.blockracing.entity.goals

/**
 * 目标栏目
 */
data class GoalColumn(
  /**
   * 难度
   *
   * @see Rating
   */
  val rating: Rating,

  /**
   * 该难度目标的所有方块
   */
  val blocks: List<GoalBlock>
)