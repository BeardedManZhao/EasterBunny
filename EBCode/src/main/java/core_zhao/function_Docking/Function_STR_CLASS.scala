package core_zhao.function_Docking

/**
 *
 * todo 该特质可实现以下种类的函数
 * 函数所需形参是一个
 * String 是 输出数据的类型
 */
trait Function_STR_CLASS {
  val Function_Name: String
  val Function_Schema: String

  def Function_algorithm: Any => String
}