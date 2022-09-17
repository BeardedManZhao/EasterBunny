package webMod.okhttp

import org.apache.spark.internal.Logging
import run_zhao.MAIN_zhao.status
import webMod.userWeb.User_Manager

object WebRun extends Logging {

  val userManager: User_Manager = new User_Manager
  if (config.WEB_ConfigBase.webui_TimingClear_ms > 0) {
    try {
      TimingClearUserHis(config.WEB_ConfigBase.webui_TimingClear_ms)
    } catch {
      case x: Exception => if (config.WEB_ConfigBase.webui_Exception_print) x.printStackTrace()
    }
  }

  /**
   * 负责对整个网站的管理
   *
   * 传值 需要使用哪个页面 页面中需要改变的参数
   */
  def main(): Unit = {
    try {
      // 状态码 和 状态码规则 不符合规则将停止 WEB 服务 这里我们需要的时候再实现
      while (status) {
        val GETResponse = new GETResponse
        new Thread(() => Handle(GETResponse), "WEB请求处理线程").start()
      }
    } catch {
      case x: Exception => if (config.WEB_ConfigBase.webui_Exception_print) x.printStackTrace()
    } finally {
      if (status) {
        println("* !>> web服务器异常，即将尝试重启web服务器。")
        Thread.sleep(1024)
        main()
      } else {
        println("* >>> WEB 服务器模块正在关闭。")
        // 关闭 WEB 服务
        config.WEB_ConfigBase.webUI_ServerSocket.close()
      }
    }
  }

  /**
   * 对于每一条用户的请求进行一个处理
   */
  def Handle(getResponse: GETResponse): Unit = {
    try {
      userManager.addUser(getResponse.Server_acc.getInetAddress.getHostName, getResponse)
      // 当实例化成功之后，我们调用回复 HTTP 头信息的方法 向对方发送头数据
      getResponse.response_Http()
      /*
   * 我们成功和对方连接，现在来发送一个HTML页面 这里的参数解释
   * WEB    =     需要发送的HTML页面名称 目前只有 First
   * prams  =     这里是我们指定的 WEB 页面需要的参数 为了实现动态效果，所以我们在这里指定，达到类似jsp的技术
   *              缺点是前后端耦合比较厉害，优点是对于性能的提升很多
   */
      getResponse.response_HTML(config.WEB_ConfigBase.WEBUI_KVpath(
        getResponse.getKV2_Value("path")
      ), config.WEB_ConfigBase.SQL_UI_History.map(_.asInstanceOf[Any]).toArray, getResponse)
    } catch {
      case x: Exception => if (config.WEB_ConfigBase.webui_Exception_print) x.printStackTrace()
    } finally {
      // 关闭本次访问对象的连接
      getResponse.close()
    }
  }

  /**
   * 线程监控用户列表 默认是102秒清理一次 清理规则请见 deleteUser 方法描述
   */
  def TimingClearUserHis(implicit ms: Long = 102400): Unit = {
    new Thread(() => {
      while (status) {
        try {
          if (userManager.userList.length > config.WEB_ConfigBase.webui_TimingClear_threshold_user_value) {
            userManager.deleteUser
          }
        } catch {
          case x: Exception => if (config.WEB_ConfigBase.webui_Exception_print) x.printStackTrace()
        } finally {
          Thread.sleep(ms)
        }
      }
      println("* >>> UserManager_History 已关闭。")
    }, "WEB系统清理器").start()
  }
}
