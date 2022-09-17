package config

import config.ConfigBase.EasterBunny_Config
import org.apache.commons.lang.time.FastDateFormat
import utils_zhao.netSocket.zhaoSocket
import webMod.okhttp.GETResponse
import webMod.web.{First_Home, History_SQL, SQL_D}

import java.net.ServerSocket
import scala.collection.mutable

object WEB_ConfigBase {

  /**
   * WEB 访问端口
   */
  lazy val webUI_port: Int = EasterBunny_Config.getOrElse("webui.port", "17887").toInt

  /**
   * WEB 模块全局编码集
   */
  lazy val webUI_CharSet: String = EasterBunny_Config.getOrElse("webui.charset", "gbk")

  /**
   * WEB 服务器对象
   */
  lazy val webUI_ServerSocket: ServerSocket = zhaoSocket.getServerSocket(webUI_port)

  /**
   * SQL执行列表
   */
  lazy val SQL_UI_History: mutable.ArrayStack[(String, String, String)] = mutable.ArrayStack()

  /**
   * 模块虚拟路径对照表
   */
  lazy val WEBUI_KVpath: Map[String, String] = Map(
    "/" -> "First_Home",
    "/SQLD" -> "SQL_D",
    "/History_SQL" -> "History_SQL"
  )

  /**
   * 在WEBUI 中 sql 语句属性对应的key
   */
  lazy val webui_sqlName = "SQL_Lan"

  /**
   * 在WEBUI 中 sql 前后端模块中对接的sql资源路径 应匹配 WEBUI_KVpath
   */
  lazy val webui_sqlD = "/SQLD"

  /**
   * 在WEBUI 中 跳转其它资源的属性对应的key
   */
  lazy val webui_Home_WEB = "/"
  /**
   * 在WEBUI中显示的logo主题
   */
  lazy val WEBUI_Logo = "Easter_Bunny 1.2"

  /**
   * WEBUI 中的首页默认请求异常备用位置 当获取不到资源的时候 将会直接跳转至此对应的WEBUI页面
   */
  lazy val WEBUI_DEFHOME = "/"

  /**
   * 是否需要再WEB出现异常的时候打印异常信息 true 代表需要
   */
  lazy val webui_Exception_print: Boolean = EasterBunny_Config.getOrElse("webui.Exception.print", "true").toBoolean

  /**
   * 获取清理线程的时间设置 若该值小于等于 0 将不会启动线程
   */
  lazy val webui_TimingClear_ms: Long = EasterBunny_Config.getOrElse("webui.TimingClear", "102400").toLong

  /**
   * WEB用户访问历史清理阈值 访问历史条数超出该值 将会被定时清理 最少可保留最新的 阈值 / 2 条访问记录
   */
  lazy val webui_TimingClear_threshold_user_value: Int = EasterBunny_Config.getOrElse("webui.TimingClear.user.thresholdValue", "24").toInt

  /**
   * 是否需要开启WEB线程镜像服务
   */
  lazy val webui_ThreadImage_service: Boolean = EasterBunny_Config.getOrElse("webui.Thread.image.service", "true").toBoolean

  /**
   * HTTP URL 字符对应库
   */
  lazy val webui_URL_BASE: mutable.HashMap[String, String] = mutable.HashMap(
    "%28" -> "(", "%29" -> ")", "%2C" -> ",", "%3D" -> "=", "%2B" -> "+", "%27" -> "'"
  )

  /**
   * webui 中需要使用的日期格式化器 这里为了减少实例化次数 所以直接使用配置库
   */
  lazy val `Date -> HH`: FastDateFormat = FastDateFormat.getInstance("HH")
  lazy val `Date -> Hms`: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
  lazy val `Date -> yMd Hms`: FastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

  /**
   *
   * @param first first 页面需要的样例类 里面存放着所需参数
   * @return first 页面的 HTML 脚本
   */
  def webUI_First(first: First_Home, getResponse: GETResponse): String = First_Home.HTML(first, getResponse)

  /**
   *
   * @param history_Sql history_sql 页面需要的样例类 里面存放着所需参数
   * @return History_SQL 页面的 HTML 脚本
   */
  def WEBUI_History(history_Sql: History_SQL): String = History_SQL.HTML(history_Sql)

  /**
   *
   * @param sqld 页面需要的样例类 里面存放着所需参数
   * @return History_SQL 页面的 HTML 脚本
   */
  def WEBUI_SQLD(sqld: SQL_D): String = SQL_D.HTML(sqld.title, sqld)
}
