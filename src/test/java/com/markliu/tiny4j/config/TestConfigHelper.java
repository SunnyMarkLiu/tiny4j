package com.markliu.tiny4j.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * author:sunnymarkliu
 * date  :16-9-14
 * time  :下午9:10
 */
public class TestConfigHelper {

    @Test
    public void testConfigHelper() {
        Assert.assertEquals(ConfigHelper.getJdbDriver(), "com.mysql.jdbc.Driver");
        Assert.assertEquals(ConfigHelper.getJdbcUrl(), "jdbc:mysql://127.0.0.1:3306/tiny4jdemo?useUnicode=true&characterEncoding=utf-8");
        Assert.assertEquals(ConfigHelper.getJdbcUserName(), "root");
        Assert.assertEquals(ConfigHelper.getJdbcPassword(), "992101");
        Assert.assertEquals(ConfigHelper.getAppBasePackage(), "com.markliu.tiny4jdemo");
        Assert.assertEquals(ConfigHelper.getAppJspPath(), "/WEB-INF/view/");
        Assert.assertEquals(ConfigHelper.getAppAssetPath(), "/asset/");
    }
}
