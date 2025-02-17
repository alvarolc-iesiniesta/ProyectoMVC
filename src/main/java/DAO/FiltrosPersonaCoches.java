/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.PersonaVehiculoDTO;
import MODELO.Coche;
import MODELO.Persona;
import PERSISTENCIA.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author AlvaroLópezCano
 */
public class FiltrosPersonaCoches {

    //metodo para mostrar la paginacion con las personas, y tambien meto los filtros todo mezclado
    public List<PersonaVehiculoDTO> filtrarUsuariosConCochesPaginados(String nombre, String marca, String modelo, Integer anio, int pagina, int tamanoPagina) {
        List<PersonaVehiculoDTO> listaUsuariosCoches = new ArrayList<>();

        try {
            if (!esFiltroValido(nombre) || !esFiltroValido(marca) || !esFiltroValido(modelo)) {
                throw new IllegalArgumentException("No se permiten caracteres especiales.");
            }

            StringBuilder query = new StringBuilder("SELECT u.nombre, u.apellido, u.dni, c.marca, c.modelo, c.año, c.matricula, h.fecha_inicio, h.fecha_fin "
                    + "FROM usuarios u "
                    + "INNER JOIN coches c ON u.id_usuario = c.id_usuario "
                    + "LEFT JOIN historialpropietarios h ON c.id_coche = h.id_coche "
                    + "WHERE 1=1");

            List<Object> parametros = new ArrayList<>();

            if (nombre != null && !nombre.isEmpty()) {
                query.append(" AND u.nombre LIKE ?");
                parametros.add("%" + nombre + "%");
            }
            if (marca != null && !marca.equalsIgnoreCase("Todas")) {
                query.append(" AND c.marca = ?");
                parametros.add(marca);
            }
            if (modelo != null && !modelo.equalsIgnoreCase("Todos")) {
                query.append(" AND c.modelo = ?");
                parametros.add(modelo);
            }
            if (anio != null && anio > 0) {
                query.append(" AND c.año = ?");
                parametros.add(anio);
            }

            query.append(" LIMIT ? OFFSET ?");
            parametros.add(tamanoPagina);
            parametros.add((pagina - 1) * tamanoPagina);

            try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(query.toString())) {

                for (int i = 0; i < parametros.size(); i++) {
                    stmt.setObject(i + 1, parametros.get(i));
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    PersonaVehiculoDTO usuarioVehiculo = new PersonaVehiculoDTO();
                    usuarioVehiculo.setNombre(rs.getString("nombre"));
                    usuarioVehiculo.setApellido(rs.getString("apellido"));
                    usuarioVehiculo.setDni(rs.getString("dni"));
                    usuarioVehiculo.setMarca(rs.getString("marca"));
                    usuarioVehiculo.setModelo(rs.getString("modelo"));
                    usuarioVehiculo.setAnio(rs.getInt("año"));
                    usuarioVehiculo.setMatricula(rs.getString("matricula"));
                    usuarioVehiculo.setFechaInicio(rs.getDate("fecha_inicio"));
                    usuarioVehiculo.setFechaFin(rs.getDate("fecha_fin"));

                    listaUsuariosCoches.add(usuarioVehiculo);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al filtrar usuarios con coches: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de filtro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return listaUsuariosCoches;
    }

    //metodo para guardar un coche
    public boolean guardarCoche(Coche coche) {
        String sql = "INSERT INTO coches (matricula, año, marca, modelo) VALUES (?, ?, ?, ?)";

        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coche.getMatricula());
            stmt.setInt(2, coche.getAño());
            stmt.setString(3, coche.getMarca());
            stmt.setString(4, coche.getModelo());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el coche: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al guardar el coche: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    //metodo para eliminar por matricula
    public boolean eliminarCochePorMatricula(String matricula) {
        String eliminarHistorial = "DELETE FROM historialpropietarios WHERE id_coche = (SELECT id_coche FROM coches WHERE matricula = ?)";
        String eliminarCoche = "DELETE FROM coches WHERE matricula = ?";

        try ( Connection conn = Conexion.conectar();  PreparedStatement stmtHistorial = conn.prepareStatement(eliminarHistorial);  PreparedStatement stmtCoche = conn.prepareStatement(eliminarCoche)) {

            conn.setAutoCommit(false);

            stmtHistorial.setString(1, matricula);
            stmtHistorial.executeUpdate();

            stmtCoche.setString(1, matricula);
            int filasEliminadas = stmtCoche.executeUpdate();

            conn.commit();
            return filasEliminadas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el vehículo: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al eliminar el vehículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    //metodo para asociar un vehiculo
    public boolean asociarVehiculoAPersona(int idCoche, int idPersona) {
        String query = "INSERT INTO historialpropietarios (id_coche, id_usuario, fecha_inicio) VALUES (?, ?, NOW())";
        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCoche);
            stmt.setInt(2, idPersona);
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al asociar vehículo a persona: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al asociar vehículo a persona: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

   public List<Persona> obtenerPersonasLibres() {
    List<Persona> personas = new ArrayList<>();
    String sql = "SELECT id_usuario, nombre, dni, sexo FROM usuarios WHERE id_usuario IS NOT NULL";
    try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Persona persona = new Persona();
            persona.setId(rs.getInt("id_usuario")); // Asegúrate de que esta línea asigna correctamente el ID
            persona.setNombre(rs.getString("nombre"));
            persona.setDni(rs.getString("dni"));
            persona.setGenero(rs.getString("sexo"));
            personas.add(persona);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener personas libres: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener personas libres: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    return personas;
}

    //metodo para obtener los vehiculos LIBRES que hay en la base de datos y se pueden asociar
    public List<Coche> obtenerVehiculosLibres() {
        List<Coche> vehiculosLibres = new ArrayList<>();

        String query = "SELECT c.id_coche, c.matricula, c.año, c.marca, c.modelo "
                + "FROM coches c "
                + "LEFT JOIN historialpropietarios h ON c.id_coche = h.id_coche "
                + "WHERE h.id_usuario IS NULL OR h.fecha_fin IS NOT NULL";

        try ( Connection conn = Conexion.conectar();  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Coche coche = new Coche();
                coche.setId(rs.getInt("id_coche"));
                coche.setMatricula(rs.getString("matricula"));
                coche.setAño(rs.getInt("año"));
                coche.setMarca(rs.getString("marca"));
                coche.setModelo(rs.getString("modelo"));

                vehiculosLibres.add(coche);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener vehículos libres: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener vehículos libres: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return vehiculosLibres;
    }

    private boolean esFiltroValido(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            return true;
        }
        //Devuelve los filtros
        return filtro.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$");
    }
    
}
