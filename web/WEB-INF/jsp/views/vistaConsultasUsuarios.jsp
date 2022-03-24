<%-- 
    Document   : vistaConsultasUsuarios
    Created on : 12/03/2022, 08:39:53 AM
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
       <%---- nombre: <% String nombre = (String)request.getAttribute("n"); %>
        <%= nombre %>
        
        <br>
        
        correo: <% String correo = (String)request.getAttribute("c"); %>
        <%= correo %>
        ------%>
       <h1 style="text-align: center;">Usuarios</h1>
       <div class="panel panel-primary" style="margin: auto; width: 50%">
           <div class="panel-heading"></div>
           <table class="table table-hover">
               
               <thead>
                   <tr>
               <th>Id</th>
               <th>Nombre</th>
               <th>Correo</th>
               <th>Cedula</th>
               <th>Telefono</th>
               <th>Direccion</th>
               <th>Ciudad</th>
               <th>Foto</th>
               <th>opciones</th>
                   </tr>
               </thead>
       
       </div>
       
       <c:forEach items="${Usuario}" var="personas">
      
   <tr class="text-center">
           <td> <c:out value="${personas.id}" > </c:out> </td>
          <td>  <c:out value="${personas.nombre}" > </c:out> </td>
        <td>    <c:out value="${personas.correo}" > </c:out> </td>
        <td>    <c:out value="${personas.cedula}" > </c:out> </td>
         <td>   <c:out value="${personas.telefono}" > </c:out> </td>
        <td>    <c:out value="${personas.direccion}" > </c:out> </td>
        <td>    <c:out value="${personas.ciudad}" > </c:out> </td>
        
        <td>    
            <img style="height: 50px; width: 50px;" src='<c:url value="${personas.foto}"/>' /> 
        </td>             
        
    </tr>
        </c:forEach>
         <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
         
         
        <br>
            </table>
           
    </body>
</html>
