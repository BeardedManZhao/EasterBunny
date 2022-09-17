package core_zhao.sqlParsing

import config.WEB_ConfigBase
import org.apache.spark.sql.{AnalysisException, Dataset}
import run_zhao.MAIN_zhao
import run_zhao.MAIN_zhao.{sc_sql, view_manager}

/**
 * Scala类于 2022/8/30 19:11:40 创建
 *
 * @author 4
 */
class SelectGrammar extends Grammar {

  /**
   *
   * @param args 命令参数
   * @return 是否可以执行改类的逻辑
   */
  override def judge(args: Array[String]): Boolean = args.head.equalsIgnoreCase("select") && args.length > 1

  /**
   * 类执行逻辑的实现
   *
   * @return 执行是否成功
   */
  override def run[>>>](SQL: Array[String], dataset: Dataset[>>>]): Boolean = {
    new Thread(() => {
      try {
        val sql_lan = SQL.mkString(" ")
        view_manager.BackData = sc_sql.sql(sql_lan)
        view_manager.BackData.show(numRows = 124, truncate = false)
        config.WEB_ConfigBase.SQL_UI_History.push((
          System.getProperty("user.name"),
          WEB_ConfigBase.`Date -> yMd Hms`.format(System.currentTimeMillis()),
          sql_lan
        ))
      } catch {
        case e: AnalysisException => println("* >>> 异常：可能是语法错误 -> " + e)
        case r: RuntimeException => println("* >>> 异常：可能是参数有误 -> " + r)
        case x: Exception => println("* >>> 异常：" + x)
      }
    }, "SQL处理器").start()
    Thread.sleep(1000)
    true
  }

  /**
   * 类资源的释放
   *
   * @return 返回释放是否成功
   */
  override def close(): Boolean = true

  override def HelpStr(): String = MAIN_zhao.help(Array("select"))
}
