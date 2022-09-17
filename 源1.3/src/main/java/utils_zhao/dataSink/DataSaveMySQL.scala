package utils_zhao.dataSink

import config.ConfigBase
import org.apache.spark.sql.Dataset

object DataSaveMySQL {

  /**
   *
   * @param dataset                                      需要保存的数据集
   * @param `IP:Port>>>user>>>password>>>DataBase.Table` 固定格式的数据库信息
   * @tparam >>> Dataset的类型 这里是隐式转换，不需要手动干预
   */
  def save[>>>](dataset: Dataset[>>>], `IP:Port>>>user>>>password>>>DataBase.Table`: String): Unit = {
    val ipupd: Array[String] = `IP:Port>>>user>>>password>>>DataBase.Table`.split(">>>")
    dataset.write.format("jdbc")
      .option("driver", ConfigBase.MySQL_Driver)
      .option("numPartitions", ConfigBase.Writer_Partitions) // 设置并发
      .option("url", s"jdbc:mysql://${ipupd.head}/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true")
      .option("user", ipupd(1))
      .option("password", ipupd(2))
      .option("dbtable", ipupd(3))
      .mode("overwrite")
      .save()
  }
}
