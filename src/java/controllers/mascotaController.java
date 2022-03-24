/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import Dao.AdopcionDao;
import Dao.ConectarDB;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import models.MascotasBean;
import models.MascotasBeanValidation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
public class mascotaController {
    
     private final JdbcTemplate jdbcTemplate;
     private MascotasBeanValidation validarMascotas;
     
    public mascotaController(){
        ConectarDB con = new ConectarDB();
        jdbcTemplate = new JdbcTemplate(con.conDB());
        this.validarMascotas = new MascotasBeanValidation();
    }
    
    /*********************************/
    
     @RequestMapping("listarMascotas.htm")
    public ModelAndView formMascotas(){
        ModelAndView mav = new ModelAndView();
        String sql = "select * from mascotas";
        List datos = jdbcTemplate.queryForList(sql);
        mav.addObject("mascotas",datos);
        mav.setViewName("views/listarMascotas");
        return mav;
    }
    
    /************insertar*********/
    
        @RequestMapping(value="addMascota.htm", method = RequestMethod.GET)
        public ModelAndView addmascota(){
        ModelAndView mav = new  ModelAndView();
        mav.addObject("Mascota",new MascotasBean());
        mav.setViewName("views/addMascota");
        return mav;
        }

        //-------------------------------version nueva insert--------------------------------------//
        private static final String UPLOAD_DIRECTORY="..\\..\\web\\images\\photos";
        
        //cargar configuracion
        
        private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; //3mb
        private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; //40mb
        private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; //50mb
        
