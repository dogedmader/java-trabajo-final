-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-03-2022 a las 02:07:27
-- Versión del servidor: 10.4.21-MariaDB
-- Versión de PHP: 8.0.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `adopta`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adopcion`
--

CREATE TABLE `adopcion` (
  `id_formulario` int(11) NOT NULL,
  `fecha_adopcion` varchar(255) NOT NULL,
  `id_mascotas` int(11) NOT NULL,
  `id_personas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `adopcion`
--

INSERT INTO `adopcion` (`id_formulario`, `fecha_adopcion`, `id_mascotas`, `id_personas`) VALUES
(4, '12/8/2013', 10, 1),
(5, '16/10/2018', 1, 1),
(7, '19/5/2035', 1, 1),
(8, '19/5/2035', 4, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mascotas`
--

CREATE TABLE `mascotas` (
  `id` int(11) NOT NULL,
  `placa` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `sexo` varchar(255) NOT NULL,
  `raza` varchar(255) NOT NULL,
  `edad` varchar(255) NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `fotoOld` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `mascotas`
--

INSERT INTO `mascotas` (`id`, `placa`, `nombre`, `sexo`, `raza`, `edad`, `foto`, `fotoOld`) VALUES
(1, '1341646513', 'roki', 'macho', 'corgi', '5', NULL, ''),
(2, '1341646513', 'roki', 'macho', 'corgi', '6', NULL, ''),
(4, '13246543', 'jorge', 'macho', 'border coling', '4 ', NULL, ''),
(6, '13246543', 'jorge', 'macho', 'border coling', '4', NULL, ''),
(10, '13246543', 'jorge', 'macho', 'border coling', '6', NULL, ''),
(11, '1649616164', 'pinkie', 'macho', 'podle', '5', 'images/photos/razas-de-perros.jpg', ''),
(15, '351684', 'mario', 'macho', 'corgi', '15', 'images/photos/ID-351684-husky-siberiano-redes.jpg', 'images/photos/ID-351684-husky-siberiano-redes.jpg'),
(18, '351684', 'mariano', 'macho', 'corgi', '15', 'images/photos/ID-351684-husky-siberiano-redes.jpg', 'images/photos/ID-351684-husky-siberiano-redes.jpg'),
(20, '351684', 'mario', 'macho', 'corgi', '10', 'images/photos/ID-351684-husky-siberiano-redes.jpg', 'images/photos/ID-351684-husky-siberiano-redes.jpg'),
(24, '351684', 'mario', 'macho', 'bulldog', '16', 'images/photos/ID-351684-Lista-razas-perros-problemas-salud_1579952059_139471912_667x375.jpg', 'images/photos/ID-351684-Lista-razas-perros-problemas-salud_1579952059_139471912_667x375.jpg'),
(28, '351685', 'mario', 'macho', 'corgi', '19', 'images/photos/ID-351685-Lista-razas-perros-problemas-salud_1579952059_139471912_667x375.jpg', 'images/photos/ID-351685-Lista-razas-perros-problemas-salud_1579952059_139471912_667x375.jpg'),
(29, '134613', 'lupe', 'macho', 'pastor aleman', '10', 'images/photos/ID-lupe-descarga.jfif', 'images/photos/ID-lupe-descarga.jfif');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personas`
--

CREATE TABLE `personas` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `cedula` varchar(255) NOT NULL,
  `telefono` varchar(255) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `ciudad` varchar(255) NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `fotoOld` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `personas`
--

INSERT INTO `personas` (`id`, `nombre`, `correo`, `cedula`, `telefono`, `direccion`, `ciudad`, `foto`, `fotoOld`) VALUES
(1, 'og simpson', 'ogkillersimpson@gmail.com', '53135164', '1531562', '4v64wev4q68', 'alabama', NULL, NULL),
(12, 'jorge', 'nada@nada.com', '13211631', '35146345', '3B 48-9 este', 'atlantis', NULL, NULL),
(13, 'jorge', 'nada@nada.com', '13211631', '35146345', '3B 48-9 este', 'atlantis', NULL, NULL),
(17, 'jose', 'nada@nada.com', 'vwevwsv', '35146345', '3B 48-9 este', 'toronto', 'images/photos/ID-jose-', 'images/photos/ID-jose-LACTEOS CBA.png'),
(20, 'milton', '516516164986', 'fbi@gmail.com', '135461987', '15 d miracle street', 'toronto', 'images/photos/captura de pantalla.png', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `adopcion`
--
ALTER TABLE `adopcion`
  ADD PRIMARY KEY (`id_formulario`),
  ADD KEY `id macotas` (`id_mascotas`),
  ADD KEY `id personas` (`id_personas`);

--
-- Indices de la tabla `mascotas`
--
ALTER TABLE `mascotas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `personas`
--
ALTER TABLE `personas`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `adopcion`
--
ALTER TABLE `adopcion`
  MODIFY `id_formulario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `mascotas`
--
ALTER TABLE `mascotas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT de la tabla `personas`
--
ALTER TABLE `personas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `adopcion`
--
ALTER TABLE `adopcion`
  ADD CONSTRAINT `adopcion_ibfk_1` FOREIGN KEY (`id_personas`) REFERENCES `personas` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `adopcion_ibfk_2` FOREIGN KEY (`id_mascotas`) REFERENCES `mascotas` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
