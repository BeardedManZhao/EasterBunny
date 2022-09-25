# ![image](https://user-images.githubusercontent.com/113756063/192141174-ef9e8139-b4db-48e6-a1d3-cf712c8fe58c.png) EasterBunny1.4
大数据环境数仓工具，相较于Spark，它能够让使用者的使用更加的方便

- 所需依赖

  JDK1.8  Hadoop基本的环境配置
  如您所见，这是一个maven的工程，如果您有需要使用本软件可致电：+86 17802240898

- 软件所需第三方目录结构

  conf: 配置文件存储目录，内置模板，EasterBunny本身也具有配置模块撰写的功能，如果您的配置文件被删除的话，这个功能将会启动

  lib： 依赖程序存储路径，其中包含插件与依赖jar包

 - 启动界面
 
 ![image](https://user-images.githubusercontent.com/113756063/192140732-6e6456e6-d287-4eae-8fca-4d130ddaf7b4.png)
 
 - 健壮的语法体系
 
 ![image](https://user-images.githubusercontent.com/113756063/192140844-cea15645-82d3-439a-ba32-1d9e338d74dc.png)
 
 - 启动须知
 
  在启动的时候会去加载 “Easter_Bunny1.4\lib\plug_in” 中的所有插件，其中必须包含一个数字验证插件（AppInit），该插件的有效期是有时间限制的，您可以通过 Releases 找到这个插件包，将其粘贴到上面所说的插件目录，然后重启本程序，即可成功更新数字验证时间！
  
  当您没有该插件的时候，系统启动会提示 “一级缓存失败” 的错误日志！一定要将插件导入进去哦！
  
![image](https://user-images.githubusercontent.com/113756063/192140802-835e1d6a-b934-4745-8402-e6a0bd154ae2.png)
