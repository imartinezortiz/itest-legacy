-- MySQL dump 10.13  Distrib 5.1.41, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bd_itest
-- ------------------------------------------------------
-- Server version	5.1.41-3ubuntu12.6-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asignaturas`
--

DROP TABLE IF EXISTS `asignaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asignaturas` (
  `idasig` int(11) NOT NULL AUTO_INCREMENT,
  `cod` varchar(10) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `curso` varchar(2) NOT NULL,
  `estudios` varchar(20) NOT NULL,
  `comentarios` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idasig`),
  UNIQUE KEY `cod` (`cod`)
) ENGINE=InnoDB AUTO_INCREMENT=296 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `califs`
--

DROP TABLE IF EXISTS `califs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `califs` (
  `idcalif` int(11) NOT NULL AUTO_INCREMENT,
  `alu` int(11) NOT NULL,
  `exam` int(11) NOT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `nota` decimal(6,2) DEFAULT NULL,
  `fecha_ini` datetime DEFAULT NULL,
  `fecha_fin` datetime DEFAULT NULL,
  `tiempo` int(8) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idcalif`),
  UNIQUE KEY `aluexam` (`alu`,`exam`),
  KEY `califs_ibfk_2` (`exam`),
  CONSTRAINT `califs_ibfk_1` FOREIGN KEY (`alu`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `califs_ibfk_2` FOREIGN KEY (`exam`) REFERENCES `examenes` (`idexam`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42005 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `centros`
--

DROP TABLE IF EXISTS `centros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `centros` (
  `idcentro` int(11) NOT NULL AUTO_INCREMENT,
  `cod` varchar(10) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `localidad` varchar(50) DEFAULT NULL,
  `cpostal` varchar(5) DEFAULT NULL,
  `provincia` varchar(15) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `web` varchar(100) DEFAULT NULL,
  `p_contacto` varchar(60) DEFAULT NULL,
  `tlf_contacto` varchar(20) DEFAULT NULL,
  `titulacion` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`idcentro`),
  UNIQUE KEY `cod` (`cod`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `centros_estudios`
--

DROP TABLE IF EXISTS `centros_estudios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `centros_estudios` (
  `idcen` int(11) NOT NULL AUTO_INCREMENT,
  `centro` int(11) NOT NULL,
  `estudio` varchar(40) NOT NULL,
  PRIMARY KEY (`idcen`),
  KEY `centro` (`centro`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conexiones`
--

DROP TABLE IF EXISTS `conexiones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conexiones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idusuario` int(11) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ip` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idusuario` (`idusuario`)
) ENGINE=MyISAM AUTO_INCREMENT=51092 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cuestionarios`
--

DROP TABLE IF EXISTS `cuestionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuestionarios` (
  `idcuest` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) DEFAULT NULL,
  `instrucciones` text,
  `tipo_usuario` varchar(16) DEFAULT 'ANY',
  `fecha_ini` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `fecha_fin` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `visibilidad` tinyint(4) NOT NULL DEFAULT '0',
  `centro` int(11) DEFAULT NULL,
  `grupo` int(11) DEFAULT NULL,
  `idcreador` int(11) NOT NULL,
  PRIMARY KEY (`idcuest`),
  KEY `cuestionarios_ibfk_1` (`centro`),
  KEY `cuestionarios_ibfk_2` (`grupo`),
  KEY `cuestionarios_ibfk_3` (`idcreador`),
  CONSTRAINT `cuestionarios_ibfk_1` FOREIGN KEY (`centro`) REFERENCES `centros` (`idcentro`) ON DELETE CASCADE,
  CONSTRAINT `cuestionarios_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE,
  CONSTRAINT `cuestionarios_ibfk_3` FOREIGN KEY (`idcreador`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exam_individ`
--

DROP TABLE IF EXISTS `exam_individ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exam_individ` (
  `idexami` int(11) NOT NULL AUTO_INCREMENT,
  `alumno` int(11) NOT NULL,
  `examen` int(11) NOT NULL,
  PRIMARY KEY (`idexami`),
  KEY `alumno` (`alumno`),
  KEY `examen` (`examen`)
) ENGINE=MyISAM AUTO_INCREMENT=1143 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `examenes`
--

DROP TABLE IF EXISTS `examenes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `examenes` (
  `idexam` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(60) DEFAULT NULL,
  `grupo` int(11) NOT NULL,
  `visibilidad` tinyint(4) NOT NULL DEFAULT '0',
  `distrib_pregs` tinyint(4) NOT NULL DEFAULT '0',
  `duracion` int(8) NOT NULL,
  `fecha_ini` datetime DEFAULT NULL,
  `fecha_fin` datetime DEFAULT NULL,
  `nota_max` float(4,2) NOT NULL DEFAULT '10.00',
  `peso_exam` tinyint(4) DEFAULT NULL,
  `rev_activa` tinyint(4) NOT NULL DEFAULT '0',
  `corr_parcial` tinyint(4) NOT NULL DEFAULT '1',
  `muestra_num_corr` tinyint(4) NOT NULL DEFAULT '1',
  `p_preg_fallada` double NOT NULL DEFAULT '0',
  `p_preg_no_resp` double NOT NULL DEFAULT '0',
  `p_resp_fallada` double NOT NULL DEFAULT '0',
  `cota_calif_preg` double NOT NULL DEFAULT '0',
  `fecha_ini_rev` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `fecha_fin_rev` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `r_nivel_confianza` double NOT NULL DEFAULT '0',
  `publicado` tinyint(4) NOT NULL DEFAULT '0',
  `p_nivel_confianza` double NOT NULL DEFAULT '0',
  `nivelConfianza` tinyint(4) NOT NULL DEFAULT '0',
  `personalizado` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`idexam`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `examenes_ibfk_1` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3715 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `extra_pregs`
--

DROP TABLE IF EXISTS `extra_pregs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `extra_pregs` (
  `idextrap` int(11) NOT NULL AUTO_INCREMENT,
  `preg` int(11) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `tipo` tinyint(4) DEFAULT NULL,
  `orden` tinyint(4) DEFAULT NULL,
  `nombre` varchar(40) NOT NULL,
  `ancho` varchar(8) DEFAULT NULL,
  `alto` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`idextrap`),
  KEY `preg` (`preg`),
  CONSTRAINT `extra_pregs_ibfk_1` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20711 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `extra_pregs_comentario`
--

DROP TABLE IF EXISTS `extra_pregs_comentario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `extra_pregs_comentario` (
  `idextrapcom` int(11) NOT NULL AUTO_INCREMENT,
  `preg` int(11) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `tipo` tinyint(4) DEFAULT NULL,
  `orden` tinyint(4) DEFAULT NULL,
  `nombre` varchar(40) NOT NULL,
  `ancho` varchar(8) DEFAULT NULL,
  `alto` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`idextrapcom`),
  KEY `preg` (`preg`),
  CONSTRAINT `extra_pregs_com_ibfk_1` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `extra_resps`
--

DROP TABLE IF EXISTS `extra_resps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `extra_resps` (
  `idextrar` int(11) NOT NULL AUTO_INCREMENT,
  `resp` int(11) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `tipo` tinyint(4) DEFAULT NULL,
  `orden` tinyint(4) DEFAULT NULL,
  `nombre` varchar(40) NOT NULL,
  `ancho` varchar(8) DEFAULT NULL,
  `alto` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`idextrar`),
  KEY `resp` (`resp`),
  CONSTRAINT `extra_resps_ibfk_1` FOREIGN KEY (`resp`) REFERENCES `respuestas` (`idresp`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5510 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupos`
--

DROP TABLE IF EXISTS `grupos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupos` (
  `idgrupo` int(11) NOT NULL AUTO_INCREMENT,
  `asig` int(11) NOT NULL,
  `grupo` varchar(4) NOT NULL DEFAULT 'A',
  `anio` varchar(9) NOT NULL,
  `centro` int(11) NOT NULL,
  `tipo_alumno` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`idgrupo`),
  KEY `asig` (`asig`),
  KEY `centro` (`centro`),
  CONSTRAINT `grupos_ibfk_1` FOREIGN KEY (`asig`) REFERENCES `asignaturas` (`idasig`) ON DELETE CASCADE,
  CONSTRAINT `grupos_ibfk_2` FOREIGN KEY (`centro`) REFERENCES `centros` (`idcentro`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=846 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imparten`
--

DROP TABLE IF EXISTS `imparten`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imparten` (
  `idimp` int(11) NOT NULL AUTO_INCREMENT,
  `profe` int(11) NOT NULL,
  `grupo` int(11) NOT NULL,
  PRIMARY KEY (`idimp`),
  KEY `profe` (`profe`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `imparten_ibfk_1` FOREIGN KEY (`profe`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `imparten_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1211 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_exams`
--

DROP TABLE IF EXISTS `log_exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_exams` (
  `idlogexams` int(11) NOT NULL AUTO_INCREMENT,
  `exam` int(11) NOT NULL,
  `alu` int(11) NOT NULL,
  `preg` int(11) NOT NULL,
  `resp` int(11) NOT NULL,
  `marcada` tinyint(4) NOT NULL DEFAULT '0',
  `puntos` decimal(6,2) DEFAULT NULL,
  `hora_resp` datetime DEFAULT NULL,
  `nivel_confianza` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idlogexams`),
  KEY `exam` (`exam`),
  KEY `alu` (`alu`),
  KEY `preg` (`preg`),
  KEY `resp` (`resp`),
  CONSTRAINT `log_exams_ibfk_1` FOREIGN KEY (`alu`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `log_exams_ibfk_2` FOREIGN KEY (`exam`) REFERENCES `examenes` (`idexam`) ON DELETE CASCADE,
  CONSTRAINT `log_exams_ibfk_3` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE,
  CONSTRAINT `log_exams_ibfk_4` FOREIGN KEY (`resp`) REFERENCES `respuestas` (`idresp`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2561111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `matriculas`
--

DROP TABLE IF EXISTS `matriculas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `matriculas` (
  `idmat` int(11) NOT NULL AUTO_INCREMENT,
  `alumno` int(11) NOT NULL,
  `grupo` int(11) NOT NULL,
  PRIMARY KEY (`idmat`),
  KEY `alumno` (`alumno`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `matriculas_ibfk_1` FOREIGN KEY (`alumno`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `matriculas_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22633 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permisos` (
  `idper` int(11) NOT NULL AUTO_INCREMENT,
  `permiso` varchar(16) NOT NULL DEFAULT '',
  `usuario` varchar(20) NOT NULL,
  PRIMARY KEY (`idper`),
  KEY `FK_permisos_1` (`usuario`),
  CONSTRAINT `permisos_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9264 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `preguntas`
--

DROP TABLE IF EXISTS `preguntas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preguntas` (
  `idpreg` int(11) NOT NULL AUTO_INCREMENT,
  `enunciado` text NOT NULL,
  `dificultad` tinyint(4) NOT NULL DEFAULT '0',
  `tema` int(11) NOT NULL,
  `grupo` int(11) NOT NULL,
  `visibilidad` tinyint(4) NOT NULL DEFAULT '0',
  `comentario` text,
  `activa` tinyint(4) NOT NULL DEFAULT '1',
  `n_resp_correctas` tinyint(4) NOT NULL DEFAULT '1',
  `used_in_exam` tinyint(4) NOT NULL DEFAULT '0',
  `topic` int(11) DEFAULT NULL,
  `titulo` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`idpreg`),
  KEY `tema` (`tema`),
  KEY `grupo` (`grupo`),
  KEY `topic` (`topic`),
  CONSTRAINT `preguntas_ibfk_1` FOREIGN KEY (`tema`) REFERENCES `temas` (`idtema`) ON DELETE CASCADE,
  CONSTRAINT `preguntas_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE,
  CONSTRAINT `preguntas_ibfk_3` FOREIGN KEY (`topic`) REFERENCES `topics` (`idtopic`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=93385 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `preguntas_cuestionarios`
--

DROP TABLE IF EXISTS `preguntas_cuestionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preguntas_cuestionarios` (
  `idpreg` int(11) NOT NULL AUTO_INCREMENT,
  `enunciado` text NOT NULL,
  `seccion` varchar(50) DEFAULT '',
  `resp_si_no` tinyint(4) NOT NULL DEFAULT '0',
  `valor_maximo` tinyint(4) DEFAULT NULL,
  `orden` int(11) DEFAULT NULL,
  `cuestionario` int(11) NOT NULL,
  PRIMARY KEY (`idpreg`),
  KEY `preguntas_cuestionarios_ibfk_1` (`cuestionario`)
) ENGINE=MyISAM AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respuestas`
--

DROP TABLE IF EXISTS `respuestas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `respuestas` (
  `idresp` int(11) NOT NULL AUTO_INCREMENT,
  `texto` text NOT NULL,
  `preg` int(11) NOT NULL,
  `solucion` tinyint(4) NOT NULL DEFAULT '0',
  `valor` tinyint(4) NOT NULL DEFAULT '0',
  `activa` tinyint(4) NOT NULL DEFAULT '1',
  `used_in_exam_question` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idresp`),
  KEY `preg` (`preg`),
  KEY `i1_respuestas` (`preg`),
  CONSTRAINT `respuestas_ibfk_1` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=355150 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `respuestas_cuestionarios`
--

DROP TABLE IF EXISTS `respuestas_cuestionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `respuestas_cuestionarios` (
  `idresp` int(11) NOT NULL AUTO_INCREMENT,
  `etiqueta` varchar(20) DEFAULT '',
  `valor` tinyint(4) DEFAULT '0',
  `orden` int(11) DEFAULT NULL,
  `respondidas` int(11) DEFAULT '0',
  `pregunta` int(11) NOT NULL,
  PRIMARY KEY (`idresp`),
  KEY `respuestas_cuestionarios_ibfk_1` (`pregunta`)
) ENGINE=MyISAM AUTO_INCREMENT=205 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `results_cuestionarios`
--

DROP TABLE IF EXISTS `results_cuestionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `results_cuestionarios` (
  `idresult` int(11) NOT NULL AUTO_INCREMENT,
  `cuestionario` int(11) NOT NULL,
  `anio` varchar(9) DEFAULT '',
  `nivel_educativo` varchar(30) DEFAULT '',
  `sexo` varchar(6) DEFAULT '',
  `centro` int(11) DEFAULT NULL,
  `asignatura` varchar(50) DEFAULT '',
  `departamento` varchar(100) DEFAULT '',
  `tipo_usuario` varchar(16) DEFAULT '',
  `usuario` int(11) DEFAULT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idresult`),
  KEY `results_cuestionarios_ibfk_1` (`cuestionario`),
  KEY `results_cuestionarios_ibfk_2` (`centro`),
  KEY `results_cuestionarios_ibfk_3` (`usuario`)
) ENGINE=MyISAM AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temas`
--

DROP TABLE IF EXISTS `temas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temas` (
  `idtema` int(11) NOT NULL AUTO_INCREMENT,
  `tema` varchar(60) NOT NULL,
  `orden` tinyint(4) NOT NULL,
  `grupo` int(11) NOT NULL,
  PRIMARY KEY (`idtema`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `temas_ibfk_1` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8642 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temas_exam`
--

DROP TABLE IF EXISTS `temas_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temas_exam` (
  `idtemaexam` int(11) NOT NULL AUTO_INCREMENT,
  `exam` int(11) NOT NULL,
  `tema` int(11) NOT NULL,
  `n_pregs` tinyint(4) NOT NULL,
  `n_resp_x_preg` tinyint(4) DEFAULT NULL,
  `dificultad_max` tinyint(4) NOT NULL DEFAULT '0',
  `dificultad_min` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idtemaexam`),
  KEY `exam` (`exam`),
  KEY `tema` (`tema`),
  CONSTRAINT `temas_exam_ibfk_3` FOREIGN KEY (`exam`) REFERENCES `examenes` (`idexam`) ON DELETE CASCADE,
  CONSTRAINT `temas_exam_ibfk_4` FOREIGN KEY (`tema`) REFERENCES `temas` (`idtema`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16526 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topics` (
  `idtopic` int(11) NOT NULL AUTO_INCREMENT,
  `asig` int(11) NOT NULL,
  `topic` varchar(60) NOT NULL,
  `orden` tinyint(4) NOT NULL,
  PRIMARY KEY (`idtopic`),
  KEY `asig` (`asig`),
  CONSTRAINT `topics_ibfk_1` FOREIGN KEY (`asig`) REFERENCES `asignaturas` (`idasig`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `idusu` int(11) NOT NULL AUTO_INCREMENT,
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `apes` varchar(50) NOT NULL,
  `centro` int(11) NOT NULL,
  `email` varchar(40) DEFAULT NULL,
  `usuario` varchar(20) NOT NULL,
  `passw` varchar(32) DEFAULT NULL,
  `ruta_foto` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`idusu`),
  UNIQUE KEY `usuario` (`usuario`),
  KEY `centro` (`centro`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`centro`) REFERENCES `centros` (`idcentro`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9720 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-01-02 22:00:52
