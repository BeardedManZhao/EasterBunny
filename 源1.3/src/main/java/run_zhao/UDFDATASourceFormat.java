package run_zhao;

import config.classLoad.Init_Plug_in;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;

public interface UDFDATASourceFormat extends Init_Plug_in {
    /**
     * 数据加载组件列表，本集合会被外界的视图管理器获取并进行类组件的提取
     * <p>
     * 请注意 当两个类的命令标识一样的时候，将会取最新版本的类
     */
    HashMap<String, UDFDATASourceFormat> UDFDATA_SOURCE_FORMAT_HASH_MAP = new HashMap<>();

    /**
     * @return 自定义数据加载组件的命令标识，用来识别组件的，因此请不要重复
     */
    String getCommand();

    /**
     * @param sparkSession Spark数据加载工具，通过此可以转换出需要的DataSet
     * @param args         加载数据组件需要的参数
     * @return 数据是否有加载成功
     */
    RDD<Row> run(SparkSession sparkSession, String[] args);
}
