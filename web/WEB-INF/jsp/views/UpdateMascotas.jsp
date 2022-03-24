<%-- 
    Document   : UpdateMascotas
    Created on : 28/02/2022, 05:27:08 PM
    Author     : F&F
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
            
    <%---CSS only----%>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    
     
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actualizar Mascota</title>
    </head>
    
    <body class="bg-secondary">
        <section class="d-flex justify-content-center">
            
            <form:form commandName="Mascota" enctype="multipart/form-data">
                <br>
                <form:errors path="*" element="div" class="alert alert-danger"></form:errors>
                <br>
                <form:label path="placa">Numero de placa</form:label>
                <form:input class="form-control" path="placa"></form:input>
                <br>
                <form:label path="nombre">Nombre del animal</form:label>
                <form:input class="form-control" path="nombre"></form:input>
                <br>
                <form:label path="sexo">Sexo del animal</form:label>
                <form:input class="form-control" path="sexo"></form:input>
                <br>
                <form:label path="raza">Raza del animal</form:label>
                <form:input class="form-control" path="raza"></form:input>
                <br>
                <form:label path="edad">Edad del animal</form:label>
                <form:input class="form-control" path="edad"></form:input>
                <br>
                <form:label path="foto">Adjunte aqui su foto</form:label>
                <img id="fotoOld"  name="fotoOld" style="height: 50px; width: 50px;"
                     src='<c:url value="${Mascota.fotoOld}"/>'/> 
                <form:input type="file" path="foto" class="form-control"></form:input>
                <br>
                
                
                <form:button name="Enviar" class="btn btn-primary my-2">Enviar</form:button>
                
                <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
                <a href="listarMascotas.htm" class="btn btn-primary my-2">Listar Mascota</a>
            </form:form>
               
         </section> 
    </body>
</html>
