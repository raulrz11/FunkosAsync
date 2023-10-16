package org.example;

import models.Funkos;
import repositorys.FunkoRepoImpl;
import services.DatabaseManager;
import services.funkos.FunkoService;
import services.funkos.FunkoServiceImpl;
import utils.IdGenerator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        DatabaseManager db = DatabaseManager.getInstance();
        FunkoService funkoService = FunkoServiceImpl.getInstance(FunkoRepoImpl.getInstance(db));
        //Consultas a la BD
        System.out.println("Todos los Funkos");
        funkoService.findAll().get().forEach(System.out::println);
        System.out.println();

        System.out.println("Funko con el nombre deseado");
        funkoService.findByNombre("Spiderman").get().forEach(System.out::println);
        System.out.println();

        System.out.println("Insertamos un Funko");
        Funkos funkoNuevo = new Funkos();
        funkoNuevo.setNombre("Miguel de Cervantes");
        funkoNuevo.setModelo(Funkos.Modelo.OTROS);
        funkoNuevo.setPrecio(15.99);
        funkoNuevo.setFechaLanzamiento(LocalDate.now());
        funkoService.save(funkoNuevo);
    }
}