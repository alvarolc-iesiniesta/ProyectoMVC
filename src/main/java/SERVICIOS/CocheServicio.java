/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICIOS;

/**
 *
 * @author AlvaroLópezCano
 */
import DAO.CocheDAO;
import DTO.PaginacionDTO;
import MODELO.Coche;
import java.util.List;

public class CocheServicio {
    
    private CocheDAO cocheDAO;
    
    public CocheServicio() {
        this.cocheDAO = new CocheDAO();
    }
    
    public boolean agregarCoche(Coche coche) {
        if (coche != null) {
            return cocheDAO.agregarCoche(coche);
        }
        return false;
    }

    
    public List<Coche> obtenerTodosLosCochesConPropietario() {
        return cocheDAO.obtenerTodosLosCochesConPropietario();
    }
    

     
    public boolean eliminarPorDNI(String dni) {
        if (dni != null && !dni.isEmpty()) {
            return cocheDAO.eliminarPorDNI(dni);
        }
        return false;
    }
    public PaginacionDTO<Coche> obtenerCochesPaginados(int pagina) {
    CocheDAO cocheDAO = new CocheDAO();
    int tamanoPagina = 10; 
    int totalElementos = cocheDAO.obtenerTotalCoches();
    List<Coche> coches = cocheDAO.obtenerCochesPaginados(pagina, tamanoPagina);

    PaginacionDTO<Coche> paginacion = new PaginacionDTO<>();
    paginacion.setPaginaActual(pagina);
    paginacion.setTotalElementos(totalElementos);
    paginacion.setTamanoPagina(tamanoPagina);
    paginacion.setElementos(coches);
    paginacion.calcularTotalPaginas();

    return paginacion;
}
    public List<Coche> filtrarCoches(String nombre, String genero, String marca, String modelo, String anio, String numVehiculos) {
        return cocheDAO.obtenerCochesFiltrados(nombre, genero, marca, modelo, anio, numVehiculos);
    }
}