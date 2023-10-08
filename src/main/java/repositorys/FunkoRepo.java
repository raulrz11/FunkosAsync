package repositorys;

import models.Funkos;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FunkoRepo extends CrudRepo<Funkos, Integer>{
    CompletableFuture<List<Funkos>> findByNombre(String nombre) throws SQLException;
}
