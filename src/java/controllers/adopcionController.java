/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import Dao.AdopcionDao;
import Dao.ConectarDB;
import Dao.adopcionBeanDao;
import Dao.usuarioDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import models.addadopcionBeanValidation;
import models.adopcionBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author F&F
 */
public class adopcionController {
    
    private JdbcTemplate jdbcTemplate;
    private addadopcionBeanValidation validarAdopcion;
    
     public adopcionController(){
        ConectarDB con = new ConectarDB();
        jdbcTemplate = new JdbcTemplate(con.conDB());
        this.validarAdopcion = new addadopcionBeanValidation();
    }
    
    @RequestMapping(value = "formRegistrarAdopcion.htm", method = RequestMethod.GET)
    public ModelAndView formAdopcion(){
        ModelAndView mav = new ModelAndView();
        adopcionBean adopcion = new adopcionBean();
        adopcionBeanDao AdopcionesDao = new adopcionBeanDao();
        List datos = AdopcionesDao.consultarAdopciones();

        mav.addObject("adopcion" , datos);
        mav.setViewName("views/formRegistrarAdopcion");
        return mav;
    }
    @RequestMapping(value = "addAdopcion.htm", method = RequestMethod.GET)
    public ModelAndView agregarAdopcion(){
        ModelAndView mav = new ModelAndView();
        adopcionBean adopcion = new adopcionBean();
        usuarioDao usDao = new usuarioDao();
        AdopcionDao adopDao = new AdopcionDao();
        adopcionBeanDao adopBeanDao = new adopcionBeanDao();
        
        //-----------------------//
        List codPersona = usDao.consultarUsuario();
        mav.addObject("listaPersonas",codPersona);
        //-----------------------//
        List codMascota = adopDao.consultarAdopcionMascotas();
        mav.addObject("listaMascotas",codMascota);
        //-----------------------//
        int cod = adopBeanDao.consultarCodigo();
        adopcion.setId_formulario(cod);
        
        //-----------------------//
        mav.addObject("adopcion" , adopcion);
        mav.setViewName("views/addAdopcion");
        return mav;
    }
    
    /************insertar*****funciona****/
        
        @RequestMapping(value="addAdopcion.htm", method = RequestMethod.POST)
        public ModelAndView agregaAdopcion(@ModelAttribute("adopcion")adopcionBean adb,
                 BindingResult result,
                 SessionStatus status){ 
            {
            ModelAndView mav = new  ModelAndView();
            this.validarAdopcion.validate(adb, result);
            if(result.hasErrors()){
                mav.addObject("adopcion", new adopcionBean());
                mav.setViewName("views/addAdopcion");
            return mav;
            }
            
            else{    
                String sql = "insert into adopcion(fecha_adopcion, id_mascotas, id_personas) values(?,?,?)";
                jdbcTemplate.update(sql, adb.getFecha_adopcion(), adb.getId_mascotas(), adb.getId_personas());
                mav.setViewName("redirect:/formRegistrarAdopcion.htm");
                return mav;
            }
        }
        }
    
        /************borrar******funciona***/
   
     @RequestMapping("deleteAdopcion.htm")
     public ModelAndView borrarAdopcion( HttpServletRequest req){
        ModelAndView mav = new ModelAndView();
        int id = Integer.parseInt(req.getParameter("id"));
        String sql = "delete from adopcion where id_formulario = ?";
        jdbcTemplate.update(sql, id);
        mav.setViewName("redirect:/formRegistrarAdopcion.htm");
        return mav;
     }
     
     /************actualizar******no funciona***/
    
     @RequestMapping(value= "updateAdopcion.htm", method = RequestMethod.GET)
     public ModelAndView actAdopcion(HttpServletRequest req){
        ModelAndView mav = new ModelAndView();
        
        int id = Integer.parseInt(req.getParameter("id"));
        
        adopcionBean adb = consuladopcionId(id);
        
        mav.addObject("adopcion",adb);
        
        mav.setViewName("views/updateAdopcion");
        
        return mav;
     }
     
     public adopcionBean consuladopcionId(int id){
         adopcionBean adb = new adopcionBean();
         String sql = "select * from adopcion where id_formulario = "+ id;
         return (adopcionBean)jdbcTemplate.query(
                 sql,
                 new ResultSetExtractor<adopcionBean>(){
                   @Override
                   public adopcionBean extractData(ResultSet rs) throws SQLException, DataAccessException {
                   if(rs.next()){ 
                       adb.setId_formulario(rs.getInt("id_formulario"));
                       adb.setFecha_adopcion(rs.getString("fecha_adopcion"));
                       adb.setId_mascotas(rs.getInt("id_mascotas"));
                       adb.setId_personas(rs.getInt("id_personas"));
                       
                   }
                   return adb;
                   }
                 }
         );
     }
     @RequestMapping(value="updateAdopcion.htm", method = RequestMethod.POST)
     public ModelAndView actAdopcion(adopcionBean adb){
        ModelAndView mav = new ModelAndView();
        String sql = "insert into adopcion(fecha_adopcion, id_mascotas, id_personas) values(?,?,?)";
        jdbcTemplate.update(sql, adb.getFecha_adopcion(), adb.getId_mascotas(),adb.getId_personas());
        
        mav.setViewName("redirect:/formRegistrarAdopcion.htm");
        return mav;
     }
     
