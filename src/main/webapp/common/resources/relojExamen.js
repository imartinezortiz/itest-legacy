// JavaScript Document

var recuentoMinutos;
var recuentoSegundos;
var recuentoHoras;
var redireccion;
var visible;
var showtext;
var hidetext;
var endtext;
var basepath;
var intervalo;

function tiempoExamen(tiempo,red,base,show,hide,end)
{	
	recuentoHoras = Math.floor(tiempo / 3600); // Horas de examen
	recuentoMinutos = Math.floor(tiempo / 60) % 60; // Minutos de examen 
	recuentoSegundos = tiempo % 60; // Segundos de examen
	redireccion=red;
	visible=true;
	showtext=show;
	hidetext=hide;
	basepath=base;
	endtext=end;
	document.cd.disp.value = mostrarReloj(recuentoHoras,recuentoMinutos,recuentoSegundos); 
}

function startExam() {
	intervalo=setInterval("refrescarReloj()",1000);
	//remove the div that hides the exam
	document.getElementById("divHideExam").style.display = "none";
	document.getElementById("endButton").style.display = "inline";
}

function horas(obj) 
{
	for(var i = 0; i < obj.length; i++) 
	{
		if(obj.substring(i, i + 1) == ":")
		break;
	}

	return(obj.substring(0, i)); 
}

function minutos(obj) 
{
	for(var i = 0; i < obj.length; i++) 
	{
		if(obj.substring(i, i + 1) == ":")
		break;
	}

	return(obj.substring(0, i)); 
}

function segundos(obj) 
{
	for(var i = 0; i < obj.length; i++) 
	{
		if(obj.substring(i, i + 1) == ":")
		break;
	}

	return(obj.substring(i + 1, obj.length)); 
}

function mostrarReloj(recuentoHoras,recuentoMinutos,recuentoSegundos) 
{
	var disp;
	
	if (!visible) {
		disp="--:--:--";	
		return(disp);
	}
	
	if(recuentoHoras <= 9)
	{
		disp = "0";
	}
	else
	{
		disp = "";
	}
	
	disp += recuentoHoras + ":";
	
	if(recuentoMinutos <= 9) 
	{
		disp += "0";
	}
	else
	{
		disp += "";
	}
	
	disp += recuentoMinutos + ":";
	
	if(recuentoSegundos <= 9) 
	{
		disp += "0" + recuentoSegundos;
	}
	else 
	{
		disp += recuentoSegundos;
	}
	
	return(disp); 
}

function refrescarReloj() 
{
	recuentoSegundos--;
	
	if((recuentoMinutos == 0) && (recuentoSegundos == -1))
	{
		recuentoMinutos = 60;
		recuentoHoras--;
	}
	
	if(recuentoSegundos == -1) 
	{
		recuentoSegundos = 59; 
		recuentoMinutos--; 		
		
		if (recuentoHoras==0 && (recuentoMinutos == 4 || recuentoMinutos == 0) && !visible)
			switchClock();
	}
	
	document.cd.disp.value = mostrarReloj(recuentoHoras,recuentoMinutos,recuentoSegundos);
	
	if((recuentoHoras == 0) && (recuentoMinutos == 0) && (recuentoSegundos == 0)) 
	{
		window.alert(endtext);
		window.location = redireccion;
	}
}

function switchClock() {
	visible = !visible;
	var img = document.getElementById("imgvisible");
	if (visible) {
		img.src = basepath + "/visible.gif";
		img.title = hidetext;
		img.alt = hidetext;
	}
	else {
		img.src = basepath + "/invisible.gif";
		img.title = showtext;
		img.alt = showtext;
	}
	document.cd.disp.value = mostrarReloj(recuentoHoras,recuentoMinutos,recuentoSegundos);
}