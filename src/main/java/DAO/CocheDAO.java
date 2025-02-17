/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import PERSISTENCIA.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AlvaroLópezCano
 */
import MODELO.Coche;
import PERSISTENCIA.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CocheDAO {

    // Agrega un coche a la base de datos
    public boolean agregarCoche(Coche coche) {
        String sql = "INSERT INTO coches (marca, modelo, año, matricula, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coche.getMarca());
            stmt.setString(2, coche.getModelo());
            stmt.setInt(3, coche.getAño());
            stmt.setString(4, coche.getMatricula());
            stmt.setInt(5, coche.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el coche: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al agregar el coche: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Obtiene todos los coches con su propietario
    public List<Coche> obtenerTodosLosCochesConPropietario() {
        List<Coche> coches = new ArrayList<>();
        String sql = "SELECT coches.id_coche, coches.marca, coches.modelo, coches.año, coches.matricula, "
                + "usuarios.nombre AS nombrePropietario, usuarios.dni AS dniPropietario, "
                + "usuarios.sexo AS sexoPropietario "
                + "FROM coches LEFT JOIN usuarios ON coches.id_usuario = usuarios.id_usuario";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Coche coche = new Coche();
                coche.setId(rs.getInt("id_coche"));
                coche.setMarca(rs.getString("marca"));
                coche.setModelo(rs.getString("modelo"));
                coche.setAño(rs.getInt("año"));
                coche.setMatricula(rs.getString("matricula"));
                coche.setNombrePropietario(rs.getString("nombrePropietario"));
                coche.setDNI(rs.getString("dniPropietario"));
                coche.setSexoPropietario(rs.getString("sexoPropietario"));
                coches.add(coche);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener los coches con propietario: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener los coches con propietario: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return coches;
    }

    // Elimina un coche de la base de datos por DNI
    public boolean eliminarPorDNI(String dni) {
        String sql = "DELETE FROM coches WHERE dni = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el coche por DNI: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al eliminar el coche: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Obtiene coches paginados
    public List<Coche> obtenerCochesPaginados(int pagina, int tamanoPagina) {
        List<Coche> coches = new ArrayList<>();
        String sql = "SELECT coches.id_coche, coches.marca, coches.modelo, coches.año, coches.matricula, "
                + "usuarios.nombre AS nombrePropietario, usuarios.dni AS dniPropietario "
                + "FROM coches INNER JOIN usuarios ON coches.id_usuario = usuarios.id_usuario "
                + "LIMIT ? OFFSET ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tamanoPagina);
            stmt.setInt(2, (pagina - 1) * tamanoPagina);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Coche coche = new Coche();
                coche.setId(rs.getInt("id_coche"));
                coche.setMarca(rs.getString("marca"));
                coche.setModelo(rs.getString("modelo"));
                coche.setAño(rs.getInt("año"));
                coche.setMatricula(rs.getString("matricula"));
                coche.setNombrePropietario(rs.getString("nombrePropietario"));
                coche.setDNI(rs.getString("dniPropietario"));
                coches.add(coche);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener los coches paginados: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener los coches paginados: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return coches;
    }

    // Obtiene el total de coches
    public int obtenerTotalCoches() {
        String sql = "SELECT COUNT(*) FROM coches";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el total de coches: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener el total de coches: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    // Obtiene coches filtrados por criterios específicos
    public List<Coche> obtenerCochesFiltrados(String nombre, String genero, String marca, String modelo, String anio, String numVehiculos) {
        List<Coche> listaCoches = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT usuarios.nombre AS nombrePropietario, usuarios.dni AS dniPropietario, "
                + "coches.matricula, coches.año, coches.marca, coches.modelo "
                + "FROM coches INNER JOIN usuarios ON coches.id_usuario = usuarios.id_usuario WHERE 1=1");
        List<Object> parametros = new ArrayList<>();
        try {
            if (nombre != null && !nombre.isEmpty()) {
                query.append(" AND usuarios.nombre LIKE ?");
                parametros.add("%" + nombre.trim() + "%");
            }
            if (genero != null && !genero.isEmpty()) {
                query.append(" AND usuarios.genero = ?");
                parametros.add(genero.trim());
            }
            if (marca != null && !marca.equals("Todas")) {
                query.append(" AND coches.marca = ?");
                parametros.add(marca.trim());
            }
            if (modelo != null && !modelo.equals("Todos")) {
                query.append(" AND coches.modelo = ?");
                parametros.add(modelo.trim());
            }
            if (anio != null && !anio.isEmpty()) {
                query.append(" AND coches.año = ?");
                parametros.add(Integer.parseInt(anio.trim()));
            }
            try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
                for (int i = 0; i < parametros.size(); i++) {
                    pstmt.setObject(i + 1, parametros.get(i));
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Coche coche = new Coche();
                        coche.setNombrePropietario(rs.getString("nombrePropietario"));
                        coche.setDNI(rs.getString("dniPropietario"));
                        coche.setMatricula(rs.getString("matricula"));
                        coche.setAño(rs.getInt("año"));
                        coche.setMarca(rs.getString("marca"));
                        coche.setModelo(rs.getString("modelo"));
                        listaCoches.add(coche);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener los coches con filtros: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Formato de año inválido: " + anio, "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al obtener los coches con filtros: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return listaCoches;
    }

    // Asocia un vehículo a una persona
    public boolean asociarVehiculoAPersona(int idCoche, int idUsuario) {
        if (!usuarioExiste(idUsuario)) {
            JOptionPane.showMessageDialog(null, "Error: el usuario con ID " + idUsuario + " no existe.", "Error de Asociación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean exito = false;
        try (Connection conn = Conexion.conectar()) {
            String actualizarCocheQuery = "UPDATE coches SET id_usuario = ? WHERE id_coche = ?";
            try (PreparedStatement stmt = conn.prepareStatement(actualizarCocheQuery)) {
                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idCoche);
                if (stmt.executeUpdate() > 0) {
                    String registrarHistorialQuery = "INSERT INTO historialpropietarios (id_coche, id_usuario, fecha_inicio) VALUES (?, ?, NOW())";
                    try (PreparedStatement historialStmt = conn.prepareStatement(registrarHistorialQuery)) {
                        historialStmt.setInt(1, idCoche);
                        historialStmt.setInt(2, idUsuario);
                        exito = historialStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al asociar vehículo: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al asociar vehículo: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }

    private boolean usuarioExiste(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar el usuario: " + e.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado al verificar el usuario: " + e.getMessage(), "Error general", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}