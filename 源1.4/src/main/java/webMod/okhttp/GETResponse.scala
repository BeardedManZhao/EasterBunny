package webMod.okhttp

import utils_zhao.netSocket.zhaoSocket
import webMod.web.{First_Home, History_SQL, SQL_D}

import java.io.BufferedOutputStream

/*
HTTP/1.1 200 OK
ccess-control-allow-credentials: true
access-control-allow-headers: content-type,user_name,Cookie,sec-fetch-mode,sec-fetch-site,Accept,Connection,User-Agent,Host,Accept-Encoding,sec-fetch-user,sec-ch-ua,sec-ch-ua-mobile,Cache-Control,upgrade-insecure-requests,X-Forwarded-For,Accept-Language,X-Real-IP,sec-fetch-dest
access-control-allow-methods: GET, POST, PUT, DELETE, OPTIONS
access-control-max-age: 86400
content-encoding: gzip
content-language: zh-CN
content-type: text/html;charset=utf-8
date: Thu, 28 Apr 2022 00:11:08 GMT
server: openresty
strict-transport-security: max-age= 31536000
vary: Accept-Encoding
  todo html
 */

/**
 * 回复模块 其中已自动包含请求解析模块
 */
class GETResponse extends GETRequest {

  /**
   * 回复流
   */
  val Out_stream: BufferedOutputStream = new BufferedOutputStream(Server_acc.getOutputStream)

  /**
   * 回复HTTP
   */
  def response_Http(): Unit = zhaoSocket.respinseData(toString, Out_stream, `isHTTP?` = true)

  /**
   *
   * @return GET响应信息
   */
  override def toString: String = {
    s"""
       |HTTP/1.1 200 OK
       |ccess-control-allow-credentials: true
       |access-control-allow-methods: GET
       |content-language: zh-CN
       |content-type: text/html;charset=${config.WEB_ConfigBase.webUI_CharSet}
       |""".stripMargin
  }

  /**
   *
   * @param WEB   需要回复的页面
   * @param prams 该页面种需要的参数
   */
  def response_HTML(WEB: String, prams: Array[Any], getResponse: GETResponse): Unit = {
    WEB match {
      case "First_Home" => zhaoSocket.respinseData(
        config.WEB_ConfigBase.webUI_First(First_Home(WEB), getResponse: GETResponse
        ), Out_stream, `isHTTP?` = false
      )
      case "History_SQL" => zhaoSocket.respinseData(
        config.WEB_ConfigBase.WEBUI_History(
          History_SQL(WEB, prams.map(_.asInstanceOf[(String, String, String)]))
        ), Out_stream, `isHTTP?` = false
      )
      case "SQL_D" => zhaoSocket.respinseData(
        config.WEB_ConfigBase.WEBUI_SQLD(SQL_D("SQL处理器", getKV2_Value(config.WEB_ConfigBase.webui_sqlName))), Out_stream, `isHTTP?` = false
      )
      case _ => zhaoSocket.respinseData(s"<script>alert('未找到 $WEB 资源');window.history.back()</script>", Out_stream, `isHTTP?` = false)
    }
  }

  def close(): Unit = {
    Out_stream.flush()
    Out_stream.close()
    Server_acc.close()
  }

}
