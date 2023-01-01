package utils_zhao.sparkTool

import org.apache.log4j.BasicConfigurator
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * @author Liming - 赵凌宇
 * @version 2.0
 *          创建自：2022-04-07
 */
class Spark_Tool_Core() extends Adapter[SparkContext] {

  /// TODO: -------------------参数列表------------------------

  /**
   *
   * @return AppName 的 String 形式
   */
  def getAppName: String = AppName

  /**
   * todo 将所有参数初始化 并将参数打印
   *
   * @param args main参数项
   * @param DB   数据源
   * @return 参数列表
   */
  override def Listparm(args: Array[String], DB: String): String = {
    BasicConfigurator.configure()
    val argsK: ArrayBuffer[(String, String)] = ArrayBuffer("Master" -> args(0), "AppName" -> args(1))
    Master = argsK(0)._2
    AppName = argsK(1)._2
    sc = {
      DB match {
        //        case "HBASE" =>
        //          val context = new SparkContext(new SparkConf()
        //            .setMaster(Master).setAppName(AppName)
        //            .set("spark.testing.memory", "2147480000")
        //            // TODO: 设置使用Kryo 序列化方式
        //            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        //            // TODO: 注册序列化的数据类型
        //            .registerKryoClasses(Array(classOf[ImmutableBytesWritable], classOf[Result])))
        //          argsK.append("DataSrc" -> "HBASE数据库")
        //          context
        case _ => getSparkENT(List[(String, String)](("-----", Master), ("-----", AppName)))
      }
    }
    "\tSparkCore模块" + argsK.map(x => s"* 配置名：${x._1}\t 参数值：${x._2}").mkString(
      s"\n+${"=" * 70}+\n|\t", s"\n+${"-" * 70}+\n|\t", s"\n+${"=" * 70}+\n"
    )
  }


  /**
   * todo 获取 SparkContext 对象
   *
   * @param confList 配置参描述集合列表，K是配置项  V是参数值 todo 代修复
   * @return SparkContext 对象
   */
  override def getSparkENT(confList: List[(String, String)]): SparkContext = {
    val conf = new SparkConf()
    conf.setMaster(confList.head._2).setAppName(confList.last._2).set("spark.testing.memory", "471859200")
    SparkContext.getOrCreate(conf)
  }

  /**
   * todo 关闭Spark连接对象。
   */
  override def close(): Unit = {
    sc match {
      case x if x != null => try {
        sc.stop()
        println(s"\n+${"=" * 60}+\n|\t\t" + s" >>> SparkCore 已关闭 <<<" + s"\n+${"=" * 60}+\n")
      } catch {
        case e: Exception => println(s"\n+${"=" * 70}+\n|" + s"* 未正常关闭sparkCore 原因 = $e" + s"\n+${"=" * 70}+\n")
      }
      case _ => println(s"\n+${"=" * 70}+\n|\t\t" + s" >>> SparkCore 未启动，因此无须关闭 <<<" + s"\n+${"=" * 70}+\n")
    }
  }
}

object Spark_Tool_Core {

  /**
   * todo 迭代算子
   *
   * @param RDD 被解析的结果集
   * @param wit 解析出来的表格的宽度
   */
  def prRDD[>>>, >>](RDD: RDD[(>>>, >>)], wit: Int): Unit = {
    if (RDD.count() > 0) {
      println(s"\n >>> ok!!!")
      RDD.foreachPartition(i_it => {
        i_it.foreach(
          i => {
            print(s"|\t列 1 = ${i._1}\t|\t列 2 = ${i._2}\n" + s"+${"-" * wit}+\n")
          }
        )
      })
    } else {
      print(s"\n+${"=" * wit}+\n" + "|\t 没有查找到您需要的数据。" + s"+${"=" * wit}+\n")
    }
  }

