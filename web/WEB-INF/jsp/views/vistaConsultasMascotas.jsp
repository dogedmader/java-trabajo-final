<%-- 
    Document   : vistaConsultasMascotas
    Created on : 12/03/2022, 08:41:48 AM
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
       <h1 style="text-align: center;">Mascotas</h1>
       <div class="panel panel-primary" style="margin: auto; width: 50%">
           <div class="panel-heading"></div>
           <table class="table table-hover">
               
               <thead>
                   <tr>
               <th>Id</th>
               <th>Placa</th>
               <th>Nombre</th>
               <th>Sexo</th>
               <th>Raza</th>
               <th>Años</th>
               <th>Foto</th>
               <th>opciones</th>
                   </tr>
               </thead>
       
       </div>
       
       <c:forEach items="${mascotas}" var="mascotas">
      
   <tr class="text-center">
        <td> <c:out value="${mascotas.id}" > </c:out> </td>
        <td> <c:out value="${mascotas.placa}" > </c:out> </td>
        <td> <c:out value="${mascotas.nombre}" > </c:out> </td>
        <td> <c:out value="${mascotas.sexo}" > </c:out> </td>
        <td> <c:out value="${mascotas.raza}" > </c:out> </td>
        <td> <c:out value="${mascotas.edad}" > </c:out> </td>
        
        <td>    
            <img style="height: 50px; width: 50px;" src='<c:url value="${mascotas.foto}"/>' /> 
        </td>
        
    </tr>
        </c:forEach>
         <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
        <br>
            </table> 
    </body>
</html>
