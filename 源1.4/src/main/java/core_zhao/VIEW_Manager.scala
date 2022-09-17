package core_zhao

import config.ConfigBase
import core_zhao.sqlParsing.Grammar
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Dataset, Row}
import run_zhao.MAIN_zhao.sc_sql
import run_zhao.{MAIN_zhao, UDFDATASourceFormat}
import utils_zhao.dataSink.DataSaveMySQL
import utils_zhao.dataSrcHbase.DataFromHbase

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @author zhao
 * @param Grammars 命令处理类组，其中包含Grammar的实现类
 */
class VIEW_Manager(Grammars: Array[Grammar]) extends project {
  /**
   * todo 数据路径与数据表 匹配器
   */
  override val CON: mutable.Map[String, String] = mutable.Map()

  var BackData: Dataset[Row] = _

  def getGrammars: Array[Grammar] = this.Grammars

  /**
   * todo 这里是命令解析模块 会根据命令参数 使用不同的处理方案
   *
   * @param args    命令列表
   * @param dataset 数据集合 每一个元素是一个RDD
   * @param Con     一个字典匹配器 专用于从数组中获取指定 列的数据 由默认值实现 字段与编号的关系
   * @tparam >>> 每个单元数据的类型
   */
  override def T_[>>>](args: Array[String], dataset: Dataset[Array[>>>]])(implicit Con: Map[String, String]): Unit = {
    val `>= 2`: Boolean = args.length >= 2
    val `>= 4`: Boolean = if (`>= 2`) args.length >= 4 else `>= 2`
    val C_view: Boolean = args.contains("view")
    var DataSrc_type_str: String = null
    var StructType_Schame: Array[String] = null
    if (args.head == "create") {
      DataSrc_type_str = if (`>= 4`) args(4) else "text"
      StructType_Schame = args.splitAt(5)._2.map(x => x.trim)
    }
    if (C_view && args.head == "create") {
      createView(DataSrc_type_str, StructType_Schame, args)
    } else {
      D_(BackData, args)
    }
  }

  /**
   * todo 数据视图创建模块
   *
   * @param DataSrc_type_str  数据源平台类型
   * @param StructType_Schame 字段描述，会根据这个字段生成新的Schema
   * @param args              目录语句 从中获取InPath
   */
  def createView(DataSrc_type_str: String, StructType_Schame: Array[String], args: Array[String]): Unit = {
    DataSrc_type_str match {
      case "text" => D_(
        sc_sql.createDataFrame(sc_sql.sparkContext.textFile(args(2))
          .flatMap(_.split("\n"))
          .map(line => {
            Row.fromSeq(line.trim.split("\\s+").toSeq.map(_.trim))
          }), getSchame(StructType_Schame)), args)
      case "json" => D_(
        if (StructType_Schame.length == 0) {
          sc_sql.read.json(args(2))
        } else {
          sc_sql.read.schema(getSchame(StructType_Schame)).json(args(2))
        }, args)
      case "csv" => if (StructType_Schame.length == 0) {
        D_(sc_sql.read.option("Seq", ",").option("header", "true").option("inferSchema", "true").csv(args(2)), args)
      } else {
        D_(sc_sql.read.schema(getSchame(StructType_Schame)).csv(args(2)), args)
      }
      case "MySQL" =>
        val `url >>> user >>> password` = args(2).split(">>>")
        D_(
          sc_sql.read.format("jdbc")
            .option("driver", ConfigBase.MySQL_Driver)
            .option("url", s"jdbc:mysql://${`url >>> user >>> password`.head}/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true")
            .option("user", `url >>> user >>> password`(1))
            .option("password", `url >>> user >>> password`(2))
            .option("dbtable", `url >>> user >>> password`(3))
            .load(), args)
      case "HBASE" => D_(
        sc_sql.createDataFrame(DataFromHbase.getDataRDD_FromHBASE(args(2)).map(Row.fromSeq(_))
          , getSchame(StructType_Schame)), args)
      case "parquet" => D_(sc_sql.read.parquet(args(2)), args)
      case "Hive" => D_(sc_sql.read.table(args(2)), args)
      case x => val strings: Array[String] = x.split(':')
        if (strings.length > 1) {
          // 提取出来操作格式与参数行 被提取格式应如：UDF:DT=cat....
          val command = strings(1).split("=") // Array(DT, cat...)
          val format = UDFDATASourceFormat.UDFDATA_SOURCE_FORMAT_HASH_MAP.get(command.head) // 使用DT
          if (format != null) {
            val isC = (command.last + (if (strings.length > 2) ":" + strings(2) else "")).split(">+")
            println("* >>> 解析：" + isC.mkString(" "))
            D_(sc_sql.createDataFrame(format.run(sc_sql, isC), getSchame(StructType_Schame)), args) // 使用参数行
          } else {
            logError("很抱歉，没有找到标识为【" + strings(1) + "】的UDF插件。")
          }
        } else {
          logError("您好，您设置的【Type】参数不正确，这里没有找到对应的模块或插件。")
        }
    }
  }

