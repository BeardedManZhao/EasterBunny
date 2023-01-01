package core_zhao.sqlParsing

import org.apache.spark.sql.Dataset
import run_zhao.MAIN_zhao
import run_zhao.MAIN_zhao.{function_manager, sc_sql, st_sql}

/**
 * Scala类于 2022/8/30 19:37:14 创建
 *
 * @author 4
 */
class ShowGrammar extends Grammar {
  /**
   *
   * @param args 命令参数
   * @return 是否可以执行改类的逻辑
   */
  override def judge(args: Array[String]): Boolean = args.head equalsIgnoreCase "show"

  /**
   * 类执行逻辑的实现
   *
   * @param args    命令
   * @param dataset 数据集，保存等模块可能会使用到
   * @return 执行是否成功
   */
  override def run[>>>](args: Array[String], dataset: Dataset[>>>]): Boolean = {
    args(1) match {
      case "view" => println(
        if (MAIN_zhao.view_manager.CON.nonEmpty) {
          MAIN_zhao.view_manager.CON
            .map(x => s"* 视图名：${x._1}\t|\t数据源：${x._2}")
            .mkString(s"\n+${"=" * 80}+\n|\t", s"\n+${"-" * 80}+\n|\t", s"\n+${"=" * 80}+\n")
        } else {
          s"\n+${"=" * 80}+\n|\t" + "|\t您还没有添加视图到列表哦 请使用create进行视图的添加。\t|" + s"\n+${"=" * 80}+\n"
        })
        true
      case "functions" => println(function_manager.FunctionsList)
        true
      case "conf" => println(st_sql.getMapList(sc_sql.conf.getAll, 80))
        true
    }

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
  override def HelpStr(): String = MAIN_zhao.help(Array("show"))
}
