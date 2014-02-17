-- Creacion de la BD con conjunto de caracteres UTF-8:
-- CREATE DATABASE bd_itest DEFAULT CHARACTER SET utf8;


--
-- Estructura de la tabla `centros`
--
DROP TABLE IF EXISTS `centros`;
CREATE TABLE `centros` (
  `idcentro` int(11) NOT NULL auto_increment,
  `cod` varchar(10) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(50) default NULL,
  `localidad` varchar(50) default NULL,
  `cpostal` varchar(5) default NULL,
  `provincia` varchar(15) default NULL,
  `telefono` varchar(20) default NULL,
  `fax` varchar(20) default NULL,
  `web` varchar(100) default NULL,
  `tlf_contacto` varchar(20) default NULL,
  `email` varchar(50) default NULL,
  `p_contacto` varchar(60) default NULL,
  `titulacion` varchar(60) default '',
  PRIMARY KEY  (`idcentro`),
  UNIQUE KEY `cod` (`cod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Datos iniciales para la tabla `centros`
--
INSERT INTO centros(idcentro,cod,nombre) VALUES (1,'0000000001','iTest Center');


--
-- Estructura de la tabla `centros_estudios`
--
DROP TABLE IF EXISTS `centros_estudios`;
CREATE TABLE centros_estudios (
  idcen int(11) NOT NULL auto_increment,
  centro int(11) NOT NULL,
  estudio varchar(40) NOT NULL,
  PRIMARY KEY (idcen),
  FOREIGN KEY (centro) REFERENCES centros(idcentro) ON DELETE CASCADE
);

--
-- Estructura de la tabla `usuarios`
--
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `idusu` int(11) NOT NULL auto_increment,
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `apes` varchar(50) NOT NULL,
  `centro` int(11) NOT NULL,
  `email` varchar(40) default NULL,
  `usuario` varchar(20) NOT NULL,
  `passw` varchar(32) default NULL,
  `ruta_foto` varchar(30) default NULL,
  PRIMARY KEY  (`idusu`),
  UNIQUE KEY `usuario` (`usuario`),
  KEY `centro` (`centro`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`centro`) REFERENCES `centros` (`idcentro`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Datos iniciales para la tabla `usuarios`
--
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (1,'00000001A','Administrador','Principal',1,'admin',md5('IadminTest'));
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (2,'00000001B','Profesor','Inicial',1,'tutor',md5('ItutorTest'));
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (3,'00000001C','Alumno','Inicial',1,'learner',md5('IlearnerTest'));
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (4,'00000001C','Kid','Inicial',1,'kid',md5('IkidTest'));


--
-- Estructura de la tabla `permisos`
--
DROP TABLE IF EXISTS `permisos`;
CREATE TABLE `permisos` (
  `idper` int(11) NOT NULL auto_increment,
  `permiso` varchar(16) NOT NULL default '',
  `usuario` varchar(20) NOT NULL,
  PRIMARY KEY  (`idper`),
  KEY `FK_permisos_1` (`usuario`),
  CONSTRAINT `permisos_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Datos iniciales para la tabla `permisos`
--
INSERT INTO `permisos` VALUES (1,'ADMIN','admin'),(2,'TUTOR','tutor'),(3,'LEARNER','learner'),(4,'KID','kid');


--
-- Estructura de la tabla `asignaturas`
--
DROP TABLE IF EXISTS `asignaturas`;
CREATE TABLE `asignaturas` (
  `idasig` int(11) NOT NULL auto_increment,
  `cod` varchar(10) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `curso` varchar(2) NOT NULL,
  `estudios` varchar(20) NOT NULL,
  `comentarios` varchar(250) default NULL,
  PRIMARY KEY  (`idasig`),
  UNIQUE KEY `cod` (`cod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Datos iniciales para la tabla `asignaturas`
--
INSERT INTO `asignaturas` VALUES (1,'001','Asignatura Inicial','1','Estudios',NULL);


--
-- Estructura de la tabla `grupos`
--
DROP TABLE IF EXISTS `grupos`;
CREATE TABLE `grupos` (
  `idgrupo` int(11) NOT NULL auto_increment,
  `asig` int(11) NOT NULL,
  `grupo` varchar(4) NOT NULL default 'A',
  `anio` varchar(9) NOT NULL,
  `centro` int(11) NOT NULL,
  `tipo_alumno` tinyint(4) default '0',
  PRIMARY KEY  (`idgrupo`),
  KEY `asig` (`asig`),
  KEY `centro` (`centro`),
  CONSTRAINT `grupos_ibfk_1` FOREIGN KEY (`asig`) REFERENCES `asignaturas` (`idasig`) ON DELETE CASCADE,
  CONSTRAINT `grupos_ibfk_2` FOREIGN KEY (`centro`) REFERENCES `centros` (`idcentro`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Datos iniciales para la tabla `grupos`
--
INSERT INTO grupos(idgrupo,asig,grupo,anio,centro) VALUES (1,1,'A','2006-2007',1);
INSERT INTO grupos(idgrupo,asig,grupo,anio,centro) VALUES (2,1,'B','2006-2007',1);



--
-- Estructura de la tabla `matriculas`
--
DROP TABLE IF EXISTS `matriculas`;
CREATE TABLE `matriculas` (
  `idmat` int(11) NOT NULL auto_increment,
  `alumno` int(11) NOT NULL,
  `grupo` int(11) NOT NULL,
  PRIMARY KEY  (`idmat`),
  KEY `alumno` (`alumno`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `matriculas_ibfk_1` FOREIGN KEY (`alumno`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `matriculas_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Datos iniciales para la tabla `matriculas`
--
INSERT INTO `matriculas` VALUES (1,3,1),(2,4,2);


--
-- Estructura de la tabla `imparten`
--
DROP TABLE IF EXISTS `imparten`;
CREATE TABLE `imparten` (
  `idimp` int(11) NOT NULL auto_increment,
  `profe` int(11) NOT NULL,
  `grupo` int(11) NOT NULL,
  PRIMARY KEY  (`idimp`),
  KEY `profe` (`profe`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `imparten_ibfk_1` FOREIGN KEY (`profe`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `imparten_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Datos iniciales para la tabla `imparten`
--
INSERT INTO `imparten` VALUES (1,2,1),(2,2,2);


--
-- Estructura de la tabla `temas`
--
DROP TABLE IF EXISTS `temas`;
CREATE TABLE `temas` (
  `idtema` int(11) NOT NULL auto_increment,
  `grupo` int(11) NOT NULL,
  `tema` varchar(60) NOT NULL,
  `orden` tinyint(4) NOT NULL,
  PRIMARY KEY  (`idtema`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `temas_ibfk_1` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Datos iniciales para la tabla `temas`
--
INSERT INTO `temas` VALUES (1,1,'Tema 1 Grupo A',1),(2,2,'Tema 1 Grupo B',1);


--
-- Estructura de la tabla `topics`
--
DROP TABLE IF EXISTS `topics`;
CREATE TABLE `topics` (
  `idtopic` int(11) NOT NULL auto_increment,
  `asig` int(11) NOT NULL,
  `topic` varchar(60) NOT NULL,
  `orden` tinyint(4) NOT NULL,
  PRIMARY KEY  (`idtopic`),
  KEY `asig` (`asig`),
  CONSTRAINT `topics_ibfk_1` FOREIGN KEY (`asig`) REFERENCES `asignaturas` (`idasig`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Estructura de la tabla `preguntas`
--
DROP TABLE IF EXISTS `preguntas`;
CREATE TABLE `preguntas` (
  `idpreg` int(11) NOT NULL auto_increment,
  `enunciado` text NOT NULL,
  `dificultad` tinyint(4) NOT NULL default '0',
  `tema` int(11) NOT NULL,
  `grupo` int(11) NOT NULL,
  `visibilidad` tinyint(4) NOT NULL default '0',
  `comentario` text,
  `activa` tinyint(4) NOT NULL default '1',
  `n_resp_correctas` tinyint(4) NOT NULL default '1',
  `used_in_exam` tinyint(4) NOT NULL default '0',
  `topic` int(11) default NULL,
  `titulo` varchar(60) default NULL,
  `tipo` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY  (`idpreg`),
  KEY `tema` (`tema`),
  KEY `grupo` (`grupo`),
  KEY `topic` (`topic`),
  CONSTRAINT `preguntas_ibfk_3` FOREIGN KEY (`topic`) REFERENCES `topics` (`idtopic`) ON DELETE SET NULL,
  CONSTRAINT `preguntas_ibfk_1` FOREIGN KEY (`tema`) REFERENCES `temas` (`idtema`) ON DELETE CASCADE,
  CONSTRAINT `preguntas_ibfk_2` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `extra_pregs`
--
DROP TABLE IF EXISTS `extra_pregs`;
CREATE TABLE `extra_pregs` (
  `idextrap` int(11) NOT NULL auto_increment,
  `preg` int(11) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `tipo` tinyint(4) default NULL,
  `orden` tinyint(4) default NULL,
  `nombre` varchar(40) NOT NULL,
  `ancho` varchar(8) default NULL,
  `alto` varchar(8) default NULL,
  `geogebra_type` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY  (`idextrap`),
  KEY `preg` (`preg`),
  CONSTRAINT `extra_pregs_ibfk_1` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `extra_pregs_comentario`
--
DROP TABLE IF EXISTS `extra_pregs_comentario`;
CREATE TABLE `extra_pregs_comentario` (
  `idextrapcom` int(11) NOT NULL auto_increment,
  `preg` int(11) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `tipo` tinyint(4) default NULL,
  `orden` tinyint(4) default NULL,
  `nombre` varchar(40) NOT NULL,
  `ancho` varchar(8) default NULL,
  `alto` varchar(8) default NULL,
  PRIMARY KEY  (`idextrapcom`),
  KEY `preg` (`preg`),
  CONSTRAINT `extra_pregs_com_ibfk_1` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `respuestas`
--
DROP TABLE IF EXISTS `respuestas`;
CREATE TABLE `respuestas` (
  `idresp` int(11) NOT NULL auto_increment,
  `texto` text NOT NULL,
  `preg` int(11) NOT NULL,
  `solucion` tinyint(4) NOT NULL default '0',
  `valor` tinyint(4) NOT NULL default '0',
  `activa` tinyint(4) NOT NULL default '1',
  `used_in_exam_question` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`idresp`),
  KEY `preg` (`preg`),
  CONSTRAINT `respuestas_ibfk_1` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `extra_resps`
--
DROP TABLE IF EXISTS `extra_resps`;
CREATE TABLE `extra_resps` (
  `idextrar` int(11) NOT NULL auto_increment,
  `resp` int(11) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `tipo` tinyint(4) default NULL,
  `orden` tinyint(4) default NULL,
  `nombre` varchar(40) NOT NULL,
  `ancho` varchar(8) default NULL,
  `alto` varchar(8) default NULL,
  PRIMARY KEY  (`idextrar`),
  KEY `resp` (`resp`),
  CONSTRAINT `extra_resps_ibfk_1` FOREIGN KEY (`resp`) REFERENCES `respuestas` (`idresp`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `examenes`
--
DROP TABLE IF EXISTS `examenes`;
CREATE TABLE `examenes` (
  `idexam` int(11) NOT NULL auto_increment,
  `titulo` varchar(60) default NULL,
  `grupo` int(11) NOT NULL,
  `visibilidad` tinyint(4) NOT NULL default '0',
  `distrib_pregs` tinyint(4) NOT NULL default '0',
  `duracion` int(8) NOT NULL,
  `fecha_ini` datetime default NULL,
  `fecha_fin` datetime default NULL,
  `nota_max` float(4,2) NOT NULL DEFAULT '10.00',
  `peso_exam` tinyint(4) default NULL,
  `rev_activa` tinyint(4) NOT NULL default '0',
  `corr_parcial` tinyint(4) NOT NULL default '1',
  `muestra_num_corr` tinyint(4) NOT NULL default '1',
  `p_preg_fallada` double NOT NULL default '0',
  `p_preg_no_resp` double NOT NULL default '0',
  `p_resp_fallada` double NOT NULL default '0',
  `cota_calif_preg` double NOT NULL default '0',
  `fecha_ini_rev` datetime NOT NULL default '1970-01-01 00:00:00',
  `fecha_fin_rev` datetime NOT NULL default '1970-01-01 00:00:00',
  `r_nivel_confianza` double NOT NULL DEFAULT '0',
  `publicado` tinyint(4) NOT NULL DEFAULT '0',
  `p_nivel_confianza` double NOT NULL DEFAULT '0',
  `nivelConfianza` tinyint(4) NOT NULL DEFAULT '0',
  `personalizado` tinyint default '0',
  PRIMARY KEY  (`idexam`),
  KEY `grupo` (`grupo`),
  CONSTRAINT `examenes_ibfk_1` FOREIGN KEY (`grupo`) REFERENCES `grupos` (`idgrupo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `temas_exam`
--
DROP TABLE IF EXISTS `temas_exam`;
CREATE TABLE `temas_exam` (
  `idtemaexam` int(11) NOT NULL auto_increment,
  `exam` int(11) NOT NULL,
  `tema` int(11) NOT NULL,
  `n_pregs` tinyint(4) NOT NULL,
  `n_resp_x_preg` tinyint(4) default NULL,
  `dificultad_max` tinyint(4) NOT NULL default '0',
  `dificultad_min` tinyint(4) NOT NULL default '0',
  `tipo_pregs` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY  (`idtemaexam`),
  KEY `exam` (`exam`),
  KEY `tema` (`tema`),
  CONSTRAINT `temas_exam_ibfk_2` FOREIGN KEY (`tema`) REFERENCES `temas` (`idtema`) ON DELETE CASCADE,
  CONSTRAINT `temas_exam_ibfk_3` FOREIGN KEY (`exam`) REFERENCES `examenes` (`idexam`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Estructura de la tabla `log_exams`
--
DROP TABLE IF EXISTS `log_exams`;
CREATE TABLE `log_exams` (
  `idlogexams` int(11) NOT NULL auto_increment,
  `exam` int(11) NOT NULL,
  `alu` int(11) NOT NULL,
  `preg` int(11) NOT NULL,
  `resp` int(11) NOT NULL,
  `marcada` tinyint(4) NOT NULL default '0',
  `puntos` decimal(6,2) default NULL,
  `hora_resp` datetime default NULL,
  `nivel_confianza` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY  (`idlogexams`),
  KEY `exam` (`exam`),
  KEY `alu` (`alu`),
  KEY `preg` (`preg`),
  KEY `resp` (`resp`),
  CONSTRAINT `log_exams_ibfk_1` FOREIGN KEY (`exam`) REFERENCES `examenes` (`idexam`) ON DELETE CASCADE,
  CONSTRAINT `log_exams_ibfk_2` FOREIGN KEY (`alu`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `log_exams_ibfk_3` FOREIGN KEY (`preg`) REFERENCES `preguntas` (`idpreg`) ON DELETE CASCADE,
  CONSTRAINT `log_exams_ibfk_4` FOREIGN KEY (`resp`) REFERENCES `respuestas` (`idresp`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `califs`
--
DROP TABLE IF EXISTS `califs`;
CREATE TABLE `califs` (
  `idcalif` int(11) NOT NULL auto_increment,
  `alu` int(11) NOT NULL,
  `exam` int(11) NOT NULL,
  `ip` varchar(15) default NULL,
  `nota` decimal(6,2) default NULL,
  `fecha_ini` datetime default NULL,
  `fecha_fin` datetime default NULL,
  `tiempo` int(8) NOT NULL default '0',
  PRIMARY KEY  (`idcalif`),
  UNIQUE KEY `aluexam` (`alu`,`exam`),
  KEY `califs_ibfk_2` (`exam`),
  CONSTRAINT `califs_ibfk_1` FOREIGN KEY (`alu`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE,
  CONSTRAINT `califs_ibfk_2` FOREIGN KEY (`exam`) REFERENCES `examenes` (`idexam`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Estructura de la tabla `conexiones`
--
DROP TABLE IF EXISTS `conexiones`;
CREATE TABLE `conexiones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idusuario` int(11) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ip` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idusuario` (`idusuario`)
) ENGINE=MyISAM AUTO_INCREMENT=536 DEFAULT CHARSET=utf8;

--
-- Estructura de la tabla `exam_individ`
--
DROP TABLE IF EXISTS `exam_individ`
CREATE TABLE `exam_individ`(
  `idexami` int(11) NOT NULL auto_increment,
  `alumno` int(11) NOT NULL,
  `examen` int(11) NOT NULL,
  PRIMARY KEY (`idexami`),
  FOREIGN KEY (`alumno`) REFERENCES `usuarios`(`idusu`) ON DELETE CASCADE,
  FOREIGN KEY (`examen`) REFERENCES `examenes`(`idexam`) ON DELETE CASCADE
)ENGINE = InnoDB;

--
-- Estructura de la tabla `log_exams_fill`
--
CREATE TABLE log_exams_fill(
  -- Campos provenientes de examenes
  idlogexamsfill int(11) NOT NULL auto_increment,
  exam int(11) NOT NULL,     -- Examen
  alu int(11) NOT NULL,      -- Alumno
  preg int(11) NOT NULL,     -- Pregunta
  resp TEXT,
  puntos DECIMAL(6,2),
  hora_resp DATETIME,        -- Fecha y hora de respuesta
  nivel_confianza tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (idlogexamsfill),
  FOREIGN KEY (exam) REFERENCES examenes(idexam) ON DELETE CASCADE,
  FOREIGN KEY (alu) REFERENCES usuarios(idusu) ON DELETE CASCADE,
  FOREIGN KEY (preg) REFERENCES preguntas(idpreg) ON DELETE CASCADE
);

--
-- Estructura de la tabla `recupera_pass` 
--
CREATE TABLE `recupera_pass` (
  `idrec` int(11) NOT NULL auto_increment,
  `idusu` int(11) NOT NULL,
  `token` varchar(32) NOT NULL,
  `fecha_insert` datetime NOT NULL,
  `fecha_caducidad` datetime NOT NULL,
  `fecha_cambio` datetime default NULL,
  PRIMARY KEY  (`idrec`),
  UNIQUE KEY `token` (`token`),
  KEY `idusu` (`idusu`),
  CONSTRAINT `recupera_pass_ibfk_1` FOREIGN KEY (`idusu`) REFERENCES `usuarios` (`idusu`) ON DELETE CASCADE
);

-- Indices:
-- Log_exam
create index i1_log_exams on log_exams(exam,alu,preg);
-- Respuestas
create index i1_respuestas on respuestas(preg);