  /**
   * todo 将对应的字段对象集 加载好  对象集 StructType  里面每一个元素都是一个字段描述
   *
   * @param `col:type` 字段1 : 数据类型 ，  字段2 : 数据类型 ......
   * @return 对应的Schema 字段集合对象 StructType 这里会被传入创建视图的字段描述中
   */
  def getSchame(`col:type`: Array[String]): StructType = {
    val StructField_List: ListBuffer[StructField] = ListBuffer()
    for (ct <- `col:type`) {
      val ct0001 = ct.split(":")
      `:: StructField`(StructField_List, ct0001)
    }
    StructType(StructField_List)
  }

  //                            _ooOoo_
  //                           o8888888o    todo 这里是计算模块
  //                           88" . "88
  //                           (| -_- |)
  //                            O\ = /O
  //                        ____/`---'\____
  //                      .   ' \\| |// `.
  //                       / \\||| : |||// \
  //                     / _||||| -:- |||||- \
  //                       | | \\\ - /// | |
  //                     | \_| ''\---/'' | |
  //                      \ .-\__ `-` ___/-. /
  //                   ___`. .' /--.--\ `. . __

  /**
   * todo 向 StructType 中，添加一个字段的描述 StructField对象 这个模块的产生是为了解析对应的数据类型
   *
   * @param StructField_List 每一个字段描述对象的集合
   * @param ct0001           单个字段的描述信息
   */
  def `:: StructField`(StructField_List: ListBuffer[StructField], ct0001: Array[String]): Unit = {
    ct0001.last
    match {
      case "String" => StructField_List.append(StructField(ct0001.head, StringType))
      case "Double" => StructField_List.append(StructField(ct0001.head, DoubleType))
      case "Long" => StructField_List.append(StructField(ct0001.head, LongType))
      case "Int" => StructField_List.append(StructField(ct0001.head, IntegerType))
      case "Float" => StructField_List.append(StructField(ct0001.head, FloatType))
      case "Short" => StructField_List.append(StructField(ct0001.head, ShortType))
      case "Byte" => StructField_List.append(StructField(ct0001.head, ByteType))
      case "Date" => StructField_List.append(StructField(ct0001.head, DateType))
      case "Boolean" => StructField_List.append(StructField(ct0001.head, BooleanType))
      case _ => StructField_List.append(StructField(ct0001.head, StringType))
    }
  }

  /**
   *
   * @param SQL SQL命令组
   */
  override def D_[>>>](dataset: Dataset[>>>], SQL: Array[String]): Unit = {
    for (g: Grammar <- this.Grammars if g.judge(SQL)) {
      try {
        g.run(SQL, dataset)
      } catch {
        case _: Exception => println(g.HelpStr())
        case _: ArrayIndexOutOfBoundsException => println(g.HelpStr())
      }
      return
    }
    println(MAIN_zhao.help(SQL))
  }

  /**
   *
   * @param DataSnk_type_str 数据保存平台标识
   * @param dataset          数据集
   * @param savePath         保存路径
   * @tparam >>> 数据集类型 自动隐式转换
   */
  def saveMd[>>>](DataSnk_type_str: String, dataset: Dataset[>>>], savePath: String): Unit = {
    if (dataset != null) {
      DataSnk_type_str match {
        case "MySQL" => DataSaveMySQL.save(dataset, savePath)
        case "hdfs" => dataset.rdd.saveAsTextFile(savePath)
      }
    } else {
      println("* >>> 您还没有过查询记录，无法保存。")
    }
  }

  /**
   *
   * @param Line 不建议使用
   * @param args 不建议使用
   * @return 指定类型的数据
   * @todo 其中的干涉过多 现在已经被弃用 已经实现了自动转类型的功能
   */
  @Deprecated
  def getTypeRow(Line: String, args: Array[String]): Row = {
    val datas = Line.trim.split("\\s+").toSeq.map(_.trim)
    val new_seq: ListBuffer[Any] = ListBuffer()
    for (v <- datas; t <- args.map(_.split(":").last)) {
      new_seq.append {
        t match {
          case "Double" => v.toDouble
          case "Long" => v.toLong
          case "Int" => v.toInt
          case "Float" => v.toFloat
          case "Short" => v.toShort
          case "Byte" => v.toByte
          case "Date" => v.toLong
          case "Boolean" => v.toBoolean
          case _ => v
        }
      }
    }
    Row.fromSeq(new_seq)
  }
}