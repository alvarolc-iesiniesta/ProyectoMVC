/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VISTA;

import CONTROLADOR.PersonaControlador;
import CONTROLADOR.ValidarFiltros;
import MODELO.Persona;
import javax.swing.JFrame;

/**
 *
 * @author AlvaroLópezCano
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearPersona extends JFrame {

    private JTextField txtNombre;
    private JTextField txtDNI;
    private JRadioButton rbHombre;
    private JRadioButton rbMujer;
    private ButtonGroup bgGenero;
    private JButton btnCrear;
    private JButton btnCancelar;
    private PersonaControlador personaControlador;
    private AsociarVehiculo asociarVehiculo;
    private ValidarFiltros validador;  // Añadimos el validador de filtros

    public CrearPersona(AsociarVehiculo asociarVehiculo) {
        this.asociarVehiculo = asociarVehiculo;
        personaControlador = new PersonaControlador();
        validador = new ValidarFiltros();  // Inicializamos el validador

        setTitle("Crear Persona");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new GridLayout(4, 2, 5, 5));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();
        panelPrincipal.add(lblNombre);
        panelPrincipal.add(txtNombre);

        JLabel lblDNI = new JLabel("DNI:");
        txtDNI = new JTextField();
        panelPrincipal.add(lblDNI);
        panelPrincipal.add(txtDNI);

        JLabel lblGenero = new JLabel("Género:");
        rbHombre = new JRadioButton("Hombre");
        rbMujer = new JRadioButton("Mujer");
        bgGenero = new ButtonGroup();
        bgGenero.add(rbHombre);
        bgGenero.add(rbMujer);

        JPanel panelGenero = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGenero.add(rbHombre);
        panelGenero.add(rbMujer);

        panelPrincipal.add(lblGenero);
        panelPrincipal.add(panelGenero);

        btnCrear = new JButton("Crear");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);

        // Acción del botón Crear
        btnCrear.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String dni = txtDNI.getText().trim();
        String genero = rbHombre.isSelected() ? "Hombre" : rbMujer.isSelected() ? "Mujer" : null;

        // Verificar si el nombre contiene solo letras
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            JOptionPane.showMessageDialog(CrearPersona.this, "Error: El campo 'Nombre' solo permite letras y espacios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si los campos contienen palabras prohibidas
        if (validador.contienePalabrasProhibidas(nombre) || validador.contienePalabrasProhibidas(dni)) {
            JOptionPane.showMessageDialog(CrearPersona.this, "Error: Se han detectado palabras no permitidas en los campos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones de campos vacíos y caracteres especiales
        if (nombre.isEmpty() || dni.isEmpty() || genero == null) {
            JOptionPane.showMessageDialog(CrearPersona.this, "Rellena todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Persona nuevaPersona = new Persona();
            nuevaPersona.setNombre(nombre);
            nuevaPersona.setDni(dni);
            nuevaPersona.setGenero(genero);
            
            boolean exito = personaControlador.agregarPersona(nuevaPersona);
            if (exito) {
                JOptionPane.showMessageDialog(CrearPersona.this, "Persona creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                asociarVehiculo.cargarDatos(); 
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(CrearPersona.this, "Error al crear la persona.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});
        // Acción del botón Cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Método para validar si el nombre contiene solo caracteres permitidos (letras, espacios, acentos)
    private boolean esNombreValido(String nombre) {
        return nombre.matches("^[\\p{L}\\p{N}\\s.'-]+$");
    }
}
