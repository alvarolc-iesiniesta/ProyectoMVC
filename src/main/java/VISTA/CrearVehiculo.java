/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VISTA;

import CONTROLADOR.CochePersonaControlador;
import CONTROLADOR.ValidarFiltros;
import MODELO.Coche;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author AlvaroLópezCano
 */
public class CrearVehiculo extends JFrame {

    private JTextField textMatricula;
    private JTextField textAño;
    private JComboBox<String> comboMarca;
    private JComboBox<String> comboModelo;
    private boolean confirmado;

    private CochePersonaControlador controlador;
    private ValidarFiltros validador;  // Añadimos el validador de filtros

    public CrearVehiculo(CochePersonaControlador controlador) {
        this.controlador = controlador;
        this.validador = new ValidarFiltros();  // Inicializamos el validador

        setTitle("Crear Vehículo");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Matrícula:"));
        textMatricula = new JTextField();
        add(textMatricula);

        add(new JLabel("Año:"));
        textAño = new JTextField();
        add(textAño);

        add(new JLabel("Marca:"));
        comboMarca = new JComboBox<>(new String[]{"Toyota", "Ford", "BMW", "Audi"});
        comboMarca.addActionListener(e -> actualizarModelos());
        add(comboMarca);

        add(new JLabel("Modelo:"));
        comboModelo = new JComboBox<>();
        actualizarModelos();
        add(comboModelo);

        JButton buttonOK = new JButton("OK");
        buttonOK.addActionListener(e -> {
            // Verificar si los campos contienen palabras prohibidas
            if (validador.contienePalabrasProhibidas(textMatricula.getText()) ||
                validador.contienePalabrasProhibidas(textAño.getText()) ||
                validador.contienePalabrasProhibidas(comboMarca.getSelectedItem().toString()) ||
                validador.contienePalabrasProhibidas(comboModelo.getSelectedItem().toString())) {
                
                JOptionPane.showMessageDialog(this, "Error: Se han detectado palabras no permitidas en los campos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar la matrícula para caracteres especiales
            if (esMatriculaValida(textMatricula.getText())) {
                confirmado = true;
                guardarVehiculo();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "La matrícula contiene caracteres especiales no permitidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        add(buttonOK);
        add(buttonCancel);
    }

    private void actualizarModelos() {
        comboModelo.removeAllItems();
        String marca = (String) comboMarca.getSelectedItem();
        if (marca.equals("Toyota")) {
            comboModelo.addItem("Corolla");
            comboModelo.addItem("Camry");
            comboModelo.addItem("Rav4");
        } else if (marca.equals("Ford")) {
            comboModelo.addItem("Focus");
            comboModelo.addItem("Fiesta");
            comboModelo.addItem("Mustang");
        } else if (marca.equals("BMW")) {
            comboModelo.addItem("X3");
            comboModelo.addItem("X5");
            comboModelo.addItem("Serie 3");
        } else if (marca.equals("Audi")) {
            comboModelo.addItem("A4");
            comboModelo.addItem("A6");
            comboModelo.addItem("Q5");
        }
    }

    private void guardarVehiculo() {
        try {
            String matricula = textMatricula.getText();
            int año = Integer.parseInt(textAño.getText());
            String marca = (String) comboMarca.getSelectedItem();
            String modelo = (String) comboModelo.getSelectedItem();

            controlador.guardarVehiculoEnBD(matricula, año, marca, modelo);
            JOptionPane.showMessageDialog(this, "Vehículo guardado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el vehículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean esMatriculaValida(String matricula) {
        return matricula.matches("^[a-zA-Z0-9]+$");
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}