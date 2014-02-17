-- Usuario de la BD sin privilegios de administracion y solo para "localhost":
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,ALTER,DROP
     ON bd_itest.*
     TO uitest@localhost
     IDENTIFIED BY 'cambiar!';
     
connect bd_itest;

INSERT INTO centros(idcentro,cod,nombre) VALUES (1,'0000000001','iTest Center');

INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (1,'00000001A','Administrador','Principal',1,'admin',md5('IadminTest'));
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (2,'00000001B','Profesor','Inicial',1,'tutor',md5('ItutorTest'));
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (3,'00000001C','Alumno','Inicial',1,'learner',md5('IlearnerTest'));
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (4,'00000001C','Kid','Inicial',1,'kid',md5('IkidTest'));

INSERT INTO `permisos` VALUES (1,'ADMIN','admin'),(2,'TUTOR','tutor'),(3,'LEARNER','learner'),(4,'KID','kid');

INSERT INTO `asignaturas` VALUES (1,'001','Asignatura Inicial','1','Estudios',NULL);

INSERT INTO grupos(idgrupo,asig,grupo,anio,centro) VALUES (1,1,'A','2006-2007',1);
INSERT INTO grupos(idgrupo,asig,grupo,anio,centro) VALUES (2,1,'B','2006-2007',1);

INSERT INTO `matriculas` VALUES (1,3,1),(2,4,2);

INSERT INTO `imparten` VALUES (1,2,1),(2,2,2);

INSERT INTO `temas` VALUES (1,1,'Tema 1 Grupo A',1),(2,2,'Tema 1 Grupo B',1);

