 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import Dao.ConectarDB;
import Dao.usuarioDao;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import models.UsuarioBean;
import models.UsuarioBeanValidation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author SENA
 */

public class usuarioController {
    
    private JdbcTemplate jdbcTemplate;
    private UsuarioBeanValidation validarUsuario;
    
    public usuarioController(){
        ConectarDB con = new ConectarDB();
        jdbcTemplate = new JdbcTemplate(con.conDB());
        this.validarUsuario = new UsuarioBeanValidation();
    }
    
    /*********************************/
    
    @RequestMapping("listarUsuarios.htm")
    public ModelAndView formUsuario(){
        ModelAndView mav = new ModelAndView();
        String sql = "select * from personas";
        List datos = jdbcTemplate.queryForList(sql);
        mav.addObject("personas",datos);
        mav.setViewName("views/listarUsuarios");
        return mav;
    }
    /************insertar*********/
    
        @RequestMapping(value="addUsuario.htm", method = RequestMethod.GET)
        public ModelAndView addusuario(){
            ModelAndView mav = new  ModelAndView();
        mav.addObject("Usuario",new UsuarioBean());
        mav.setViewName("views/addUsuario");
        return mav;
        }

        //-------------------------------version nueva insert--------------------------------------//
        private static final String UPLOAD_DIRECTORY="..\\..\\web\\images\\photos";
        
        //cargar configuracion
        
        private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; //3mb
        private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; //40mb
        private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; //50mb
        
