/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author AlvaroLópezCano
 */
import MODELO.Persona;
import PERSISTENCIA.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;

public class PersonaDAO {

    // Método para agregar una persona a la base de datos
    public boolean agregarPersona(Persona persona) {
        String sql = "INSERT INTO usuarios (nombre, apellido, email, dni) VALUES (?, ?, ?, ?)";
        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (persona.getNombre().length() > 20) {
                JOptionPane.showMessageDialog(null, "El nombre es demasiado largo. Máximo permitido: 20 caracteres.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Asigna los valores
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido() != null ? persona.getApellido() : "");
            stmt.setString(3, persona.getEmail() != null ? persona.getEmail() : "no-email-" + UUID.randomUUID() + "@example.com");
            stmt.setString(4, persona.getDni());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar persona: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al agregar persona: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Método para obtener una lista de todas las personas en la base de datos
    public List<Persona> obtenerTodasLasPersonas() {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            // Itera sobre el resultado y construye objetos Persona
            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id_usuario"));
                persona.setNombre(rs.getString("nombre"));
                persona.setDni(rs.getString("dni"));
                persona.setGenero(rs.getString("sexo"));
                personas.add(persona);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener personas: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener personas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return personas;
    }

    // Método para eliminar una persona de la base de datos usando su DNI
    public boolean eliminarPersonaPorDNI(String dni) {
        String sql = "DELETE FROM usuarios WHERE dni = ?";
        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni); // Asigna el DNI al parámetro de la consulta
            return stmt.executeUpdate() > 0; // Ejecuta la eliminación y retorna true si fue exitosa
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar persona por DNI: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Argumento inválido al eliminar persona: " + e.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al eliminar persona: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Método para verificar si un usuario existe en la base de datos según su ID
    public boolean usuarioExiste(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE id_usuario = ?";
        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId); // Asigna el ID del usuario al parámetro de la consulta
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el usuario existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al verificar existencia de usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void guardarPersona(String nombre, String dni, String genero) {
        String sql = "INSERT INTO usuarios (nombre, dni, genero) VALUES (?, ?, ?)";
        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar los valores a la consulta
            stmt.setString(1, nombre);
            stmt.setString(2, dni);
            stmt.setString(3, genero);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Persona guardada correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar la persona.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar persona: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al guardar persona: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
