# Implementation of user-defined data reading component
- 切换至[中文文档](https://github.com/BeardedManZhao/EasterBunny/blob/main/Implementation%20of%20user-defined%20data%20reading%20component-Chinese.md)
- Introduction to Custom Data Reading Components

The user-defined component needs to implement the interface "run_zhao. UDFDATASourceFormat", which is inherited from the initialization plug-in interface "Init_Plug_in", that is, your user-defined component can be added to EasterBunny in the following ways.
![image](https://user-images.githubusercontent.com/113756063/192914662-01ce0d39-5ee0-404a-9f97-b8b25bd8dcd4.png)

Here we will take the "DataTear" data source plug-in in "EasterBunny" as an example to introduce how to customize a data source loading plug-in.As you can see, the selected component in the following figure is a user-defined data source component, which is implemented using the DataTear framework. For information about DataTear, see:[dataTear](https://github.com/BeardedManZhao/dataTear)

  * 1 Add your data source components to the customized data source component set of "EasterBunny" through the methods provided by the "Init_Plug_in" interface.
   
   Here, we implement the name of the plug-in in "getName", and add our plug-in to the data source component collection in "run(): Boolean",Don't forget that you need to import EasterBunny's package before all this starts.
  ![3cead1f1301c9b9d8ad5387ec4b6e78](https://user-images.githubusercontent.com/113756063/192923964-40996af9-7d72-4e75-b8b6-833e6f308d1b.jpg)
  ![image](https://user-images.githubusercontent.com/113756063/192914398-c0d1d409-2776-4f82-810c-bff93c1aa8d8.png)
  
  * 2 Through the "UDFDATASourceFormat" interface, the location command of the component and the processing logic of the command are realized.
  
  "GetCommand: String" provides the calling command of this component. "EasterBunny" will obtain commands through this method to match your loading mode, find the target component and execute its logic. Its command processing logic is implemented in the "run (sparkSession: SparkSession, strings: Array [String]): RDD [Row]" method.
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
 * DataTear data reading component
 *
 * This component can be loaded into EasterBunny to enable EasterBunny to read data through DataTear
 */
class MAIN extends UDFDATASourceFormat {
  // Get the object of the data reading component of a DataTear
  private val cat = new ZHAOCat()

  /**
   * Command processing logic implementation of the data source component
   *
   * @param sparkSession SparkSession object from EasterBunny
   * @param strings      Command parameter, the first parameter is cat
   *                     <br>
   *                     When the data loading method (Type) parameter input in your EasterBunny session is "UDF: DT=cat>DT> hdfs://out/test.txt "Your parameter is as follows
   *                     <br>
   *                     "Array(cat, DT, hdfs://out/test.txt ）", you can implement the command processing method you need according to this command processing rule
   * @return The loaded RDD [Row] data set will eventually be hosted by EasterBunny
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
   * @return The name of the plug-in (as the initialization plug-in)
   */
  override def getName: String = "DataTear数据读取插件"

  /**
   * The plug-in initialization logic is generally to add itself to the custom source operation component collection here
   * @return The plug-in was initialized successfully or unsuccessfully
   */
  override def run(): Boolean = {
    UDFDATASourceFormat.UDFDATA_SOURCE_FORMAT_HASH_MAP.put(getCommand, this)
    true
  }

  /**
   *
   * @return The location command of the plug-in. If "UDF: DT" is entered in the Loading Data Type parameter, it represents the implementation logic of entering the component
   */
  override def getCommand: String = "DT"
}

```
- 切换至[中文文档](https://github.com/BeardedManZhao/EasterBunny/blob/main/Implementation%20of%20user-defined%20data%20reading%20component-Chinese.md)
