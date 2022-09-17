package webMod.web

import config.WEB_ConfigBase.{WEBUI_Logo, `Date -> Hms`, webui_URL_BASE}
import run_zhao.MAIN_zhao.sc_sql

/**
 *
 * @param title 标题
 * @param prams SQL命令
 */
case class SQL_D(title: String, prams: String)

object SQL_D {

  def HTML(title: String, sqld: SQL_D): String = {
    val sql = Data_HTTPURL(sqld.prams)
    var Datafrema: Dataset[Row] = null
    val dataARSTR: Array[String] = {
      try {
        if (sql != "select 'NotFond', '请检查您的请求'") {
          Datafrema = sc_sql.sql(sql)
          Datafrema.take(124).map(x => x.mkString(start = "|\t", sep = "\t\t|\t\t", end = "\t|"))
        } else {
          Array("错误7887",
            """Sorry NotFond 请检查您的请求 => 我们没有在您的请求中获取到正确的表单。
              |疑似原因：    您可能是通过主页进行的访问，主页中入口并不存在SQL语句。
              |解决方案：    如果满足上述错误请前往历史服务中进行SQL结果的查询，若不满足 请尝试重新提交或更改编码集。""".stripMargin)
        }
      } catch {
        case x: Exception => Array("错误7887", "原因：" + x.getLocalizedMessage)
      }
    }
    val `isExcrption?` = dataARSTR.head == "错误7887"
    s"""
       |<!DOCTYPE html>
       |<html lang="zh">
       |
       |<head>
       |    <title>$title</title>
       |    <meta charset="UTF-8">
       |    <style>
       |        body {
       |           color: rgb(194, 238, 255);
       |        ${
      if (`isExcrption?`) {
        "background-color: rgb(175, 93, 56);"
      } else {
        "background-color: rgb(41, 26, 65);"
      }
    }
       |            width: 150%;
       |            height: 100%;
       |            background-repeat: no-repeat;
       |            background-attachment: fixed;
       |        }
       |
       |#SQLManager {
       |        width: 150%;
       |        /* 宽度 */
       |        height: ${49 * dataARSTR.length * (if (`isExcrption?`) 2 else 1)}px;
       |        /* 高度 */
       |        border-width: 0;
       |        /* 边框宽度 */
       |        border-radius: 3px;
       |        /* 边框半径 */
       |        background: rgba(18, 80, 75, 0.49);
       |        /* 背景颜色 */
       |        outline: none;
       |        /* 不显示轮廓线 */
       |        font-family: Microsoft YaHei, serif;
       |        /* 设置字体 */
       |        color: white;
       |        /* 字体颜色 */
       |        font-size: 20px;
       |        /* 字体大小 */
       |
       |
       |        #L1 {
       |            width: 90%;
       |            height: 90%;
       |            text-align: center;
       |            background-color: rgba(252, 248, 255, 0.11);
       |        }
       |
       |        .nl {
       |            margin: 0 auto;
       |            border-radius: 25px;
       |            background: #c191dd56;
       |            padding: 20px;
       |            width: 75%;
       |            height: 255px;
       |        }
       |    </style>
       |</head>
       |
       |<body>
       |<style>
       |    div.资源 {
       |        text-shadow: 2px 2px 5px rgb(153, 186, 247);
       |        text-align: center;
       |        font-size: 25px;
       |    }
       |
       |    input {
       |        /* 按钮美化 */
       |        width: 270px;
       |        /* 宽度 */
       |        height: 40px;
       |        /* 高度 */
       |        border-width: 0;
       |        /* 边框宽度 */
       |        border-radius: 3px;
       |        /* 边框半径 */
       |        background: #1e8fff3d;
       |        /* 背景颜色 */
       |        cursor: pointer;
       |        /* 鼠标移入按钮范围时出现手势 */
       |        outline: none;
       |        /* 不显示轮廓线 */
       |        font-family: Microsoft YaHei, serif;
       |        /* 设置字体 */
       |        color: white;
       |        /* 字体颜色 */
       |        font-size: 17px;
       |        /* 字体大小 */
       |    }
       |</style>
       |<!----------------------------------------------------------------------------------------------------------------->
       |<div>
       |    <h1>$WEBUI_Logo| 当前时间：${`Date -> Hms`.format(System.currentTimeMillis())}</h1>
       |    <hr>
       |    <h3>您的目标语句：$sql</h3>
       |    <hr>
       |    <h3>语句涉及视图：${
      if (sql.contains("from")) sql.replaceAll("\\s*,+\\s*", ",").split("\\s*from\\s*")(1).split("\\s+").head
      else "-------未涉及-------"
    }</h3>
       |    <hr>
       |    <h3>结果字段信息：</h3>
       |    <h3>${
      try {
        if (`isExcrption?`) "----------SQL异常未解析----------" else Datafrema.schema.treeString.replace("\n", "<br>")
      } catch {
        case _: Exception => "----------字段异常未解析----------"
      }
    }</h3>
       |    <hr>
       |    <h3>SQL查询结果信息：</h3>
       |    ${
      if (`isExcrption?`) dataARSTR.mkString("<pre id='SQLManager'><hr>", "<br>", "<br>* >>> 发生错误了!!!!</pre><br>")
      else dataARSTR.mkString("<pre id='SQLManager'><hr>", "<hr>", "<hr>* >>> 默认显示前124条数据哦!!!</pre><br>")
    }
       |</div>
       |<!----------------------------------------------------------------------------------------------------------------->
       |<hr>
       |</body>
       |<script>
       |
       |    /**
       |     *
       |     * @param url 修改的指定URL资源 例如 goto(baidu) = http//IP:port/baidu
       |     */
       |    function goto(url) {
       |        // var href = window.location.href;
       |        // href.replace(href.substring(href.indexOf("17887"), href.length - 1), url)
       |        window.location.href = url
       |    }
       |
       |    function mousein(id) {
       |        var x = document.getElementById(id);
       |        x.style.backgroundColor = "#1e8fff85";
       |    }
       |
       |    function mouseout(id) {
       |        var x = document.getElementById(id);
       |        x.style.backgroundColor = "#1e8fff3d";
       |    }
       |</script>
       |
       |</html>""".stripMargin
  }

  /**
   * 解析HTTP中参数的小括号 逗号 等于号
   *
   * @param data 需要被解析的数据
   * @return 解析之后的数据
   */
  def Data_HTTPURL(data: String): String = {
    val flnData: StringBuilder = new StringBuilder
    val end_lan = data.length - 3
    var start_lan = 0
    while (start_lan <= end_lan) {
      val chars: String = data.substring(start_lan, start_lan + 3)
      val URLBASE_char = webui_URL_BASE.getOrElse(chars, "NONONONO")
      if (URLBASE_char != "NONONONO") {
        flnData.append(URLBASE_char)
        start_lan += 3
      } else {
        start_lan += 1
        if (!(start_lan <= end_lan)) {
          flnData.append(data.substring(end_lan, end_lan + 3))
        } else {
          flnData.append(chars.head)
        }
      }
    }
    flnData.toString().replace("--", " ")
  }
}