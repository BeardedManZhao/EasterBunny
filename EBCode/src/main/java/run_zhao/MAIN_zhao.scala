package run_zhao

//TODO  @ AUT-ZH-UTF0-8-AO-TIME password zhaozhao

import config.ConfigBase
import core_zhao.function_Docking.Function_Long_CLASS
import core_zhao.sqlParsing.{CreateGrammar, SaveGrammar, SelectGrammar, ShowGrammar}
import core_zhao.{Function_Manager, VIEW_Manager}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import utils_zhao.netSocket
import utils_zhao.netSocket.zhaoSocket
import utils_zhao.sparkTool.Spark_Tool_SQL
import webMod.okhttp.WebRun

import java.io.BufferedOutputStream
import scala.io.StdIn

/**
 * 最后一次维护 2022-05-11
 *
 * @author Liming
 * @version 1.2 提供WEBUI 优化系统性能
 * @todo 有关SparkCore的模块被暂时停用
 */
object MAIN_zhao extends Logging {
  lazy val sc_sql: SparkSession = st_sql.spark
  var view_manager: VIEW_Manager = _
  var function_manager: Function_Manager = _
  var st_sql: Spark_Tool_SQL = _
  var MODE: String = "sparksql"
  var status: Boolean = true

  /**
   *
   * @param args 启动参数 [Master] [AppName] [MODE]
   */
  def main(args: Array[String]): Unit = {
    var Initok = true
    if (args.head != "help" || args.length >= 3) {
      try {
        if (args.length > 3) {
          Initok = START.loadClassUDF(args)
        }
        if (Initok) {
          ConfigBase.load(s"请稍等，${System.getProperty("user.name")}，EasterBunny 正在初始化中.").start()
          init(args, mode = args(2))
          // 下面是使用我们的框架添加一个函数进去
          function_manager.putFunction(f_str = null, f_long = new Function_Long_CLASS {
            override val Function_Name: String = "zhao_Long"
            override val Function_Schema: String = "形参(x): 接收一个任意数值类型的参数，将其转化为Long"

            override def Function_algorithm: Any => Long = x => x.toString.toLong
          }, f_tstr = null, f_tlong = null)
          new Thread(() => WebRun.main(), "WebUI服务器").start()
          println(
            s"""             >>> Welcome to My program <<<
               |                      :oooo
               |                       YAAAAAAs_
               |               'AA.    ' AAAAAAAAs
               |                !AAAA_   ' AAAAAAAAs
               |                  VAAAAA_.   AAAAAAAAs
               |                   !AAAAAAAA_  AAAAAAAb
               |                     VVAAAAAAA\\/VAAAAAAb
               |                       'VVAAAAAAAXXAAAAAb        Version: ${ConstantRegion.VERSION}
               |                           ~~VAAAAAAAAAABb           -- 复活节兔子
               |                                 ~~~VAAAAB__
               |                                   ,AAAAAAAAA_
               |                                 ,AAAAAAAAA(*)AA_
               |            _nnnnnnnnnnnnnnmmnnAAAAAAAAAAAAA8GAAAAn_
               |        ,AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAo
               |      ,AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAf~""
               |     ,AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA)
               |    iAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP
               |    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
               |   ,AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA]
               |   [AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA]
               |   [AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
               |   [AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!
               |    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA~
               |    YAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA`
               | __.'YAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA.
               |[AAAAA8AAAAAAAAAAAAAAAAAAAAAAAAAA~AAAAA_
               |(AAAAAAAAAAAAAAAAAAAAAAAAAAAAVf`   YAAAA]
               | VAAAAAAAAAAAAAAAAAAAAAAAAAAA_      AAAAAAAs
               |   'VVVVVVVVVVVVVVVVVVVVVVVVVV+      !VVVVVVV
               |""".stripMargin)
          Thread.sleep(240)
          while (status) {
            print("Liming >>> ")
            try {
              ConstantRegion.INVISIBLE_ALL_PATTERN.split(StdIn.readLine()) match {
                case x if (x.head == "select" || x.head == "create" || x.head == "save" || x.head == "show" || x.head == "desc") && MODE == "sparksql" => SQL_T(x)
                case x if x.head == "exit" => status = false
                case a if a.mkString(" ") == "who is Master" => println("* >>> Master is " + ConfigBase.EasterBunny_Config.get("spark.master"))
                case x if MODE != "sparksql" && x.length >= 4 => println("* >>> 不支持 " + MODE + " 模式。默认切换回SQL模块。")
                  init(args, "sparksql")
                  MODE = "sparksql"
                case x => println("* >>> " + help(x))
              }
            } catch {
              case array: ArrayIndexOutOfBoundsException => println("* >>> 请检查您的指令是否正确 => " + array)
              case numte: NumberFormatException => println("* >>> 无法转换您指定的数据 => " + numte)
              case nulle: NullPointerException => println("* >>> 请检查您的配置或环境 => " + nulle)
                nulle.printStackTrace()
              case cnull: NoSuchElementException => println("* >>> 请检查您的配置或环境 => " + cnull)
                cnull.printStackTrace()
              case x: Exception => println("* !>> " + x)
            }
          }
        } else {
          println("* >?> MAIN二层缓存启动失败！准备退出。")
        }
      } catch {
        case c: ClassNotFoundException => println("* >?> 您设置的插件类路径，似乎是错误的，详细原因：" + c)
        case e: Exception => e.printStackTrace()
      } finally {
        close()
      }
    } else {
      println(
        """|版本：1.2
           |作者：复活节兔子-赵凌宇
           |帮助信息：jar 【Master】【AppName】【MODE】【需要使用的数据加载插件】
           |        Master     >>>   将Spark作业提交到的集群
           |                         > local[*]    >>>    设置为本地模式运行，*可以是一个数值，代表运行几个Task线程并发
           |                         > spark://IP:Port    设置为Spark集群提交 这里是Spark主节点的位置
           |                         > yarn               设置为提交到Yarn集群 一般会通过本地配置文件获取yarn的信息
           |                         > ....               其它集群
           |        AppName    >>>   本次提交到集群，显示的名称
           |                         > 默认 :              run_zhao
           |        MODE       >>>   运行模式 代表您想要使用的Spark程序的入口
           |                         > sparksql           SparkSession 作为Spark的程序入口 << 默认
           |                         > Sparkcore          SparkContext 作为Spark的程序入口 << 未集成该模块
           |""".stripMargin)
    }
  }

