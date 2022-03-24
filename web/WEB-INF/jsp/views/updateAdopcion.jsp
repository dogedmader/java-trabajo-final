<%-- 
    Document   : updateAdopcion
    Created on : 9/03/2022, 06:59:00 PM
    Author     : F&F
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
      <%---CSS only----%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actualizar Usuario</title>
    </head>
    <body class="bg-secondary"> 
        <section class="d-flex justify-content-center">
            
            <form:form commandName="adopcion">
                <br>
                <form:errors path="*" element="div" class="alert alert-danger"></form:errors>
                <br>
                <form:label path="id_formulario">id</form:label>
                <form:input class="form-control" path="id_formulario"></form:input>
                <br>
                <form:label path="fecha_adopcion">fecha</form:label>
                <form:input class="form-control" path="fecha_adopcion"></form:input>
                <br>
                <form:label path="id_mascotas">id mascota</form:label>
                <form:input class="form-control" path="id_mascotas"></form:input>
                <br>
                <form:label path="id_personas">id persona</form:label>
                <form:input class="form-control" path="id_personas"></form:input>
                <br>
                
                <form:button name="Enviar" class="btn btn-primary my-2">Enviar</form:button>
                
                <a href="index.htm" class="btn btn-primary my-2">Regresar</a>
                <a href="formRegistrarAdopcion.htm" class="btn btn-primary my-2">ListarUsuarios</a>
                
            </form:form>
                
         </section>       
        
    </body>
</html>
