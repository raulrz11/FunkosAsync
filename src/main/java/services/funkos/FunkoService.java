package services.funkos;

import exceptions.FunkoNotFoundException;
import exceptions.FunkoNotSaveException;
import models.Funkos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface FunkoService {
    CompletableFuture<Funkos> save(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotSaveException;
    CompletableFuture<Funkos> update(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotFoundException;
    CompletableFuture<List<Funkos>> findByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException;
    CompletableFuture<List<Funkos>> findAll() throws SQLException, ExecutionException, InterruptedException;
    CompletableFuture<Optional<Funkos>> findById(int id) throws SQLException, ExecutionException, InterruptedException;
    CompletableFuture<Void> deleteAll() throws SQLException, ExecutionException, InterruptedException;
    CompletableFuture<Boolean> deleteById(int id) throws SQLException, ExecutionException, InterruptedException;
}
