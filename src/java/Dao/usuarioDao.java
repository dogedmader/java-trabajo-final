/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import models.UsuarioBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author F&F
 */
public class usuarioDao {
     JdbcTemplate jdbcTemplate;
     ConectarDB con = new ConectarDB();
     //--------------consulta personas-------------------//
    public List consultarUsuario(){
        List datos = new ArrayList();
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        String sql = "select id, nombre from personas";
        datos = this.jdbcTemplate.queryForList(sql);
        return datos;   
    }
    
    //-------------------------metodo borrado de imagen---------------------------//
    
    public void borrarImagen(String foto, String deletePaht, int id){
        final String DELETE_DIRECTORY = "..\\..\\web\\";
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        //construimos la ubicacion del archivo en el servidor
        String deleteFile = deletePaht + DELETE_DIRECTORY + foto;
        File borrar = new File(deleteFile);
        if(borrar.delete()){
            String sql = "delete from personas where id = ?";
            jdbcTemplate.update(sql, id);
            //System.out.print("borrado");
        }else{
            System.out.println("no se pudo borrar..");        
        }
    }
    
    //--------------------------------borrar mascota e imagen-----------------------------------------//
    public void borrarImagenActualizada(String foto, String deletePath){
        final String DELETE_DIRECTORY = "..\\..\\web\\";
        //construimos la ubicacion del archivo en el servidor
        String deleteFile = deletePath + DELETE_DIRECTORY + foto;
        File borrar = new File(deleteFile);
        if(borrar.delete()){
            System.out.println("borrado");
        }else{
            System.out.println("no se pudo borrar..");
        }
    }
    
    //--------------------------actualiza usuario sin la foto-------------------------//
    
