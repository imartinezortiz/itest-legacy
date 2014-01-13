CREATE USER itest IDENTIFIED BY 'itest';

INSERT INTO centros(idcentro,cod,nombre,direccion,localidad,cpostal,provincia) VALUES (1, 28300, 'CES Felipe II', 'C/ Capitán, 39', 'Aranjuez', '28300', 'Madrid');
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (1, 'nd', 'Admin', 'iTest', 1,'admin','21232f297a57a5a743894a0e4a801fc3');
INSERT INTO permisos VALUES (1,'ADMIN','admin');
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (2, 'nd', 'Tutor', 'iTest', 1,'tutor','1f6f42334e1709a4e0f9922ad789912b');
INSERT INTO permisos VALUES (2,'TUTOR','tutor');
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (3, 'nd', 'Learner', 'iTest', 1,'learner','c0a24b98e089b6b0f5d3674430cebe0c');
INSERT INTO permisos VALUES (3,'LEARNER','learner');
INSERT INTO usuarios(idusu,dni,nombre,apes,centro,usuario,passw) VALUES (4, 'nd', 'TutorAV', 'iTest', 1,'tutorav','e7b4b64b9ca9b78e22e67fefc2755701');
INSERT INTO permisos VALUES (4,'TUTORAV','tutorav');

