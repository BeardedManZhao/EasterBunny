package utils_zhao.dataSrcHbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import run_zhao.MAIN_zhao

import scala.collection.mutable.ArrayBuffer

object DataFromHbase {
  /**
   * todo 将数据转换成RDD集合
   *
   * @param inpath       数据来源路径
   * @param Column_Split 列切割正则
   * @param row_Split    行切割正则
   * @tparam >>> 需要的RDD内的类型
   * @return 转换好的RDD
   */
  @Deprecated
  def getDataRDD_FromHDFS[>>>](inpath: String, Column_Split: String, row_Split: String): RDD[>>>] = {
    MAIN_zhao.sc_sql.sparkContext.textFile(inpath)
      .flatMap(x => x.split(row_Split)) // 按行切分
      .map(x => x.split(Column_Split)) // 按列切分
      .asInstanceOf[RDD[>>>]]
  }

  /**
   * todo 将HBASE中的一张表数据转换为RDD
   *
   * @param inpath HBASE数据表的名称  IP->Table
   * @return RDD集合    Array( 元素1， 元素2， 元素3 ) 每一个元素都是一个单元格数据 同一数组代表同一行
   */
  def getDataRDD_FromHBASE(inpath: String): RDD[Array[String]] = {
    val HBASEConf: Configuration = new Configuration()
    val `IP->Table` = inpath.split("->")
    HBASEConf.set("hbase.zookeeper.quorum", `IP->Table`(0))
    HBASEConf.set("zookeeper.znode.parent", "/hbase")
    // 设置HBASE表名称
    HBASEConf.set(TableInputFormat.INPUT_TABLE, `IP->Table`(1))
    MAIN_zhao.sc_sql.sparkContext.newAPIHadoopRDD(
      HBASEConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    ).map(
      x => {
        val row: ArrayBuffer[String] = ArrayBuffer(Bytes.toString(x._1.get(), x._1.getOffset, x._1.getLength)) // 这里用来盛放一行的数据 TODO 第一个元素是行键
        // todo 下面将一行数据的所有单元格的值 直接添加到行数据容器中
        x._2.rawCells().foreach(cell => row.append(Bytes.toString(cell.getValueArray, cell.getValueOffset, cell.getValueLength)))
        row.toArray // 最后返回行数据组 里面存储的就是我们需要的 Array[String] 一个元素
      }
    )
  }
}
