package core_zhao

import config.ConfigBase
import core_zhao.function_Docking._
import org.apache.commons.lang.time.FastDateFormat
import org.lionsoul.ip2region.{DbConfig, DbSearcher}
import run_zhao.MAIN_zhao
import run_zhao.MAIN_zhao.sc_sql

import java.io.File
import java.util.Date
import scala.collection.mutable

class Function_Manager extends project {

  /**
   * 函数列表
   */
  lazy val FunctionsList: String = MAIN_zhao.st_sql.getMapList(CON.toMap, 80)
  lazy val searcher = new DbSearcher(new DbConfig(), ConfigBase.ipBase_dir)

  /**
   * 函数库列表 String结果值
   *
   * 其中的元素是函数特质 每一个元素代表一个函数 一个特质中有三个成员 分别是函数的 名字 介绍 和实现
   * 此处的函数返回值都是String
   */
  lazy val FB_STR: Seq[Function_STR_CLASS] = Seq(
    new Function_STR_CLASS {
      override val Function_Name: String = "zhao_String"
      override val Function_Schema: String = "形参(x): 会接收一个任意数值类型的参数 将其转化为String。"

      override def Function_algorithm: Any => String = x => x.toString
    }
  )

  /**
   * 函数库列表 String结果值  这里的函数具有两个形参
   * 其中的元素是函数特质 每一个元素代表一个函数 一个特质中有三个成员 分别是函数的 名字 介绍 和实现
   * 此处的函数返回值都是String
   */
  lazy val FB_TSTR: Seq[Function_TSTR_CLASS] = Seq(
    new Function_TSTR_CLASS {
      override val Function_Name: String = "format_MS"
      override val Function_Schema: String = "形参(str, MS): 分别是日期格式的描述和日期毫秒值，将其转化为指定的日期格式"

      override def Function_algorithm: (Any, Any) => String = (str, ms) => {
        FastDateFormat.getInstance(str.toString).format(ms.toString.toLong)
      }
    },
    new Function_TSTR_CLASS {
      sc_sql.sparkContext.addFile(new File("conf/ip2region.db").getAbsolutePath)
      override val Function_Name: String = "IP"
      override val Function_Schema: String = "形参(ip, Index): 解析一个字符串类型的IP，Index 支持[0=国, 2=省, 3=市, 4=运营商 其它=全部显示]"

      override def Function_algorithm: (Any, Any) => String = (IP, Index_0) => {
        val Index = Index_0.toString.toInt
        if (Index <= 4 && Index >= 0) {
          new DbSearcher(new DbConfig(), SparkFiles.get("ip2region.db")).btreeSearch(IP.toString).getRegion.split("\\|")(Index)
        } else {
          new DbSearcher(new DbConfig(), SparkFiles.get("ip2region.db")).btreeSearch(IP.toString).getRegion.replace("|", "-")
        }
      }
    }
  )

  /**
   * 函数库列表 String结果值
   *
   * 其中的元素是函数特质 每一个元素代表一个函数 一个特质中有三个成员 分别是函数的 名字 介绍 和实现
   * 此处的函数返回值都是 Long
   */
  lazy val FB_Long: Seq[Function_Long_CLASS] = Seq(
    new Function_Long_CLASS {
      override val Function_Name: String = "step"
      override val Function_Schema: String = "形参(x): 求x! 注意 这里的 x 不得 >= 20 更不可违反数学规则。"

      override def Function_algorithm: Any => Long = x => {
        val v = x.toString.toInt
        if (v > 1 && v < 20) Array.range(1, v + 1).product else v
      }
    },
    new Function_Long_CLASS {
      override val Function_Name: String = "now_MS"
      override val Function_Schema: String = "形参(x) : 返回当前时间毫秒值 + x 秒"

      override def Function_algorithm: Any => Long = x => new Date().getTime + (x.toString.toInt * 1000)
    }
  )
  /**
   * todo 函数解释器
   */
  override val CON: mutable.Map[String, String] = mutable.Map()

  /**
   * todo 这里是命令解析模块 会根据命令参数 使用不同的处理方案
   */
  override def T_[>>>](args: Array[String], dataset: Dataset[Array[>>>]])(implicit Con: Map[String, String]): Unit = {
    for (fn_str <- FB_STR) putFunction(fn_str, null, null, null)
    for (fn_long <- FB_Long) putFunction(null, fn_long, null, null)
    for (fn_tstr <- FB_TSTR) putFunction(null, null, fn_tstr, null)
  }

  /**
   * 外部函数库接口
   *
   * @param f_str  返回结果为字符串的函数 对应的接口特质实现
   * @param f_long 返会结果为Long的函数  对应的接口特质实现
   */
  def putFunction(f_str: Function_STR_CLASS, f_long: Function_Long_CLASS
                  , f_tstr: Function_TSTR_CLASS, f_tlong: Function_TLong_CLASS): Unit = {
    if (f_str != null) E_STR(f_str, `type->type` = "Any -> String")
    if (f_long != null) E_Long(f_long, `type->type` = "Any -> Long")
    if (f_tstr != null) E_TSTR(f_tstr, `type->type` = "Any,Any -> String")
    if (f_tlong != null) E_TLong(f_tlong, `type->type` = "Any,Any -> Long")
  }

  /**
   *
   * @param FB_TSTR      函数封装类
   * @param `type->type` 函数类型表述 这里默认是 Any Any -> String
   */
  def E_TSTR(FB_TSTR: Function_TSTR_CLASS, `type->type`: String): Unit = {
    sc_sql.udf.register(FB_TSTR.Function_Name, FB_TSTR.Function_algorithm)
    CON.put(FB_TSTR.Function_Name, s"\t${`type->type`}\t|\t" + FB_TSTR.Function_Schema)
  }

  /**
   *
   * @param FB_TLong     函数封装类
   * @param `type->type` 函数类型表述 这里默认是 Any Any -> Long
   */
  def E_TLong(FB_TLong: Function_TLong_CLASS, `type->type`: String): Unit = {
    sc_sql.udf.register(FB_TLong.Function_Name, FB_TLong.Function_algorithm)
    CON.put(FB_TLong.Function_Name, s"\t${`type->type`}\t|\t" + FB_TLong.Function_Schema)
  }

  /**
   *
   * @param FB_STR       函数封装类
   * @param `type->type` 函数类型表述 这里默认是 Any -> String
   */
  def E_STR(FB_STR: Function_STR_CLASS, `type->type`: String): Unit = {
    sc_sql.udf.register(FB_STR.Function_Name, FB_STR.Function_algorithm)
    CON.put(FB_STR.Function_Name, s"\t${`type->type`}\t|\t" + FB_STR.Function_Schema)
  }

  /**
   *
   * @param FB_Long      函数封装类
   * @param `type->type` 函数类型表述 这里默认是 Any -> Long
   */
  def E_Long(FB_Long: Function_Long_CLASS, `type->type`: String): Unit = {
    sc_sql.udf.register(FB_Long.Function_Name, FB_Long.Function_algorithm)
    CON.put(FB_Long.Function_Name, s"\t${`type->type`}\t|\t" + FB_Long.Function_Schema)
  }

  /**
   *
   * @param SQL SQL命令组
   * @todo 该方法被停用
   */
  @deprecated
  override def D_[>>>](dataset: Dataset[>>>], SQL: Array[String]): Unit = {}
}