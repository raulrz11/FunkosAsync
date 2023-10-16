package services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import models.Funkos;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String serverUrl;
    private String dataBaseName;
    private boolean chargeInit;
    private String conURL;
    private String initScript;


    private DatabaseManager(){
        try {
            configFromProperties();
            openConnection();
            if (chargeInit){
                executeScript(initScript,true);
                insertarDatos();
            }
            System.out.println("Successfully");
        }catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static DatabaseManager getInstance(){
        if (instance==null){
            instance=new DatabaseManager();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            try{
                openConnection();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return connection;
    }

    private synchronized void openConnection() throws SQLException{
        connection = DriverManager.getConnection(conURL);
    }

    public synchronized void closeConnection() throws SQLException{
        if (preparedStatement != null){ preparedStatement.close();}
        connection.close();
    }

    private synchronized void configFromProperties(){
        try{
            Properties properties = new Properties();
            properties.load(DatabaseManager.class.getClassLoader().getResourceAsStream("database.properties"));

            dataBaseName = properties.getProperty("database.name","Funkos");
            chargeInit =Boolean.parseBoolean(properties.getProperty("database.initTables","false"));
            conURL =properties.getProperty("database.connectionUrl", "jdbc:sqlite:Funkos.db");
            initScript=properties.getProperty("database.initScript","init.sql");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public synchronized void executeScript(String script, boolean logWriter) throws IOException, SQLException {
        ScriptRunner runner = new ScriptRunner(connection);
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(script);
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            runner.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
            runner.runScript(reader);
            closeConnection();
        } else {
            throw new FileNotFoundException("Script not found: " + script);
        }
    }

    /*private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private HikariDataSource dataSource;
    private String driver;
    private String name;
    private String port;
    private String connUrl = "jdbc:sqlite:Funkos.db";
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
            var properties = new Properties();
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
            insertarDatos();
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

    public void insertarDatos(){
        List<Funkos> funkos = new ArrayList<>();
        String csvFile = Paths.get("").toAbsolutePath() + File.separator + "data" + File.separator + "funkos.csv";
        String query = "INSERT INTO FUNKOS(uuid, nombre, modelo, precio, fecha_lanzamiento) VALUES (?, ?, ?, ?, ?)";
        String line;

        try(BufferedReader br = new BufferedReader(new FileReader(csvFile));
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(query)){
            br.readLine();
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                Funkos funko = new Funkos(
                        UUID.fromString(data[0].substring(0,35)),
                        data[1],
                        Funkos.Modelo.valueOf(data[2]),
                        Double.parseDouble(data[3]),
                        Date.valueOf(data[4]).toLocalDate());
                funkos.add(funko);
            }

            for(Funkos funko : funkos){
                pstm.setString(1, funko.getCod().toString());
                pstm.setString(2, funko.getNombre());
                pstm.setString(3, funko.getModelo().toString());
                pstm.setBigDecimal(4, BigDecimal.valueOf(funko.getPrecio()));
                pstm.setDate(5, Date.valueOf(funko.getFechaLanzamiento().toString()));
                pstm.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }*/
    public void insertarDatos(){
        List<Funkos> funkos = new ArrayList<>();
        String csvFile = Paths.get("").toAbsolutePath() + File.separator + "data" + File.separator + "funkos.csv";
        String query = "INSERT INTO FUNKOS(uuid, nombre, modelo, precio, fecha_lanzamiento) VALUES (?, ?, ?, ?, ?)";
        String line;

        try(BufferedReader br = new BufferedReader(new FileReader(csvFile));
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(query)){
            br.readLine();
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                Funkos funko = new Funkos(
                        UUID.fromString(data[0].substring(0,35)),
                        data[1],
                        Funkos.Modelo.valueOf(data[2]),
                        Double.parseDouble(data[3]),
                        Date.valueOf(data[4]).toLocalDate());
                funkos.add(funko);
            }

            for(Funkos funko : funkos){
                pstm.setString(1, funko.getCod().toString());
                pstm.setString(2, funko.getNombre());
                pstm.setString(3, funko.getModelo().toString());
                pstm.setBigDecimal(4, BigDecimal.valueOf(funko.getPrecio()));
                pstm.setDate(5, Date.valueOf(funko.getFechaLanzamiento().toString()));
                pstm.executeUpdate();
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
