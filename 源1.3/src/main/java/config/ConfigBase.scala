package config

import run_zhao.MAIN_zhao.{sc_sql, st_sql}

import scala.collection.mutable

object ConfigBase {

  /**
   * 所有配置集合
   */
  lazy val EasterBunny_Config: mutable.HashMap[String, String] = new mutable.HashMap[String, String]() ++ sc_sql.conf.getAll

  /**
   * IP库位置 是IP解析模块的主要组件
   */
  lazy val ipBase_dir: String = EasterBunny_Config.getOrElse("ipBase.dir", "不可变 >> conf/ip2region.db << IP库文件。")
  lazy val configFile: String =
    """
      |#
      |#             ____________________________________________________
      |#            /       感谢您的使用 这里是配置文件，您应该有以下的配置项      \
      |#           |    _____________________________________________     |
      |#           |   |复活节兔子 > 这里已生成默认的配置文件               |    |
      |#           |   |spark.sql.shuffle.partitions = 4  测试则需要   |    |
      |#           |   |spark.testing.memory = 471859200  测试则需要   |    |
      |#           |   |app.is.hive=true  对接hive 则需要              |    |
      |#           |   |app.hive.metastore.uris=thrift://IP:9083 同上 |    |
      |#           |   |mysql.jdbc.driver=???         对接MySQL 则需要 |    |
      |#           |   |writer.partitions = 2         写数据建议设置    |    |
      |#           |   |                                             |    |
      |#           |   |                                             |    |
      |#           |   |                                             |    |
      |#           |   |                                             |    |
      |#           |   |                                             |    |
      |#           |   |                                             |    |
      |#           |   |_____________________________________________|    |
      |#           |                                                      |
      |#            \_____________________________________________________/
      |#                   \_______________________________________/
      |#                _______________________________________________
      |#             _-'    .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.  --- `-_
      |#          _-'.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.--.  .-.-.`-_
      |#       _-'.-.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-`__`. .-.-.-.`-_
      |#    _-'.-.-.-.-. .-----.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-----. .-.-.-.-.`-_
      |# _-'.-.-.-.-.-. .---.-. .-----------------------------. .-.---. .---.-.-.-.`-_
      |#:-----------------------------------------------------------------------------:
      |# spark的分区数
      |spark.sql.shuffle.partitions = 4
      |# spark的测试内存分配
      |spark.testing.memory = 471859200
      |## Hive MetaStore
      |app.is.hive=true
      |app.hive.metastore.uris=thrift://192.168.0.140:9083
      |# mysql 驱动
      |mysql.jdbc.driver=com.mysql.cj.jdbc.Driver
      |# 写数据分区
      |writer.partitions = 2
      |# 优化算法
      |spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version = 2
      |# web设置
      |webui.port = 17887
      |webui.charset = gbk
      |# web服务器是否需要打印异常信息
      |webui.Exception.print = true
      |# web服务器是否需要自动维护线程 如果是请设置自动清理毫秒时间，反之可以不设置或设置为小于等于0，这样不会启动系统维护线程
      |webui.TimingClear = 102400
      |# WEB用户访问历史清理阈值 访问历史条数超出该值 将会被定时清理 最少可保留最新的 阈值 / 2 条访问记录
      |webui.TimingClear.user.thresholdValue = 24
      |# web系统-线程镜像服务 每一次访问都会将当前系统线程状态拍个照 绘制到主页中
      |webui.Thread.image.service = true
      |""".stripMargin

  lazy val MySQL_Driver: String = sc_sql.conf.get("mysql.jdbc.driver")
  lazy val Writer_Partitions: String = {
    try {
      sc_sql.conf.get("writer.partitions")
    } catch {
      case _: Exception => "10"
    }
  }

  /**
   * 是否集成hive
   *
   * @todo 注意 该设置项，不可直接读取来自 SparkSession 的源，应采取直接获取配置集合中的数据
   */
  lazy val ISHive: Boolean = {
    st_sql
      .appConfig
      .properties
      .getProperty("app.is.hive", "true").toBoolean
  }

  /**
   * 加载线程 会提示用户正在加载的信息
   *
   * @param str 提示信息
   * @return 一个线程 是否开启由您决定
   */
  def load(str: String): Thread = {
    new Thread(() => {
      Thread.sleep(240)
      var Time = 0
      for (s <- str + ("." * 7) + "\n") {
        print(s)
        Time += 5
        Thread.sleep(Time)
      }
    }, str)
  }
}
