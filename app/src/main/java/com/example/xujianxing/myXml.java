package com.example.xujianxing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.integer;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

 

public class myXml {
	
	StringBuffer sb=new StringBuffer();

	public void xml(String cFullFileName,String cRootName) {
		  sb.append( "<?xml version=\"1.0\" encoding=\"utf-8\"?> \r\n"); 
          sb.append( "<string xmlns=\"http://tempuri.org/\">kg�﹫��� ");
          sb.append( "�O��ƽ�ס� ");
          sb.append( "Pcs��pcs�� ");
          sb.append( "Roll���</string> ");
		try {
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder=dbf.newDocumentBuilder();
		  
		    Document document=dBuilder.parse(new File(cFullFileName));
		  
		    document.getDocumentElement().normalize();
		    NodeList nodeList=document.getElementsByTagName(cRootName);
		    for(int iNodeIndex=0;iNodeIndex<nodeList.getLength();iNodeIndex++)
		    {
		    	Node node=nodeList.item(iNodeIndex);
		    	
		    }
		    
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//<editor-fold desc="PULL XML字符串解析，用于从WEBSERVICE中返回的字符串值">
	public static String xml(StringBuilder sb)
	{
		String cContext="";
		try {
			XmlPullParserFactory xmlFactory=XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser =xmlFactory.newPullParser();
			xmlPullParser.setInput(new StringReader(sb.toString()));
			int eventType=xmlPullParser.getEventType();

			while (eventType!=XmlPullParser.END_DOCUMENT)
			{
				String cRead=xmlPullParser.getName();
				switch (eventType)
				{
					case XmlPullParser.START_TAG:
						cContext="";
						if("string".equals(cRead)) {
							cContext = xmlPullParser.nextText();
						}
						break;
				}

				eventType=xmlPullParser.next();

			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cContext;
	}
	//</editor-fold>



}
