/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICIOS;

/**
 *
 * @author AlvaroLópezCano
 */
import DAO.PersonaDAO;
import MODELO.Persona;
import java.util.List;

public class PersonaServicios {

    private PersonaDAO personaDAO;

    public PersonaServicios() {
        this.personaDAO = new PersonaDAO();
    }

 
    public boolean agregarPersona(Persona persona) {
        if (persona != null) {
            return personaDAO.agregarPersona(persona);
        }
        return false;
    }

   
    public List<Persona> obtenerTodasLasPersonas() {
        return personaDAO.obtenerTodasLasPersonas();
    }


    public boolean eliminarPersonaPorDNI(String dni) {
        if (dni != null && !dni.isEmpty()) {
            return personaDAO.eliminarPersonaPorDNI(dni);
        }
        return false;
    }
}
