package webMod.okhttp

import run_zhao.ConstantRegion
import utils_zhao.netSocket.zhaoSocket

import java.net.Socket
import scala.collection.mutable

// 来自客户端的 HTTP GET请求
//GET / HTTP/1.1
//Host: localhost:17887
//  Connection: keep-alive
//  Cache-Control: max-age=0
//  sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="8"
//  sec-ch-ua-mobile: ?0
//  Upgrade-Insecure-Requests: 1
//  User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 SLBrowser/8.0.0.2242 SLBChan/103
//  Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
//Sec-Fetch-Site: none
//Sec-Fetch-Mode: navigate
//Sec-Fetch-User: ?1
//Sec-Fetch-Dest: document
//Accept-Encoding: gzip, deflate, br
//Accept-Language: zh-CN,zh;q=0.9
//进程已结束，退出代码为 0


/**
 * 请求解析模块
 */
class GETRequest {
  final val Server_acc: Socket = config.WEB_ConfigBase.webUI_ServerSocket.accept()
  Server_acc.setTcpNoDelay(true)
  final val HTTP_STR: Array[String] = ConstantRegion.WRAP_PATTERN.split(zhaoSocket.receive_Data(Server_acc.getInputStream))
  final val HTTP_Version: String = HTTP_STR.head

  /**
   * 请求头列表
   */
  final val HTTP_KV: Map[String, String] = HTTP_STR.splitAt(1)._2.flatMap(ConstantRegion.WRAP_PATTERN.split(_)).map(kv => {
    val KV = kv.split(": ")
    KV.head -> KV.last
  }).toMap

  /**
   * 所有参数列表
   */
  final val HTTP_KV2: mutable.HashMap[String, String] = mutable.HashMap[String, String]() ++ {
    val x = ConstantRegion.QUESTION_MARK_PATTERN.split(HTTP_KV.getOrElse("Referer", s"http://127.0.0.1:${config.WEB_ConfigBase.webUI_port}/?name=zhao"))
    val strings = x(0).split(config.WEB_ConfigBase.webUI_port.toString)
    if (x.nonEmpty && x.length > 1) {
      ConstantRegion.ATTRIBUTE_SEGMENTATION.split(x.last).map(ConstantRegion.EQ_PATTERN.split(_)).filter(_.length > 1).map(kv => kv(0) -> kv(1)).toMap ++ Map("path" -> {
        if (strings.length <= 1) "/" else strings.last
      })
    } else {
      Map("path" -> {
        if (strings.length <= 1) "/" else strings.last
      })
    }
  }

  /**
   *
   * @param key 请求参数名称
   * @return 该参数对应的属性
   */
  def getKV2_Value(key: String): String = HTTP_KV2.getOrElse(key, "select 'NotFond', '请检查您的请求'").trim

  /**
   *
   * @param f 解析参数列表的算法
   * @return 解析之后的结果
   */
  def getAllKVList(implicit f: mutable.HashMap[String, String] => String = _.mkString(s"\n+${"=" * 100}+\n|\t", "\n|\t", s"\n+${"=" * 100}+\n")): String = f(HTTP_KV2)

}
