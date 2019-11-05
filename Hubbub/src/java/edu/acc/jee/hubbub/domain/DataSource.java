package edu.acc.jee.hubbub.domain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private final HikariConfig config = new HikariConfig();
    private final HikariDataSource ds;
    
    public DataSource(String jdbcUrl, String username, String password){
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        ds = new HikariDataSource(config);    
    }
    
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
}