        @RequestMapping(value="addMascota.htm", method = RequestMethod.POST)
        public ModelAndView addmascota(MascotasBean msb, HttpServletRequest request){
            ModelAndView mav = new ModelAndView();
            //obtener la ruta de lectura del archivo
            //determine si el atributop de caga esta configurado en el formulario
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            //creamos una variable tipo list para recorrer el vector
            ArrayList<String> listados = new ArrayList<>();
            if (isMultipart){
            //creamos la instancia del archivo file item
                DiskFileItemFactory file = new DiskFileItemFactory();
                //configurar parametros de carga
                //establecer los archivos temporales de umbral de memoria se generanran y almacenaran en el directorio
                file.setSizeThreshold(MEMORY_THRESHOLD);
                //establecer el directorio de almacenamiento temporal
                
                file.setRepository(new File(System.getProperty("java.io.tmpdir")));
                //le pasamos el fileitem como parametro a la variable
                ServletFileUpload fileUpload = new ServletFileUpload(file);
                //establecer el valor maximo de carga de archivos
                fileUpload.setFileSizeMax(MAX_FILE_SIZE);
                //Establecer el valor maximo de solicitud (incluidos los datos de archivo y formulario)
                fileUpload.setSizeMax(MAX_FILE_SIZE);
                //construye una ruta temporal para almacenar archivos cargados
                // esta ruta es relativa al directorio actual de la aplicacion
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                //crear si el directorio no existe
                File uploadDir = new File(uploadPath);
                if(!uploadDir.exists()){
                    uploadDir.mkdir();
                }
                //creamos una lista con los valores pasado desde el formulario
                List<FileItem> items = null;
                try{
                    items = fileUpload.parseRequest(request);
                }   catch (FileUploadException ex) {
                    System.out.print("carga..." + ex.getMessage());
                }
                for (int i = 0; i < items.size(); i++){
                    //crear variable fileitem y el parseo de la lista
                    //esta recorre los valores del formulario
                    FileItem fileItem = (FileItem) items.get(i);
                    //hacemos un condicional para saber cual variable es el archivo
                    if(!fileItem.isFormField()){
                    String fileName = new File(fileItem.getName()).getName();
                    String filePath = uploadPath + File.separator + fileName;
                    File uploadFile = new File(filePath);
                    //obtener el nombre de archivo
                    String nameFile = ("images/photos/"+ fileName);
                    try {
                        //Almacenar secuencia de archivo en disco (en el directorio tomcat)
                        fileItem.write(uploadFile);
                        msb.setFoto(nameFile);
                        msb.setFotoOld(nameFile);
                    }catch (Exception e){
                        System.out.print("escritura... " + e.getMessage());
                    }
                } else{
                       listados.add(fileItem.getString()); 
                    }
                }
                    msb.setPlaca(listados.get(0));
                    msb.setNombre(listados.get(1));
                    msb.setSexo(listados.get(2));
                    msb.setRaza(listados.get(3));
                    msb.setEdad(listados.get(4));
                }
            //--------------------------------------------------------------------------//
            String sql = "insert into mascotas(placa, nombre,"
                    + " sexo, raza, edad, foto, fotoOld) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(), msb.getSexo(), 
                msb.getRaza(),msb.getEdad(), msb.getFoto(), msb.getFotoOld());
        mav.setViewName("redirect:/listarMascotas.htm");
        return mav;
            }
        //-------------------------------version nueva insert--------------------------------------//
        
        
         /************borrar*********/
        @RequestMapping("deleteMascota.htm")
        public ModelAndView deleteMascota(HttpServletRequest req){
            ModelAndView mav = new ModelAndView();
            AdopcionDao adoptDao = new AdopcionDao();
            int id = Integer.parseInt(req.getParameter("id"));
            //captura la direccion del archivo
            String deletePath = req.getServletContext().getRealPath("")+ File.separator;
            String foto = req.getParameter("foto");
            //metodo para borrar el cliente y la imagen
            adoptDao.borrarImagenmascota(foto, deletePath, id);
            mav.setViewName("redirect:/listarMascotas.htm");
            return mav;
        }
        
        /************actualizar*********/
    
     @RequestMapping(value= "updateMascota.htm", method = RequestMethod.GET)
     public ModelAndView actMascota(HttpServletRequest req){
        ModelAndView mav = new ModelAndView();
        int id = Integer.parseInt(req.getParameter("id"));
        String fotoOld = req.getParameter("fotoOld");
        //recuperamos la ruta real de la ubicacion de la imagen guardada
        MascotasBean msb = consulMascotaId(id);
        msb.setFotoOld(fotoOld);
        mav.addObject("Mascota",msb);
        mav.setViewName("views/UpdateMascotas");
        return mav;
     }
     
     public MascotasBean consulMascotaId(int id){
         MascotasBean msb = new MascotasBean();
         String sql = "select * from mascotas where id = "+ id;
         return (MascotasBean)jdbcTemplate.query(
                 sql,
                 new ResultSetExtractor<MascotasBean>(){
                   @Override
                   public MascotasBean extractData(ResultSet rs) throws SQLException, DataAccessException {
                   if(rs.next()){ 
                       msb.setId(rs.getInt("id"));
                       msb.setPlaca(rs.getString("placa"));
                       msb.setNombre(rs.getString("nombre"));
                       msb.setSexo(rs.getString("sexo"));
                       msb.setRaza(rs.getString("raza"));
                       msb.setEdad(rs.getString("edad"));
                   }
                   return msb;
                   }
                 }
         );
     }
     @RequestMapping(value="updateMascota.htm", method = RequestMethod.POST)
     public ModelAndView actMascota(MascotasBean msb,HttpServletRequest req){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        ArrayList<String> listados = new ArrayList<>();
        //Determine si el atributo de carga ha sido modificado
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        //validamos si el archivo cliente ha sido modificado
        DiskFileItemFactory file = new DiskFileItemFactory();
        //le pasamos el fileitem como parametro a la variable
        ServletFileUpload fileUpload = new ServletFileUpload(file);
        List<FileItem> items = null;
        try{
            items = fileUpload.parseRequest(req);
            for (int i = 0; i < items.size(); i++){
                //System.out.println("error aca..."+ i + "-" + items.size());
                FileItem fileItem = (FileItem) items.get(i);
                listados.add(fileItem.getString());
            }
        }catch (FileUploadException ex){
            System.out.print("error en la carga de la imagen mascotaController/updateMascota..." + ex.getMessage());
        }
        if (listados.get(5).isEmpty() || listados.get(5).equals("")||listados.get(5).equals(null)){
            adoptDao.actMascotaSinFoto(msb, items);
        } else {
            adoptDao.actMascotaConImagenFoto(msb, isMultipart, req, items);
        }
        mav.setViewName("redirect:/listarMascotas.htm");
        return mav;
     }
  
     //-----------------------------CONSULTAS---------------------------------------------------------------------------------------//
     
     //-----------------------------consultar mascota por id----------------------//
     
     @RequestMapping(value="formConsultarMascotaXId.htm", method=RequestMethod.GET)
     public ModelAndView listarMascotaxId(){
         ModelAndView mav = new ModelAndView();
         MascotasBean msb = new MascotasBean();
         mav.addObject("Mascota",msb);
         mav.setViewName("views/formConsultarMascotaId");
         return mav;
     }
     @RequestMapping(value="formConsultarMascotaXId.htm", method=RequestMethod.POST)
     public ModelAndView listarMascotaxId(MascotasBean msb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        int id = msb.getId();
        mav.addObject("Mascota",adoptDao.consultarMascotaById(id));
        mav.setViewName("views/vistaConsultasMascotas");
        return mav;
     }
     
     //------------------------------consultar mascota por placa------------------------//  
     
     @RequestMapping(value="formConsultarMascotaXPlaca.htm", method=RequestMethod.GET)
     public ModelAndView listarMascotaxPlaca(){
         ModelAndView mav = new ModelAndView();
         MascotasBean msb = new MascotasBean();
         mav.addObject("Mascota",msb);
         mav.setViewName("views/formConsultarMascotaPlaca");
         return mav;
     }
     @RequestMapping(value="formConsultarMascotaXPlaca.htm", method=RequestMethod.POST)
     public ModelAndView listarMascotaxPlaca(MascotasBean msb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        String placa = msb.getPlaca();
        mav.addObject("Mascota",adoptDao.consultarMascotaByPlaca(placa));
        mav.setViewName("views/vistaConsultasMascotas");
        return mav;
     }
     
      //------------------------------consultar mascota por nombre------------------------//  
     
     @RequestMapping(value="formConsultarMascotaXNombre.htm", method=RequestMethod.GET)
     public ModelAndView listarMascotaxNombre(){
         ModelAndView mav = new ModelAndView();
         MascotasBean msb = new MascotasBean();
         mav.addObject("Mascota",msb);
         mav.setViewName("views/formConsultarMascotaNombre");
         return mav;
     }
     @RequestMapping(value="formConsultarMascotaXNombre.htm", method=RequestMethod.POST)
     public ModelAndView listarMascotaxNombre(MascotasBean msb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        String nombre = msb.getNombre();
        mav.addObject("Mascota",adoptDao.consultarMascotaByNombre(nombre));
        mav.setViewName("views/vistaConsultasMascotas");
        return mav;
     }
     
     //------------------------------consultar mascota por sexo------------------------//  
     
     @RequestMapping(value="formConsultarMascotaXSexo.htm", method=RequestMethod.GET)
     public ModelAndView listarMascotaxSexo(){
         ModelAndView mav = new ModelAndView();
         MascotasBean msb = new MascotasBean();
         mav.addObject("Mascota",msb);
         mav.setViewName("views/formConsultarMascotaSexo");
         return mav;
     }
     @RequestMapping(value="formConsultarMascotaXSexo.htm", method=RequestMethod.POST)
     public ModelAndView listarMascotaxSexo(MascotasBean msb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        String sexo = msb.getSexo();
        mav.addObject("Mascota",adoptDao.consultarMascotaBySexo(sexo));
        mav.setViewName("views/vistaConsultasMascotas");
        return mav;
     }
     
     //------------------------------consultar mascota por raza------------------------//  
     
     @RequestMapping(value="formConsultarMascotaXRaza.htm", method=RequestMethod.GET)
     public ModelAndView listarMascotaxRaza(){
         ModelAndView mav = new ModelAndView();
         MascotasBean msb = new MascotasBean();
         mav.addObject("Mascota",msb);
         mav.setViewName("views/formConsultarMascotaRaza");
         return mav;
     }
     @RequestMapping(value="formConsultarMascotaXRaza.htm", method=RequestMethod.POST)
     public ModelAndView listarMascotaxRaza(MascotasBean msb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        String raza = msb.getRaza();
        mav.addObject("Mascota",adoptDao.consultarMascotaByRaza(raza));
        mav.setViewName("views/vistaConsultasMascotas");
        return mav;
     }
     
     //------------------------------consultar mascota por edad------------------------//  
     
     @RequestMapping(value="formConsultarMascotaXEdad.htm", method=RequestMethod.GET)
     public ModelAndView listarMascotaxEdad(){
         ModelAndView mav = new ModelAndView();
         MascotasBean msb = new MascotasBean();
         mav.addObject("Mascota",msb);
         mav.setViewName("views/formConsultarMascotaEdad");
         return mav;
     }
     @RequestMapping(value="formConsultarMascotaXEdad.htm", method=RequestMethod.POST)
     public ModelAndView listarMascotaxEdad(MascotasBean msb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        AdopcionDao adoptDao = new AdopcionDao();
        String edad = msb.getEdad();
        mav.addObject("Mascota",adoptDao.consultarMascotaByEdad(edad));
        mav.setViewName("views/vistaConsultasMascotas");
        return mav;
     }
}

