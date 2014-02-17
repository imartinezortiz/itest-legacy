package es.itest.engine.test.business.control;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
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

import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.MediaElem;

public class ItemPdfWriter extends AbstractPdfWriter {

	private Item item;
	
	public ItemPdfWriter(File rootPath, Item item) {
		super(rootPath);
		this.item = item;
	}
	
	public void write(OutputStream out) throws DocumentException, IOException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();
		if(item!=null){
			document.addCreationDate();
			document.addCreator("Herramienta de generacion automatizada para visualizar preguntas en PDF de iTest");
			document.addKeywords("pregunta,"+item.getTitle()+","+item.getId()+".pdf");
			document.addTitle("Pregunta: "+item.getId()+" Titulo: "+item.getTitle()+".pdf");
			
			PdfPTable questionInfo = new PdfPTable(1);
			questionInfo.getDefaultCell().setGrayFill(0.8f);
			questionInfo.getDefaultCell().setBorderWidth(0.0f);
			questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			questionInfo.addCell("Pregunta "+item.getId()+"  Respuestas correctas: "+item.getNumCorrectAnswers());
				
			questionInfo.setSpacingBefore(40.0f);
			questionInfo.setSpacingAfter(10.0f);
			document.add(questionInfo);
			
			//Body
			PdfPTable questionBody = new PdfPTable(1);
			questionInfo.getDefaultCell().setBorder(3);
			questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
			Paragraph body = new Paragraph();
			
			//Question text
			String text = item.getText();
			Paragraph questionForm = new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
			
			body.add(rellenaParrafo(text, questionForm,true));
			
			
			body.add("\n\n");
			if (item.getMmedia()!=null)
				for (MediaElem mmediaElem : item.getMmedia()) {
					try {
						if (mmediaElem.getType() != MediaElem.IMAGE) throw new DocumentException();
						Image imgElem = Image.getInstance(rootPath.getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
						imgElem.scaleToFit(200, 200);
						body.add(new Chunk(imgElem,100,0));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
					}
					catch(Exception e) {
						body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
					}
				}

			for (ItemResponse answer : item.getAnswers()) {
				if(item.getType() == 0){
					if (answer.getSolution() == 1 && answer.getMarked()) {
						Paragraph correctAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.GREEN));
						text = answer.getText();
						body.add(rellenaParrafo(text,correctAnswer,false));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
						
					}
					if (answer.getSolution() == 0 && answer.getMarked()) {
						Paragraph incorrectAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.RED));
						text = answer.getText();
						body.add(rellenaParrafo(text,incorrectAnswer,false));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
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
					bodyAnswer.add(new Phrase("************************************************************"));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("Respuesta correcta: "+"\""+text+"\""));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("************************************************************"));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("Respuesta del alumno: "));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("************************************************************"));
					body.add(bodyAnswer);
					body.add(Chunk.NEWLINE);
				}
			}
			questionBody.addCell(body);
			document.add(questionBody);
			
			
		}
		document.close();
	}
}
