package webMod.web

import config.WEB_ConfigBase.{WEBUI_KVpath, WEBUI_Logo, `Date -> HH`, webui_ThreadImage_service}
import run_zhao.MAIN_zhao
import utils_zhao.threadTool.Thread_Image
import webMod.okhttp.{GETResponse, WebRun}


/**
 *
 * @param title 页面主题
 */
case class First_Home(title: String)

object First_Home {

  def HTML(first_Home: First_Home, getResponse: GETResponse): String = {
    val viewCount = MAIN_zhao.view_manager.CON.size
    val userCount = WebRun.userManager.userList.length
    val threadImage = if (webui_ThreadImage_service) new Thread_Image else null
    val nototd = NOOTD()
    s"""
       |<html lang="zh">
       |<head>
       |    <title>${first_Home.title}</title>
       |</head>
       |<body>
       |<style>
       |        body {
       |            color: rgb(194, 238, 255);
       |            width: 100%;
       |            height: 100%;
    ${
      if (nototd) {
        "background-color: rgb(41, 26, 65);color: rgb(194, 238, 255);"
      } else {
        "background-color: rgba(36, 162, 132, 0.51);color: rgb(41, 26, 65);"
      }
    }
       |            background-repeat: round;
       |            background-attachment: fixed;
       |        }
       |
       |    #L2 {
       |        /* 按钮美化 */
       |        width: 270px;
       |        /* 宽度 */
       |        height: 40px;
       |        /* 高度 */
       |        border-width: 0;
       |        /* 边框宽度 */
       |        border-radius: 3px;
       |        /* 边框半径 */
       |        background: rgb(159, 189, 49);
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
       |        text-align: center;
       |    }
       |
       |    #view {
       |        width: 100%;
       |        /* 宽度 */
       |        height: ${51 * viewCount}px;
       |        /* 高度 */
       |        border-width: 0;
       |        /* 边框宽度 */
       |        border-radius: 3px;
       |        /* 边框半径 */
       |        background: rgba(159, 189, 49, 0.49);
       |        /* 背景颜色 */
       |        outline: none;
       |        /* 不显示轮廓线 */
       |        font-family: Microsoft YaHei, serif;
       |        /* 设置字体 */
       |        color: white;
       |        /* 字体颜色 */
       |        font-size: 20px;
       |        /* 字体大小 */
       |    }
       |
       |        #userManager {
       |        width: 100%;
       |        /* 宽度 */
       |        height: ${49 * userCount}px;
       |        /* 高度 */
       |        border-width: 0;
       |        /* 边框宽度 */
       |        border-radius: 3px;
       |        /* 边框半径 */
       |        background: rgba(159, 189, 49, 0.49);
       |        /* 背景颜色 */
       |        outline: none;
       |        /* 不显示轮廓线 */
       |        font-family: Microsoft YaHei, serif;
       |        /* 设置字体 */
       |        color: white;
       |        /* 字体颜色 */
       |        font-size: 20px;
       |        /* 字体大小 */
       |    }
       |
       |  #ThreadManager{
       |        width: 100%;
       |        /* 宽度 */
       |        height: ${if (webui_ThreadImage_service) 49 * threadImage.NowAThreadCount() else 49}px;
       |        /* 高度 */
       |        border-width: 0;
       |        /* 边框宽度 */
       |        border-radius: 3px;
       |        /* 边框半径 */
       |        background: rgba(159, 189, 49, 0.49);
       |        /* 背景颜色 */
       |        outline: none;
       |        /* 不显示轮廓线 */
       |        font-family: Microsoft YaHei, serif;
       |        /* 设置字体 */
       |        color: white;
       |        /* 字体颜色 */
       |        font-size: 20px;
       |        /* 字体大小 */
       |  }
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
       |
       |    .nl {
       |        margin: 0 auto;
       |        border-radius: 25px;
       |        background: #c191dd56;
       |        padding: 20px;
       |        width: 75%;
       |        height: 255px;
       |    }
       |</style>
       |<h1>$WEBUI_Logo| ${if (nototd) "晚上好，请注意休息" else "欢迎回来"}
       |<hr>
       |<h3>WEBUI入口</h3>
       |${
      var SNum: Int = 1
      WEBUI_KVpath.map(x => {
        SNum <<= 1
        s"""      <div id=L1>
           |          <input type="button" value="${x._2}" id="resources$SNum" onmouseover="mousein('resources$SNum')"
           |                 onmouseout="mouseout('resources$SNum')" onclick="goto('${x._1}')">
           |      </div>""".stripMargin
      }).mkString("<br>")
    }
       |<h2>所有存在视图 查询到：$viewCount 个</h2>
       |${MAIN_zhao.view_manager.CON.map(x => ("\t\t\t" + x._1 + "\t\t\t", "\t\t\t" + x._2)).mkString("<pre id='view'><hr>|", "<hr>|", "<hr></pre><br>")}
      ${
      if (webui_ThreadImage_service) {
        s"""
           |<h2>系统当前线程 查询到：${threadImage.NowAThreadCount()} 个</h2>
           |${threadImage.ListThreads(_.mkString("<pre id='ThreadManager'><hr>", "<hr>", "<hr></pre><br>"))}
           |""".stripMargin
      } else {
        """
       |<h2>系统当前线程 查询到：---- 个</h2>
       |<pre id='ThreadManager'><hr>|       -----==未启用镜像服务==-----        |<hr></pre><br>
       |""".stripMargin
      }
    }
       |${
      try {
        s"""
           |<h2>用户访问历史 查询到：$userCount 条</h2>
           |${WebRun.userManager.listData(x => x.mkString("<pre id='userManager'><hr>", "<hr>", "<hr></pre><br>"))}""".stripMargin
      } catch {
        case x: Exception =>
          s"""
             |<h2>用户访问历史 查询到：---- 条</h2>
             |${s"<pre id='userManager'>异常：<hr>${x.getLocalizedMessage}<hr></pre><br>"}
             |""".stripMargin
      }
    }
       |<hr>
       |</body>
       |</html>
       |<script>
       |
       |    /**
       |     * 配合点击事件 在提交之前做到更改地址栏
       |     * @param url 修改的指定URL资源 例如 goto(baidu) = http//IP:port/baidu
       |     */
       |    function goto(url) {
       |        // var href = window.location.href;
       |        // href.replace(href.substring(href.indexOf("17887"), href.length - 1), url)
       |        window.location.href = url
       |        alert("已受理。目标地址成功更换。再次点击即可跳转。")
       |    }
       |
       |    /**
       |     * 鼠标移入时的效果
       |     * @param id 被控制的标签ID
       |     */
       |    function mousein(id) {
       |        var x = document.getElementById(id);
       |        x.style.backgroundColor = "#1e8fff85";
       |    }
       |
       |    /**
       |     * 鼠标移出时的效果
       |     * @param id 被控制的标签ID
       |     */
       |    function mouseout(id) {
       |        var x = document.getElementById(id);
       |        x.style.backgroundColor = "#1e8fff3d";
       |    }
       |</script>
       |""".stripMargin
  }

  /**
   *
   * @return 现在是否非白天
   */
  def NOOTD(): Boolean = {
    val HH = (`Date -> HH` format System.currentTimeMillis()).toInt
    HH < 5 || HH > 17
  }
}