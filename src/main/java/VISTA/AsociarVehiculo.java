/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VISTA;

import CONTROLADOR.CochePersonaControlador;
import CONTROLADOR.PersonaControlador;
import MODELO.Coche;
import MODELO.Persona;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author AlvaroLópezCano
 */
public class AsociarVehiculo extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel tableModelUsuarios;
    private JTable tablaVehiculos;
    private DefaultTableModel tableModelVehiculos;
    private JButton btnAsociar;
    private JButton btnMostrarAsociaciones;
    private JCheckBox chkOcultarVehiculos;
    private PersonaControlador personaControlador;
    private CochePersonaControlador cochePersonaControlador;
    private JPanel panelVehiculos;

    public AsociarVehiculo() {
        setTitle("Asociar Vehículo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        personaControlador = new PersonaControlador();
        cochePersonaControlador = new CochePersonaControlador();

        inicializarTablaUsuarios();
        inicializarTablaVehiculos();

        btnAsociar = new JButton("Asociar Vehículo");
        btnAsociar.addActionListener(e -> asociarVehiculo());

        btnMostrarAsociaciones = new JButton("Mostrar Asociaciones");
        btnMostrarAsociaciones.addActionListener(e -> cargarDatos());

        chkOcultarVehiculos = new JCheckBox("Ocultar coches");
        chkOcultarVehiculos.addActionListener(e -> panelVehiculos.setVisible(!chkOcultarVehiculos.isSelected()));

        JPanel panelUsuarios = new JPanel(new BorderLayout());
        panelUsuarios.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        panelVehiculos = new JPanel(new BorderLayout());
        panelVehiculos.add(new JScrollPane(tablaVehiculos), BorderLayout.CENTER);

        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));
        panelPrincipal.add(panelUsuarios);
        panelPrincipal.add(panelVehiculos);

        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(chkOcultarVehiculos);
        panelSuperior.add(btnMostrarAsociaciones);

        setLayout(new BorderLayout());
        add(panelSuperior, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
        add(btnAsociar, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void inicializarTablaUsuarios() {
        String[] columnNamesUsuarios = {"ID Usuario", "Nombre", "DNI", "Sexo"};
        tableModelUsuarios = new DefaultTableModel(columnNamesUsuarios, 0);
        tablaUsuarios = new JTable(tableModelUsuarios);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo permite una selección de fila
    }

    private void inicializarTablaVehiculos() {
        String[] columnNamesVehiculos = {"ID Coche", "Matrícula", "Año", "Marca", "Modelo"};
        tableModelVehiculos = new DefaultTableModel(columnNamesVehiculos, 0);
        tablaVehiculos = new JTable(tableModelVehiculos);
        tablaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo permite una selección de fila
    }

    public void cargarDatos() {
        List<Persona> usuariosLibres = personaControlador.obtenerPersonasLibres();
        tableModelUsuarios.setRowCount(0);

        for (Persona usuario : usuariosLibres) {
            tableModelUsuarios.addRow(new Object[]{
                usuario.getId(),  
                usuario.getNombre(),
                usuario.getDni(),
                usuario.getGenero() 
            });
        }

        List<Coche> vehiculosLibres = cochePersonaControlador.obtenerVehiculosLibres();
        tableModelVehiculos.setRowCount(0);

        for (Coche coche : vehiculosLibres) {
            tableModelVehiculos.addRow(new Object[]{
                coche.getId(),  
                coche.getMatricula(),
                coche.getAño(),
                coche.getMarca(),
                coche.getModelo()
            });
        }
    }

    void asociarVehiculo() {
        int filaUsuario = tablaUsuarios.getSelectedRow();
        int filaVehiculo = tablaVehiculos.getSelectedRow();

        if (filaUsuario == -1 || filaVehiculo == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario y un vehículo para asociar.");
            return;
        }

        try {
            int usuarioId = Integer.parseInt(tableModelUsuarios.getValueAt(filaUsuario, 0).toString());
            int vehiculoId = Integer.parseInt(tableModelVehiculos.getValueAt(filaVehiculo, 0).toString());

            String nombreUsuario = tableModelUsuarios.getValueAt(filaUsuario, 1).toString();
            String matriculaVehiculo = tableModelVehiculos.getValueAt(filaVehiculo, 1).toString();

            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Deseas asociar el vehículo con matrícula " + matriculaVehiculo + " al usuario " + nombreUsuario + "?",
                    "Confirmación de asociación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                boolean exito = cochePersonaControlador.asociarVehiculoAPersona(vehiculoId, usuarioId);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Vehículo asociado correctamente al usuario.");
                    cargarDatos(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Error al asociar el vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de usuario o vehículo. Asegúrate de que las filas seleccionadas son válidas.\n"
                    + "Detalles del error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AsociarVehiculo ventana = new AsociarVehiculo();
            ventana.setVisible(true);
        });
    }
}
