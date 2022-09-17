package core_zhao


import org.apache.spark.sql.Dataset

import scala.collection.mutable

trait project {

  /**
   * todo 字段匹配器
   */
  val CON: mutable.Map[String, String]

  /**
   * todo 这里是命令解析模块 会根据命令参数 使用不同的处理方案
   *
   * @param args    命令列表
   * @param dataset 数据集合 每一个元素是一个RDD
   * @param Con     一个字典匹配器 专用于从数组中获取指定 列的数据 由默认值实现 字段与编号的关系
   * @tparam >>> 每个单元数据的类型
   */
  def T_[>>>](args: Array[String], dataset: Dataset[Array[>>>]])(implicit Con: Map[String, String] = CON.toMap): Unit

  /**
   *
   * @param SQL SQL命令组
   */
  def D_[>>>](dataset: Dataset[>>>], SQL: Array[String]): Unit

}
