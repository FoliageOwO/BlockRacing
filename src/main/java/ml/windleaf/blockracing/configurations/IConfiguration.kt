package ml.windleaf.blockracing.configurations

/**
 * 配置抽象类, 用来描述插件各个配置文件
 */
abstract class IConfiguration(val name: String) {
  /**
   * 开始加载此配置文件到内存中
   */
  abstract fun loadConfig()
}