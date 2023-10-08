package repositorys;

import models.Funkos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CrudRepo<T, ID> {
    CompletableFuture<Funkos> save(T t) throws SQLException;
    CompletableFuture<Funkos> update(T t) throws SQLException;
    CompletableFuture<List<Funkos>> findAll() throws SQLException;
    CompletableFuture<Optional<Funkos>> findById(ID id) throws SQLException;
    CompletableFuture<Void> deleteAll() throws SQLException;
    CompletableFuture<Boolean> deleteById(ID id) throws SQLException;





}
