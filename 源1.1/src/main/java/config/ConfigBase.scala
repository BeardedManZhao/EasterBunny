package config

import run_zhao.MAIN_zhao
import run_zhao.MAIN_zhao.sc_sql

object ConfigBase {
  /**
   * IP库位置 是IP解析模块的主要组件
   */
  lazy val ipBase_dir: String = MAIN_zhao.sc_sql.conf.get("ipBase.dir")
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
   * 加载线程 会提示用户正在加载的信息
   *
   * @param str 提示信息
   * @return 一个线程 是否开启由您决定
   */
  def load(str: String): Thread = {
    val ld = new Thread(() => {
      val string = str + ("." * 10)
      for (s <- string) {
        print(s)
        Thread.sleep(150)
      }
    }
    )
    ld.setName(str)
    ld
  }
}
