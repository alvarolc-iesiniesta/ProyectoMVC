/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICIOS;

/**
 *
 * @author AlvaroLópezCano
 */
import java.util.List;

public abstract class ServicioGenerico<T> {

    public abstract boolean guardar(T entidad);

    public abstract boolean actualizar(T entidad);

    public abstract boolean eliminar(int id);

    public abstract T obtenerPorId(int id);

    public abstract List<T> obtenerTodos();
}
