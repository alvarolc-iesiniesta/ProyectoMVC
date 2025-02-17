/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLADOR;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author AlvaroLópezCano
 */
public class ValidarFiltros {

    private List<String> palabrasProhibidas = Arrays.asList(
            "SELECT", "UPDATE", "DROP", "ALTER", "OR", "NULL", "INNER", "LEFT", "CREATE", "TABLE","coches", "usuarios", "FROM" , "JOIN"
    );

    public boolean esNombreValido(String nombre) {
        return nombre != null && nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");
    }

    public boolean esMarcaModeloValido(String marcaModelo) {
        return marcaModelo != null && marcaModelo.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$");
    }

    public boolean esSoloNumeros(String texto) {
        return texto != null && texto.matches("\\d+"); // Solo números
    }

    public boolean esAnioValido(String anioText) {
        if (esSoloNumeros(anioText)) { // Verifica que solo contenga dígitos
            int anio = Integer.parseInt(anioText);
            int anioActual = LocalDate.now().getYear();
            return anio >= 1886 && anio <= anioActual; // Solo entre 1886 y el año actual
        }
        return false; // Si contiene caracteres no numéricos, es inválido
    }

    public boolean esDniValido(String dni) {
        return dni != null && dni.matches("^[a-zA-Z0-9]+$"); // Solo letras y números
    }

    // Método para verificar si el texto contiene palabras prohibidas
    public boolean contienePalabrasProhibidas(String texto) {
        if (texto == null) {
            return false;
        }

        String upperText = texto.toUpperCase(); // Convertir el texto a mayúsculas para comparación
        for (String palabra : palabrasProhibidas) {
            if (upperText.contains(palabra)) {
                return true; // Retorna true si encuentra una palabra prohibida
            }
        }
        return false; // Retorna false si no encuentra ninguna palabra prohibida
    }
}
