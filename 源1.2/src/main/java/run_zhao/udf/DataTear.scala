package run_zhao.udf

import run_zhao.UDFDATASourceFormat

class DataTear extends UDFDATASourceFormat {

  /**
   * 将自己添加进数据组件系统，便于数仓可以检索到本类
   */
  override def Init(): Unit = UDFDATASourceFormat.UDFDATA_SOURCE_FORMAT_HASH_MAP.put(getCommand, this)

  /**
   *
   * @return 自定义数据加载组件的命令标识，用来识别组件的，因此请不要重复
   */
  override def getCommand: String = "DT"

  /**
   * @param sparkSession Spark数据加载工具，通过此可以转换出需要的DataSet
   * @param args         加载数据组件需要的参数
   * @return 数据是否有加载成功
   */
  override def run(sparkSession: SparkSession, args: String*): RDD[Row] = {
    /* 数据操作！ */
    sparkSession.sparkContext.makeRDD(Seq())
  }
}
