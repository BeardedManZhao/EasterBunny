# EasterBunny 初始化插件文档
- Switch to [English document](https://github.com/BeardedManZhao/EasterBunny/blob/main/KnowledgeDocument/EasterBunny%20initialization%20plug-in%20development.md)
- 初始化插件介绍

EasterBunny提供了一个名为“config.classLoad.Init_Plug_in”的接口，该接口用于定制一个可以参与EasterBonny初始化的插件。

启动EasterBunny L1缓存后，初始化插件将反映在jvm中。

您可以通过此操作实现各种初始化要求，例如注册驱动程序、注册自定义组件等。

- 初始化插件接口

以下是插件界面的代码屏幕截图。这里提供了两个方法，“String getName（）；”和“boolean run（）；”，关于这两种方法的解释，请参阅以下文档。
![image](https://user-images.githubusercontent.com/113756063/193179859-ea8cf32a-dbe9-498e-9c3a-1eb357dcbe34.png)

- 初始化插件开发示例

我们希望在初始化期间打印一些数据，而不更改"EasterBunny"的代码。此需求将使用我们的初始化插件开发！

具体开发请参考以下源代码。。

请注意，您需要首先将下面这个包导入插件的项目依赖项中。
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

- 将初始化插件加载到EasterBunny

首先，我们将初始化插件打印到一个jar中。请注意，您需要写下主类路径！
![image](https://user-images.githubusercontent.com/113756063/193181071-36df14db-05ff-499f-a84a-e18340b26f9c.png)

接下来，我们将插件jar移动到插件目录“lib/plug_in”，如下图所示！
![image](https://user-images.githubusercontent.com/113756063/193181167-dffaabd5-23dc-464f-8843-91b526f63405.png)

- 启动 EasterBunny

单击下图中的脚本文件以启动EasterBunny。此时将出现提示。您只需要一步一步地按照提示操作。值得注意的是，您现在需要粘贴初始化插件的主类路径，如下图所示。
![image](https://user-images.githubusercontent.com/113756063/193182437-b6eff165-8832-40b6-9849-f59efaca9018.png)

接下来，我们可以看到插件打印的数据！！
![image](https://user-images.githubusercontent.com/113756063/193182506-6809551d-01fa-48b8-9be9-a499463f37b4.png)

- Switch to [English document](https://github.com/BeardedManZhao/EasterBunny/blob/main/KnowledgeDocument/EasterBunny%20initialization%20plug-in%20development.md)
