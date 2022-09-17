package utils_zhao

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
   * 将Int类型IPv4地址转换为字符串类型
   */
  def number2IpString(ip: Int): String = {
    val buffer: Array[Int] = new Array[Int](4)
    buffer(0) = (ip >> 24) & 0xff
    buffer(1) = (ip >> 16) & 0xff
    buffer(2) = (ip >> 8) & 0xff
    buffer(3) = ip & 0xff
    // 返回IPv4地址
    buffer.mkString(".")
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
