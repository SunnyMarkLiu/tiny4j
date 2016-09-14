package com.markliu.tiny4j.config;

/**
 * 保留配置文件的相关配置项常量
 * author:sunnymarkliu
 * date  :16-9-14
 * time  :下午8:09
 */
public final class ConfigConstant {

    /** classpath 路径下的配置文件 */
    public static final String CONFIG_FILE = "tiny4j.properties";

    /** 配置文件中链接数据库的相关 key */
    public static final String JDBC_DRIVER = "tiny4j.jdbc.driver";
    public static final String JDBC_URL = "tiny4j.jdbc.url";
    public static final String JDBC_USERNAME = "tiny4j.jdbc.username";
    public static final String JDBC_PASSWORD = "tiny4j.jdbc.password";

    /** 项目的基础包名 */
    public static final String APP_BASE_PACKAGE = "tiny4j.app.base_package";
    /** jsp 的基础路径 */
    public static final String APP_JSP_PATH = "tiny4j.app.jsp_path";
    /** 静态资源文件的基础路径 */
    public static final String APP_ASSET_PATH = "tiny4j.app.asset_path";
}