     //----------------------------------CONSULTAS------no funciona---------------------------------------//
     //-----------------------------consultar adopcion por id_formulario--------------//
     
     @RequestMapping(value="formConsultarAdopcionXId.htm", method=RequestMethod.GET)
     public ModelAndView listarAdopcionxId(){
         ModelAndView mav = new ModelAndView();
         adopcionBean adb = new adopcionBean();
         mav.addObject("adopcion",adb);
         mav.setViewName("views/formConsultarAdopcionId");
         return mav;
     }
     @RequestMapping(value="formConsultarAdopcionXId.htm", method=RequestMethod.POST)
     public ModelAndView listarAdopcionoxId(adopcionBean adb, BindingResult result,SessionStatus status)
     {
        
        ModelAndView mav = new ModelAndView();
        adopcionBeanDao adBeanDao = new adopcionBeanDao();
        int id = adb.getId_formulario();
        mav.addObject("adopcion",adBeanDao.consultarAdopcionById(id));
        mav.setViewName("views/vistaConsultasAdopciones");
        return mav;
     }
     
     //-----------------------------consultar adopcion por fecha_adopcion--------no funciona------//
     
     @RequestMapping(value="formConsultarAdopcionXFecha.htm", method=RequestMethod.GET)
     public ModelAndView listarAdopcionxFecha(){
         ModelAndView mav = new ModelAndView();
         adopcionBean adb = new adopcionBean();
         mav.addObject("adopcion",adb);
         mav.setViewName("views/formConsultarAdopcionFecha");
         return mav;
     }
     @RequestMapping(value="formConsultarAdopcionXFecha.htm", method=RequestMethod.POST)
     public ModelAndView listarAdopcionxFecha(adopcionBean adb, BindingResult result,SessionStatus status)
     {
        
        ModelAndView mav = new ModelAndView();
        adopcionBeanDao adBeanDao = new adopcionBeanDao();
        String fecha = adb.getFecha_adopcion();
        mav.addObject("adopcion",adBeanDao.consultarAdopcionByFecha(fecha));
        mav.setViewName("views/vistaConsultasAdopciones");
        return mav;
     }
     
     //-----------------------------consultar adopcion por id_mascotas--------no funciona------//
     
     @RequestMapping(value="formConsultarAdopcionXIdmascotas.htm", method=RequestMethod.GET)
     public ModelAndView listarAdopcionxIdmascota(){
         ModelAndView mav = new ModelAndView();
         adopcionBean adb = new adopcionBean();
         mav.addObject("adopcion",adb);
         mav.setViewName("views/formConsultarAdopcionIdmascota");
         return mav;
     }
     @RequestMapping(value="formConsultarAdopcionXIdmascotas.htm", method=RequestMethod.POST)
     public ModelAndView listarAdopcionxIdmascota(adopcionBean adb, BindingResult result,SessionStatus status)
     {
        
        ModelAndView mav = new ModelAndView();
        adopcionBeanDao adBeanDao = new adopcionBeanDao();
        int idmascota = adb.getId_mascotas();
        mav.addObject("adopcion",adBeanDao.consultarAdopcionByIdmascota(idmascota));
        mav.setViewName("views/vistaConsultasAdopciones");
        return mav;
     }
 
     //-----------------------------consultar adopcion por id_personas--------no funciona------//
     
     @RequestMapping(value="formConsultarAdopcionXIdpersonas.htm", method=RequestMethod.GET)
     public ModelAndView listarAdopcionxIdusuario(){
         ModelAndView mav = new ModelAndView();
         adopcionBean adb = new adopcionBean();
         mav.addObject("adopcion",adb);
         mav.setViewName("views/formConsultarAdopcionIdusuario");
         return mav;
     }
     @RequestMapping(value="formConsultarAdopcionXIdpersonas.htm", method=RequestMethod.POST)
     public ModelAndView listarAdopcionxIdusuario(adopcionBean adb, BindingResult result,SessionStatus status)
     {
        
        ModelAndView mav = new ModelAndView();
        adopcionBeanDao adBeanDao = new adopcionBeanDao();
        int idusuario = adb.getId_personas();
        mav.addObject("adopcion",adBeanDao.consultarAdopcionByIdusuario(idusuario));
        mav.setViewName("views/vistaConsultasAdopciones");
        return mav;
     }
}