# EasterBunny initialization plug-in development
- 切换到 [中文文档](https://github.com/BeardedManZhao/EasterBunny/blob/main/KnowledgeDocument/EasterBunny%20initialization%20plug-in%20development-Chinese.md)
- Introduction to Initialization Plug ins

EasterBunny An interface named "config.classLoad.Init_Plug_in" is provided,This interface is used to customize a plug-in that can participate in the initialization of EasterBunny.
The initialization plug-in will be reflected in the jvm after the EasterBunny L1 cache is started.
You can achieve various initialization requirements through this operation, such as registering drivers, registering custom components, and so on.

- Initialize plug-in interface

The following is a screenshot of the plug-in interface. Two methods are provided here, "String getName();" And "boolean run ();", Please refer to the following document for the explanation of these two methods.
![image](https://user-images.githubusercontent.com/113756063/193179859-ea8cf32a-dbe9-498e-9c3a-1eb357dcbe34.png)

- Initialization plug-in development example

We hope to print some data during initialization without changing the code. This requirement will be developed using our initialization plug-in!
For specific development, please refer to the following source code.

Please note that you need to import the following package into the plug-in's project dependency first.
![image](https://user-images.githubusercontent.com/113756063/193180429-52505d56-e82a-4160-b6f0-6c932e9cc58a.png)
![image](https://user-images.githubusercontent.com/113756063/193180586-897f5879-e31e-422f-b473-5756913b248c.png)

```
package com.zhao.eb.run

import config.classLoad.Init_Plug_in

/**
 * EasterBunny的自定义初始化插件实现 Implementation of EasterBunny's custom initialization plug-in.
 */
class MyInit_Plug_in extends Init_Plug_in{
  override def getName: String = "Customized initialization plug-in"

  override def run(): Boolean = {
    System.out.println("* >>> The customized initialization plug-in is running!!!!")
    true
  }
}
```

- Load the initialization plug-in to EasterBunny

First, we print the initialization plug-in into a jar. Please note that you need to write down the main class path!
![image](https://user-images.githubusercontent.com/113756063/193181071-36df14db-05ff-499f-a84a-e18340b26f9c.png)

Next, we will move the plug-in jar to the plug-in directory "lib/plug_in", as shown in the following figure!
![image](https://user-images.githubusercontent.com/113756063/193181167-dffaabd5-23dc-464f-8843-91b526f63405.png)

- Start EasterBunny

Click the script file in the following figure to start EasterBunny. At this time, there will be a prompt. You just need to follow the prompt step by step. It is worth noting that you need to paste the main class path of the initialization plug-in just now, as shown in the following figure.
![image](https://user-images.githubusercontent.com/113756063/193182437-b6eff165-8832-40b6-9849-f59efaca9018.png)

Next, we can see the data printed by our plug-in!!
![image](https://user-images.githubusercontent.com/113756063/193182506-6809551d-01fa-48b8-9be9-a499463f37b4.png)

- 切换到 [中文文档](https://github.com/BeardedManZhao/EasterBunny/blob/main/KnowledgeDocument/EasterBunny%20initialization%20plug-in%20development-Chinese.md)