//----------------------------------------repositorio funcionalidades veijas----------------------------------------------------//

        
//        @RequestMapping(value="addMascota.htm", method = RequestMethod.POST)
//        public ModelAndView addmascota(@ModelAttribute("Mascota") MascotasBean msb,
//                BindingResult result, 
//                SessionStatus status){
//            {
//        ModelAndView mav = new  ModelAndView();
//        //-------------------------------------------------
//        this.validarMascotas.validate(msb, result);
//        
//        if(result.hasErrors()){
//        mav.addObject("Mascota",new MascotasBean());
//        mav.setViewName("views/addMascota");
//        return mav;
//        }
//        
//        else{
//        String sql = "insert into mascotas(placa, nombre, sexo, raza, edad) values(?,?,?,?,?)";
//        jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(), msb.getSexo(), msb.getRaza(),msb.getEdad());
//        mav.setViewName("redirect:/listarMascotas.htm");
//        return mav;
//        }
//            }
//        }
        

//-----------------------------------------------version vieja insert-----------------------------------------------------------------------//
        
//            //--------------------------cargar archivo imagen-----------------------------//
//            //ruta de lectura
//            String uploadFilePath = request.getSession().getServletContext().getRealPath("images/photos/");
//            // determine si el atributo de carga esta configurado en el formulario
//            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//            //crear variable tipo List
//            ArrayList<String> listados = new ArrayList<>();
//            if (isMultipart){
//                //creamos la instancia archivo file item
//                FileItemFactory file = new DiskFileItemFactory();
//                //pasar itemfile como parametro variable
//                ServletFileUpload fileUpload = new ServletFileUpload(file);
//                //creamos una lista con los valores pasados desde el formulario
//                List<FileItem> items = null;
//                try{
//                    items = fileUpload.parseRequest(request);
//                }   catch (FileUploadException ex) {
//                    System.out.print("carga..." + ex.getMessage());
//                }
//                for (int i = 0; i < items.size(); i++){
//                    //crear variable fileitem y el parseo de la lista
//                    //esta recorre los valores del formulario
//                    FileItem fileItem = (FileItem) items.get(i);
//                    //hacemos un condicional para saber cual variable es el archivo
//                    if(!fileItem.isFormField()){
//                        // Obtener el nombre del archivo
//                        File f = new File(fileItem.getName());
//                        // Crear una secuencia de archivo
//                        String nameFile = ("images/photos/"+ f.getName());
//                        File uploadFile = new File(uploadFilePath, f.getName());
//                        try {
//                            // Almacenar secuenciade archivo en disco
//                            fileItem.write(uploadFile);
//                            msb.setFoto(nameFile);
//                        } catch (Exception e){ 
//                            System.out.print("escritura... " + e.getMessage());
//                        }    
//                    }  else {
//                                listados.add(fileItem.getString());
//                                }
//                    }
//                    msb.setPlaca(listados.get(0));
//                    msb.setNombre(listados.get(1));
//                    msb.setSexo(listados.get(2));
//                    msb.setRaza(listados.get(3));
//                    msb.setEdad(listados.get(4));
//                }
//            //--------------------------------------------------------------------------//
//            String sql = "insert into mascotas(placa, nombre, sexo, raza, edad, foto) values(?,?,?,?,?,?)";
//        jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(), msb.getSexo(), msb.getRaza(),msb.getEdad(), msb.getFoto());
//        mav.setViewName("redirect:/listarMascotas.htm");
//        return mav;
//            }


