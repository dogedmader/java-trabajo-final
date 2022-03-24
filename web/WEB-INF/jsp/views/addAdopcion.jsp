<%-- 
    Document   : addAdopcion
    Created on : 5/03/2022, 08:13:36 PM
    Author     : F&F
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<%---CSS only----%>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario Registro Adopción</title>
    </head>
    
    <body class="bg-secondary"> 
        <section class="d-flex justify-content-center">
            
            <form:form commandName="adopcion" method="post">
                <br>
                <form:errors path="*" element="div" class="alert alert-danger"></form:errors>
                <br>
                <form:label path="id_formulario">Id del formulario</form:label>
                <form:input class="form-control" path="id_formulario"></form:input>
                <br>
                
                <%----------------------------------------%>
                <form:label path="fecha_adopcion">Fecha de la adopción</form:label>
                <form:input class="form-control" path="fecha_adopcion"></form:input>
                <br>
                
                <form:label path="id_mascotas">Codigo de la mascota</form:label>
                <form:select class="form-control" path="id_mascotas">
                    <c:forEach var="data" items="${listaMascotas}">
                        <option value="${data.id}">${data.nombre}</OPTION>
                    </c:forEach>
                </form:select>
                <br>
                <%----------------------------------------%>
                <form:label path="id_personas">Codigo de la persona que adopta</form:label>
                <form:select class="form-control" path="id_personas">
                    <c:forEach var="dato" items="${listaPersonas}">
                        <option value="${dato.id}">${dato.nombre}</OPTION>
                    </c:forEach>
                </form:select>
                <br>
                
                <%---------------------------------------------------------------------%>
                
                <form:button name="Enviar" class="btn btn-primary my-2">Enviar</form:button>
                
                <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
                <a href="formRegistrarAdopcion.htm" class="btn btn-primary my-2">Lista de Adopciones Registradas</a>
            </form:form>
                
         </section>       
        
    </body>          
    
</html>
