package es.itest.engine.test.business.control;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.MediaElem;
import es.itest.engine.test.business.entity.TestSession;

public class TestSessionPdfWriter extends AbstractPdfWriter {

	private TestSession testSession;
	
	public TestSessionPdfWriter(File rootPath, TestSession test) {
		super(rootPath);
		this.testSession = test;
	}
	
	public void write(OutputStream out) throws DocumentException, IOException{

		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		if (testSession!=null) {
			boolean questionAnswered = false;
			//META
			document.addCreationDate();
			document.addCreator("Herramienta de generacion automatizada de examenes iTest");
			document.addKeywords("examen,"+testSession.getTitle()+","+testSession.getGroup().getCourse().getName()+".pdf");
			document.addTitle("Examen: "+testSession.getTitle()+" Asignatura: "+testSession.getGroup().getCourse().getName()+".pdf");
			
			//Leyenda
			PdfPTable leyenda = new PdfPTable(1);
			leyenda.getDefaultCell().setGrayFill(0.8f);
			leyenda.getDefaultCell().setBorderWidth(0.0f);
			leyenda.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			leyenda.addCell("Leyenda");
			leyenda.setSpacingAfter(10.0f);
			document.add(leyenda);

			PdfPTable leyenda2 = new PdfPTable(1);
			leyenda2.getDefaultCell().setBackgroundColor(Color.PINK);
			leyenda2.getDefaultCell().setBorder(0);
			leyenda2.addCell("1.- Las respuestas contestadas por el alumno y las respuestas correctas se identifican según:");

			Paragraph respuestaCorrecta = new Paragraph("\t\t\t\t\t\t[X]\t\t\tRespuesta correcta",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.BOLD,Color.GREEN));
			Paragraph respuestaIncorrecta = new Paragraph("\t\t\t\t\t\t[X]\t\t\tRespuesta incorrecta",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.BOLD,Color.RED));
			Paragraph respuestaNoContestada = new Paragraph("\t\t\t\t\t\t[   ]\t\t\tRespuesta no contestada",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.NORMAL,Color.BLACK));
			leyenda2.addCell(respuestaCorrecta);
			leyenda2.addCell(respuestaIncorrecta);
			leyenda2.addCell(respuestaNoContestada);

			leyenda2.addCell("2.- La puntuación obtenida en cada pregunta se muestra al lado del título de la misma");
			leyenda2.addCell("3.- Existen comentarios del profesor al final de algunas preguntas");
			leyenda2.setSpacingAfter(40.0f);
			document.add(leyenda2);

			//Titulo examen
			PdfPTable info = new PdfPTable(1);
			info.getDefaultCell().setBackgroundColor(Color.ORANGE);
			info.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			info.getDefaultCell().setBorder(0);
			info.addCell(new Paragraph("Asignatura: "+testSession.getGroup().getCourse().getName()+" ("+testSession.getGroup().getName()+")",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.UNDERLINE,Color.BLACK)));
			info.addCell(new Paragraph("Titulo: "+testSession.getTitle(),FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.BOLD,Color.BLACK)));
			document.add(info);

			//Preguntas
			int numQuestion = 1;
			for (ItemSession question : testSession.getQuestions()) {
				//Info
				PdfPTable questionInfo = new PdfPTable(1);
				questionInfo.getDefaultCell().setGrayFill(0.8f);
				questionInfo.getDefaultCell().setBorderWidth(0.0f);
				questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				if (testSession.isShowNumCorrectAnswers())
					questionInfo.addCell("Pregunta "+numQuestion+": "+question.getQuestionGrade()+" puntos ("+question.getNumCorrectAnswers()+" respuestas correctas)");
				else
					questionInfo.addCell("Pregunta "+numQuestion+": "+question.getQuestionGrade()+" puntos");
				questionInfo.setSpacingBefore(40.0f);
				questionInfo.setSpacingAfter(10.0f);
				document.add(questionInfo);
				numQuestion++;
				
				//Body
				PdfPTable questionBody = new PdfPTable(1);
				questionInfo.getDefaultCell().setBorder(3);
				questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
				Paragraph body = new Paragraph();
				
				//Question text
				String text = question.getText();
				Paragraph questionForm = new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
				
				body.add(rellenaParrafo(text, questionForm,true));
				
				
				body.add("\n\n");
				if (question.getMmedia()!=null)
					for (MediaElem mmediaElem : question.getMmedia()) {
						try {
							if (mmediaElem.getType() == MediaElem.IMAGE){
								Image imgElem = Image.getInstance(rootPath.getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
								imgElem.scaleToFit(200, 200);
								body.add(new Chunk(imgElem,100,0));
								body.add(Chunk.NEWLINE);
								body.add(Chunk.NEWLINE);
							}
						}
						catch(Exception e) {
							body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
						}
					}

				for (ItemSessionResponse answer : question.getAnswers()) {
					if(question.getType() == 0){
						//ES UNA PREGUNTA DE TIPO TEST
						if (answer.getSolution() == 1 && answer.getMarked()) {
							//ES UNA PREGUNTA CORRECTA
							Paragraph correctAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.GREEN));
							text = answer.getText();
							body.add(rellenaParrafo(text,correctAnswer,false));
							body.add(Chunk.NEWLINE);
							body.add(Chunk.NEWLINE);
							questionAnswered = true;
							continue;
						}
						if (answer.getSolution() == 0 && answer.getMarked()) {
							//ES UNA PREGUNTA INCORRECTA
							Paragraph incorrectAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.RED));
							text = answer.getText();
							body.add(rellenaParrafo(text,incorrectAnswer,false));
							body.add(Chunk.NEWLINE);
							body.add(Chunk.NEWLINE);
							questionAnswered = true;
							continue;
						}
						Paragraph noAnswer = new Paragraph("\t\t\t\t\t\t[   ]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
						text = answer.getText();
						body.add(rellenaParrafo(text,noAnswer,false));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
						for (MediaElem mmediaElem : answer.getMmedia()) {
							try {
								if (mmediaElem.getType() == MediaElem.IMAGE) {
									Image imgElem = Image.getInstance(rootPath.getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
									imgElem.scaleToFit(100, 100);
									body.add(new Chunk(imgElem,50,0));
									body.add(Chunk.NEWLINE);
									body.add(Chunk.NEWLINE);
								}
							}
							catch(Exception e) {
								body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
							}
						}
						continue;
					}else{
						Paragraph bodyAnswer = new Paragraph();
						text = answer.getText();
						/*bodyAnswer.add(new Phrase("************************************************************"));
						bodyAnswer.add(Chunk.NEWLINE);
						bodyAnswer.add(new Phrase("Respuesta correcta: "+"\""+text+"\""));
						bodyAnswer.add(Chunk.NEWLINE);
						bodyAnswer.add(new Phrase("************************************************************"));
						bodyAnswer.add(Chunk.NEWLINE);*/
						if(question.getLearnerFillAnswer()!=null){
							bodyAnswer.add(new Phrase("Respuesta del alumno: "));
							if(question.getLearnerFillAnswer().toLowerCase().trim().equalsIgnoreCase(text.trim().toLowerCase())){
								bodyAnswer.add(new Phrase("\""+question.getLearnerFillAnswer()+"\"",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.GREEN)));
								questionAnswered = true;
							}else{
								bodyAnswer.add(new Phrase("\""+question.getLearnerFillAnswer()+"\"",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.RED)));
								questionAnswered = true;
							}
						}else
							bodyAnswer.add(new Phrase("Respuesta del alumno: "+"\"\""));
						bodyAnswer.add(Chunk.NEWLINE);
						bodyAnswer.add(new Phrase("************************************************************"));
						body.add(bodyAnswer);
						body.add(Chunk.NEWLINE);
					}
				}
				questionBody.addCell(body);
				document.add(questionBody);
			}
			//Puntuacion
			PdfPTable gradeTable = new PdfPTable(1);
			gradeTable.setSpacingBefore(100.0f);
			gradeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			gradeTable.getDefaultCell().setBorderWidth(0.8f);
			Paragraph gradeParagraph = new Paragraph();
			Paragraph infoGrade = new Paragraph("Puntuacion final:\n",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.black));
			Paragraph finalGrade = null;
			if(questionAnswered)
				finalGrade = new Paragraph(""+testSession.getExamGrade(),FontFactory.getFont(FontFactory.HELVETICA,20.0f,Font.BOLD,Color.black));
			else
				finalGrade = new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA,20.0f,Font.BOLD,Color.black));
			gradeParagraph.add(infoGrade);
			gradeParagraph.add(finalGrade);
			gradeTable.addCell(gradeParagraph);
			document.add(gradeTable);
			document.close();
		}
		else throw new NullPointerException();

	}
}
