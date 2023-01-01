package utils_zhao.threadTool

class Thread_Image {
  private val threadCount: Int = Thread.activeCount()
  private val threads_Now: Array[Thread] = new Array[Thread](threadCount)
  Thread.enumerate(threads_Now)

  /**
   *
   * @return 当前镜像中包含的线程数量
   */
  def NowAThreadCount(): Int = threadCount

  /**
   *
   * @return 本镜像中记录的所有活跃线程集合
   */
  def NowAThreadArray(): Array[Thread] = threads_Now

  /**
   *
   * @param format 格式化方式 默认是打印宽 50 的列表
   * @return 会将镜像中的线程集合进行一个格式化
   */
  def ListThreads(implicit format: Array[String] => String =
  _.mkString(s"\n+${"=" * 50}+\n|\t", s"\n+${"-" * 50}+\n|\t", s"\n+${"=" * 50}+\n")): String = {
    format(threads_Now.map(th => {
      s"|\t线程ID：${th.getId}\t\t|\t\t线程优先级：${th.getPriority}\t\t|\t\t线程名称：${th.getName}\t\t|"
    }))
  }
}

object Thread_Image {

  /**
   * 这里会按照传入Map集合的对应关系将线程的名字更改过来 如果Map中存在的话
   *
   * @param oldName_NewName Map("线程旧的名字" -> "线程新的名字")
   */
  def setAllThreadName(oldName_NewName: Map[String, String]): Unit = {
    for (thread <- new Thread_Image().threads_Now.map(th => th.getName -> th)) {
      val NEWNAME: String = oldName_NewName.getOrElse(thread._1, "NONO")
      if (NEWNAME != "NONO") thread._2.setName(NEWNAME)
    }
  }
}
