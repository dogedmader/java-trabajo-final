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
import models.MascotasBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author F&F
 */
public class AdopcionDao {
     JdbcTemplate jdbcTemplate;
     ConectarDB con = new ConectarDB();
    //------------------consulta de adopcion------------------//
    public List consultarAdopcionMascotas(){
        List datos = new ArrayList();
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        String sql = "select id, nombre from mascotas";
        datos = this.jdbcTemplate.queryForList(sql);
        return datos;
    }
    
    //-------------------------metodo borrado de imagen---------------------------//
    
    public void borrarImagenmascota(String foto, String deletePaht, int id){
        final String DELETE_DIRECTORY = "..\\..\\web\\";
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        //construimos la ubicacion del archivo en el servidor
        String deleteFile = deletePaht + DELETE_DIRECTORY + foto;
        File borrar = new File(deleteFile);
        if(borrar.delete()){
            String sql = "delete from mascotas where id = ?";
            jdbcTemplate.update(sql, id);
            //System.out.print("borrado");
        }else{
            System.out.println("no se pudo borrar..");        
        }
    }
    
    //--------------------------------borrar mascota e imagen-----------------------------------------//
    public void borrarImagenActualizada(String foto, String deletePaht){
        final String DELETE_DIRECTORY = "..\\..\\web\\";
        //construimos la ubicacion del archivo en el servidor
        String deleteFile = deletePaht + DELETE_DIRECTORY + foto;
        File borrar = new File(deleteFile);
        if(borrar.delete()){
            System.out.println("borrado");
        }else{
            System.out.println("no se pudo borrar..");
        }
    }
    //-------------------------actualizar mascota sin la foto-------------------------------------//
    
    public void actMascotaSinFoto(MascotasBean msb, List lista){
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        ArrayList<String> listados = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++){
            //System.out.println("error aca.." + i + "-" + items.size());
            FileItem fileItem = (FileItem)lista.get(i);
            listados.add(fileItem.getString());
        }
        msb.setPlaca(listados.get(0));
        msb.setNombre(listados.get(1));
        msb.setSexo(listados.get(2));
        msb.setRaza(listados.get(3));
        msb.setEdad(listados.get(4));
//------------------------------------------------------//
    String sql = "update mascotas set placa = ?, set nombre = ?, sexo = ?, raza = ?, edad = ? "
            + "WHERE id = ?";
    jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(),msb.getSexo(), msb.getRaza(),msb.getEdad(), msb.getId());
    }
    
    //-----------------------actualizar imagen de Mascota-------------------------//
     
    private static final String UPLOAD_DIRECTORY="..\\..\\web\\images\\photos";
        
        //cargar configuracion
        
        private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; //3mb
        private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; //40mb
        private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; //50mb
        
    public void actMascotaConImagenFoto(MascotasBean msb, boolean isMultipart,
            HttpServletRequest request,
            List items){
        this.jdbcTemplate = new JdbcTemplate(con.conDB());
        //-------------------------------------------------------------//
        
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
                    borrarImagenActualizada(msb.getFotoOld(), deletePath);
                     //borrar la imagen en caso de estar con el mismo nombre
                     uploadFile.delete();
                     //almacenar secuencia de archivo en disco (en el directorio tomcat)
                     fileItem.write(uploadFile);
                     msb.setFoto(nameFile);
                     msb.setFotoOld(nameFile);
                 } catch (Exception e) {
                     System.out.print("escritura...." + e.getMessage());
                 }
             }else{
                 listados.add(fileItem.getString());
             }
         }
            
        msb.setPlaca(listados.get(0));
        msb.setNombre(listados.get(1));
        msb.setSexo(listados.get(2));
        msb.setRaza(listados.get(3));
        msb.setEdad(listados.get(4));
        }
        //-------------------------------------------------//
         String sql = "update mascotas set placa = ?, nombre = ?, sexo = ?, raza = ?, edad = ?,"
                 + "foto = ?, fotoOld = ? WHERE id = ?";
    jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(),
            msb.getSexo(), msb.getRaza(),msb.getEdad(), msb.getFoto(),
            msb.getFoto() , msb.getId());
    
    }
    
    
    
    
    //---------------------CONSULTAS--------------------------------------//
    //----------------consulta mascotas id----------------//
     public List consultarMascotaById(int id){
      List msb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from mascotas where id = ?";
       msb = this.jdbcTemplate.queryForList(sql, id);
       return msb;
     }
     
     
     //----------------consulta mascotas placa ----------//
     public List consultarMascotaByPlaca(String placa){
      List msb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from mascotas where placa = ?";
       msb = this.jdbcTemplate.queryForList(sql, placa);
       return msb;
     }
     
     //----------------consulta mascotas nombre ----------//
     public List consultarMascotaByNombre(String nombre){
      List msb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from mascotas where nombre = ?";
       msb = this.jdbcTemplate.queryForList(sql, nombre);
       return msb;
     }
     
     //----------------consulta mascotas sexo ----------//
     public List consultarMascotaBySexo(String sexo){
      List msb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from mascotas where sexo = ?";
       msb = this.jdbcTemplate.queryForList(sql, sexo);
       return msb;
     }
     
      //----------------consulta mascotas raza ----------//
     public List consultarMascotaByRaza(String raza){
      List msb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from mascotas where raza = ?";
       msb = this.jdbcTemplate.queryForList(sql, raza);
       return msb;
     }
     
      //----------------consulta mascotas edad ----------//
     public List consultarMascotaByEdad(String edad){
      List msb = new ArrayList();
       this.jdbcTemplate = new JdbcTemplate(con.conDB());
       String sql = "select * from mascotas where edad = ?";
       msb = this.jdbcTemplate.queryForList(sql, edad);
       return msb;
     }
     
     
}
