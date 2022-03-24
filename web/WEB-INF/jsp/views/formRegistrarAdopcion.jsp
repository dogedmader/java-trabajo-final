<%-- 
    Document   : formRegistrarAdopcion
    Created on : 3/03/2022, 07:42:09 AM
    Author     : SENA
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%---CSS only----%>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
       <h1 style="text-align: center;">Adopciones</h1>
       <div class="panel panel-primary" style="margin: auto; width: 50%">
           <div class="panel-heading"></div>
           <table class="table table-hover">
               
               <thead>
                   <tr>
               <th>Id del formulario</th>
               <th>Fecha de adopcion</th>
               <th>Id de la mascota</th>
               <th>Id de la persona que adopta</th>
                   </tr>
               </thead>
       
       </div>
       
       <c:forEach items="${adopcion}" var="datos">
      
   <tr class="text-center">
        <td> <c:out value="${datos.id_formulario}" > </c:out> </td>
        <td> <c:out value="${datos.fecha_adopcion}" > </c:out> </td>
        <td> <c:out value="${datos.id_mascotas}" > </c:out> </td>
        <td> <c:out value="${datos.id_personas}" > </c:out> </td>
        
        <td> <a href="deleteAdopcion.htm?id=${datos.id_formulario}"class="btn btn-danger my-2">Borrar adopción</a></td>
        
        
        
        <%--<td> <a href="deleteAdopcion.htm?id=${datos.id_fomulario}"class="btn btn-danger my-2">Borrar mascota</a></td>--%>
        
        <td> <a href="updateAdopcion.htm?id=${datos.id_fomulario}"class="btn btn-warning my-2">Actualizar adopción</a></td>
        
    </tr>
        </c:forEach>
         <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
         <a href="addAdopcion.htm" class="btn btn-primary my-2">Registrar Adopción</a>
         
        <br>
            </table> 
    </body>
</html>
