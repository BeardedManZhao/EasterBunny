package utils_zhao.netSocket

import config.WEB_ConfigBase

import java.io.{BufferedOutputStream, InputStream}
import java.net.{ServerSocket, Socket}

/**
 * 网络通讯工具类 底层类
 */
object zhaoSocket {

  /**
   *
   * @param port 服务器1web页面端口
   * @return 服务器通讯对象
   */
  def getServerSocket(port: Int): ServerSocket = new ServerSocket(port)

  /**
   *
   * @param IP   客户端IP
   * @param port 客户端端口
   * @return 客户端通讯对象
   */
  def getSocket(IP: String, port: Int): Socket = new Socket(IP, port)

  /**
   *
   * @param Data      需要发送的数据
   * @param socket    发送数据由谁来负责
   * @param `isHTTP?` 是否发送HTTP ？
   */
  def respinseData(Data: String, stream: BufferedOutputStream, `isHTTP?`: Boolean): Unit = {
    if (`isHTTP?`) sendGETHEAD(Data, stream) else sandData(Data, stream)
    Thread.sleep(500)
  }

  /**
   * 将头标题发送出去
   *
   * @param Data   需要被发送的数据
   * @param stream 发送数据的对象 应是一个被Socket创建出来的流
   */
  def sendGETHEAD(Data: String, stream: BufferedOutputStream): Unit = {
    stream.write((Data.split("\n").mkString("\r\n") + "\r\n").getBytes(WEB_ConfigBase.webUI_CharSet))
  }

  /**
   * 将数据发送发送出去
   *
   * @param Data   数据
   * @param stream 由谁来发送 这里应是一个被Socket创建出来的流
   */
  def sandData(Data: String, stream: BufferedOutputStream): Unit = {
    stream.write(Data.getBytes(WEB_ConfigBase.webUI_CharSet))
  }

  /**
   * 将数据接收 并转化为字符串
   *
   * @param socket_inputstream 服务器输入流 应由ServerSocket创建的InputStreaming
   * @return 接收到的数据字符串
   */
  def receive_Data(socket_inputstream: InputStream): String = {
    val data_byte: Array[Byte] = new Array[Byte](1240)
    socket_inputstream.read(data_byte)
    new String(data_byte).trim
  }

  /**
   * 将Int类型IPv4地址转换为字符串类型
   */
  def number2IpString(ip: Int): String = {
    val buffer: Array[Int] = new Array[Int](4)
    buffer(0) = (ip >> 24) & 0xff
    buffer(1) = (ip >> 16) & 0xff
    buffer(2) = (ip >> 8) & 0xff
    buffer(3) = ip & 0xff
    // 返回IPv4地址
    buffer.mkString(".")
  }
}
