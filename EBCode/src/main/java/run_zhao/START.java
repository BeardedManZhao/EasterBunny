package run_zhao;

import config.classLoad.RunInitClass;

/**
 * 第一场启动
 */
public class START {
    public static void main(String[] args) {
        if (loadStart("run_zhao.udf.INIT_C")) {
            MAIN_zhao.main(args);
        } else {
            System.err.println("* >>> START一层缓存，启动失败。");
        }
    }

    /**
     * 加载所有的DUF数据解析插件, 便于后期的处理
     *
     * @param args 命令参数 索引为4的参数将会被视为是DUF数据加载类插件的集合，需要使用逗号分割哦
     */
    public static boolean loadClassUDF(String[] args) {
        String[] strings = args[3].split(",+");
        RunInitClass.R r = new RunInitClass.R("lib/plug_in");
        return r.runAllClass(strings);
    }

    /**
     * 一级加载 不会记录run
     *
     * @param arg 一级必需类
     * @return 加载情况
     */
    private static boolean loadStart(String arg) {
        RunInitClass.R r = new RunInitClass.R("lib/plug_in");
        boolean b = r.runAllClass(new String[]{arg});
        r.dropClass("Easter_Bunny插件初始化启动器");
        return b;
    }
}
