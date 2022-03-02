# psp-exmaen-udp-servidorCursos

Examen del 2º trimestre de UDP del módulo PSP en IES Portada Alta.

Se trata de un servidor que gestiona cursos y alumnos, puedes añadir cursos, alumnos y matricular a los alumnos.

<br>

Para poder usar el programa como cliente hay que hacer uso de un comando de linux como este:

echo -n $mensaje | nc -w1 -u localhost $puerto

ejemplo: echo -n "@nuevocur#programación@" | nc -w1 -u localhost 7600