        @RequestMapping(value="addUsuario.htm", method = RequestMethod.POST)
        public ModelAndView addusuario(UsuarioBean usb, HttpServletRequest request){
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
                        usb.setFoto(nameFile);
                        usb.setFotoOld(nameFile);
                    }catch (Exception e){
                        System.out.print("escritura... " + e.getMessage());
                    }
                } else{
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
            //--------------------------------------------------------------------------//
            String sql = "insert into personas(nombre, correo, cedula, telefono, "
                    + "direccion, ciudad, foto, fotoOld) values(?,?,?,?,?,?,?,?)";
                jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(),
                        usb.getTelefono(), usb.getDireccion(), usb.getCiudad(), usb.getFoto() , usb.getFotoOld());
                mav.setViewName("redirect:/listarUsuarios.htm");
               return mav;
            }
        //-------------------------------version nueva insert--------------------------------------//
        
        

        
        /************borrar*********/
        @RequestMapping("deleteUsuario.htm")
        public ModelAndView deleteUsuario(HttpServletRequest req){
            ModelAndView mav = new ModelAndView();
            usuarioDao usDao = new usuarioDao();
            int id = Integer.parseInt(req.getParameter("id"));
            //captura la direccion del archivo
            String deletePath = req.getServletContext().getRealPath("")+ File.separator;
            String foto = req.getParameter("foto");
            //metodo para borrar el cliente y la imagen
            usDao.borrarImagen(foto, deletePath, id);
            mav.setViewName("redirect:/listarUsuarios.htm");
            return mav;
        }
        
     
        /************actualizar*********/
    
     @RequestMapping(value= "updateUsuario.htm", method = RequestMethod.GET)
     public ModelAndView actUsuario(HttpServletRequest req){
        ModelAndView mav = new ModelAndView();
        int id = Integer.parseInt(req.getParameter("id"));
        String fotoOld = req.getParameter("fotoOld");
        //recuperamos la ruta real de la ubicacion de la imagen guardada
        UsuarioBean usb = consulUsuarioId(id);
        usb.setFotoOld(fotoOld);
        mav.addObject("Usuario",usb);
        mav.setViewName("views/updateUsuario");
        return mav;
     }
     
     public UsuarioBean consulUsuarioId(int id){
         UsuarioBean usb = new UsuarioBean();
         String sql = "select * from personas where id = "+ id;
         return (UsuarioBean)jdbcTemplate.query(
                 sql,
                 new ResultSetExtractor<UsuarioBean>(){
                   @Override
                   public UsuarioBean extractData(ResultSet rs) throws SQLException, DataAccessException {
                   if(rs.next()){ 
                       usb.setId(rs.getInt("id"));
                       usb.setNombre(rs.getString("nombre"));
                       usb.setCorreo(rs.getString("correo"));
                       usb.setCedula(rs.getString("cedula"));
                       usb.setTelefono(rs.getString("telefono"));
                       usb.setDireccion(rs.getString("direccion"));
                       usb.setCiudad(rs.getString("ciudad"));
                   }
                   return usb;
                   }
                 }
         );
     }
     @RequestMapping(value="updateUsuario.htm", method = RequestMethod.POST)
     public ModelAndView actUsuario(UsuarioBean usb, HttpServletRequest req){
        ModelAndView mav = new ModelAndView();
        usuarioDao usuDao = new usuarioDao();
        ArrayList<String> listados = new ArrayList<>();
        //determine si el atributo de carga esta configurado en el formulario
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        //validamos si el archivo usuario ha sido modificado
        DiskFileItemFactory file = new DiskFileItemFactory();
        //le pasamos el file item como parametro a la variable
        ServletFileUpload fileUpload = new ServletFileUpload(file);
        List<FileItem> items = null;
        try {
            items = fileUpload.parseRequest(req);
            for(int i = 0; i < items.size(); i++){
                //System.out.println("error aca... " + i + "-" + items.size());
                FileItem fileItem = (FileItem) items.get(i);
                listados.add(fileItem.getString());
            }
        } catch (FileUploadException ex){
            System.out.print("error en la carga de la imagen usuarioController/updateUsuario..." + ex.getMessage());
        }
        if(listados.get(5).isEmpty() || listados.get(5).equals("")|| listados.get(5).equals(null)){
            usuDao.actUsuarioSinFoto(usb,items);
        }else{
            usuDao.actUsuarioConImagenFoto(usb,isMultipart, req, items);
        }
        
        mav.setViewName("redirect:/listarUsuarios.htm");
        return mav;
     }
     
     //-----------------------------CONSULTAS---------------------------------------------------------------------------------------//
     
     //-----------------------------consultar usuario por id----------------------//
     
     @RequestMapping(value="formConsultarUsuarioXId.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxId(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioId");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXId.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxId(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        int id = usb.getId();
        mav.addObject("Usuario",usDao.consultarUsuarioById(id));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
     
     //------------------------------consultar usuario por nombre------------------------//  
     
     @RequestMapping(value="formConsultarUsuarioXNombre.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxNombre(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioNombre");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXNombre.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxNombre(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        String nombre = usb.getNombre();
        mav.addObject("Usuario",usDao.consultarUsuarioByName(nombre));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
     
     //------------------------------consultar usuario por correo------------------------//  
     
     @RequestMapping(value="formConsultarUsuarioXCorreo.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxCorreo(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioCorreo");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXCorreo.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxCorreo(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        String correo = usb.getCorreo();
        mav.addObject("Usuario",usDao.consultarUsuarioByMail(correo));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
     
     //------------------------------consultar usuario por cedula------------------------//  
     
     @RequestMapping(value="formConsultarUsuarioXCedula.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxCedula(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioCedula");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXCedula.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxCedula(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        String cedula = usb.getCedula();
        mav.addObject("Usuario",usDao.consultarUsuarioByCedula(cedula));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
     
     //------------------------------consultar usuario por telefono------------------------//  
     
     @RequestMapping(value="formConsultarUsuarioXTelefono.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxTelefono(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioTelefono");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXTelefono.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxTelefono(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        String telefono = usb.getTelefono();
        mav.addObject("Usuario",usDao.consultarUsuarioByTelefono(telefono));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
     
     //------------------------------consultar usuario por direccion------------------------//  
     
     @RequestMapping(value="formConsultarUsuarioXDireccion.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxDireccion(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioDireccion");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXDireccion.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxDireccion(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        String direccion = usb.getTelefono();
        mav.addObject("Usuario",usDao.consultarUsuarioByDireccion(direccion));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
     
     //------------------------------consultar usuario por ciudad------------------------//  
     
     @RequestMapping(value="formConsultarUsuarioXCiudad.htm", method=RequestMethod.GET)
     public ModelAndView listarUsuarioxCiudad(){
         ModelAndView mav = new ModelAndView();
         UsuarioBean usb = new UsuarioBean();
         mav.addObject("Usuario",usb);
         mav.setViewName("views/formConsultarUsuarioCiudad");
         return mav;
     }
     @RequestMapping(value="formConsultarUsuarioXCiudad.htm", method=RequestMethod.POST)
     public ModelAndView listarUsuarioxCiudad(UsuarioBean usb,BindingResult result,SessionStatus status){
        ModelAndView mav = new ModelAndView();
        usuarioDao usDao = new usuarioDao();
        String ciudad = usb.getCiudad();
        mav.addObject("Usuario",usDao.consultarUsuarioByCiudad(ciudad));
        mav.setViewName("views/vistaConsultasUsuarios");
        return mav;
     }
}

//---------------------------------------------------------repositorio de funcionalidades antiguas-------------------------------------------------//

//---------el insert de la tabla de personas estaba en el update viejo que esta copiado completo mas abajo---------------//

//        String sql = "insert into personas(nombre, correo, cedula, telefono, direccion, ciudad) values(?,?,?,?,?,?)";
//        jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(), 
//                            usb.getTelefono(), usb.getDireccion(), usb.getCiudad());
//        
//---------el insert de la tabla de personas estaba en el update viejo que esta copiado completo mas abajo---------------//

        
        /************borrar**viejo metodo*******/
//    
//     @RequestMapping("deleteUsuario.htm")
//     public ModelAndView deleteUsuario( HttpServletRequest req){
//        ModelAndView mav = new ModelAndView();
//        int id = Integer.parseInt(req.getParameter("id"));
//        String sql = "delete from personas where id = ?";
//        jdbcTemplate.update(sql, id);
//        mav.setViewName("redirect:/listarUsuarios.htm");
//        return mav;
//     }
        /************borrar**viejo metodo*******/


        
//        @RequestMapping(value="addUsuario.htm", method = RequestMethod.POST)
//        public ModelAndView addusuario(@ModelAttribute("Usuario")UsuarioBean usb,
//                 BindingResult result,
//                 SessionStatus status){ 
//            {
//            ModelAndView mav = new  ModelAndView();
//            //-----------------------------------------------------
//            this.validarUsuario.validate(usb, result);
//            
//            if(result.hasErrors()){
//            mav.addObject("Usuario", new UsuarioBean());
//            mav.setViewName("views/addUsuario");
//            return mav;
//            }
//            
//            else{    
//                String sql = "insert into personas(nombre, correo, cedula, telefono, direccion, ciudad, foto) values(?,?,?,?,?,?,?)";
//                jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(),usb.getTelefono(), usb.getDireccion(), usb.getCiudad(), usb.getFoto());
//                mav.setViewName("redirect:/listarUsuarios.htm");
//                return mav;
//            }
//        }
//        }


//-------------------------------version vieja insert--------------------------------------//
        
//        @RequestMapping(value="addUsuario.htm", method = RequestMethod.POST)
//        public ModelAndView addusuario(HttpServletRequest request){
//            UsuarioBean usb = new UsuarioBean();
//            ModelAndView mav = new ModelAndView();
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
//                }   catch (FileUploadException ex){
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
//                            usb.setFoto(nameFile);
//                        } catch (Exception e){ 
//                            System.out.print("escritura... " + e.getMessage());
//                        }    
//                    }  else {
//                                listados.add(fileItem.getString());
//                                }
//                    }
//                    usb.setNombre(listados.get(0));
//                    usb.setCorreo(listados.get(1));
//                    usb.setCedula(listados.get(2));
//                    usb.setTelefono(listados.get(3));
//                    usb.setDireccion(listados.get(4));
//                    usb.setCiudad(listados.get(5));
//                }
//            //--------------------------------------------------------------------------//
//            String sql = "insert into personas(nombre, correo, cedula, telefono, direccion, ciudad, foto) values(?,?,?,?,?,?,?)";
//                jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(),usb.getTelefono(), usb.getDireccion(), usb.getCiudad(), usb.getFoto());
//                mav.setViewName("redirect:/listarUsuarios.htm");
//               return mav;
//            }
//        //-------------------------------version vieja insert--------------------------------------//


/************actualizar***version vieja******/
//    
//     @RequestMapping(value= "updateUsuario.htm", method = RequestMethod.GET)
//     public ModelAndView actUsuario(HttpServletRequest req){
//        ModelAndView mav = new ModelAndView();
//        
//        int id = Integer.parseInt(req.getParameter("id"));
//        
//        UsuarioBean usb = consulUsuarioId(id);
//        
//        mav.addObject("Usuario",usb);
//        
//        mav.setViewName("views/updateUsuario");
//        
//        return mav;
//     }
//     
//     public UsuarioBean consulUsuarioId(int id){
//         UsuarioBean usb = new UsuarioBean();
//         String sql = "select * from personas where id = "+ id;
//         return (UsuarioBean)jdbcTemplate.query(
//                 sql,
//                 new ResultSetExtractor<UsuarioBean>(){
//                   @Override
//                   public UsuarioBean extractData(ResultSet rs) throws SQLException, DataAccessException {
//                   if(rs.next()){ 
//                       usb.setId(rs.getInt("id"));
//                       usb.setNombre(rs.getString("nombre"));
//                       usb.setCedula(rs.getString("cedula"));
//                       usb.setCorreo(rs.getString("correo"));
//                       usb.setDireccion(rs.getString("direccion"));
//                       usb.setCiudad(rs.getString("ciudad"));
//                   }
//                   return usb;
//                   }
//                 }
//         );
//     }
//     @RequestMapping(value="updateUsuario.htm", method = RequestMethod.POST)
//     public ModelAndView actUsuario(UsuarioBean usb){
//        ModelAndView mav = new ModelAndView();
//        String sql = "insert into personas(nombre, correo, cedula, telefono, direccion, ciudad) values(?,?,?,?,?,?)";
//        jdbcTemplate.update(sql, usb.getNombre(), usb.getCorreo(), usb.getCedula(), 
//                            usb.getTelefono(), usb.getDireccion(), usb.getCiudad());
//        
//        mav.setViewName("redirect:/listarUsuarios.htm");
//        return mav;
//     }
