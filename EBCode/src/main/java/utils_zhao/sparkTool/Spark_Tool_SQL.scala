package utils_zhao.sparkTool

import config.{AppConfig, ConfigBase}
import org.apache.spark.sql.SparkSession

import java.io.File
import scala.collection.mutable.ArrayBuffer

/**
 * @author LIMING
 * @param args sql命令
 * @param DB   数据库源 todo 未启用
 */
class Spark_Tool_SQL(args: Array[String], DB: String) extends Adapter[SparkSession] {

  lazy val spark: SparkSession = {
    println(Listparm(args, DB))
    getSparkENT(argsK.toList)
  }
  val argsK: ArrayBuffer[(String, String)] = {
    ArrayBuffer("Master" -> args(0), "AppName" -> (if (args.length >= 2) args(1) else "run_zhao"))
  }

  /**
   * 读取配置文件的类对象 其中包含一个配置数据集合 被我们的配置库使用
   */
  val appConfig: AppConfig = new AppConfig(new File("conf/spark_zhao_config").getAbsolutePath)
  appConfig.ReadTOProperties()

  import spark.implicits._

  val `Text -> View`: (String, String, String) => Unit = (InPath, split_rex, init_view) => {
    spark.read.textFile(InPath).flatMap(_.split(split_rex)).createOrReplaceTempView(init_view)
  }

  /**
   * todo 将程序进行初始化的方法 它会加载好Spark入口
   *
   * @param args 参数列表 【Master】【AppName】
   * @param DB   数据源
   * @return 返回参数列表
   */
  override def Listparm(args: Array[String], DB: String): String = {
    Master = argsK(0)._2
    AppName = argsK(1)._2
    "\tEasterBunny_SparkSQL模块" + argsK.map(x => s"* 配置名：${x._1}\t 参数值：${x._2}").mkString(
      s"\n+${"=" * 70}+\n|\t", s"\n+${"-" * 70}+\n|\t", s"\n+${"=" * 70}+\n"
    )
  }

  /**
   * todo 将sc获取到 会被初始化方法调用
   *
   * @param confList 配置参数列表 第一个是Master 第二个是AppName
   * @return Spark程序入口
   */
  override def getSparkENT(confList: List[(String, String)]): SparkSession = {
    val Builder = SparkSession.builder().master(confList.head._2).appName(confList(1)._2)
    appConfig.ConfTOSparkSession(
      if (ConfigBase.ISHive) Builder.enableHiveSupport() else Builder
    ).getOrCreate()
  }

  /**
   * todo 关闭程序连接
   */
  override def close(): Unit = {
    spark match {
      case x if x != null => try {
        spark.stop()
        println(s"\n+${"=" * 60}+\n|\t\t" + s" >>> Easter_Bunny 已关闭 <<<" + s"\n+${"=" * 60}+\n")
      } catch {
        case e: Exception => println(s"\n+${"=" * 70}+\n|" + s"* 未正常关闭 Easter_Bunny 原因 = $e" + s"\n+${"=" * 70}+\n")
      }
      case _ => println(s"\n+${"=" * 70}+\n|\t\t" + s" >>> Easter_Bunny 未启动，因此无须关闭 <<<" + s"\n+${"=" * 70}+\n")
    }
  }
}