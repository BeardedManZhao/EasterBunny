package utils_zhao.sparkTool

/**
 * @author LIMIGN
 * @version 2.0
 * @tparam >>> Spark程序入口的类型
 *
 *             一个 SparkTool 应具有的属性
 */
trait expand[>>>] {

  var sc: >>>
  var Master: String
  var AppName: String

  /**
   *
   * @param map 需要被转换的集合
   * @param wit 列表的宽度
   * @return 列表样式字符串
   */
  def getMapList(map: Map[String, String], wit: Int): String = {
    map.mkString(s"\n+${"=" * wit}+\n|\t", s"\n+${"-" * wit}+\n|\t", s"\n+${"=" * wit}+\n")
  }

  /**
   * todo 将程序进行初始化的方法
   *
   * @param args 参数列表
   * @param DB   数据源
   * @return 返回参数列表
   */
  def Listparm(args: Array[String], DB: String): String

  /**
   * todo 将sc获取到
   *
   * @param confList 配置参数列表
   * @return Spark程序入口
   */
  def getSparkENT(confList: List[(String, String)]): >>>

  /**
   * todo 关闭程序连接
   */
  def close(): Unit
}