    public void actUsuarioSinFoto(UsuarioBean usb, List lista){
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        ArrayList<String> listados = new ArrayList<>();
        for(int i = 0; i < lista.size(); i++){
            //System.out.println("error aca..." + i + "-" + items.size());
            FileItem fileItem = (FileItem) lista.get(i);
            listados.add(fileItem.getString());
        }
        usb.setNombre(listados.get(0));
        usb.setCorreo(listados.get(1));            
        usb.setCedula(listados.get(2));            
        usb.setTelefono(listados.get(3));            
        usb.setDireccion(listados.get(4));
        usb.setCiudad(listados.get(5));
        
        //------------------------------------------------//
        
         String sql = "update personas set nombre = ?, correo set = ?, cedula set = ?, telefono set = ?, direccion set = ?"
                 + "WHERE id = ?";
        jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(), 
                            usb.getTelefono(), usb.getDireccion(), usb.getCiudad(), usb.getId());
    }
    
    
    //--------------------actualizar imagen en usuario-----------------//
    
    private static final String UPLOAD_DIRECTORY="..\\..\\web\\images\\photos";
        
        //cargar configuracion
        
        private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; //3mb
        private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; //40mb
        private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; //50mb
    
     public void actUsuarioConImagenFoto(UsuarioBean usb, 
             boolean isMultipart,
             HttpServletRequest request,
             List items){
         this.jdbcTemplate = new JdbcTemplate(con.conDB());
         //-----------------------------------------------------------//
         ArrayList<String> listados = new ArrayList<>();
         if(isMultipart){
         DiskFileItemFactory file = new DiskFileItemFactory();
         //configura parametros de carga
         //establecer los archivos temporales del umbral de memoria se generan y almacenaran en el directorio
         file.setSizeThreshold(MEMORY_THRESHOLD);
         //establecer el directorio de almacenamiento temporal
         file.setRepository(new File(System.getProperty("java.io.tmpdir")));
         //le pasamos el fileitem como parametro a la variable
         ServletFileUpload fileUpload = new ServletFileUpload(file);
         //establecer el valor maximo de cargar de archivos
         fileUpload.setFileSizeMax(MAX_FILE_SIZE);
         //establecer el valor maximo de solicitud (incluidos los datos de archivo de y formulario)
         fileUpload.setSizeMax(MAX_REQUEST_SIZE);
         //Construye una ruta temporal para almacenar archivos cargados
         //Esta ruta es relativa al directorio actual de la aplicación
         String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
         //esta ruta es relativa al archivo actual de la imagen a ser borrada
         String deletePath = request.getServletContext().getRealPath("") + File.separator;
         //crear si el directorio no existe
         File uploadDir = new File(uploadPath); 
         if (!uploadDir.exists()){
             uploadDir.mkdir();
         }
         for (int i = 0; i < items.size(); i++){
         //creamos la variable fileitem y hacemos un parseo de la lista
             //que recorre todos los valores del formulario
             FileItem fileItem = (FileItem) items.get(i);
             //hacemos un condicional para saber cual variable es el archivo
             if(!fileItem.isFormField()){
                 String fileName = new File(fileItem.getName()).getName();
                 String filePath = uploadPath + File.separator + "ID-" + listados.get(0) + "-" + fileName; 
                 File uploadFile = new File(filePath);
                 //obtener el nombre del archivo
                 String nameFile = ("images/photos/" + "ID-" + listados.get(0) + "-" + fileName );
                 try {
                     //borrar la imagen del servidor anterior
                    borrarImagenActualizada(usb.getFotoOld(), deletePath);
                     //borrar la imagen en caso de estar con el mismo nombre
                     uploadFile.delete();
                     //almacenar secuencia de archivo en disco (en el directorio tomcat)
                     fileItem.write(uploadFile);
                     usb.setFoto(nameFile);
                     usb.setFotoOld(nameFile);
                 } catch (Exception e) {
                     System.out.print("escritura...." + e.getMessage());
                 }
             }else{
                 listados.add(fileItem.getString());
             }
         }
        usb.setNombre(listados.get(0));
        usb.setCorreo(listados.get(1));            
        usb.setCedula(listados.get(2));            
        usb.setTelefono(listados.get(3));            
        usb.setDireccion(listados.get(4));
        usb.setCiudad(listados.get(5));
     }
         //--------------------------------------------------------//
         String sql = "update personas set nombre = ?, correo = ?, cedula = ?, telefono = ?, direccion = ?,"
                 + "ciudad = ?, foto = ?, fotoOld = ? WHERE id = ?";
        jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(), 
                            usb.getTelefono(), usb.getDireccion(), usb.getCiudad(), 
                            usb.getFoto(), usb.getFoto() ,usb.getId());
     }
    
    //---------------------CONSULTAS--------------------------------------//
    //----------------consulta personas id----------------//
     public List consultarUsuarioById(int id){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where id = ?";
       usb = this.jdbcTemplate.queryForList(sql, id);
       return usb;
     }
     
    //----------------consulta personas nombre ----------//
     public List consultarUsuarioByName(String nombre){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where nombre = ?";
       usb = this.jdbcTemplate.queryForList(sql, nombre);
       return usb;
     }
     
     //----------------consulta personas correo ----------//
     public List consultarUsuarioByMail(String correo){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where correo = ?";
       usb = this.jdbcTemplate.queryForList(sql, correo);
       return usb;
     }
     
     //----------------consulta personas cedula ----------//
     public List consultarUsuarioByCedula(String cedula){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where cedula = ?";
       usb = this.jdbcTemplate.queryForList(sql, cedula);
       return usb;
     }
     
     //----------------consulta personas telefono ----------//
     public List consultarUsuarioByTelefono(String telefono){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where telefono = ?";
       usb = this.jdbcTemplate.queryForList(sql, telefono);
       return usb;
     }
     
     //----------------consulta personas direccion ----------//
     public List consultarUsuarioByDireccion(String direccion){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where direccion = ?";
       usb = this.jdbcTemplate.queryForList(sql);
       return usb;
     }
     
     //----------------consulta personas ciudad ----------//
     public List consultarUsuarioByCiudad(String ciudad){
      List usb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from personas where ciudad = ?";
       usb = this.jdbcTemplate.queryForList(sql, ciudad);
       return usb;
     }

    //--------------------insertar imagen en directorio-----------------//
}