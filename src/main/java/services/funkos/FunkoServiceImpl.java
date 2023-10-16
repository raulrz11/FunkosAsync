package services.funkos;

import exceptions.FunkoNotFoundException;
import exceptions.FunkoNotSaveException;
import models.Funkos;
import repositorys.FunkoRepo;
import services.cache.FunkoCache;
import services.cache.FunkoCacheImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FunkoServiceImpl implements FunkoService{
    private final int cacheSize = 10;
    private static FunkoServiceImpl instance;
    private FunkoCache cache;
    private FunkoRepo funkoRepo;

    private FunkoServiceImpl(FunkoRepo funkoRepo) {
        this.funkoRepo = funkoRepo;
        this.cache = new FunkoCacheImpl(cacheSize);
    }
    public synchronized static FunkoServiceImpl getInstance(FunkoRepo funkoRepo){
        if (instance == null){
            instance = new FunkoServiceImpl(funkoRepo);
        }
        return instance;
    }

    @Override
    public CompletableFuture<Funkos> save(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotSaveException {
        funko = funkoRepo.save(funko).get();
        cache.put(funko.getId(), funko);
        Funkos finalFunko = funko;
        return CompletableFuture.supplyAsync(() -> finalFunko);
    }

    @Override
    public CompletableFuture<Funkos> update(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotFoundException {
        funko = funkoRepo.update(funko).get();
        cache.put(funko.getId(), funko);
        Funkos finalFunko = funko;
        return CompletableFuture.supplyAsync(() -> finalFunko);
    }

    @Override
    public CompletableFuture<List<Funkos>> findByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return funkoRepo.findByNombre(nombre).get();
            } catch (InterruptedException | ExecutionException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Funkos>> findAll() throws SQLException, ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return funkoRepo.findAll().get();
            } catch (InterruptedException | ExecutionException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //
    //
    //REVISAR Y MODIFICAR PORQUE NO ESTA BIEN
    //
    //
    @Override
    public CompletableFuture<Optional<Funkos>> findById(int id) throws SQLException, ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return funkoRepo.findById(id).get();
            } catch (InterruptedException | ExecutionException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() throws SQLException, ExecutionException, InterruptedException {
        return CompletableFuture.runAsync(() -> {
            try {
                funkoRepo.deleteAll().get();
            } catch (InterruptedException | ExecutionException | SQLException e) {
                throw new RuntimeException(e);
            }
            cache.clear();
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(int id) throws SQLException, ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            Boolean borrarFunko;
            try {
                borrarFunko = funkoRepo.deleteById(id).get();
            } catch (InterruptedException | ExecutionException | SQLException e) {
                throw new RuntimeException(e);
            }
            if (borrarFunko){
                cache.remove(id);
            }
            return borrarFunko;
        });

    }
}
