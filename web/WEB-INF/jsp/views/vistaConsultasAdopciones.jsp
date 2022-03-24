<%-- 
    Document   : vistaConsultasAdopciones
    Created on : 12/03/2022, 08:41:14 AM
    Author     : F&F
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

    </tr>
        </c:forEach>
         <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
         
        <br>
            </table> 
    </body>
</html>
