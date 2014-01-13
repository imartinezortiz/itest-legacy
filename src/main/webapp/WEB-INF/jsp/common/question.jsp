<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/BBCodecolorPicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/BBCodefunctions.js"></script>	
<script type="text/javascript">
	function resizeImage(){
		var imagenes = document.getElementsByName('imageMM');
		for(var i=0;i<imagenes.length;i++){
			var height = imagenes[i].style.height;
			var lastChar = height[height.length-1];
			if(height!=''){
				if(lastChar=='%'){
					var value = height.substring(0, height.length-1)
					value = ((screen.height-200)*value)/100;
					if(value>screen.height-200){
						value = screen.height-200;
						imagenes[i].style.height= value+'px';
					}else{
						imagenes[i].style.height= value+'px';
					}
				}else{
					var value = height.substring(0, height.length-2)
					if(value>screen.height-200){
						value = screen.height-200;
						imagenes[i].style.height= value+'px';
					}else{
						imagenes[i].style.height= value+'px';
					}
				}
			}
		}
	}

	function fullScreenApplet(ob,rowApplet){
		var menu = document.getElementById('menu');
		var cabecera = document.getElementById('cabecera');
		var div = document.createElement('div');
		if(document.getElementById('contenido')!=null)
			var center2 = document.getElementById('contenido').getElementsByTagName('center')[0];
		if(center2!=null)
			center2.style.display = 'none';
		div.className="floatingDiv";
		div.id="floatingDivGeogebra";
		div.style.display='';
		div.style.backgroundColor = 'white';
		div.width=screen.width;
		div.height=screen.height;
		div.style.position="absolute";
		var head = document.createElement('div');
			head.width ='100%';
			head.style.backgroundColor='red';
			var center = document.createElement('center');
			var boton = document.createElement('input');
				boton.value="<fmt:message key="hideGeogebraDiv"/>";
				boton.type='button';
				boton.onclick = function(){
					ob.height='375px';
					ob.width='500px';
					rowApplet.appendChild(ob);
					div.parentNode.removeChild(div);
					if(center2!=null)
						center2.style.display = '';
					ob.focus();
				}
			center.appendChild(boton);
			head.appendChild(center);
		
		var body = document.createElement('div');
			body.id="appletDiv";
			body.style.width = '100%';
			body.style.height= '90%';
			body.appendChild(ob);
			ob.height='95%';
			ob.width='95%';
		
		var height = 0;
		if(cabecera!=null)
			height += cabecera.height;
		if(menu!=null)
			height += menu.height;
		div.appendChild(head);
		div.appendChild(body);
		if(document.getElementById('contenido'))
			document.getElementById('contenido').appendChild(div);
		else
			document.body.appendChild(div);
	}

</script>

<p class="tituloPregunta">(<fmt:message key="labelId"/> <c:out value="${question.id}"/>) <fmt:message key="question"/><c:out value="${param.numQuestion}"/><c:if test="${not empty param.questionsNumber}">/<c:out value="${param.questionsNumber}"/></c:if> 
<c:choose>
	<c:when test="${param.view eq 'review'}">
		: <fmt_rt:formatNumber type="number" maxFractionDigits="2" value="${question.questionGrade}"/> <fmt:message key="points"/>
	</c:when>
	<c:otherwise>
	<c:choose>
		<c:when test="${param.showCorrectAnswers and question.type eq 0}">
			- (<c:out value="${question.numCorrectAnswers}"/> <fmt:message key="answers"/>)
		</c:when>
	</c:choose>
	</c:otherwise>
