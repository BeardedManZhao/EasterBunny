package core_zhao.sqlParsing

import org.apache.spark.sql.Dataset
import run_zhao.MAIN_zhao
import run_zhao.MAIN_zhao.sc_sql

/**
 * Scala类于 2022/8/30 19:40:05 创建
 *
 * @author 4
 */
class CreateGrammar extends Grammar {
  /**
   *
   * @param args 命令参数
   * @return 是否可以执行改类的逻辑
   */
  override def judge(args: Array[String]): Boolean = args.head.equalsIgnoreCase("create")

  /**
   * 类执行逻辑的实现
   *
   * @param args    命令
   * @param dataset 数据集，保存等模块可能会使用到
   * @return 执行是否成功
   */
  override def run[>>>](args: Array[String], dataset: Dataset[>>>]): Boolean = {
    val viewName = args(3)
    dataset.createOrReplaceTempView(viewName)
    dataset.printSchema()
    MAIN_zhao.view_manager.CON += (viewName -> args(2))
    sc_sql.catalog.cacheTable(viewName)
    true
  }

  /**
   * 类资源的释放
   *
   * @return 返回释放是否成功
   */
  override def close(): Boolean = true

  /**
   * 帮助信息
   *
   * @return 本类的帮助信息
   */
  override def HelpStr(): String = MAIN_zhao.help(Array("create"))
}
