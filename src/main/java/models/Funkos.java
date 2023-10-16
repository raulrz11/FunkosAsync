package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Funkos {
    private UUID cod = UUID.randomUUID();

    private String nombre;
    private Modelo modelo;
    private double precio;
    private LocalDate fechaLanzamiento;
    private LocalDateTime updateTime;
    private int id;
    private int myId;

    public Funkos() {

    }

    public enum Modelo{
        MARVEL,
        DISNEY,
        ANIME,
        OTROS
    }

    public Funkos(UUID cod, String nombre, Modelo modelo, double precio, LocalDate fechaLanzamiento) {
        this.cod = cod;
        this.nombre = nombre;
        this.modelo = modelo;
        this.precio = precio;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getCod() {
        return cod;
    }

    public void setCod(UUID cod) {
        this.cod = cod;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    @Override
    public String toString() {
        return "Funkos{" +
                "id=" + id +
                ", cod=" + cod +
                ", myId=" + myId +
                ", nombre='" + nombre + '\'' +
                ", modelo=" + modelo +
                ", precio=" + precio +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", updateTime=" + updateTime +
                '}';
    }
}
