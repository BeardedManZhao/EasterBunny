package utils_zhao.time

import org.apache.commons.lang.time.FastDateFormat

import java.util.Date

object zhaoDate {

  /**
   *
   * @return 当前时间的格式化好的字符串类型
   */
  def now_String(): String = getformatDate("yyyy-MM-dd HH:mm:ss", now_Long())

  /**
   *
   * @return 当前时间毫秒值
   */
  def now_Long(): Long = now_Date().getTime

  /**
   *
   * @return 当前时间的Date对象
   */
  def now_Date(): Date = new Date()

  /**
   *
   * @param format 毫秒转日志的数据格式
   * @param MS     毫秒值
   * @return 毫秒值被格式化之后的数据
   */
  def getformatDate(format: String, MS: Long): String = FastDateFormat.getInstance(format).format(MS)
}