  /**
   *
   * @param args 查询模块命令参数 select * from View
   *             视图模块命令参数 create view InPath View
   */
  def SQL_T(args: Array[String]): Unit = {
    args match {
      case _ if args.length > 0 => view_manager.T_(args, null)
      case _ => println("* >>> " + help(args))
    }
  }

  /**
   * todo 返回帮助信息
   *
   * @param args 命令列表
   */
  def help(args: Array[String]): String = {
    args(0) match {
      case "show" =>
        """帮助信息：
          |         show view       >>> 查询所有的视图对应的数据目录。
          |         show conf       >>> 查询所有的配置参数列表
          |         show functions  >>> 查询EB函数库，标准函数库中的函数不会显示
          |""".stripMargin
      case "select" =>
        """帮助信息：
          |         select functions  >>> 查询所有附加函数
          |         select【col】 from 【view】 where 【条件】... group by 【gcol】
          |                TODO: 这里显示的数据不会超过前124条数据
          |                col        >>>  字段列表
          |                view       >>>  视图名称
          |                条件        >>>  过滤条件
          |                gcol       >>>  分组字段
          |""".stripMargin
      case "create" =>
        """帮助信息：create view 【InPath】【view】【Type】【col1:type col2:type...】
          |                 InPath >>>  将来自哪个位置的数据需要被加载进视图
          |                             > HDFS                    hdfs://IP/Path
          |                             > Local                   file:///Path
          |                             > MySQL                   IP:Port>>>user>>>password>>>DataBase.Table
          |                             > HBASE                   IP->Table
          |                             > Hive                    DataBase.Table
          |                 view   >>>  视图名称
          |                 Type   >>>  读取方式，不同的类型将会调用不同的读取方式
          |                             > text                    代表读取文本类型数据 必须指定列的字段--目前是将会把一行数据按 \\s+ 正则切分
          |                             > json                    代表读取json类型数据 允许您自定义字段--会自动的解析其中的Schema 字段描述，若有指定，将使用指定的字段名
          |                             > csv                     从csv读取数据       允许您自定义字段--如果您没有键入字段信息 代表将第一行数据作为字段
          |                             > parquet                 从parquet 文件中读取数据  不支持自定义字段--将由Spark自动解析其中的Schema
          |                             > MySQL                   从MySQL 数据库中读取数据  不支持自定义字段--将由数据库中的字段自动生成
          |                             > HBASE                   从HBASE 数据库中读取数据  必须指定列的字段--行键会作为DataFrema第一列，其余字段会作为其它列，列簇等信息将会被屏蔽
          |                                                           * 这种方式需要指定字段 并且需要指定字段的类型为String 原因在于我们的HBASE中存储的是字节数组
          |                                                           * 对于数值的使用 将会自动的调用转换函数 也可手动的使用数值类型转换函数
          |                             > Hive                    代表从Hive中读取数据      不支持指定列字段--根据 Hive中的元数据 自动生成Schema
          |                                                           * 这种方式需要打开spark的thriftserver，和 beeline
          |                                                           * 还需要打开Hive的metastore 由spark的beeline去访问Hive的Metastore
          |                                                           * 当执行语句的时候 由thriftserver负责处理和记录，因此会留下痕迹在Spark集群
          |                             > UDF:XXX                  使用自定义的数据加载组件，XXX代表需要使用的组件所认识的命令，通过这个命令会找到对应的数据加载类
          |                                                           例如：UDF:DT >> 代表使用自定义的DT标识的数据加载类，这个是加载的DataTear类型的命令
          |                                                           示例：UDF:DT=cat>DT>hdfs....... 需要注意 插件命令需要使用 ‘>’ 作为分隔
          |                                                           注意：需要加载的插件一定要在启动的时候，添加全类路径。
          |             col:type   >>>  DataSet字段描述，将会以这个描述为中心 创建出对应的DataFrema 最终将会以该数据框 创建视图
          |                        TODO : 目前text HBASE 模式下，请将字段类型设置为String 程序会自动的去识别数据类型和操作
          |                               如有特别需要 可使用函数 zhao_*** 将数据类型转换
          |                             : 目前HBASE模式下，初次对于同一个视图的首次查询会慢，因为第一次需要连接HBASE
          |                             > name:String             一个name的字段 字段中的数据是 String 类型
          |                             > nums:Int                一个nums的字段 字段中的数据是   Int  类型
          |                             > name:String nums:Int    两列字段 分别是String的name   Int的nums
          |""".stripMargin
      case "save" =>
        """帮助信息：save to 【Sink】【OutPath】
          |              TODO: 该指令，将会把上一次的查询结果做一个保存
          |              Sink       >>>  保存到外部数据源。
          |                              > hdfs                    代表将数据保存到 HDFS  文件系统
          |                              > MySQL                   代表将数据保存到 MySQL 数据库中
          |              OutPath    >>>  对于保存位置的细节描述
          |                              > hdfs                    hdfs://IP/Path
          |                              > MySQL                   IP:Port>>>user>>>password>>>DataBase.Table
          |""".stripMargin
      case _ => "帮助信息：show | select | create | save | who is Master"
    }
  }

