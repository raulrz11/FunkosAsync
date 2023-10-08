package services.funkos;

import exceptions.FunkoNotFoundException;
import exceptions.FunkoNotSaveException;
import models.Funkos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FunkoServiceImpl implements FunkoService{
    @Override
    public Funkos save(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotSaveException {
        return null;
    }

    @Override
    public Funkos update(Funkos funko) throws SQLException, ExecutionException, InterruptedException, FunkoNotFoundException {
        return null;
    }

    @Override
    public List<Funkos> findByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public List<Funkos> findAll() throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Optional<Funkos> findById(int id) throws SQLException, ExecutionException, InterruptedException {
        return Optional.empty();
    }

    @Override
    public void deleteAll() throws SQLException, ExecutionException, InterruptedException {

    }

    @Override
    public boolean deleteById(int id) throws SQLException, ExecutionException, InterruptedException {
        return false;
    }
}
