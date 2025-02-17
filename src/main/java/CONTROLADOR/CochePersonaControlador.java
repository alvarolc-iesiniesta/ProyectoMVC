/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLADOR;

import DAO.CocheDAO;
import java.util.List;

/**
 *
 * @author AlvaroLópezCano
 */
import DTO.PersonaVehiculoDTO;
import DAO.FiltrosPersonaCoches;
import MODELO.Coche;
import java.util.List;

public class CochePersonaControlador {

    private FiltrosPersonaCoches filtrosPersonaCoches;
    private CocheDAO cocheDAO;
    private ValidarFiltros validadorFiltros;

    public CochePersonaControlador() {
        filtrosPersonaCoches = new FiltrosPersonaCoches();
        cocheDAO = new CocheDAO();
        this.validadorFiltros = new ValidarFiltros();

    }

    public List<PersonaVehiculoDTO> obtenerPersonasConVehiculosFiltrados(String nombre, String marca, String modelo, Integer anio, int paginaActual, int tamanoPagina) {
        return filtrosPersonaCoches.filtrarUsuariosConCochesPaginados(nombre, marca, modelo, anio, paginaActual, tamanoPagina);
    }

    public boolean guardarVehiculoEnBD(String matricula, int anio, String marca, String modelo) {
        Coche coche = new Coche(matricula, anio, marca, modelo);
        return filtrosPersonaCoches.guardarCoche(coche);
    }

    public boolean eliminarVehiculoPorMatricula(String matricula) {
        return filtrosPersonaCoches.eliminarCochePorMatricula(matricula);
    }

    public List<Coche> obtenerVehiculosLibres() {
        return filtrosPersonaCoches.obtenerVehiculosLibres();
    }

    public boolean asociarVehiculoAPersona(int idCoche, int idUsuario) {
        return cocheDAO.asociarVehiculoAPersona(idCoche, idUsuario);
    }

    public ValidarFiltros getValidadorFiltros() {
        return validadorFiltros;
    }
    
}
