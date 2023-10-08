package services.funkos;

import exceptions.FunkoNotFoundException;
import exceptions.FunkoNotSaveException;
import models.Funkos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface FunkoService {
    //METER LAS EXCEPCIONES CREADAS POR MI
    Funkos save(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotSaveException;
    Funkos update(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotFoundException;
    List<Funkos> findByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException;
    List<Funkos> findAll() throws SQLException, ExecutionException, InterruptedException;
    Optional<Funkos> findById(int id) throws SQLException, ExecutionException, InterruptedException;
    void deleteAll() throws SQLException, ExecutionException, InterruptedException;
    boolean deleteById(int id) throws SQLException, ExecutionException, InterruptedException;
}
