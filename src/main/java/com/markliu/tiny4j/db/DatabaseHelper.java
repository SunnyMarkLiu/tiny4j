package com.markliu.tiny4j.db;

import com.markliu.tiny4j.config.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库操作类
 * <p>
 * author:sunnymarkliu
 * date  :16-10-5
 * time  :下午4:02
 */
public class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static ThreadLocal<Connection> connectionContainer = new ThreadLocal<Connection>();

    /**
     * 获取连接
     */
    private static Connection getConnection() {

        Connection connection = connectionContainer.get();
        if (connection == null) {
            String jdbDriver = ConfigHelper.getJdbDriver();
            try {
                Class.forName(jdbDriver);
                connection = DriverManager.getConnection(
                                        ConfigHelper.getJdbcUrl(),
                                        ConfigHelper.getJdbcUserName(),
                                        ConfigHelper.getJdbcPassword());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connectionContainer.set(connection);
            }
        }
        return connection;
    }

    /**
     * 关闭连接
     */
    private static void closeConnection() {
        Connection connection = connectionContainer.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionContainer.remove();
            }
        }
    }

    /**
     * 开启事务
     */
    public static void begainTransaction() {

        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection connection = getConnection();
        try {
            connection.commit();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事务回滚
     */
    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.rollback();
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
