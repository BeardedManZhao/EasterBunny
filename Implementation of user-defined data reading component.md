# Implementation of user-defined data reading component（hang in the air）
- 切换至[中文文档]()
- Introduction to Custom Data Reading Components
The user-defined component needs to implement the interface "run_zhao. UDFDATASourceFormat", which is inherited from the initialization plug-in interface "Init_Plug_in", that is, your user-defined component can be added to EasterBunny in the following ways.

  *1 Add your data source components to the customized data source component set of "EasterBunny" through the methods provided by the "Init_Plug_in" interface.

  *2 Through the "UDFDATASourceFormat" interface, the location command of the component and the processing logic of the command are realized.
