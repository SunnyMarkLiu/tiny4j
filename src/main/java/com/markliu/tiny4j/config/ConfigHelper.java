package com.markliu.tiny4j.config;

import com.markliu.tiny4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 读取配置文件
 * author:sunnymarkliu
 * date  :16-9-14
 * time  :下午8:47
 */
public class ConfigHelper {

    /** 加载配置文件所对应的 Properties */
    public static final Properties CONFIG_PROPERTIES =
            PropertiesUtil.loadProperties(ConfigConstant.CONFIG_FILE);

    /**
     * 获取 jdbc 驱动
     * @return jdbc 驱动所对应的全类名
     */
    public static String getJdbDriver() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取 jdbc 连接的url
     * @return jdbc url
     */
    public static String getJdbcUrl() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.JDBC_URL);
    }

    /**
     * 获取数据库连接的用户名
     */
    public static String getJdbcUserName() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.JDBC_USERNAME);
    }

    /**
     * 获取数据库连接的密码
     */
    public static String getJdbcPassword() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 项目的基础包名
     */
    public static String getAppBasePackage() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * jsp 的基础路径
     * 默认值为 /WEB-INF/view/
     */
    public static String getAppJspPath() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取静态资源文件的基础路径
     * 默认值为 /asset/
     */
    public static String getAppAssetPath() {
        return PropertiesUtil.getStringValue(CONFIG_PROPERTIES, ConfigConstant.APP_ASSET_PATH, "/asset/");
    }
}
