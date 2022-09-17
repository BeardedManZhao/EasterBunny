package config.classLoad;

/**
 * 初始化插件接口
 */
public interface Init_Plug_in {
    /**
     * @return 该插件的名称
     */
    String getName();

    /**
     * 插件的初始化方法
     *
     * @return 初始化之后，是否允许继续运行，true的话就不会干程序继续执行
     */
    boolean run();
}
