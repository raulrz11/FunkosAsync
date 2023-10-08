package repositorys;

import exceptions.FunkoNotFoundException;
import exceptions.FunkoNotSaveException;
import models.Funkos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.DatabaseManager;
import utils.IdGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FunkoRepoImpl implements FunkoRepo{
    private static FunkoRepoImpl instance;
    private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager db;
    private static IdGenerator myId;
    private FunkoRepoImpl(DatabaseManager db, IdGenerator id){
        this.db = db;
        this.myId = id;
    }

    public synchronized static FunkoRepoImpl getInstance(DatabaseManager db, IdGenerator id){
        if (instance == null){
           instance = new FunkoRepoImpl(db, id);
        }
        return instance;
    }
    @Override
    public CompletableFuture<Funkos> save(Funkos funkos) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO FUNKOS (uuid, myId, nombre, modelo, precio, fecha_lanzamiento, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (var conn = db.getConnection();
            var stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
                logger.debug("Guardando nuevo Funko");
                stmt.setObject(1, funkos.getCod());
                stmt.setInt(2, myId.getMyId());
                stmt.setString(3, funkos.getNombre());
                stmt.setString(4, funkos.getModelo().toString());
                stmt.setDouble(5, funkos.getPrecio());
                stmt.setDate(6, Date.valueOf(funkos.getFechaLanzamiento()));
                stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                var res = stmt.executeUpdate();

                if (res > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    while (rs.next()){
                        funkos.setId(rs.getInt(1));
                    }
                    rs.close();
                    logger.debug("Funko guardado");
                }else {
                    throw new FunkoNotSaveException("El Funko " + funkos.getNombre() + " no se ha guardado correctamennte");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return funkos;
        });

    }

    @Override
    public CompletableFuture<Funkos> update(Funkos funkos) throws SQLException {
        return CompletableFuture.supplyAsync(() ->{
            String query = "UPDATE FUNKOS SET nombre = ?, modelo = ?, precio = ?, updated_at = ? WHERE id = ?";
            try(var conn = db.getConnection();
            var stmt = conn.prepareStatement(query)){
                logger.debug("Actualizando Funko");
                stmt.setString(1, funkos.getNombre());
                stmt.setString(2, funkos.getModelo().toString());
                stmt.setDouble(3, funkos.getPrecio());
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setInt(5, funkos.getId());
                var res = stmt.executeUpdate();

                if (res > 0){
                    logger.debug("Funko actualizado");
                }else {
                    throw new FunkoNotFoundException("El Funko con ID " + funkos.getId() + " no se ha encontrado en la BD");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return funkos;
        });
    }

    @Override
    public CompletableFuture<List<Funkos>> findAll() throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            var query = "SELECT * FROM FUNKOS";
            List<Funkos> listaFunkos = new ArrayList<>();
            try(var conn = db.getConnection();
            var stmt = conn.prepareStatement(query)){

                logger.debug("Obteniendo todos los Funkos");
                var res = stmt.executeQuery();
                while (res.next()){
                    Funkos funkos = new Funkos();
                    funkos.setId(res.getInt("id"));
                    funkos.setCod(res.getObject("uuid", UUID.class));
                    funkos.setNombre(res.getString("name"));
                    funkos.setModelo(Funkos.Modelo.valueOf(res.getString("modelo")));
                    funkos.setPrecio(res.getDouble("precio"));
                    funkos.setFechaLanzamiento(res.getDate("fecha_lanzamiento").toLocalDate());
                    listaFunkos.add(funkos);
                }
                logger.debug("Funkos obtenidos");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return listaFunkos;
        });
    }

    @Override
    public CompletableFuture<Optional<Funkos>> findById(Integer id) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            var query = "SELECT * FROM FUNKOS";
            Optional<Funkos> funko = Optional.empty();
            try(var conn = db.getConnection();
                var stmt = conn.prepareStatement(query)){

                logger.debug("Obteniendo Funko con ID: " + id);
                stmt.setInt(1, id);
                var res = stmt.executeQuery();
                while (res.next()){
                    Funkos funkos = new Funkos();
                    funkos.setId(res.getInt("id"));
                    funkos.setCod(res.getObject("uuid", UUID.class));
                    funkos.setNombre(res.getString("name"));
                    funkos.setModelo(Funkos.Modelo.valueOf(res.getString("modelo")));
                    funkos.setPrecio(res.getDouble("precio"));
                    funkos.setFechaLanzamiento(res.getDate("fecha_lanzamiento").toLocalDate());
                    funko = Optional.of(funkos);
                }
                logger.debug("Funkos obtenido");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return funko;
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() throws SQLException {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM FUNKOS";
            try(var conn = db.getConnection();
            var stmt =  conn.prepareStatement(query)){

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Integer id) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            String query = "DELETE FROM FUNKOS WHERE id = ?";
            try (var conn = db.getConnection();
            var stmt = conn.prepareStatement(query)){

                stmt.setInt(1, id);
                var res = stmt.executeUpdate();
                return res > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Funkos>> findByNombre(String nombre) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            var query = "SELECT * FROM FUNKOS WHERE nombre LIKE ?";
            List<Funkos> listaFunkos = new ArrayList<>();
            try(var conn = db.getConnection();
                var stmt = conn.prepareStatement(query)){

                logger.debug("Obteniendo todos los Funkos");
                stmt.setString(1, "%" + nombre + "%");
                var res = stmt.executeQuery();
                while (res.next()){
                    Funkos funkos = new Funkos();
                    funkos.setId(res.getInt("id"));
                    funkos.setCod(res.getObject("uuid", UUID.class));
                    funkos.setNombre(res.getString("name"));
                    funkos.setModelo(Funkos.Modelo.valueOf(res.getString("modelo")));
                    funkos.setPrecio(res.getDouble("precio"));
                    funkos.setFechaLanzamiento(res.getDate("fecha_lanzamiento").toLocalDate());
                    listaFunkos.add(funkos);
                }
                logger.debug("Funkos obtenidos");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return listaFunkos;
        });
    }
}
