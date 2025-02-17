/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLADOR;

/**
 *
 * @author AlvaroLópezCano
 */
import MODELO.Coche;
import DAO.FiltrosPersonaCoches;
import SERVICIOS.CocheServicio;
import java.util.List;

public class CocheControlador {

    private CocheServicio cocheServicio;

    public CocheControlador() {
        this.cocheServicio = new CocheServicio();
    }

    public boolean agregarCoche(Coche coche) {
        if (validarCoche(coche)) {
            return cocheServicio.agregarCoche(coche);
        }
        return false;
    }

    public List<Coche> obtenerTodosLosCoches() {
        return cocheServicio.obtenerTodosLosCochesConPropietario();
    }

    public boolean eliminarCochePorDNI(String dni) {
        if (dni != null && !dni.isEmpty()) {
            return cocheServicio.eliminarPorDNI(dni);
        }
        return false;
    }
    //metodo para validar si un coche es correcto
    private boolean validarCoche(Coche coche) {
        if (coche == null) {
            return false;
        }
        // Valido que la marca no esté vacía
        if (coche.getMarca() == null || coche.getMarca().isEmpty()) {
            System.err.println("Error: La marca no puede estar vacía.");
            return false;
        }
        // Valido que el modelo no esté vacío
        if (coche.getModelo() == null || coche.getModelo().isEmpty()) {
            System.err.println("Error: El modelo no puede estar vacío.");
            return false;
        }
        // Validdo que la matrícula no esté vacía
        if (coche.getMatricula() == null || coche.getMatricula().isEmpty()) {
            System.err.println("Error: La matrícula no puede estar vacía.");
            return false;
        }
        // Valido que el año sea razonable (1900 <= año <= año actual)
        int anioActual = java.time.Year.now().getValue();
        if (coche.getAño() < 1900 || coche.getAño() > anioActual) {
            System.err.println("Error: Año de fabricación inválido.");
            return false;
        }
        // Valido la longitud máxima de la matrícula (ejemplo: 10 caracteres)
        if (coche.getMatricula().length() > 10) {
            System.err.println("Error: La matrícula no puede tener más de 10 caracteres.");
            return false;
        }

        return true;
    }

    public List<Coche> obtenerCochesFiltrados(String nombre, String genero, String marca, String modelo, String anio, String numVehiculos) {
        // Filtrar coches con el servicio
        return cocheServicio.filtrarCoches(nombre, genero, marca, modelo, anio, numVehiculos);
    }
}
//     public List<Coche> obtenerCochesFiltradosConPaginacion(String nombre, String genero, String marca, String modelo, String anio, int pagina, int tamanoPagina) {
//        // Calcula el offset basado en la página actual y el tamaño de la página
//        int offset = (pagina - 1) * tamanoPagina;
//
//        return cocheServicio.obtenerCochesFiltradosConPaginacion(nombre, genero, marca, modelo, anio, tamanoPagina, offset);
//    }

    
    
