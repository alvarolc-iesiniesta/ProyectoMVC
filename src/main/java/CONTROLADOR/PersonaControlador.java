/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLADOR;

import DAO.FiltrosPersonaCoches;
import DAO.PersonaDAO;
import MODELO.Persona;
import java.util.List;

/**
 *
 * @author AlvaroLópezCano
 */
public class PersonaControlador {

    private FiltrosPersonaCoches filtrosPersonaCoches;
    private PersonaDAO personaDAO; 

    public PersonaControlador() {
        this.filtrosPersonaCoches = new FiltrosPersonaCoches();
        this.personaDAO = new PersonaDAO();
    }

    public List<Persona> obtenerPersonasLibres() {
        return filtrosPersonaCoches.obtenerPersonasLibres();
    }

    public boolean asociarVehiculoAPersona(int idCoche, int idPersona) {
        return filtrosPersonaCoches.asociarVehiculoAPersona(idCoche, idPersona);
    }

    public boolean agregarPersona(Persona nuevaPersona) {
        return personaDAO.agregarPersona(nuevaPersona);
    }

    public boolean usuarioExiste(int usuarioId) {
        return personaDAO.usuarioExiste(usuarioId); 
    }
}
