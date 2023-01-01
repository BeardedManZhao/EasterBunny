package utils_zhao.sparkTool

abstract class Adapter[>>>] extends expand[>>>] {
  override var sc: >>> = _
  override var Master: String = _
  override var AppName: String = _

  /**
   * todo 将程序进行初始化的方法
   *
   * @param args 参数列表
   * @param DB   数据源
   * @return 返回参数列表
   */
  override def Listparm(args: Array[String], DB: String): String = ???

  /**
   * todo 将sc获取到
   *
   * @param confList 配置参数列表
   * @return Spark程序入口
   */
  override def getSparkENT(confList: List[(String, String)]): >>> = ???

  /**
   * todo 关闭程序连接
   */
  override def close(): Unit = ???
}
