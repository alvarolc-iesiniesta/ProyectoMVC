/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package VISTA;

/**
 *
 * @author AlvaroLópezCano
 */
import CONTROLADOR.CocheControlador;
import CONTROLADOR.CochePersonaControlador;
import CONTROLADOR.ValidarFiltros;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import DAO.CocheDAO;
import DTO.PersonaVehiculoDTO;
import MODELO.Coche;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroVehiculos extends JFrame {

    private JTable tablaCoches;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbModelo;
    private JComboBox<String> cbMarca;
    private JTextField txtNombre;
    private JTextField txtAnio;
    private JTextField txtNumeroVehiculos;
    private JRadioButton rbHombre;
    private JRadioButton rbMujer;
    private ButtonGroup bgGenero;
    private Map<String, String[]> modelosPorMarca;
    private int paginaActual = 1;
    private final int tamanoPagina = 10;
    private JLabel lblPaginaActual;
    private CochePersonaControlador controlador;

    public RegistroVehiculos() {
        setTitle("Registro de Vehículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        controlador = new CochePersonaControlador();
        inicializarModelosPorMarca();
        inicializarTabla();

        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Opciones");

        JMenuItem menuItemCrear = new JMenuItem("Crear Vehículo");
        JMenuItem menuItemCrearPersona = new JMenuItem("Crear Persona");
        JMenuItem menuItemEliminar = new JMenuItem("Eliminar Vehículo");
        JMenuItem menuItemAsociar = new JMenuItem("Asociar Vehículo");

        menuItemCrear.addActionListener(e -> {
            CrearVehiculo crearVehiculo = new CrearVehiculo(controlador);
            crearVehiculo.setVisible(true);
        });
        menuItemEliminar.addActionListener(e -> eliminarVehiculoSeleccionado());

        menuItemAsociar.addActionListener(e -> {
            AsociarVehiculo asociarVehiculo = new AsociarVehiculo();
            asociarVehiculo.setVisible(true);
        });
        menuItemCrearPersona.addActionListener(e -> {
            AsociarVehiculo asociarVehiculo = new AsociarVehiculo();
            CrearPersona crearPersona = new CrearPersona(asociarVehiculo);
            crearPersona.setVisible(true);
        });
        menuOpciones.add(menuItemCrear);
        menuOpciones.add(menuItemCrearPersona);
        menuOpciones.add(menuItemEliminar);
        menuOpciones.add(menuItemAsociar);
        menuBar.add(menuOpciones);
        setJMenuBar(menuBar);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new GridLayout(3, 6, 5, 5));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(10);

        JLabel lblGenero = new JLabel("Género:");
        rbHombre = new JRadioButton("Hombre");
        rbMujer = new JRadioButton("Mujer");
        bgGenero = new ButtonGroup();
        bgGenero.add(rbHombre);
        bgGenero.add(rbMujer);

        JLabel lblMarca = new JLabel("Marca:");
        cbMarca = new JComboBox<>(new String[]{"Todas", "Toyota", "Ford", "BMW", "Audi", "Honda", "Mercedes", "Tesla", "Volkswagen", "Nissan", "Chevrolet", "Hyundai", "Kia", "Mazda", "Subaru", "Jeep", "Lexus"});

        JLabel lblFiltrosAvanzados = new JLabel("\u25BC Filtros avanzados");
        lblFiltrosAvanzados.setForeground(Color.BLUE);
        lblFiltrosAvanzados.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel panelFiltrosAvanzados = new JPanel(new FlowLayout(FlowLayout.LEFT));

        lblFiltrosAvanzados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean isVisible = panelFiltrosAvanzados.isVisible();
                panelFiltrosAvanzados.setVisible(!isVisible);
                lblFiltrosAvanzados.setText(isVisible ? "\u25BC Filtros avanzados" : "\u25B2 Filtros avanzados");
            }
        });

        cbMarca.addActionListener(e -> {
            actualizarComboModelo((String) cbMarca.getSelectedItem());
            paginaActual = 1;
            cargarDatosPagina();
        });

        JLabel lblModelo = new JLabel("Modelo:");
        cbModelo = new JComboBox<>(new String[]{"Todos"});

        JLabel lblAnio = new JLabel("Año de Matriculación:");
        txtAnio = new JTextField(5);

        JLabel lblNumeroVehiculos = new JLabel("N° de Vehículos:");
        txtNumeroVehiculos = new JTextField(5);

        JButton btnAplicarFiltros = new JButton("Aplicar Filtros");
        btnAplicarFiltros.addActionListener(e -> aplicarFiltros());

        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    aplicarFiltros();
                }
            }
        };

        txtNombre.addKeyListener(enterKeyListener);
        txtAnio.addKeyListener(enterKeyListener);
        txtNumeroVehiculos.addKeyListener(enterKeyListener);

        panelFiltros.add(lblNombre);
        panelFiltros.add(txtNombre);
        panelFiltros.add(lblGenero);
        panelFiltros.add(rbHombre);
        panelFiltros.add(rbMujer);
        panelFiltros.add(lblMarca);
        panelFiltros.add(cbMarca);
        panelFiltros.add(lblModelo);
        panelFiltros.add(cbModelo);
        panelFiltros.add(lblAnio);
        panelFiltros.add(txtAnio);
        panelFiltros.add(lblNumeroVehiculos);
        panelFiltros.add(txtNumeroVehiculos);
        panelFiltros.add(btnAplicarFiltros);
        panelFiltrosAvanzados.setVisible(false);

        JPanel panelPaginacion = new JPanel();
        JButton btnAnterior = new JButton("Anterior");
        JButton btnSiguiente = new JButton("Siguiente");
        lblPaginaActual = new JLabel("Página: " + paginaActual);
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPaginaActual);
        panelPaginacion.add(btnSiguiente);

        btnAnterior.addActionListener(e -> {
            if (paginaActual > 1) {
                paginaActual--;
                cargarDatosPagina();
                actualizarEtiquetaPagina();
            }
        });

        btnSiguiente.addActionListener(e -> {
            paginaActual++;
            cargarDatosPagina();
            actualizarEtiquetaPagina();
        });

        setLayout(new BorderLayout());
        add(panelFiltros, BorderLayout.NORTH);
        add(new JScrollPane(tablaCoches), BorderLayout.CENTER);
        add(panelPaginacion, BorderLayout.SOUTH);

        cargarDatosPagina();
    }

    private void aplicarFiltros() {
        ValidarFiltros validador = new ValidarFiltros();

        // Verificar palabras prohibidas en los campos de filtro
        if (validador.contienePalabrasProhibidas(txtNombre.getText())
                || validador.contienePalabrasProhibidas(cbMarca.getSelectedItem().toString())
                || validador.contienePalabrasProhibidas(cbModelo.getSelectedItem().toString())
                || validador.contienePalabrasProhibidas(txtAnio.getText())
                || validador.contienePalabrasProhibidas(txtNumeroVehiculos.getText())) {

            JOptionPane.showMessageDialog(this, "Error: Se han detectado palabras no permitidas en los filtros.", "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación para el nombre y otros campos generales
        if ((!txtNombre.getText().isEmpty() && !controlador.getValidadorFiltros().esNombreValido(txtNombre.getText()))
                || (!cbMarca.getSelectedItem().toString().equals("Todas") && !controlador.getValidadorFiltros().esMarcaModeloValido(cbMarca.getSelectedItem().toString()))
                || (!cbModelo.getSelectedItem().toString().equals("Todos") && !controlador.getValidadorFiltros().esMarcaModeloValido(cbModelo.getSelectedItem().toString()))) {

            JOptionPane.showMessageDialog(this, "Filtros inválidos detectados. No se permiten caracteres especiales.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación para el campo Año de Matriculación
        String anioText = txtAnio.getText().trim();
        if (!anioText.isEmpty()) {
            try {
                int anio = Integer.parseInt(anioText);
                int anioActual = LocalDate.now().getYear();
                if (anio < 1886 || anio > anioActual) {
                    JOptionPane.showMessageDialog(this, "El año debe estar entre 1900 y " + anioActual + ".",
                            "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El campo 'Año de Matriculación' debe contener solo números y ser un año válido.",
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String numeroVehiculosText = txtNumeroVehiculos.getText().trim();
        if (!numeroVehiculosText.isEmpty()) {
            try {
                int numeroVehiculos = Integer.parseInt(numeroVehiculosText);
                if (numeroVehiculos < 0) {
                    JOptionPane.showMessageDialog(this, "El número de vehículos no puede ser negativo.",
                            "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El campo 'N° de Vehículos' debe contener solo números.",
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        paginaActual = 1;
        cargarDatosPagina();
    }

    private boolean esFiltroValido(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            return true;
        }
        return filtro.matches("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑüÜ]*$");
    }

    private void inicializarTabla() {
        String[] columnNames = {"Nombre", "DNI", "Matrícula", "Año", "Marca", "Modelo", "fecha_inicio", "fecha_fin"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tablaCoches = new JTable(tableModel);
    }

    private void cargarDatosPagina() {
        String nombre = txtNombre.getText();
        String marca = cbMarca.getSelectedItem().toString();
        String modelo = cbModelo.getSelectedItem().toString();
        Integer anio = txtAnio.getText().isEmpty() ? null : Integer.parseInt(txtAnio.getText());
        Integer numeroVehiculos = txtNumeroVehiculos.getText().isEmpty() ? null : Integer.parseInt(txtNumeroVehiculos.getText());
        String genero = rbHombre.isSelected() ? "Hombre" : rbMujer.isSelected() ? "Mujer" : null;

        List<PersonaVehiculoDTO> listaCoches = controlador.obtenerPersonasConVehiculosFiltrados(nombre, marca, modelo, anio, paginaActual, tamanoPagina);

        tableModel.setRowCount(0);

        for (PersonaVehiculoDTO coche : listaCoches) {
            Object[] row = {
                coche.getNombre(),
                coche.getDni(),
                coche.getMatricula(),
                coche.getAnio(),
                coche.getMarca(),
                coche.getModelo(),
                coche.getFechaInicio(),
                coche.getFechaFin()
            };
            tableModel.addRow(row);
        }

        if (listaCoches.isEmpty() && paginaActual > 1) {
            JOptionPane.showMessageDialog(this, "No hay más datos.");
            paginaActual--;
            actualizarEtiquetaPagina();
        }
    }

    private void actualizarEtiquetaPagina() {
        lblPaginaActual.setText("Página: " + paginaActual);
    }

    private void inicializarModelosPorMarca() {
        modelosPorMarca = new HashMap<>();
        modelosPorMarca.put("Toyota", new String[]{"Corolla", "Camry", "Rav4"});
        modelosPorMarca.put("Ford", new String[]{"Focus", "Fiesta", "Mustang"});
        modelosPorMarca.put("BMW", new String[]{"X3", "X5", "Serie 3"});
        modelosPorMarca.put("Audi", new String[]{"A4", "A6", "Q5"});
        modelosPorMarca.put("Honda", new String[]{"Civic", "Accord", "CR-V"});
        modelosPorMarca.put("Mercedes", new String[]{"C300", "E350", "GLA"});
        modelosPorMarca.put("Tesla", new String[]{"Model S", "Model X", "Model 3"});
        modelosPorMarca.put("Volkswagen", new String[]{"Golf", "Passat", "Tiguan"});
        modelosPorMarca.put("Nissan", new String[]{"Altima", "Sentra", "Rogue"});
        modelosPorMarca.put("Chevrolet", new String[]{"Malibu", "Impala", "Camaro"});
        modelosPorMarca.put("Hyundai", new String[]{"Elantra", "Sonata", "Tucson"});
        modelosPorMarca.put("Kia", new String[]{"Optima", "Sorento", "Sportage"});
        modelosPorMarca.put("Mazda", new String[]{"Mazda3", "Mazda6", "CX-5"});
        modelosPorMarca.put("Subaru", new String[]{"Impreza", "Forester", "Outback"});
        modelosPorMarca.put("Jeep", new String[]{"Compass", "Cherokee"});
        modelosPorMarca.put("Lexus", new String[]{"IS250", "RX350", "LX570"});
        modelosPorMarca.put("Todas", new String[]{"Todos"});
    }

    private void actualizarComboModelo(String marcaSeleccionada) {
        cbModelo.removeAllItems();
        String[] modelos = modelosPorMarca.getOrDefault(marcaSeleccionada, new String[]{"Todos"});
        for (String modelo : modelos) {
            cbModelo.addItem(modelo);
        }
    }

    private void eliminarVehiculoSeleccionado() {
        int selectedRow = tablaCoches.getSelectedRow();
        if (selectedRow >= 0) {
            String matricula = tableModel.getValueAt(selectedRow, 2).toString();

            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el vehículo con matrícula: " + matricula + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = controlador.eliminarVehiculoPorMatricula(matricula);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Vehículo eliminado.");
                    cargarDatosPagina();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un vehículo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistroVehiculos ventana = new RegistroVehiculos();
            ventana.setVisible(true);
        });
    }
}
