/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author SENA
 */
public class adopcionBean {
    int id_formulario;
    String fecha_adopcion;
    int id_mascotas;
    int id_personas;

    public adopcionBean() {
    }

    public adopcionBean(int id_formulario, String fecha_adopcion, int id_mascotas, int id_personas) {
        this.id_formulario = id_formulario;
        this.fecha_adopcion = fecha_adopcion;
        this.id_mascotas = id_mascotas;
        this.id_personas = id_personas;
    }

    public int getId_formulario() {
        return id_formulario;
    }

    public void setId_formulario(int id_formulario) {
        this.id_formulario = id_formulario;
    }

    public String getFecha_adopcion() {
        return fecha_adopcion;
    }

    public void setFecha_adopcion(String fecha_adopcion) {
        this.fecha_adopcion = fecha_adopcion;
    }

    public int getId_mascotas() {
        return id_mascotas;
    }

    public void setId_mascotas(int id_mascotas) {
        this.id_mascotas = id_mascotas;
    }

    public int getId_personas() {
        return id_personas;
    }

    public void setId_personas(int id_personas) {
        this.id_personas = id_personas;
    }
    
    
    
    
    
}
