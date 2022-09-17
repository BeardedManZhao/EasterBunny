package utils_zhao

import org.apache.commons.lang.time.FastDateFormat

import java.util.Date

object DateTool {

  /**
   *
   * @param format_str 获取日期格式
   * @return 被获取到的今天的日期
   */
  def getToDayTime(implicit format_str: String = "yyyy-MM-dd"): String = {
    FastDateFormat.getInstance(format_str).format(new Date())
  }

  /**
   *
   * @param format_str 获取日期格式
   * @return 被获取到的 昨天的日期
   */
  def getYestDayTime(implicit format_str: String = "yyyy-MM-dd"): String = {
    FastDateFormat.getInstance(format_str).format {
      val date = new Date()
      date.setTime(date.getTime - 86400000)
      date
    }
  }
}
