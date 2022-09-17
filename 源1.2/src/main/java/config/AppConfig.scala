package config

/**
 * 注意 这里是一些必须的配置参数 若不设置将会以下参数运行
 * fs.default.name = hdfs://liming04:8020
 * mysql.Driver = com.mysql.cj.jdbc.Driver
 */

import run_zhao.Z_test

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.Properties

/**
 * @author LIMING
 *         @ 创建时间 2022-04-19
 * @param confPath 配置文件目录
 */
class AppConfig(confPath: String) extends Logging {

  val properties: Properties = new Properties()

  /**
   *
   * @param Spark_Bulider Spark的建造者模式对象
   * @return 一个根据配置文件集合配置过的 Spark建造者对象
   */
  def ConfTOSparkSession(Spark_Bulider: Builder): Builder = {
    val keys = properties.propertyNames()
    while (keys.hasMoreElements) {
      val key = keys.nextElement().toString
      Spark_Bulider.config(key, properties.getProperty(key))
    }
    Spark_Bulider
  }

  /**
   * 将配置文件数据加载进 Properties集合
   */
  def ReadTOProperties(): Unit = {
    properties.put(Z_test.confdir_key, confPath)
    val file: File = new File(properties.getProperty("ZhaoLingYu_config.dir"))
    if (!file.exists()) {
      createConfFileOK(file)
    }
    if (file.isFile) {
      properties.load(new FileInputStream(file))
    } else {
      for (file0001 <- file.listFiles()) {
        properties.load(new FileInputStream(file0001))
      }
    }
    properties.put("ipBase.dir", "不可变参数 >> conf/ip2region.db << 用来核对IP库文件。")
  }

  /**
   * 创建配置文件 这里用于不存在配置目录的异常处理
   *
   * @param file 配置文件所在目录
   */
  def createConfFileOK(file: File): Unit = {
    val configFilePath = file.getPath + "/core_zhao.properties"
    var fileOutputStream: FileOutputStream = null
    try {
      file.mkdir()
      new File(configFilePath).createNewFile()
      fileOutputStream = new FileOutputStream(configFilePath)
      fileOutputStream.write(ConfigBase.configFile.getBytes("UTF-8"))
      logWarning("* >>> 警告，您的配置文件目录不存在。已尝试帮您创建默认配置，请立刻前去配置。")
    } catch {
      case _: Exception => logWarning("* >>> 警告：您的配置文件不存在，已尝试帮您创建，但未成功, 请前往目录添加配置文件。")
    } finally {
      fileOutputStream.close()
    }
    println("* >>> 配置文件目录：" + file.getPath)
  }
}
