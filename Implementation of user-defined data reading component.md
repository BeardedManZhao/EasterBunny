# Implementation of user-defined data reading component（hang in the air）
- 切换至[中文文档]()
- Introduction to Custom Data Reading Components

The user-defined component needs to implement the interface "run_zhao. UDFDATASourceFormat", which is inherited from the initialization plug-in interface "Init_Plug_in", that is, your user-defined component can be added to EasterBunny in the following ways.
![image](https://user-images.githubusercontent.com/113756063/192914662-01ce0d39-5ee0-404a-9f97-b8b25bd8dcd4.png)

Here we will take the "DataTear" data source plug-in in "EasterBunny" as an example to introduce how to customize a data source loading plug-in.As you can see, the selected component in the following figure is a user-defined data source component, which is implemented using the DataTear framework. For information about DataTear, see:[dataTear](https://github.com/BeardedManZhao/dataTear)

  * 1 Add your data source components to the customized data source component set of "EasterBunny" through the methods provided by the "Init_Plug_in" interface.
   
   Here, we implement the name of the plug-in in "getName", and add our plug-in to the data source component collection in "run(): Boolean",Don't forget that you need to import EasterBunny's package before all this starts.
  ![3cead1f1301c9b9d8ad5387ec4b6e78](https://user-images.githubusercontent.com/113756063/192923964-40996af9-7d72-4e75-b8b6-833e6f308d1b.jpg)
  ![image](https://user-images.githubusercontent.com/113756063/192914398-c0d1d409-2776-4f82-810c-bff93c1aa8d8.png)
  
  * 2 Through the "UDFDATASourceFormat" interface, the location command of the component and the processing logic of the command are realized.
  
  A call command for this component is provided in "". "EasterBunny" will find the component you specified through your data loading mode command and execute its logic. Its command processing logic is implemented in the "run (sparkSession: SparkSession, strings: Array [String]): RDD [Row]" method.
  ![image](https://user-images.githubusercontent.com/113756063/192914783-bc9c8d8b-4825-43c2-b1e0-a2cde911ad9a.png)
- Sample source code for custom components

The following is the sample source code of a data component extracted from "EasterBunny" and implemented by Zhao. You can connect your components to "EasterBunny" in the following way.
   
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