</c:choose>
</p>
<div id=question${question.id} class="preguntaExamen">
	<div class="preguntaArriba">
		
	</div>
	<div class="preguntaCuerpo">
	<p id="pcheckQuestion${question.id}" class="enunciadoPregunta">
		<p id="questionText${question.id}" class="enunciadoPregunta">
			<c:forEach items="${question.textParagraphs}" var="paragraph">
				<c:out value="${paragraph}" escapeXml="true"/>[br]
			</c:forEach>
		</p>
	</p>
	<script>
		parse2HTML(document.getElementById('questionText'+${question.id}).innerHTML,document.getElementById('questionText'+${question.id}));
	</script>
	<!-- comprobación de si son exactamente dos imágenes y nada más, y no está configurado a mano el tamaño de ninguna-->
	<c:set var="images" value="0"/>
	<c:forEach items="${question.mmedia}" var="media">
		<c:choose>
			<c:when test="${not ((empty media.width or media.width eq 'small') and (empty media.height or media.height eq 'small'))}">
				<c:set var="images" value="20"/>
			</c:when>
			<c:when test="${media.type eq 2}">
				<c:set var="images" value="${images + 1}"/>
			</c:when>
			<c:otherwise>
				<c:set var="images" value="20"/>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	
	<c:choose>
		<c:when test="${images eq 2}">
			<center>
				<c:forEach items="${question.mmedia}" var="media">
					<c:choose>
						<c:when test="${param.role != 'KID'}">
							<img class="multimediaImagenSimple" name="multimediaImagenSimple" style="width:40%" src="${pageContext.request.contextPath}/common/mmedia/${media.path}" onclick="window.open('${pageContext.request.contextPath}/common/mmedia/${media.path}', 'popup', 'width='+screen.availWidth+',height='+screen.availHeight+',top=0,left=0, scrollbars=yes');">
						</c:when>
						<c:otherwise>
					      	<img class="multimediaImagenSimple" name="multimediaImagenSimple" style="width:40%" src="${pageContext.request.contextPath}/common/mmedia/${media.path}">
					   </c:otherwise>
					   </c:choose>			
					
				</c:forEach>
			</center>
		</c:when>
		<c:otherwise>
		<center class="multimedia">
			<c:forEach items="${question.mmedia}" var="media">
				<c:set var="width" value=""/>
				<c:set var="height" value=""/>
				<c:choose>
					<c:when test="${media.width eq 'auto'}">
						<c:set var="width" value=""/>
						<c:set var="height" value=""/>
					</c:when>
					<c:when test="${media.width eq 'big'}">
						<c:set var="width" value="100%"/>
						<c:set var="height" value=""/>
					</c:when>
					<c:when test="${media.width eq 'medium'}">
						<c:set var="width" value="75%"/>
						<c:set var="height" value=""/>
					</c:when>
					<c:when test="${(empty media.width and empty media.height)  or media.width eq 'small'}">
						<c:set var="width" value="50%"/>
						<c:set var="height" value=""/>
					</c:when>
					<c:otherwise>
							<c:choose>
								<c:when test="${media.widthType eq '%'}">
									<c:set var="width" value="${media.width}"/>								</c:when>
								<c:otherwise>
									<c:set var="width" value="${media.width}px"/>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${media.heightType eq '%'}">
									<c:set var="height" value="${media.height}"/>								</c:when>
								<c:otherwise>
									<c:set var="height" value="${media.height}px"/>
								</c:otherwise>
							</c:choose>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${media.type eq 2}">
						<c:choose>
						<c:when test="${param.role != 'KID'}">
							<img class="multimediaImagenSimple" name="imageMM" style="width: ${width}; height: ${height}" src="${pageContext.request.contextPath}/common/mmedia/${media.path}" onclick="window.open('${pageContext.request.contextPath}/common/mmedia/${media.path}', 'popup', 'width='+screen.availWidth+',height='+screen.availHeight+',top=0,left=0, scrollbars=yes');">
						</c:when>
						<c:otherwise>
					      <img class="multimediaImagenSimple" name="imageMM" style="width: ${width}; height: ${height}" src="${pageContext.request.contextPath}/common/mmedia/${media.path}">
					   </c:otherwise>
					   </c:choose>						
					</c:when>
					<c:when test="${media.type eq 1}">
						<table class="marco" style="text-align: center"><tr><td style="text-align: center">
						<object width="500" height="300" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0">
				  			<param name="movie" value="${pageContext.request.contextPath}/common/mmedia/${media.path}" />
				  			<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}"  pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="500" height="300">
						</object>
						</td></tr></table>
					</c:when>
					<c:when test="${(media.type eq 3) and (media.extension eq 'mp3') and (param.role eq 'KID')}">
						<object type="application/x-shockwave-flash" data="${pageContext.request.contextPath}/common/resources/player.swf" id="playbutton1" height="100" width="310">
							<param name="movie" value="${pageContext.request.contextPath}/common/resources/player.swf">
							<param name="FlashVars" value="playerID=1&amp;soundFile=${pageContext.request.contextPath}/common/mmedia/${media.path}">
							<param name="quality" value="high">
							<param name="menu" value="false">
							<param name="wmode" value="transparent">
						</object>						
					</c:when>
					<c:when test="${media.type eq 3}">
						<object>
							<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}" autostart="false" loop="false" width="300" height="100"></embed>
						</object>
					</c:when>
					<c:when test="${media.type eq 4}">
						<table class="marco">
							<tr>
								<td><center><input type="button" value="<fmt:message key="buttonFullscreen"/>" onclick="javascript:fullScreenApplet(document.getElementById('geogebraApplet${media.id}'),document.getElementById('rowApplet${media.id}'))"/></center></td>
							</tr>
							<tr>
								<td id="rowApplet${media.id}">
									<applet id="geogebraApplet${media.id}" code="geogebra.GeoGebraApplet" archive="http://www.geogebra.org/webstart/geogebra.jar" width="500" height="375">
										<param name="filename" value="../common/mmedia/<c:out value="${media.path}"/>" />
										<c:if test="${media.geogebraType ne 0}">
											<param name="enableLabelDrags" value="true" />
											<param name="showToolBar" value="true" />
											<param name="showToolBarHelp" value="true" />
											<param name="showAlgebraInput" value="true" />
											<param name="allowRescaling" value="true" />
											<param name="framePossible" value="true" />
										</c:if>
											Sorry, the GeoGebra Applet could not be started. Please make sure that Java 1.4.2 (or later) is installed and activated. (<a href="http://java.sun.com/getjava">click here to install Java now</a>)
									</applet>
								</td>
							</tr>
						</table>
					</c:when>
					<c:when test="${media.type eq 5}">
						<applet code="${fn:substringAfter(media.path, '/')}" codebase="${pageContext.request.contextPath}/common/mmedia/${fn:substringBefore(media.path,'/')}" width="500" height="500"></applet>
					</c:when>
					<c:when test="${media.type eq 6}">
						<object id="ScorchPlugin"
								classid="clsid:A8F2B9BD-A6A0-486A-9744-18920D898429"
								width="100%"
								 height="800"
								codebase="http://www.sibelius.com/download/software/win/ActiveXPlugin.cab#4,0,0,0">
						<param name="src" value="${pageContext.request.contextPath}/common/mmedia/${media.path}">
						<param name="type" value="application/x-sibelius-score">
						<param name="scorch_minimum_version" value="4000">
						<param name="scorch_preferred_version" value="4000">
						<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}"
								 scorch_minimum_version="4000"
								 scorch_preferred_version="4000"
								 width="100%"
								 height="800"
								 type="application/x-sibelius-score"
								 pluginspage="http://www.sibelius.com/cgi/plugin.pl">
						</embed>
						</object>
					</c:when>
					<%-- YouTube video --%>
					<c:when test="${media.type eq 7}">
						<c:set var="youtubeUrl" value="http://www.youtube.com/v/${media.path}&hl=es&fs=1&rel=0"/>
						<object width="425" height="344">
							<param name="movie" value="${youtubeUrl}"></param>
							<param name="allowFullScreen" value="true"></param>
							<param name="allowscriptaccess" value="always"></param>
							<embed src="${youtubeUrl}" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="425" height="344"></embed>
						</object>
					</c:when>
				</c:choose>
				
			</c:forEach>
			</center>
		</c:otherwise>
	</c:choose>
	
	<div id="answer${question.id}" class="respuestasTipoTest">
		<!-- comprobación de si todas las respuestas son una imagen sin texto -->
		<c:set var="imagenotext" value="true"/>
		<c:set var="mp3notext" value="true"/>
		<c:forEach items="${question.answers}" var="answer">
			<c:if test="${not empty answer.text}">
				<c:set var="imagenotext" value="false"/>
				<c:set var="mp3notext" value="false"/>
			</c:if>
			<c:forEach items="${answer.mmedia}" var="media">
				<c:if test="${media.type ne 2}">
					<c:set var="imagenotext" value="false"/>
				</c:if>
				<c:if test="${(media.type ne 3) or (media.extension ne 'mp3')}">
					<c:set var="mp3notext" value="false"/>
				</c:if>
			</c:forEach>
		</c:forEach>
		<c:choose>
			<c:when test="${question.type eq 0}">
				<c:choose>
					<c:when test="${imagenotext}">
						<table width="100%">
						<tr>
						<c:forEach items="${question.answers}" var="answer" varStatus="n"> 
							<td>
							<c:choose>
							   <c:when test="${(param.view eq 'review') and (answer.solution eq '1')}">
							      <p class="respuestaCorrecta" id="answer${answer.id}" >
							   </c:when>
							   <c:otherwise>
							      <p id="answer${answer.id}" >
							   </c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${(param.view eq 'review') and (answer.marked eq 'true')}">
									<INPUT TYPE="checkbox" disabled="disabled" checked="checked" id="check${answer.id}"/>
								</c:when>
								<c:when test="${(param.view eq 'review') and (answer.marked eq 'false')}">
									<INPUT TYPE="checkbox" disabled="disabled" id=check${answer.id}>
								</c:when>
								<c:when test="${(param.view eq 'exam')}">
									<c:choose>
										<c:when test="${answer.marked eq 'true'}">
											<INPUT TYPE="checkbox" checked id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});"><c:out value="${answer.text}"/>
										</c:when>
										<c:otherwise>
											<INPUT TYPE="checkbox" id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});"><c:out value="${answer.text}"/>
										</c:otherwise>
									</c:choose>	
								</c:when>
								<c:when test="${(param.view eq 'tutorPreview')}">
									<INPUT TYPE="checkbox" id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});"><c:out value="${answer.text}"/>	
								</c:when>
								<c:otherwise>
									<INPUT TYPE="checkbox" id="check${answer.id}" />
								</c:otherwise>
							</c:choose>
							<c:forEach items="${answer.mmedia}" var="media">
								<img class="multimediaImagenSimpleResp" style="width:70%" src="${pageContext.request.contextPath}/common/mmedia/${media.path}" onClick="javascript:check${answer.id}.click()"/>
							</c:forEach>
							</p></td>
							<c:if test="${(n.count mod 3) eq 0}">
								</tr><tr>
							</c:if>
						</c:forEach>
						</tr></table>
					</c:when>
					<c:when test="${mp3notext and (param.role eq 'KID')}">
						<table width="100%"> 
						<tr>
						<c:forEach items="${question.answers}" var="answer" varStatus="n"> 
							<td style="text-align: center"><table>
							<c:choose>
							   <c:when test="${(param.view eq 'review') and (answer.solution eq '1')}">
							      <tr class="respuestaCorrecta" id="answer${answer.id}" >
							   </c:when>
							   <c:otherwise>
							      <tr id="answer${answer.id}" >
							   </c:otherwise>
							</c:choose>
							<td>
							<c:choose>
								<c:when test="${(param.view eq 'review') and (answer.marked eq 'true')}">
									<INPUT TYPE="checkbox" disabled="disabled" checked="checked" id="check${answer.id}"/>
								</c:when>
								<c:when test="${(param.view eq 'review') and (answer.marked eq 'false')}">
									<INPUT TYPE="checkbox" disabled="disabled" id=check${answer.id}>
								</c:when>
								<c:when test="${((param.view eq 'exam')) and (param.showCorrectAnswers)}">
									<c:choose>
										<c:when test="${answer.marked eq 'true'}">
											<INPUT TYPE="checkbox" checked id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});"><c:out value="${answer.text}"/>
										</c:when>
										<c:otherwise>
											<INPUT TYPE="checkbox" id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});"><c:out value="${answer.text}"/>
										</c:otherwise>
									</c:choose>	
								</c:when>
								<c:when test="${((param.view eq 'tutorPreview')) and (param.showCorrectAnswers)}">
									<INPUT TYPE="checkbox" id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});"><c:out value="${answer.text}"/>	
								</c:when>
								<c:when test="${((param.view eq 'exam')) and (not param.showCorrectAnswers)}">
									<INPUT TYPE="checkbox" id="check${answer.id}"><c:out value="${answer.text}"/>	
									
								</c:when>
								<c:when test="${((param.view eq 'tutorPreview')) and (not param.showCorrectAnswers)}">
									<INPUT TYPE="checkbox" id="check${answer.id}"><c:out value="${answer.text}"/>	
								</c:when>
								<c:otherwise>
									<INPUT TYPE="checkbox" id="check${answer.id}" />
								</c:otherwise>
							</c:choose>
							</td>
							<c:forEach items="${answer.mmedia}" var="media">
								<td>
								<object type="application/x-shockwave-flash" data="${pageContext.request.contextPath}/common/resources/player.swf" id="playbutton1" height="100" width="120" style="border: solid medium black">
									<param name="movie" value="${pageContext.request.contextPath}/common/resources/player.swf">
									<param name="FlashVars" value="playerID=1&amp;soundFile=${pageContext.request.contextPath}/common/mmedia/${media.path}">
									<param name="quality" value="high">
									<param name="menu" value="false">
									<param name="wmode" value="transparent">
								</object>
								</td>
							</c:forEach>
							</tr></table></td>
							<c:if test="${(n.count mod 3) eq 0}">
								</tr><tr>
							</c:if>
						</c:forEach>
						</tr></table>
					</c:when>
					<c:otherwise>
						<c:forEach items="${question.answers}" var="answer"> 
							<c:choose>
							   <c:when test="${((param.view eq 'review') or (param.view eq 'preview')) and (answer.solution eq '1')}">
							      <p class="respuestaCorrecta" id="answer${answer.id}" >
							   </c:when>
							   <c:otherwise>
							      <p id="answer${answer.id}" >
							   </c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${(param.view eq 'review') and (answer.marked eq 'true')}">
									<INPUT TYPE="checkbox" disabled="disabled" checked="checked" id="check${answer.id}" />
								</c:when>
								<c:when test="${(param.view eq 'review') and (answer.marked eq 'false')}">
									<INPUT TYPE="checkbox" disabled="disabled" id="check${answer.id}" />
								</c:when>
								<c:when test="${(param.view eq 'exam')}">
									<c:choose>
										<c:when test="${answer.marked eq 'true'}">
											<INPUT TYPE="checkbox" checked id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});" />
										</c:when>
										<c:otherwise>
											<INPUT TYPE="checkbox" id="check${answer.id}" onclick="javascript:checkNumAnswers('fq${question.id}',this.checked,this,${user.id},${question.id},${answer.id},${param.numQuestion},${param.showCorrectAnswers});" />
										</c:otherwise>
									</c:choose>
									
								</c:when>
								<c:otherwise>
									<INPUT TYPE="checkbox" id="check${answer.id}" name="check${answer.id}" />
								</c:otherwise>
							</c:choose>			
								<label id="pcheck${answer.id}" for="check${answer.id}">
									<c:forEach items="${answer.textParagraphs}" var="paragraph">
										<c:out value="${paragraph}" escapeXml="true"/>[br]
									</c:forEach>
								</label>
								
								<script>
									parse2HTML(document.getElementById('pcheck'+${answer.id}).innerHTML,document.getElementById('pcheck'+${answer.id}));
								</script>
								</p>
								<c:forEach items="${answer.mmedia}" var="media">
								<c:choose>
									<c:when test="${media.type eq 2}">
										<c:choose>
											<c:when test="${param.role != 'KID'}">
												<img class="multimediaImagenSimpleResp" src="${pageContext.request.contextPath}/common/mmedia/<c:out value="${media.path}"/>" onclick="window.open('${pageContext.request.contextPath}/common/mmedia/${media.path}', 'popup', 'width='+screen.availWidth+',height='+screen.availHeight+',top=0,left=0, scrollbars=yes');">
											</c:when>
											<c:otherwise>
							      				<img class="multimediaImagenSimpleResp" src="${pageContext.request.contextPath}/common/mmedia/<c:out value="${media.path}"/>">
							   				</c:otherwise>
							   			</c:choose>
									</c:when>
									<c:when test="${media.type eq 1}">
										<table class="marco"><tr><td>
										<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="500" height="375">
								  			<param name="movie" value="${pageContext.request.contextPath}/common/mmedia/<c:out value="${media.path}"/>" />
								  			<embed src="${pageContext.request.contextPath}/common/mmedia/<c:out value="${media.path}"/>"  pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="500" height="375">
										</object>
										</td></tr></table>
									</c:when>
									<c:when test="${(media.type eq 3) and (media.extension eq 'mp3') and (param.role eq 'KID')}">
										<object type="application/x-shockwave-flash" data="${pageContext.request.contextPath}/common/resources/player.swf" id="playbutton1" height="100" width="310">
											<param name="movie" value="${pageContext.request.contextPath}/common/resources/player.swf">
											<param name="FlashVars" value="playerID=1&amp;soundFile=${pageContext.request.contextPath}/common/mmedia/${media.path}">
											<param name="quality" value="high">
											<param name="menu" value="false">
											<param name="wmode" value="transparent">
										</object>						
									</c:when>
									<c:when test="${media.type eq 3}">
										<c:if test="${not empty answer.text}"></p><p></c:if>
										<object>
											<embed src="${pageContext.request.contextPath}/common/mmedia/<c:out value="${media.path}"/>" autostart="false" loop="false" width="300" height="50"></embed>
										</object>
									</c:when>
									<c:when test="${media.type eq 4}">
										<table class="marco"><tr><td>
										<applet code="geogebra.GeoGebraApplet" archive="http://www.geogebra.org/webstart/geogebra.jar" width="500" height="375">
											<param name="filename" value="../common/mmedia/<c:out value="${media.path}"/>" />
											<c:if test="${media.geogebraType ne 0}">
												<param name="enableLabelDrags" value="true" />
												<param name="showToolBar" value="true" />
												<param name="showToolBarHelp" value="true" />
												<param name="showAlgebraInput" value="true" />
												<param name="allowRescaling" value="true" />
												<param name="framePossible" value="true" />
											</c:if>
												Sorry, the GeoGebra Applet could not be started. Please make sure that Java 1.4.2 (or later) is installed and activated. (<a href="http://java.sun.com/getjava">click here to install Java now</a>)
										</applet>
										</td></tr></table>
									</c:when>
									<c:when test="${media.type eq 5}">
										<applet code="${fn:substringAfter(media.path, '/')}" codebase="${pageContext.request.contextPath}/common/mmedia/${fn:substringBefore(media.path,'/')}" width="500" height="375"></applet>
									</c:when>
									<c:when test="${media.type eq 6}">
										<object id="ScorchPlugin"
												classid="clsid:A8F2B9BD-A6A0-486A-9744-18920D898429"
												width="100%"
												 height="800"
												codebase="http://www.sibelius.com/download/software/win/ActiveXPlugin.cab#4,0,0,0">
										<param name="src" value="${pageContext.request.contextPath}/common/mmedia/${media.path}">
										<param name="type" value="application/x-sibelius-score">
										<param name="scorch_minimum_version" value="4000">
										<param name="scorch_preferred_version" value="4000">
										<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}"
												 scorch_minimum_version="4000"
												 scorch_preferred_version="4000"
												 width="100%"
												 height="800"
												 type="application/x-sibelius-score"
												 pluginspage="http://www.sibelius.com/cgi/plugin.pl">
										</embed>
										</object>
									</c:when>
								</c:choose>
								</c:forEach>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${question.type eq 1}">
				<c:forEach var="answer" items="${question.answers}">
					<div id="answer${answer.id}">
						<c:choose>
							<c:when test="${param.view eq 'preview' or param.view eq 'tutorPreview'}">
								<fmt:message key="labelCorrectAnswer"/>: <input type="text" value="<c:out value="${answer.text}"/>" id="inputFillAnswer${answer.id}" name="inputFillAnswer${answer.id}"/>&nbsp;&nbsp;<button><fmt:message key="saveButton"/></button><br/>
								<script>
									document.getElementById('inputFillAnswer'+${answer.id}).style.backgroundColor="#FFFFFF";
								</script>
							</c:when>
							<c:when test="${param.view eq 'review'}">
								<fmt:message key="labelUserAnswer"/>: <b><c:out value="${question.learnerFillAnswer}"/></b><br/>
								<fmt:message key="labelCorrectAnswer"/>: <b><font color="green"><c:out value="${answer.text}"/></font></b>
							</c:when>
							<c:when test="${param.view eq 'exam'}">
								<fmt:message key="labelCorrectAnswer"/>: <input type="text" name="inputFillAnswer${question.id}" id="inputFillAnswer${answer.id}" onkeydown="document.getElementById('labelModified${question.id}').style.display='';" <c:if test="${!empty question.learnerFillAnswer}"> value="${question.learnerFillAnswer}"</c:if> />&nbsp;<b id="labelModified${question.id}" style="display:none;">(*)</b>&nbsp;<button onclick="saveFillAnswer(${question.id},${answer.id})"><fmt:message key="saveButton"/></button>
							</c:when>
						</c:choose>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>

	</div>
	<c:if test="${param.ConfidenceLevel eq true}">
			<c:choose>
			<c:when test="${(param.view eq 'exam')}">
				<c:choose>
					<c:when test="${question.marked eq true}">
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" onclick="javascript:updateConfidenceLevel(this,${user.id},${question.id})" checked/></p>
					</c:when>
					<c:otherwise>
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" onclick="javascript:updateConfidenceLevel(this,${user.id},${question.id})"/></p>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${(param.view eq 'tutorPreview')}">
				<c:choose>
					<c:when test="${question.marked eq true}">
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" checked/></p>
					</c:when>
					<c:otherwise>
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" /></p>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${(param.view eq 'review')}">
				<c:choose>
					<c:when test="${question.marked eq true}">
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" disabled checked/></p>
					</c:when>
					<c:otherwise>
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" disabled/></p>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${question.marked eq true}">
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" disabled checked/></p>
					</c:when>
					<c:otherwise>
						<p align="right"><label><fmt:message key="labelConfidenceLevel"/></label><input type="checkbox" name="confidenceQuestion${question.id}" id="confidenceQuestion${question.id}" disabled/></p>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		
		</c:if>
	<c:if test="${((param.view eq 'review') or (param.view eq 'preview')) and (question.comment ne null)}">
	   <fieldset class="formComentarioProfesor">
	   <legend><b><fmt:message key="tutorcomment" /></b></legend>
	   <p id="pcomment${question.id}">
	   		<c:forEach items="${question.commentParagraphs}" var="paragraph">
				<c:out value="${paragraph}" escapeXml="true"/>[br]
			</c:forEach>
	   </p>
		<script>
			parse2HTML(document.getElementById('pcomment'+${question.id}).innerHTML,document.getElementById('pcomment'+${question.id}));
		</script>
		<center>
		<c:forEach items="${question.mmediaComment}" var="media">
			<c:choose>
				<c:when test="${param.role != 'KID'}">
					<c:choose>
						<c:when test="${media.type eq 1}">
						<table class="marco" style="text-align: center"><tr><td style="text-align: center">
						<object width="500" height="300" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0">
				  			<param name="movie" value="${pageContext.request.contextPath}/common/mmedia/${media.path}" />
				  			<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}"  pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="500" height="300">
						</object>
						</td></tr></table>
					</c:when>
					<c:when test="${(media.type eq 3) and (media.extension eq 'mp3') and (param.role eq 'KID')}">
						<object type="application/x-shockwave-flash" data="${pageContext.request.contextPath}/common/resources/player.swf" id="playbutton1" height="100" width="310">
							<param name="movie" value="${pageContext.request.contextPath}/common/resources/player.swf">
							<param name="FlashVars" value="playerID=1&amp;soundFile=${pageContext.request.contextPath}/common/mmedia/${media.path}">
							<param name="quality" value="high">
							<param name="menu" value="false">
							<param name="wmode" value="transparent">
						</object>						
					</c:when>
					<c:when test="${media.type eq 3}">
						<object>
							<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}" autostart="false" loop="false" width="300" height="100"></embed>
						</object>
					</c:when>
					<c:when test="${media.type eq 4}">
						<table class="marco">
							<tr>
								<td><center><input type="button" value="<fmt:message key="buttonFullscreen"/>" onclick="javascript:fullScreenApplet(document.getElementById('geogebraAppletComment${question.id}'),document.getElementById('rowAppletComment${question.id}'))"/></center></td>
							</tr>
							<tr>
								<td id="rowAppletComment${question.id}">
									<applet id="geogebraAppletComment${question.id}" code="geogebra.GeoGebraApplet" archive="http://www.geogebra.org/webstart/geogebra.jar" width="500" height="375">
										<param name="filename" value="../common/mmedia/<c:out value="${media.path}"/>" />
										<c:if test="${media.geogebraType ne 0}">
											<param name="enableLabelDrags" value="true" />
											<param name="showToolBar" value="true" />
											<param name="showToolBarHelp" value="true" />
											<param name="showAlgebraInput" value="true" />
											<param name="allowRescaling" value="true" />
											<param name="framePossible" value="true" />
										</c:if>
											Sorry, the GeoGebra Applet could not be started. Please make sure that Java 1.4.2 (or later) is installed and activated. (<a href="http://java.sun.com/getjava">click here to install Java now</a>)
									</applet>
								</td>
							</tr>
						</table>
					</c:when>
					<c:when test="${media.type eq 5}">
						<applet code="${fn:substringAfter(media.path, '/')}" codebase="${pageContext.request.contextPath}/common/mmedia/${fn:substringBefore(media.path,'/')}" width="500" height="500"></applet>
					</c:when>
					<c:when test="${media.type eq 6}">
						<object id="ScorchPlugin"
								classid="clsid:A8F2B9BD-A6A0-486A-9744-18920D898429"
								width="100%"
								 height="800"
								codebase="http://www.sibelius.com/download/software/win/ActiveXPlugin.cab#4,0,0,0">
						<param name="src" value="${pageContext.request.contextPath}/common/mmedia/${media.path}">
						<param name="type" value="application/x-sibelius-score">
						<param name="scorch_minimum_version" value="4000">
						<param name="scorch_preferred_version" value="4000">
						<embed src="${pageContext.request.contextPath}/common/mmedia/${media.path}"
								 scorch_minimum_version="4000"
								 scorch_preferred_version="4000"
								 width="100%"
								 height="800"
								 type="application/x-sibelius-score"
								 pluginspage="http://www.sibelius.com/cgi/plugin.pl">
						</embed>
						</object>
					</c:when>
						<c:when test="${media.type eq 7}">
							
								<c:set var="youtubeUrl" value="http://www.youtube.com/v/${media.path}&hl=es&fs=1&rel=0"/>
								<object width="425" height="344">
									<param name="movie" value="${youtubeUrl}"></param>
									<param name="allowFullScreen" value="true"></param>
									<param name="allowscriptaccess" value="always"></param>
									<embed src="${youtubeUrl}" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="425" height="344"></embed>
								</object>
							
						</c:when>
						<c:otherwise>
								<img class="multimediaImagenSimple" style="width:40%" src="${pageContext.request.contextPath}/common/mmedia/${media.path}" onclick="window.open('${pageContext.request.contextPath}/common/mmedia/${media.path}', 'popup', 'width='+screen.availWidth+',height='+screen.availHeight+',top=0,left=0, scrollbars=yes');">
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
			      	<img class="multimediaImagenSimple" style="width:40%" src="${pageContext.request.contextPath}/common/mmedia/${media.path}">
			   </c:otherwise>
			 </c:choose>			
		</c:forEach>
		</center>
    </fieldset>
	       	
    </c:if>
    </div>
    <div class="preguntaAbajo">
    </div>
</div>
<script type="text/javascript">
   	resizeImage();
</script>