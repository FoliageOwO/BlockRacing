package ml.windleaf.blockracing.entity.goals

/**
 * 目标栏目中的难度
 */
data class Rating(
  /**
   * 难度的键值, 决定在 `yml` 文件的读取
   */
  val key: String,

  /**
   * 难度的名字
   */
  val name: String,

  /**
   * 难度的颜色
   */
  val color: String
)
