package config.classLoad;

/**
 * <h3>中文</h3>
 * 初始化插件接口，该接口的实现类的jar，可以添加到插件目录中，这个jar会在EasterBunny启动的时候被调用，您可以在此定制属于您的初始化逻辑。
 * <h3>English</h3>
 * Initialize the plugin interface. The jar of the implementation class of this interface can be added to the plugin directory. This jar will be called when Easter Bunny starts. You can customize your initialization logic here.
 */
public interface Init_Plug_in {
    /**
     * @return 该插件的名称 the name of the plugin
     */
    String getName();

    /**
     * 插件的初始化方法 Plugin initialization method
     *
     * @return 初始化之后，是否允许继续运行，true的话就不会干程序继续执行
     * <p>
     * After initialization, is it allowed to continue running, if true, the program will not continue to execute.
     */
    boolean run();
}
