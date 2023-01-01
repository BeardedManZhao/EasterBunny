package run_zhao

//import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

/**
 * SparkSQL 启动ThriftServer服务，通过JDBC方式访问数据分析查询
 * i). 通过Java JDBC的方式，来访问Thrift JDBC/ODBC server，调用Spark SQL，并直接查询Hive中的数据
 * ii). 通过Java JDBC的方式，必须通过HTTP传输协议发送thrift RPC消息，Thrift JDBC/ODBC server必须通过上面命
 * 令启动HTTP模式
 * todo 这种模式，全程几乎就是将Hive当作一个数据库来进行的jdbc  实际进行处理的是Spark的那个TH服务 我们的这个程序主要就是通过RPC去发送语句给Spake
 */
object Z_test {
  val confdir_key = "ZhaoLingYu_config.dir"
  //  def main(args: Array[String]): Unit = {
  //    // 定义相关实例对象，未进行初始化
  //    var conn: Connection = null // 数据库连接对象 这里连接的是Spark的那个TH服务
  //    var pstmt: PreparedStatement = null // 数据库操作对象 这里主要就是执行语句的操作对象
  //    var rs: ResultSet = null // 数据查询结果集 这里就是将数据一条一个元素的形式保存了进来
  //    try {
  //      // TODO： a. 加载驱动类
  //      Class.forName("org.apache.hive.jdbc.HiveDriver")
  //      // TODO: b. 获取连接Connection
  //      conn = DriverManager.getConnection(
  //        "jdbc:hive2://node1.itcast.cn:10000/db_hive",
  //        "root",
  //        "123456"
  //      )
  //      // TODO: c. 构建查询语句
  //      val sqlStr: String =
  //        """
  //          |select e.ename, e.sal, d.dname from emp e join dept d on e.deptno = d.deptno
  //""".stripMargin
  //      pstmt = conn.prepareStatement(sqlStr)
  //      // TODO: d. 执行查询，获取结果
  //      rs = pstmt.executeQuery()
  //      // 打印查询结果
  //      while (rs.next()) {
  //        println(s"empno = ${rs.getInt(1)}, ename = ${rs.getString(2)}, sal = ${
  //          rs.getDouble(3
  //          )
  //        }, dname = ${rs.getString(4)}")
  //      }
  //    } catch {
  //      case e: Exception => e.printStackTrace()
  //    } finally {
  //      if (null != rs) rs.close()
  //      if (null != pstmt) pstmt.close()
  //      if (null != conn) conn.close()
  //    }
  //  }
}