/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODELO;

/**
 *
 * @author AlvaroLópezCano
 */
public class Coche {

    private int id;
    private String marca;
    private String modelo;
    private String DNI;
    private int año;
    private String matricula;
    private int idUsuario;

    private String nombrePropietario;
    private String apellidoPropietario;
    private String emailPropietario;
    private String sexoPropietario; // Nuevo campo para el género del propietario

    public Coche() {
    }

    public Coche(int id, String marca, String modelo, int año, String DNI, int idUsuario, String matricula, String nombrePropietario, String apellidoPropietario, String sexoPropietario) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.DNI = DNI;
        this.año = año;
        this.matricula = matricula;
        this.idUsuario = idUsuario;
        this.nombrePropietario = nombrePropietario;
        this.apellidoPropietario = apellidoPropietario;
        this.emailPropietario = emailPropietario;
        this.sexoPropietario = sexoPropietario;
    }

    public Coche(String matricula, int año, String marca, String modelo) {
        this.matricula = matricula;
        this.año = año;
        this.marca = marca;
        this.modelo = modelo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getApellidoPropietario() {
        return apellidoPropietario;
    }

    public void setApellidoPropietario(String apellidoPropietario) {
        this.apellidoPropietario = apellidoPropietario;
    }

    public String getEmailPropietario() {
        return emailPropietario;
    }

    public void setEmailPropietario(String emailPropietario) {
        this.emailPropietario = emailPropietario;
    }

    public String getSexoPropietario() {
        return sexoPropietario;
    }

    public void setSexoPropietario(String sexoPropietario) {
        this.sexoPropietario = sexoPropietario;
    }



}