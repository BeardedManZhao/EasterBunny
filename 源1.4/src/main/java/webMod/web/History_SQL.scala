package webMod.web

import config.WEB_ConfigBase.{WEBUI_Logo, `Date -> yMd Hms`, webUI_CharSet, webui_sqlD}
import run_zhao.MAIN_zhao

/**
 *
 * @param title 标题
 * @param prams 每一个元素的作用 执行者 执行时间 执行语句
 */
case class History_SQL(title: String, prams: Seq[(String, String, String)])

object History_SQL {
  def HTML(history_SQL: History_SQL): String = {
    val len = history_SQL.prams.length
    val `lan > 0 ?` = len > 0
    s"""<!DOCTYPE html>
       |<html>
       |
       |<head>
       |    <title>${history_SQL.title}</title>
       |    <meta http-equiv="Content-Type" content="text/html; charset=$webUI_CharSet">
       |    <style>
       |        body {
       |            color: rgb(194, 238, 255);
       |            width: 100%;
       |            height: 100%;
       |            ${
      if (`lan > 0 ?`) {
        "background-color: rgb(41, 26, 65);"
      } else {
        "background-color: rgb(175, 93, 56);"
      }
    }
       |            background-repeat: round;
       |            background-attachment: fixed;
       |        }
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
       |            height: 245px;
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
       |${
      if (`lan > 0 ?`) {
        var SNum: Int = 1
        history_SQL.prams.map(v => {
          SNum <<= 1
          s"""<div class="nl">
             |    <form action="$webui_sqlD">
             |        <div class="资源">SQL报表记录</div>
             |        <hr>
             |        <div>报表信息</div>
             |        <p>执行身份：${v._1}</p>
             |        <p>执行时间：${v._2}</p>
             |        <p>执行语句：${v._3}</p>
             |        <hr>
             |        <div style="text-align: center;">
             |            <input type="hidden" name="SQL_Lan" value="${v._3.replace(" ", "--")}">
             |            <input type="submit" value="查看SQL结果" class="but" id="resources$SNum" onmouseover="mousein('resources$SNum')"
             |                   onmouseout="mouseout('resources$SNum')" onclick="goto('$webui_sqlD')">
             |        </div>
             |    </form>
             |</div>""".stripMargin
        }).mkString("<br>\n")
      } else {
        s"""
           |<h1>$WEBUI_Logo| 您好 ${System.getProperty("user.name")}, 从启动开始，您没有一次成功查询到数据哦.......</h1>
           |<h3>现在是：${`Date -> yMd Hms`.format(System.currentTimeMillis())}</h3>
           |<hr>
           |版本：1.2
           |@复活节兔子 赵凌宇
           |${MAIN_zhao.help(Array("select")).replace("\n", "<hr>\n")}
           |""".stripMargin
      }
    }
       |<!----------------------------------------------------------------------------------------------------------------->
       |<hr>
       |</body>
       |<script>
       |
       |    /**
       |     *
       |     * @param url 需要验证的url
       |     * @returns {boolean} 如果返回true 代表可以提交
       |     */
       |    function yanurl(url) {
       |        if (url === "***") {
       |            alert("请指定要访问的入口哦！")
       |            return false
       |        } else {
       |            return true
       |        }
       |    }
       |
       |    /**
       |     * 配合提交事件 在提交之前做到更改地址栏
       |     * @param url 修改的指定URL资源 例如 goto(baidu) = http//IP:port/baidu
       |     */
       |    function goto(url) {
       |        // var href = window.location.href;
       |        // href.replace(href.substring(href.indexOf("17887"), href.length - 1), url)
       |        window.location.href = url
       |        return true
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
}