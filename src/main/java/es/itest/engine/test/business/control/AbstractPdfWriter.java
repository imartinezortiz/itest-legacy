package es.itest.engine.test.business.control;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import be.ugent.caagt.jmathtex.TeXConstants;
import be.ugent.caagt.jmathtex.TeXFormula;
import be.ugent.caagt.jmathtex.TeXIcon;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

public abstract class AbstractPdfWriter {
	
	protected File rootPath;
	
	protected AbstractPdfWriter(File rootPath) {
		this.rootPath = rootPath;
	}
	
	protected Phrase tratarTexto(String str) {
		try{
			Phrase p = new Phrase();
			long b = str.indexOf("[b]");
			if(b==-1)
				b=Long.MAX_VALUE;
			long s = str.indexOf("[del]");
			if(s==-1)
				s=Long.MAX_VALUE;
			long u = str.indexOf("[em]");
			if(u==-1)
				u=Long.MAX_VALUE;
			long color = str.indexOf("[font]");
			if(color==-1)
				color = Long.MAX_VALUE;
			long link = str.indexOf("[a]");
			if(link ==-1)
				link = Long.MAX_VALUE;
			int inicio =0;
			int fin =0;
			//bold
			if(b<s && b<u && b<color && b<link && b!=Long.MAX_VALUE){
				inicio = str.indexOf("[b]");
				fin = str.indexOf("[/b]")+4;
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				Phrase auxp = tratarTexto(str.substring(inicio+3, fin-4));
				auxp.getFont().setStyle(Font.BOLD);
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
			}
			if(s<b && s<u && s<color && s<link && s!=Long.MAX_VALUE){
				inicio = str.indexOf("[del]");
				fin = str.indexOf("[/del]")+6;
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				Phrase auxp = tratarTexto(str.substring(inicio+5, fin-6));
				auxp.getFont().setStyle(Font.UNDERLINE);
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
				
			}
			if(u<b && u<s && u<color && u<link && u!=Long.MAX_VALUE){
				inicio = str.indexOf("[em]");
				fin = str.indexOf("[/em]")+5;
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				Phrase auxp = tratarTexto(str.substring(inicio+4, fin-5));
				auxp.getFont().setStyle(Font.ITALIC);
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
			}
			if(color<b && color<u && color<s && color<link && color!=Long.MAX_VALUE){
				inicio = str.indexOf("[font]");
				fin = str.indexOf("[/font]")+7;
				int inicioColor = str.indexOf("[color]");
				int finColor = str.indexOf("[/color]")+8;
				String sColor = str.substring(inicioColor+7, finColor-8);
				Phrase auxp = tratarTexto(str.substring(finColor, fin-7));
				if(sColor.equalsIgnoreCase("red"))
					auxp = new Phrase(auxp.getContent(),FontFactory.getFont(FontFactory.defaultEncoding,12,Font.NORMAL,Color.RED));
				if(sColor.equalsIgnoreCase("green"))
					auxp = new Phrase(auxp.getContent(),FontFactory.getFont(FontFactory.defaultEncoding,12,Font.NORMAL,Color.GREEN));
				if(sColor.equalsIgnoreCase("blue"))
					auxp = new Phrase(auxp.getContent(),FontFactory.getFont(FontFactory.defaultEncoding,12,Font.NORMAL,Color.BLUE));
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
			}
			if(link<b && link<u && link<s && link<color && link!=Long.MAX_VALUE){
				inicio = str.indexOf("[a]");
				fin = str.indexOf("[/a]")+4;
				int inicioUrl= str.indexOf("[href]");
				int finUrl = str.indexOf("[/href]")+7;
				int inicioTexto = str.indexOf("[text]");
				int finTexto = str.indexOf("[/text]")+7;
				String url = str.substring(inicioUrl+6, finUrl-7);
				String texto = str.substring(inicioTexto+6, finTexto-7);
				Phrase auxp = tratarTexto(texto+"(hipervinculo: "+url+")");
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
				
			}
			if(u==Long.MAX_VALUE && b == Long.MAX_VALUE && s == Long.MAX_VALUE && color == Long.MAX_VALUE && link == Long.MAX_VALUE)
				p = new Phrase(str);
			return p;
		}catch(Exception e){
			return new Phrase(str);
		}
	}
	
	
	protected Paragraph rellenaParrafo(String text, Paragraph questionForm, boolean pregunta){
		/*
		 * QuestionForm puede ser el "Paragraph" que represente una pregunta o una respuesta
		 * */
		if(text!=null){	
			boolean salir = false;
			if(text.indexOf("`")==-1){
				questionForm.add(tratarTexto(text));
				return questionForm;
			}else
				while(text.indexOf("`")!=-1 && !salir){
					try{
						String texto0 = text.split("`")[0];
						String latex = text.split("`")[1];
						if(!texto0.trim().equalsIgnoreCase(""))
							questionForm.add(tratarTexto(texto0));
						TeXFormula formula = null;
						boolean createdLatexIcon = false;
						try {
							formula = new TeXFormula(latex);
							createdLatexIcon = true;
						} catch (Exception e1) {
							questionForm.add(tratarTexto("It's not a valid Latex form"));
							createdLatexIcon = false;
						}
						if(createdLatexIcon && formula!=null){
							TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
							icon.setInsets(new Insets(3, 5, 3, 5));
							BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
							Graphics2D g2 = image.createGraphics();
							g2.setColor(Color.white);
							g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
							JLabel jl = new JLabel();
							jl.setForeground(new Color(0, 0, 0));
							icon.paintIcon(jl, g2, 0, 0);
							File file = new File("latex.png");
							try{
								ImageIO.write(image, "png", file.getAbsoluteFile());
								Image imgElem = Image.getInstance(file.getAbsolutePath());
								Chunk imagen = null;
								if(texto0.trim().equalsIgnoreCase("")){
									imagen = new Chunk(imgElem,questionForm.getSpacingAfter()+20,questionForm.getFirstLineIndent()-5);
								}else{
									imagen = new Chunk(imgElem,questionForm.getSpacingAfter(),questionForm.getFirstLineIndent()-10);
								}
								
								questionForm.add(imagen);
							}catch(Exception e){
								e.printStackTrace();
								salir=true;
							}
						}
						
						
						String aux = new String(text);
						if(text.indexOf("`")!=-1){
							text= text.substring(texto0.length()+latex.length()+2);
						}
						if(text.equalsIgnoreCase(aux))
							salir = true;
					}catch(Exception e){
						salir = true;
					}
				}
			}
		if(!text.trim().equalsIgnoreCase(""))
			questionForm.add(tratarTexto(text));
		return questionForm;
	}

	public abstract void write(OutputStream out) throws DocumentException, IOException;
}