  /**
   * todo 计算 WordCount
   *
   * @param sc   SparkContext 对象
   * @param PATH 被处理文件位置
   * @param REX  匹配单词之间分隔符的正则
   * @param fs   处理文件的场景  hdfs:(Smallfile|无参数)  file:(local|http):(Smallfile|null)
   * @return RDD结果集合
   */
  def run_WordCount(sc: SparkContext, PATH: String, REX: String, fs: String): RDD[(String, Int)] = {
    fs.split(":") match {
      case fs if (fs(0) == "file" || fs(0) == "hdfs") && (fs(1) == "Smallfile" || fs(2) == "Smallfile") =>
        sc.wholeTextFiles(PATH)
          .filter(KV => KV != null)
          .flatMap(x => x._2.split("\\s+")
            .map((_, 1)))
          .reduceByKey(_ + _)
      case fs if fs(0) == "hdfs" =>
        sc.textFile(PATH)
          .filter(x => x != null)
          .flatMap(_.split("\\s+"))
          .mapPartitions(x_it => x_it.map(value => (value.trim, 1)))
          .reduceByKey(_ + _)
      case fs if fs(0) == "file" =>
        sc.parallelize(readSeq_parallelize(PATH, REX, fs(1)))
          .flatMap(x => x.map(word => (word.trim, 1)))
          .reduceByKey((TEMP, X) => TEMP + X)
    }
  }

  /**
   * todo 将我们的数据变为单词，读取到Seq中，便于我们将本地资源加载到，RDD
   *
   * @param inpath 数据来源路径
   * @param REX    单词之间切分正则
   * @param MODE   读取数据模式 默认local
   * @return 以单词数组作为元素的Seq集合
   */
  def readSeq_parallelize(implicit inpath: String, REX: String, MODE: String = "local"): Seq[Array[String]] = {
    val src: Source = {
      MODE match {
        case "local" => Source.fromFile(inpath)
        case "http" => Source.fromURL(inpath)
        case _ => Source.fromBytes("您的读取模式不明确".getBytes)
      }
    }
    var words: Seq[Array[String]] = Nil
    try {
      words = Seq(src.mkString.split(REX))
    } finally {
      src.close()
    }
    words
  }

  /**
   * todo 方法会根据传入的分区数进行判断
   * 不同的分区数安排，将会分配不同的方法
   * 其中本质上调用的都是RDD类中重分区函数
   * 对于增加分区数，会导致shuffle，慎用
   *
   * @param RDD      需要被更改分区数的RDD集合
   * @param startNum 当前集合的分区数
   * @param endNum   希望将分区数更改为多少
   * @tparam T RDD集合类型
   */
  def partitionScheme[T](RDD: RDD[T], startNum: Int, endNum: Int): RDD[T] = {
    (startNum, endNum) match {
      case (startNum, endNum) if startNum > endNum => RDD.coalesce(endNum)
      case (startNum, endNum) if endNum > startNum => RDD.repartition(endNum)
    }
  }

  /**
   * todo 获取 最大值 或 最小值
   *
   * @param RDD 需要被提取的RDD集合
   * @param Max 是否取每个分区的最大值  true = 最大值
   * @tparam >>> RDD内的元素类型
   * @return Array(分区1的最大值，分区2的最大值，分区3的最大值......)
   */
  def get_MAXorMIN[>>> <: Any](RDD: RDD[>>>], Max: Boolean): `>>>` = {
    RDD.map(_.toString.toInt).aggregate(RDD.take(1).head.toString.toInt)(
      // 分区内部数据的整理  将局部的最值提取出来 存入函数初始值
      (TEMP, VALUE) => {
        if ((TEMP > VALUE) == Max) {
          TEMP
        } else {
          VALUE
        }
      },
      // 分区计算的局部最值，互相比较，获取全局最值
      (P1, P2) => {
        val m = {
          if (Max) {
            Array(P1, P2).max
          } else {
            Array(P1, P2).min
          }
        }
        println(s"* >>> 值$P1 与 值$P2 开始比较 => 最值$m")
        m
      }
    ).asInstanceOf[>>>]
  }

  /**
   * todo 求出一个集合中的平均值
   *
   * @param RDD 需要求平均值的集合
   * @tparam >>> RDD内的元素类型
   * @return 最终平均值结果
   */
  def average_value[>>> <: AnyVal](RDD: RDD[>>>]): Double = {
    val value = RDD.map(x => x.toString.toInt)
    val sum = value.aggregate(zeroValue = 0)(
      (TEMP, VALUE) => {
        TEMP + VALUE
      },
      (p1, p2) => {
        p1 + p2
      }
    ).toDouble
    val c = RDD.count().toDouble
    println("* >*> RDD元素和 => " + sum)
    println("* >*> RDD元素数 => " + c)
    sum / c
  }

  /**
   * todo 高阶聚合 WordCount
   *
   * @param RDD 需要统计的RDD
   */
  def aggWordCount(RDD: RDD[(String, Int)]): Unit = {
    RDD.aggregateByKey(0)(
      (TEMP, V) => TEMP + V
      ,
      (P1, P2) => P1 + P2
    ).foreach(println)
  }
}