// JavaScript Document

var tiempoRestante;
var tiempoTotal;
var redireccion;
var visible;
var showtext;
var hidetext;
var endtext;
var basepath;
var intervalo;

function tiempoExamen(tiempo,total,red,base,show,hide,end)
{	
	tiempoRestante = tiempo;
	tiempoTotal = total;
	redireccion=red;
	visible=true;
	showtext=show;
	hidetext=hide;
	basepath=base;
	endtext=end;
	mostrarReloj();
}

function startExam() {
	intervalo=setInterval("refrescarReloj()",1000);
	//remove the div that hides the exam
	document.getElementById("divHideExam").style.display = "none";
	document.getElementById("endButton").style.display = "inline";
}

function mostrarReloj() {
	porcentaje = Math.ceil(100 * tiempoRestante / tiempoTotal);
	document.getElementById("tiempoGastado").style.width = (100 - porcentaje) + "%";
	document.getElementById("tiempoRestante").style.width = porcentaje + "%";
}

function refrescarReloj() 
{
	tiempoRestante--;
	
	mostrarReloj();
	
	if(tiempoRestante <= 0) 
	{
		window.clearInterval(intervalo);
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