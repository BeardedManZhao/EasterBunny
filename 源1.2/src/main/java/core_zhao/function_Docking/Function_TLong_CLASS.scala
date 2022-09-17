package core_zhao.function_Docking

/**
 * todo 该特质可实现以下种类的函数
 * 函数所需形参为 2 个
 * Long 是 输出数据的类型
 */
trait Function_TLong_CLASS {
  val Function_Name: String
  val Function_Schema: String

  def Function_algorithm: (Any, Any) => Long
}