/************borrar metodo viejo*********/
//    
//     @RequestMapping("deleteMascota.htm")
//     public ModelAndView deleteMascota( HttpServletRequest req){
//        ModelAndView mav = new ModelAndView();
//        int id = Integer.parseInt(req.getParameter("id"));
//        String sql = "delete from mascotas where id = ?";
//        jdbcTemplate.update(sql, id);
//        mav.setViewName("redirect:/listarMascotas.htm");
//        return mav;
//     }
        /************borrar metodo viejo*********/
//        
//
// /************actualizar*********/
//    
//     @RequestMapping(value= "updateMascota.htm", method = RequestMethod.GET)
//     public ModelAndView actMascota(HttpServletRequest req){
//        ModelAndView mav = new ModelAndView();
//        
//        int id = Integer.parseInt(req.getParameter("id"));
//        
//        MascotasBean msb = consulMascotaId(id);
//        
//        mav.addObject("Mascota",msb);
//        
//        mav.setViewName("views/UpdateMascotas");
//        
//        return mav;
//     }
//     
//     public MascotasBean consulMascotaId(int id){
//         MascotasBean msb = new MascotasBean();
//         String sql = "select * from mascotas where id = "+ id;
//         return (MascotasBean)jdbcTemplate.query(
//                 sql,
//                 new ResultSetExtractor<MascotasBean>(){
//                   @Override
//                   public MascotasBean extractData(ResultSet rs) throws SQLException, DataAccessException {
//                   if(rs.next()){ 
//                       msb.setId(rs.getInt("id"));
//                       msb.setPlaca(rs.getString("placa"));
//                       msb.setNombre(rs.getString("nombre"));
//                       msb.setSexo(rs.getString("sexo"));
//                       msb.setRaza(rs.getString("raza"));
//                       msb.setEdad(rs.getString("edad"));
//                   }
//                   return msb;
//                   }
//                 }
//         );
//     }
//     @RequestMapping(value="updateMascota.htm", method = RequestMethod.POST)
//     public ModelAndView actMascota(MascotasBean msb){
//        ModelAndView mav = new ModelAndView();
//        String sql = "insert into mascotas(placa, nombre, sexo, raza, edad) values(?,?,?,?,?)";
//        jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(), msb.getSexo(), msb.getRaza(),msb.getEdad());
//         
//        mav.setViewName("redirect:/listarMascotas.htm");
//        return mav;
//     }
//  
//        











//        String sql = "insert into mascotas(placa, nombre, sexo, raza, edad) values(?,?,?,?,?)";
//        jdbcTemplate.update(sql, msb.getPlaca() ,msb.getNombre(), msb.getSexo(), msb.getRaza(),msb.getEdad());
//         