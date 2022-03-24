<%-- 
    Document   : login
    Created on : 15/02/2022, 05:41:52 PM
    Author     : F&F
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.88.1">
    <title>Login</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/5.1/examples/sign-in/">

    

    <!-- Bootstrap core CSS -->
<link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>

    
    <!-- Custom styles for this template -->
    <link href="signin.css" rel="stylesheet">
  </head>
  <body class="text-center">
    
<main class="form-signin">
  <form>
    <img class="mb-4" src="../assets/brand/bootstrap-logo.svg" alt="" width="72" height="57">
    <h1 class="h3 mb-3 fw-normal">Por favor inicie sesion para continuar</h1>

    <div class="form-floating">
      <input type="email" class="form-control" placeholder="nombre@ejemplo.com">
      <label for="floatingInput">Correo</label>
    </div>
    <div class="form-floating">
      <input type="password" class="form-control" placeholder="Contraseña">
      <label for="floatingPassword">Contraseña</label>
    </div>

    <button class="w-100 btn btn-lg btn-primary" type="submit">Iniciar sesión</button>

  </form>
</main>


    
  </body>
</html>

