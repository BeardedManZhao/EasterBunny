# ![image](https://user-images.githubusercontent.com/113756063/192142233-2e9a27be-bd96-4a4e-a536-e69480e1aa48.png) EasterBunny1.4
Big data environment data warehouse tool, you can directly use SQL to process big data in a distributed way. This is a Spark job, which has completely encapsulated the syntax for programming, and achieved the effect of full SQL processing!

- Required Dependencies

  JDK1.8  Hadoop Basic environment configuration
  As you can see, this is a Maven project. If you need to use this software, you can call:+86 17802240898

- Third party directory structure required by software

  conf: The configuration file storage directory, built-in templates, and EasterBunny itself also have the function of writing configuration modules. If your configuration file is deleted, this function will start

  lib：Dependent program storage path, including plug-ins and dependent jar packages

![image](https://user-images.githubusercontent.com/113756063/192142287-ebabb07d-00f3-4b80-9d99-42bc3ab444e5.png)

 - Startup interface
 
 ![image](https://user-images.githubusercontent.com/113756063/192140732-6e6456e6-d287-4eae-8fca-4d130ddaf7b4.png)
 
 - Robust grammar system
 
 ![image](https://user-images.githubusercontent.com/113756063/192140844-cea15645-82d3-439a-ba32-1d9e338d74dc.png)
 
 - Instructions for starting
 
 During startup, all plug-ins in "Easter_Bunny1.4 lib plug_in" will be loaded, which must include a digital verification plug-in (AppInit). The validity period of the plug-in is time limited. You can find the plug-in package through Releases, paste it into the plug-in directory mentioned above, and restart the program to successfully update the digital verification time!



 When you do not have the plug-in, the system will prompt the error log of "L1 cache failure" when starting up! Be sure to import the plug-in!
  
![image](https://user-images.githubusercontent.com/113756063/192140802-835e1d6a-b934-4745-8402-e6a0bd154ae2.png)
