package run_zhao;

import config.classLoad.Init_Plug_in;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;

/**
 * @author 赵
 * <h3>中文</h3>
 * <p>
 * 自定义数据源操作组件接口，该接口是"EasterBunny"的初始化插件，同时是"EasterBunny"的源操作组件的抽象
 * <p>
 * 1 您需要通过初始化接口将自己添加到 "UDFDATA_SOURCE_FORMAT_HASH_MAP" 集合中，这个集合会被"EasterBunny"查询，找到您的自定义组件
 * <p>
 * 2 您需要通过自定义数据源接口 "getCommand"方法 实现命令职责，用来被"EasterBunny"定位您需要的自定义数据源组件
 * <p>
 * 3 当"EasterBunny"知道您需要的自定义组件的时候，会将命令提供给该组件，因此，您需要在“RDD<Row> run(SparkSession sparkSession, String[] args)”中进行命令的处理逻辑实现
 * <h3>English</h3>
 * <p>
 * User-defined data source operation component interface, which is the initialization plug-in of "Easter Bunny" and an abstraction of the source operation component of "Easter Bunny".
 * <p>
 * 1 You need to add yourself to the "UDFDATA_SOURCE_FORMAT_HASH_MAP" collection through the initialization interface, this collection will be queried by "EasterBunny" to find your custom component.
 * <p>
 * 2 You need to implement the command responsibility through the "getCommand" method of the custom data source interface, which is used by "EasterBunny" to locate the custom data source component you need.
 * <p>
 * 3 When "EasterBunny" knows the custom component you need, it will provide the command to the component. Therefore, you need to perform the command processing logic in "RDD<Row> run(SparkSession sparkSession, String[] args)" accomplis.
 */
public interface UDFDATASourceFormat extends Init_Plug_in {
    /**
     * 数据加载组件列表，本集合会被外界的视图管理器获取并进行类组件的提取
     * <p>
     * 请注意 当两个类的命令标识一样的时候，将会取最新版本的类
     * <h3>English</h3>
     * List of data loading components, this set will be acquired by the external view manager and the class components will be extracted.
     * <p>
     * Please note that when the command identifiers of two classes are the same, the latest version of the class will be taken.
     */
    HashMap<String, UDFDATASourceFormat> UDFDATA_SOURCE_FORMAT_HASH_MAP = new HashMap<>();

    /**
     * @return 自定义数据加载组件的命令标识，用来识别组件的，因此请不要重复
     * <p>
     * The command identifier of the custom data loading component is used to identify the component, so please do not repeat.
     */
    String getCommand();

    /**
     * 该数据源组件的命令处理逻辑实现
     * <p>
     * The command processing logic implementation of the data source component.
     *
     * @param sparkSession 来自EasterBunny的sparkSession对象
     *                     <p>
     *                     spark Session object from Easter Bunny
     * @param args         <h3>中文</h3>命令参数，下面是有关该参数的详细解释
     *                     <p>
     *                     当您的EasterBunny会话中数据加载方式（Type）参数输入为 UDF:DT=cat>DT>hdfs://out/test.txt 的时候，您的这个参数如下所示
     *                     <p>
     *                     Array(cat, DT, hdfs://out/test.txt)，您可以根据这个命令处理规律，来实现您需要的命令处理方式
     *                     <h3>English</h3>
     *                     Command parameter, the following is a detailed explanation of the parameter
     *                     <p>
     *                     When the data loading method (Type) parameter input in your EasterBunny session is UDF:DT=cat>DT>hdfs://out/test.txt, your parameter is as follows
     *                     <p>
     *                     Array(cat, DT, hdfs://out/test.txt), you can implement the command processing method you need according to this command processing rule
     * @return 加载好的RDD[Row]数据集合，这个集合最终会由EasterBunny托管
     * <p>
     * Loaded RDD[Row] data collection, this collection will eventually be hosted by Easter Bunny.
     */
    RDD<Row> run(SparkSession sparkSession, String[] args);
}
