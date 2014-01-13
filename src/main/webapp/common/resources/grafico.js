function createTable(porcentajes, leyenda, valores, alto, ancho, titulos){
	if(!(porcentajes.length == leyenda.length && leyenda.length == valores.length)){
		alert('Con los valores recibidos no se puede crear la tabla');
		return null;
	}
	var anchoColumna = 100/porcentajes.length;
	var columnas = porcentajes.length;
	var tablaHtml = '<center> <div style="width: '+ancho+'; height:'+alto+'; background-color:#B40404;">';
	tablaHtml += '<table style="width: 100%; height: 100%; text-align: center;">';
	var tablaTitulos ='';
	if(titulos != null){
		for(var i=0;i<titulos.length;i++){
			tablaTitulos +='<tr><td colspan="'+columnas+'" style="background-color:white;">'+titulos[i]+'</td></tr>';
		}
	}
	var tablaPorcentajes ='<tr style="background-color:#CEF6F5; height:80%">';
	for(var i=0;i<columnas;i++){
		tablaPorcentajes+= '<td style="vertical-align: bottom; height: 58%; width: '+anchoColumna+'%; "> <table style="width:90%; height: '+porcentajes[i]+'%" align="center"><tr><td class="mitabla" style="width:50%;">'+porcentajes[i]+'%</td></tr></table></td>'
	}
	tablaPorcentajes+='</tr>';
	var tablaLeyenda = '<tr style="height: 10%">';
	for(var i=0;i<columnas;i++){
		tablaLeyenda +='<td style="vertical-align: bottom; height: 58%; width: '+anchoColumna+'%; " class="leyenda">'+leyenda[i]+'</td>';
	}
	tablaLeyenda += '</tr>';
	
	var tablaValores='<tr style="height: 10%">';
	for(var i=0;i<columnas;i++){
		tablaValores +='<td style="vertical-align: bottom; height: 58%; width: '+anchoColumna+'%; " class="leyenda">'+valores[i]+'</td>';
	}
	tablaValores+='</tr>';
	tablaHtml+=tablaTitulos+tablaPorcentajes+tablaLeyenda+tablaValores;
	tablaHtml += '</table></div></center>';
	return tablaHtml;
}