# Implementation of user-defined data reading component（hang in the air）
- 切换至[中文文档]()
- Introduction to Custom Data Reading Components
The user-defined component needs to implement the interface "run_zhao. UDFDATASourceFormat", which is inherited from the initialization plug-in interface "Init_Plug_in", that is, your user-defined component can be added to EasterBunny in the following ways.
![image](https://user-images.githubusercontent.com/113756063/192914662-01ce0d39-5ee0-404a-9f97-b8b25bd8dcd4.png)

Here we will take the "DataTear" data source plug-in in "EasterBunny" as an example to introduce how to customize a data source loading plug-in.As you can see, the selected component in the following figure is a user-defined data source component, which is implemented using the DataTear framework. For information about DataTear, see:[dataTear](https://github.com/BeardedManZhao/dataTear)

  *1 Add your data source components to the customized data source component set of "EasterBunny" through the methods provided by the "Init_Plug_in" interface.
   
   Here, we implement the name of the plug-in in "getName", and add our plug-in to the data source component collection in "run(): Boolean".
  ![image](https://user-images.githubusercontent.com/113756063/192914398-c0d1d409-2776-4f82-810c-bff93c1aa8d8.png)
  
  *2 Through the "UDFDATASourceFormat" interface, the location command of the component and the processing logic of the command are realized.
  
  A call command for this component is provided in "". "EasterBunny" will find the component you specified through your data loading mode command and execute its logic. Its command processing logic is implemented in the "run (sparkSession: SparkSession, strings: Array [String]): RDD [Row]" method.
  ![image](https://user-images.githubusercontent.com/113756063/192914783-bc9c8d8b-4825-43c2-b1e0-a2cde911ad9a.png)

