# 自定义数据读取组件的实现
- Switch to[English document](https://github.com/BeardedManZhao/EasterBunny/blob/main/Implementation%20of%20user-defined%20data%20reading%20component.md)
- 自定义数据读取组件简介

自定义组件需要实现接口“run_zhao.UDFDATASourceFormat”，该接口继承自初始化插件接口“Init_plug_in”，也就是说，您的自定义组件可以通过以下方式添加到EasterBunny中。
![image](https://user-images.githubusercontent.com/113756063/192914662-01ce0d39-5ee0-404a-9f97-b8b25bd8dcd4.png)

这里我们将以“EasterBunny”中的“DataTear”数据源插件为例，介绍如何定制数据源加载插件。如您所见，下图中选择的组件是一个用户定义的数据源组件，它是使用DataTear框架实现的。有关DataTear的信息，请参阅:[dataTear](https://github.com/BeardedManZhao/dataTear)

  * 1 通过“Init_Plug_in”接口提供的方法，将数据源组件添加到“EasterBunny”的自定义数据源组件集。
   
   在这里，我们在“getName”中实现插件的名称，并将插件添加到“run（）：Boolean”中的数据源组件集合。不要忘记，在所有这些开始之前，您需要导入EasterBunny的包。
  ![3cead1f1301c9b9d8ad5387ec4b6e78](https://user-images.githubusercontent.com/113756063/192923964-40996af9-7d72-4e75-b8b6-833e6f308d1b.jpg)
  ![image](https://user-images.githubusercontent.com/113756063/192914398-c0d1d409-2776-4f82-810c-bff93c1aa8d8.png)
  
  * 2 通过“UDFDATASourceFormat”接口，实现组件的位置命令和命令的处理逻辑。
  
  “GetCommand:String”提供此组件的调用命令。“EasterBunny”将通过此方法获取命令，以匹配您的加载模式，找到目标组件并执行其逻辑。其命令处理逻辑在“run（sparkSession:SparkSeetion，strings:Array[String]）:RDD[Row]”方法中实现。
  ![image](https://user-images.githubusercontent.com/113756063/192914783-bc9c8d8b-4825-43c2-b1e0-a2cde911ad9a.png)
- 自定义组件的示例源代码

以下是从“EasterBunny”中提取并由Zhao实现的数据组件的示例源代码。您可以通过以下方式将组件对接到“EasterBunny”。
   
```
package zhao.io.dataTear

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import run_zhao.UDFDATASourceFormat
import zhao.io.ex.CommandParsingException

/**
 * DataTear数据读取组件
 *
 * 该组件能够装载到EasterBunny中，使得EasterBunny，能够通过DataTear读取数据
 */
class MAIN extends UDFDATASourceFormat {
  // 获取到一个DataTear的数据读取组件的对象
  private val cat = new ZHAOCat()

  /**
   * 该数据源组件的命令处理逻辑实现
   *
   * @param sparkSession 来自EasterBunny的sparkSession对象
   * @param strings      命令参数，第一个参数是 cat
   *                     <br>
   *                     当您的EasterBunny会话中数据加载方式（Type）参数输入为 UDF:DT=cat>DT>hdfs://out/test.txt 的时候，您的这个参数如下所示
   *                     <br>
   *                     Array(cat, DT, hdfs://out/test.txt)，您可以根据这个命令处理规律，来实现您需要的命令处理方式
   * @return 加载好的RDD[Row]数据集合，这个集合最终会由EasterBunny托管
   */
  override def run(sparkSession: SparkSession, strings: Array[String]): RDD[Row] = {
    if (cat.open(strings.map(_.trim))) {
      try {
        cat.run()
        cat.close()
        val string = cat.byteArrayOutputStream.toString
        sparkSession.sparkContext.makeRDD(string.split("\n").toSeq).map(line => Row.fromSeq(line.split(",")))
      } catch {
        case n: NullPointerException => n.printStackTrace()
          throw new NullPointerException("DT数据组件操作失败！")
      }
    } else {
      throw new CommandParsingException("* >!> DataTear 组件出现错误，流没有顺利打开。")
    }
  }

  /**
   * @return 该初始化插件的名称
   */
  override def getName: String = "DataTear数据读取插件"

  /**
   * 插件初始化逻辑，一般就是在这里将自己添加到自定义源操作组件集合中
   * @return 该插件是否初始化成功
   */
  override def run(): Boolean = {
    UDFDATASourceFormat.UDFDATA_SOURCE_FORMAT_HASH_MAP.put(getCommand, this)
    true
  }

  /**
   *
   * @return 该插件的定位命令，如果在加载数据Type参数输入了 UDF:DT 那么代表进入到该组件的实现逻辑
   */
  override def getCommand: String = "DT"
}

```
- Switch to[English document](https://github.com/BeardedManZhao/EasterBunny/blob/main/Implementation%20of%20user-defined%20data%20reading%20component.md)