  /**
   * todo 初始化参数 参数包括 Master AppName SparkTools Spark入口
   *
   * @param args 启动参数
   * @param mode 启动模式
   */
  def init(args: Array[String], mode: String): Unit = {
    mode match {
      case "sparksql" =>
        st_sql = new Spark_Tool_SQL(args, "HDFS")
        logInfo("SparkSession 已就绪，将会在被使用之后初始化。")
    }
    MODE = mode
    view_manager = new VIEW_Manager(Array(new CreateGrammar(), new SelectGrammar(), new SaveGrammar(), new ShowGrammar()))
    logInfo("视图管理器 已初始化完成，准备就绪。")
    function_manager = new Function_Manager()
    function_manager.T_(null, null)
  }

  /**
   * @todo 这里，将webui 关闭。 会将所有缓存的视图或RDD清理
   */
  def close(): Unit = {
    val accum = sc_sql.sparkContext.longAccumulator("被清理RDD缓存数量")
    val close_T = new BufferedOutputStream(zhaoSocket.getSocket("localhost", config.WEB_ConfigBase.webUI_port).getOutputStream)
    netSocket.zhaoSocket.sandData("zhao", close_T)
    Thread.sleep(1500)
    try {
      sc_sql.sparkContext.getPersistentRDDs.foreach(x => {
        x._2.unpersist()
        accum.add(1)
      })
      println("* >>> " + accum.value + " 个RDD缓存成功被释放。")
      sc_sql.close()
    } catch {
      case e: Exception => println(e)
    }
    try {
      close_T.close()
    } catch {
      case e: Exception => println(e)
    }
    try {
      st_sql.close()
    } catch {
      case e: Exception => println(e)
    }
    System.exit(0)
  }

}
