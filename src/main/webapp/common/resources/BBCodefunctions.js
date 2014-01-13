function merge(txt,id){
	id.innerHTML = id.innerHTML + txt +'[br]';
}

function parse2HTML(txt,id){
	var str = txt;
	str = replaceSpaceMath(str);
	//str = replaceSpaceInv(str);
	str = replace1(str);
	str = replace2(str);
	str = replaceBold(str);
	str = replaceTagU(str);
	str = replaceTagS(str);
	str = replaceTagBR(str);
	str = replacePicutrue(str);
	str = replaceLink(str);
	str = replaceFontColor(str);
	id.innerHTML = str;
}


function replaceSpaceInv(str){
	while(str.indexOf('\f')!=-1){
		str = str.replace('\f','\\f');
	}
	while(str.indexOf('\t')!=-1){
		str = str.replace('\t','\\t');
	}
	while(str.indexOf('\r')!=-1){
		str = str.replace('\r','\\r');
	}
	return str;
}

function replaceSpaceMath(str){
	while(str.indexOf('\space')!=-1){
		str = str.replace('\space','&nbsp;');
	}
	return str;
}

function replace1(str){
	while(str.indexOf('<')!=-1){
		str = str.replace('<','&lt;');
	}
	return str;
}

function replace2(str){
	while(str.indexOf('>')!=-1){
		str = str.replace('>','&gt;');
	}
	return str;
}

function replaceBold(str){
	while(str.indexOf('[b]')!=-1){
		str = str.replace('[b]','<b>');
		str = str.replace('[/b]','</b>');
	}
	return str;
}

function replaceTagU(str){
	while(str.indexOf('[em]')!=-1){
		str = str.replace('[em]','<em>');
		str = str.replace('[/em]','</em>');
	}
	return str;
}

function replaceTagS(str){
	while(str.indexOf('[del]')!=-1){
		str = str.replace('[del]','<u>');
		str = str.replace('[/del]','</u>');
	}
	return str;
}
function replaceTagBR(str){
	while(str.indexOf('[br]')!=-1){
		str = str.replace('[br]','<br/>');	

	}
	return str;
}
function replaceEOF(str){
	while(str.indexOf('\n')!=-1){
		str = str.replace('\n','<br/>');	
	}
	return str;
}
function replacePicutrue(str){
	var start = str.indexOf('[img]');
	var end = str.indexOf('[/img]');
	end+=6;
	
	while(start!=-1 && end!=-1){
		var valor_seleccionado = str.substring(start,end);
		valor_seleccionado = valor_seleccionado.replace('[img]','<img alt="Imagen no disponible" style="width:30px;" src="'+valor_seleccionado.substring(5,valor_seleccionado.length-6)+'"/>')
		valor_seleccionado = valor_seleccionado.replace('[/img]','');
		var pos = valor_seleccionado.indexOf('/>');
		pos+=2;
		valor_seleccionado = valor_seleccionado.substring(0,pos);
		str = str.substring(0,start)+valor_seleccionado+str.substring(end,str.length);
		start = str.indexOf('[img]');
		end = str.indexOf('[/img]');
		end+=6;
		var valor_seleccionado = str.substring(start,end);
	}
	
	return str;
}



function replaceLink(str){
	var start = str.indexOf('[a]');
	var end = str.indexOf('[/a]');
	while(start!=-1 && end!=-1){
		end+=4;
		var valor_seleccionado = str.substring(start+3,end);
		var startDir = valor_seleccionado.indexOf('[href]');
		var endDir = valor_seleccionado.indexOf('[/href]');
		var href = valor_seleccionado.substring(startDir+6,endDir);
		var startText = valor_seleccionado.indexOf('[text]');
		var endText = valor_seleccionado.indexOf('[/text]');
		var text = valor_seleccionado.substring(startText+6,endText);
		str = str.substring(0,start)+'<a href="'+href+'" target="_new">'+text+'</a>'+str.substring(end,str.length);
		start = str.indexOf('[a]');
		end = str.lastIndexOf('[/a]');
	}
	return str;
}

function replaceFontColor(str){
	var start = str.indexOf('[font]');
	var end = str.indexOf('[/font]');
	while(start!=-1 && end!=-1){
		end+=7;
		var valor_seleccionado = str.substring(start+6,end);
		var startColor = valor_seleccionado.indexOf('[color]');
		var endColor = valor_seleccionado.indexOf('[/color]');
		var color = valor_seleccionado.substring(startColor+7,endColor);
		var startText = valor_seleccionado.indexOf('[/color]');
		var endText = valor_seleccionado.indexOf('[/font]');
		var text = valor_seleccionado.substring(startText+8,endText);
		// compruebo que el color tiene que ser rojo azul o verde, sino lo pongo por defecto blanco
		if(color!='red'&&color!='blue'&&color!='green'){
			color = 'black';
		}
		str = str.substring(0,start)+'<font color="'+color+'">'+text+'</font>'+str.substring(end,str.length);
		start = str.indexOf('[font]');
		end = str.lastIndexOf('[/font]');
	}
	return str;
}

function getSelectedText(inputObj, tagInicial, tagFinal){
	var o = inputObj;
	var start = o.selectionStart;
	var end = o.selectionEnd;
	var valor_seleccionado = o.value.substring(start, end);
	//----------------------------------------------------------
	if(start==end){
		return null;
	}else{
		return o.value.substring(0, start)+tagInicial+valor_seleccionado+tagFinal+o.value.substring(end, o.value.length);
	}
}

function tagBold(inputObj,result){
	var valor_seleccionado = getSelectedText(inputObj,'[b]','[/b]');
	//----------------------------------------------------------
	if(valor_seleccionado==null){
		inputObj.value+='[b]Escribir aqui el texto en negrita[/b]';
	}else{
		inputObj.value=valor_seleccionado;
	}
	inputObj.focus();
}

function tagU(inputObj,result){
	var valor_seleccionado = getSelectedText(inputObj,'[em]','[/em]');
	if(valor_seleccionado==null){
		inputObj.value+='[em]Escribir aqui el texto en cursiva[/em]';
	}else{
		inputObj.value=valor_seleccionado;
	}
	inputObj.focus();
}

function tagS(inputObj,result){
	var valor_seleccionado = getSelectedText(inputObj,'[del]','[/del]');
	if(valor_seleccionado==null){
		inputObj.value+='[del]Escribir aqui el texto en subrayado[/del]';
	}else{
		inputObj.value=valor_seleccionado;
	}
	inputObj.focus();
}

function tagBr(inputObj,result){
	inputObj.value+='[br]';
	inputObj.focus();
}

function addPicture(inputObj,result){
	var imagen = prompt('Introduce la url de la imagen','http://');
	if(imagen!=null)
		inputObj.value+='[img]'+imagen+' [/img]';
	inputObj.focus();
}


function addLink(inputObj,result){
	var link = prompt('Introduce la url del link','http://');
	if(link!=null){
		var linkTxt =prompt('Introduce el texto que quieres que aparezca en el link');
		if(linkTxt!=null)
			inputObj.value+='[a]'+'[href]'+link+'[/href][text]'+linkTxt+'[/text][/a]';
	}
		inputObj.focus();
	}
}

function getColor(inputObj,color,bau){
	//var color = bau.name;
	var valor_seleccionado = getSelectedText(inputObj,'[font][color]'+color+'[/color]','[/font]');
	if(valor_seleccionado == null){
		inputObj.value+='[font][color]'+color+'[/color]escribir aqui el texto '+color+'[/font]';
	}else{
		inputObj.value=valor_seleccionado;
	}
	inputObj.focus();
}

