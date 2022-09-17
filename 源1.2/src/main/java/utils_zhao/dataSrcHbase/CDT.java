package utils_zhao.dataSrcHbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Liming
 * @version 2.0
 * @ TODO: 2022/4/11 来自HBASE程序中的一个辅助模块，在Spark对接HBASE中，我们选择了再次启用
 * @ TODO: 2022/4/17 来自HBASE程序中的一个辅助模块，在SparkSQL中，我们将其停用
 */
@Deprecated
public class CDT {
    public Configuration configuration;
    public Connection connection;
    public Admin admin;

    /**
     * todo 将参数初始化 这里改造成了类构造器
     *     1.	使用HbaseConfiguration.create()创建Hbase配置
     *     2.	使用ConnectionFactory.createConnection()创建Hbase连接
     *     3.	要创建表，需要基于Hbase连接获取admin管理对象
     *
     * @param map 需要进行的配置参数
     */
    public CDT(HashMap<String, String> map) throws IOException {
        Iterator<String> K = map.keySet().iterator();
        configuration = HBaseConfiguration.create();
        String KS;
        while (K.hasNext()) {
            KS = K.next();
            configuration.set(KS, map.get(KS));
        }
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();
    }

    /**
     * todo 新建表的方法
     *      1.	判断表是否存在  如果存在，则返回false
     *      2.	使用TableDescriptorBuilder.newBuilder构建表描述构建器
     *      3.	使用ColumnFamilyDescriptorBuilder.newBuilder构建列蔟描述构建器
     *      4.	通过列簇描述器 的 build()方法 构建 列簇描述
     *      5.  通过表描述器的setColumnFamily(列簇描述) 与 列簇 建立关联
     *      6.	这时候的表描述器 已经知道了列簇是谁 我们开始 创建表
     *      7.  创建表的方法 需要一个表描述 我们可以通过表描述构建器的 bulid 方法构建
     *
     * @param Table_CC 表名,列簇
     * @return 是否成功创建表  true 创建完成       false 创建失败，由于表已存在
     */
    public boolean create_table(String Table_CC) {
        String[] table = Table_CC.split(",");
        try {
            TableName tableName = TableName.valueOf(table[0]);
            if (!admin.tableExists(tableName)) {// todo 下面就是确定可以创建表， 真正的代码了 具体流程就和上面的注释一样
                /* 2 创建表描述的构建器对象  */
                TableDescriptorBuilder table_Bulid = TableDescriptorBuilder.newBuilder(tableName);
                /* 3 创建列簇描述的构建器对象 */
                ColumnFamilyDescriptorBuilder C_Bulid = ColumnFamilyDescriptorBuilder.newBuilder(table[1].getBytes(StandardCharsets.UTF_8));
                /* 4 构建列簇描述  我们的表描述需要与列簇描述建立关联*/
                ColumnFamilyDescriptor C = C_Bulid.build();
                /* 5 建立关联  将列簇描述 传入表描述构建器 这样在构建表描述的时候 就知道使用列簇的信息了*/
                table_Bulid.setColumnFamily(C);
                /* 6 & 7 使用管理对象创建表  这里需要的参数是 表描述，我们需要使用表构建器将表描述构建出来*/
                admin.createTable(table_Bulid.build());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * todo 删除HBASE上的表
     *      1.	判断表是否存在
     *      2.	如果存在，则禁用表
     *      3.	再删除表
     *
     * @param Table 需要删除的表
     * @return 是否成功删除表  true代表成功删除
     */
    public boolean drop_table(String Table) throws IOException {
        TableName tableName = TableName.valueOf(Table);
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * todo 关闭连接
     *     4.	 使用admin.close  connection.close  关闭连接
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
