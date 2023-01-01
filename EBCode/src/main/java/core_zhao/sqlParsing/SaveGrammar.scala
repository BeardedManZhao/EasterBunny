package core_zhao.sqlParsing

import org.apache.spark.sql.Dataset
import run_zhao.MAIN_zhao
import utils_zhao.dataSink.DataSaveMySQL

/**
 * Scala类于 2022/8/30 19:18:52 创建
 *
 * @author 4
 */
class SaveGrammar extends Grammar {
  /**
   *
   * @param args 命令参数
   * @return 是否可以执行改类的逻辑
   */
  override def judge(args: Array[String]): Boolean = args.head.equalsIgnoreCase("save") && args.length > 1

  /**
   * 类执行逻辑的实现
   *
   * @param args 命令
   * @return 执行是否成功
   */
  override def run[>>>](args: Array[String], dataset: Dataset[>>>]): Boolean = {
    try {
      saveMd(args(2), dataset, args(3))
    } catch {
      case e: Exception => println("* >>> 发生了异常：" + e + HelpStr())
        false
      case n: NullPointerException => println("* >>> 发生了异常：" + n + HelpStr())
        false
    }
  }

  /**
   *
   * @param DataSnk_type_str 数据保存平台标识
   * @param dataset          数据集
   * @param savePath         保存路径
   * @tparam >>> 数据集类型 自动隐式转换
   */
  def saveMd[>>>](DataSnk_type_str: String, dataset: Dataset[>>>], savePath: String): Boolean = {
    if (dataset != null) {
      DataSnk_type_str match {
        case "MySQL" => DataSaveMySQL.save(dataset, savePath)
        case "hdfs" => dataset.rdd.saveAsTextFile(savePath)
        case _ => println("* >>> 不支持的数据输出：" + DataSnk_type_str + "\n" + HelpStr())
      }
      true
    } else {
      println("* >>> 您还没有过查询记录，无法保存。")
      false
    }
  }

  override def HelpStr(): String = MAIN_zhao.help(Array("save"))

  /**
   * 类资源的释放
   *
   * @return 返回释放是否成功
   */
  override def close(): Boolean = true
}
