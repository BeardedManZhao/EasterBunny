package run_zhao;

import java.util.regex.Pattern;

/**
 * 常量区数据存储类，EB框架所需要的常量数据都存储在此
 *
 * @author zhao
 */
public final class ConstantRegion {
    public final static String VERSION = "1.5";
    public final static Pattern INVISIBLE_ALL_PATTERN = Pattern.compile("\\s+");
    public final static Pattern WRAP_PATTERN = Pattern.compile("\n+");
    public final static Pattern EQ_PATTERN = Pattern.compile("=");
    public final static Pattern UDF_COMMAND_PATTERN = Pattern.compile(">+");
    public final static Pattern COLON_PATTERN = Pattern.compile(":");
    public final static Pattern ATTRIBUTE_SEGMENTATION = Pattern.compile("&");
    public final static Pattern QUESTION_MARK_PATTERN = Pattern.compile("\\?");
}
