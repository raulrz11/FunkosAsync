package services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private HikariDataSource dataSource;
    private String driver;
    private String name;
    private String port;
    private String connUrl;
    private boolean initTables = false;
    private String initScript = "init.sql";
    Connection conn;

    private DatabaseManager() {
        loadProperties();

        HikariConfig config= new HikariConfig();
        config.setJdbcUrl(connUrl);
        dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection()){
            if (initTables){
                initTables(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized DatabaseManager getInstance(){
        if (instance == null){
            instance = new DatabaseManager();
        }
        return  instance;
    }

    private synchronized void loadProperties(){
        try {
            var file = ClassLoader.getSystemResource("database.properties").getFile();
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            connUrl = properties.getProperty("database.connectionUrl", "jdbc:sqlite:Funkos.db");
            driver = properties.getProperty("database.driver", "org.sqlite:JDBC");
            name = properties.getProperty("database.name", "Funkos");
            port = properties.getProperty("database.port", "3306");
            initTables = Boolean.parseBoolean(properties.getProperty("database.initTables", "false"));
            initScript = properties.getProperty("database.initScript", "init.sql");
            logger.debug("Propiedades cargadas");

        }catch (IOException e){
            e.getMessage();
        }
    }

    private synchronized void initTables(Connection conn){
        logger.debug("Inicializando tablas");
        try {
            executeScript(conn, initScript, true);
        }catch (FileNotFoundException e){
            e.getMessage();
        }
    }

    private synchronized void executeScript(Connection conn, String scriptFile, boolean logWriter) throws FileNotFoundException {
        logger.debug("Ejecutando script");
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResource(scriptFile).getFile();
        Reader reader = new BufferedReader(new FileReader((file)));
        sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
        sr.runScript(reader);

    }

    public synchronized Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

}
