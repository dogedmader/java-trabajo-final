/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author F&F
 */
public class adopcionBeanDao {
     JdbcTemplate jdbcTemplate;
     ConectarDB con = new ConectarDB();
    
    public List consultarAdopciones(){
        List datos = new ArrayList();
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        String sql = "select * from adopcion";
        datos = this.jdbcTemplate.queryForList(sql);
        
        return datos;
    }
    
    public int consultarCodigo(){
     int cod = 0;
     this.jdbcTemplate = new JdbcTemplate(con.conDB());
     String sql = "select max(id_formulario)+1 as codigo from adopcion";
     cod = this.jdbcTemplate.queryForObject(sql, Integer.class);
     
     return cod;
    }
    //------------------------------CONSULTAS-----------------------------------------------//
    //------------------consultar adopcion id_formulario-------------//
    public List consultarAdopcionById(int id_formulario){
      List adb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "SELECT * FROM adopcion WHERE id_formulario = ?";
       adb = this.jdbcTemplate.queryForList(sql, id_formulario);
       return adb;
     }
    
    //------------------consultar adopcion fecha-------------------//
    public List consultarAdopcionByFecha(String fecha_adopcion){
      List adb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "SELECT * FROM adopcion WHERE fecha_adopcion = ?";
       adb = this.jdbcTemplate.queryForList(sql, fecha_adopcion);
       return adb;
     }
    
    //------------------consultar adopcion id_mascotas-------------//
    public List consultarAdopcionByIdmascota(int id_mascotas){
      List adb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "SELECT * FROM adopcion WHERE id_mascotas = ?";
       adb = this.jdbcTemplate.queryForList(sql, id_mascotas);
       return adb;
     }
    
    //------------------consultar adopcion id_personas-------------//
    public List consultarAdopcionByIdusuario(int id_personas){
      List adb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "SELECT * FROM adopcion WHERE id_personas = ?";
       adb = this.jdbcTemplate.queryForList(sql, id_personas);
       return adb;
     }
}
