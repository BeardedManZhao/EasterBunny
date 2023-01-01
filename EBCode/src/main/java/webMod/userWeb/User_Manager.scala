package webMod.userWeb

import config.WEB_ConfigBase
import webMod.okhttp.GETResponse

import scala.collection.mutable.ArrayBuffer


class User_Manager {

  final val userList: ArrayBuffer[USER] = ArrayBuffer[USER]()
  var userNum = 0

  /**
   *
   * @param userName 需要添加的用户名称 默认系统名称
   */
  def addUser(implicit userName: String = System.getProperty("user.name"), getResponse: GETResponse): Unit = {
    userNum += 1
    userList.append(
      USER(
        userNum, userName, System.currentTimeMillis(),
        getResponse.HTTP_KV.getOrElse("Referer", s"http://127.0.0.1:${config.WEB_ConfigBase.webUI_port}/?name=zhao").trim
      )
    )
  }

  /**
   *
   * @param f 转换遍历的函数
   * @return 转换之后的数据列表
   */
  def listData(implicit f: Seq[String] => String = _.mkString(s"\n+${"=" * 70}+\n|\t", s"\n+${"-" * 70}+\n|\t", s"\n+${"=" * 70}+\n")): String = {
    f(userList.sortWith((user1, user2) => user1.Time > user2.Time).map(x => {
      s"""|\t\t记录编号：${x.NumID}\t\t|\t\t访问时间(服务器时间)：${WEB_ConfigBase.`Date -> yMd Hms`.format(x.Time)}\t\t|\t\t主机信息：${x.name}\t\t|\t\t访问URL：${x.URL}\t\t|"""
    }))
  }

  /**
   *
   * @param ID 需要删除的用户ID 需要满足的条件 默认是删除
   *           id小于等于该集合内 id平均值的数据
   */
  def deleteUser(implicit ID: Int => Boolean = null): Unit = {
    if (ID != null) {
      dropAll(ID)
    } else {
      val avg = {
        var sumId = 0
        for (id <- userList) sumId += id.NumID
        sumId
      } / userList.length
      dropAll(id => id <= avg)
    }
  }

  /**
   *
   * @param f ID 满足该条件则删除
   */
  private def dropAll(f: Int => Boolean): Unit = {
    userList --= zhao_where(f)
  }

  /**
   *
   * @param f 过滤条件
   * @return 符合条件的所有元素
   */
  private def zhao_where(f: Int => Boolean): ArrayBuffer[USER] = {
    val TEMP: ArrayBuffer[USER] = ArrayBuffer[USER]()
    for (i <- userList if f(i.NumID)) TEMP.append(i)
    TEMP
  }
}