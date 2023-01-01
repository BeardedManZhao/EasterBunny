package config.classLoad;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 加载同时初始化类方法
 */
public class RunInitClass {

    public static class R extends LoadClass {
//        Logger log = LoggerFactory.getLogger("插件初始化器");

        /**
         * 通过插件目录构建初始化运行器
         *
         * @param jarPath 插件路径（目录）
         */
        public R(String jarPath) {
            super(jarPath);
        }

        /**
         * 运行所有的插件初始化方法
         *
         * @param classPath 所有插件的路径(包路径)
         * @return 是否初始化成功，初始化失败将会强制终止该方法的执行，同时返回false
         */
        public boolean runAllClass(String[] classPath) {
            try {
                main(classPath);
                for (Init_Plug_in plug_in : LoadClass.objectArrayList) {
                    System.out.println("* >>> 加载插件：" + plug_in.getName());
                    if (!plug_in.run()) return false;
                }
                return true;
            } catch (Exception e) {
                System.err.println("* >？> 发生错误：" + e);
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 撤销一个类run方法的运行
         *
         * @param classNane 类的名称
         */
        public void dropClass(String classNane) {
            AtomicReference<Init_Plug_in> DC = new AtomicReference<>();
            LoadClass.objectArrayList.forEach(CLASS -> {
                if (classNane.equals(CLASS.getName())) {
                    DC.set(CLASS);
                }
            });
            LoadClass.objectArrayList.remove(DC.get());
        }
    }
}
