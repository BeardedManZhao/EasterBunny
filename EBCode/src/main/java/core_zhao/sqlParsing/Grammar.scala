package core_zhao.sqlParsing

import org.apache.spark.sql.Dataset

/**
 * 语法超类
 */
trait Grammar {
  /**
   *
   * @param args 命令参数
   * @return 是否可以执行改类的逻辑
   */
  def judge(args: Array[String]): Boolean

  /**
   * 类执行逻辑的实现
   *
   * @param args    命令
   * @param dataset 数据集，保存等模块可能会使用到
   * @return 执行是否成功
   */
  def run[>>>](args: Array[String], dataset: Dataset[>>>]): Boolean

  /**
   * 类资源的释放
   *
   * @return 返回释放是否成功
   */
  def close(): Boolean

  /**
   * 帮助信息
   *
   * @return 本类的帮助信息
   */
  def HelpStr(): String
}
