# EasterBunny initialization plug-in development
- 切换到 [中文文档]()
- Introduction to Initialization Plug ins
EasterBunny An interface named "a" is provided,This interface is used to customize a plug-in that can participate in the initialization of EasterBunny.
The initialization plug-in will be reflected in the jvm after the EasterBunny L1 cache is started.
You can achieve various initialization requirements through this operation, such as registering drivers, registering custom components, and so on.

- Initialize plug-in interface
The following is a screenshot of the plug-in interface. Two methods are provided here, "String getName();" And "boolean run ();", Please refer to the following document for the explanation of these two methods.
image-初始化插件接口截图

- Initialization plug-in development example
We hope to print some data during initialization without changing the code. This requirement will be developed using our initialization plug-in!
For specific development, please refer to the following source code.
```

```

- Load the initialization plug-in to EasterBunny
First, we print the initialization plug-in into a jar. Please note that you need to write down the main class path!
image-打jar包
Next, we will move the plug-in jar to the plug-in directory "lib/plug_in", as shown in the following figure!
image-移动jar到插件目录


- Start EasterBunny
Click the script file in the following figure to start EasterBunny. At this time, there will be a prompt. You just need to follow the prompt step by step. It is worth noting that you need to paste the main class path of the initialization plug-in just now, as shown in the following figure.
image-启动时候指定主类的截图
Next, we can see the data printed by our plug-in!!
image-插件打印的数据截图
- 切换到 [中文文档]()
