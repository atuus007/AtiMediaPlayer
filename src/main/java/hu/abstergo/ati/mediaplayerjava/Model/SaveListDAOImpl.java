/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava.Model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Fodor Edit
 */
public class SaveListDAOImpl implements SaveFileDAO{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SaveListDAOImpl.class);

    
    @Override
    public void saveList(List<PlayItem> items) {

        logger.info(items.get(0).toString());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        StreamResult fileRsult = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer;
        File file = new File("output.xml");
        if (file.exists() && !file.isDirectory()) {
            logger.info("Exist");
            fileRsult = new StreamResult("output.xml");
        } else {
            logger.info("Not Exist");
            fileRsult = new StreamResult(new File("output.xml"));

        }
        try {
            transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Document doc = builder.newDocument();
            DOMSource source = new DOMSource(doc);

            Element root = doc.createElement("MediaElements");
            doc.appendChild(root);

            for (PlayItem p : items) {
                Element mediaElemetns = doc.createElement("Media");
                root.appendChild(mediaElemetns);
                logger.info("Save: " + p.toString());
                Element title = doc.createElement("Title");
                Text txtTitle = doc.createTextNode(p.getTitle());
                title.appendChild(txtTitle);
                mediaElemetns.appendChild(title);

                Element length = doc.createElement("Length");
                Text txtLength = doc.createTextNode(p.getLenght());
                length.appendChild(txtLength);
                mediaElemetns.appendChild(length);

                Element path = doc.createElement("Path");
                Text txtPath = doc.createTextNode(p.getUriPath());
                path.appendChild(txtPath);
                mediaElemetns.appendChild(path);

                Element ext = doc.createElement("Extension");
                Text txtExt = doc.createTextNode(p.getExtension());
                ext.appendChild(txtExt);
                mediaElemetns.appendChild(ext);
            }
            transformer.transform(source, fileRsult);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(SaveListDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(SaveListDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }              
    }
    @Override
    public List<PlayItem> loadItems(){
        List<PlayItem> media=new ArrayList<>();
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
           
        try {
           
            DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
            File input=new File("output.xml");
            
            Document doc=dBuilder.parse(input);
            doc.normalize();
            
            NodeList nodeList = doc.getElementsByTagName("Media");
            logger.info("AAAAAAAAAAAAAAAAAAAA "+nodeList.getLength());
          
           
               
            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                Element element =(Element)node;
                
                
                
                logger.info("Betolt: "+element.getElementsByTagName("Length").item(0).getTextContent());
                logger.info("Betolt: "+element.getElementsByTagName("Path").item(0).getTextContent());
                logger.info("Betolt: "+element.getElementsByTagName("Extension").item(0).getTextContent());
                
                String title=element.getElementsByTagName("Title").item(0).getTextContent();
                String length=element.getElementsByTagName("Length").item(0).getTextContent();
                String path=element.getElementsByTagName("Path").item(0).getTextContent();
                String ext=element.getElementsByTagName("Extension").item(0).getTextContent();
                
                PlayItem init=new PlayItem(title, length, ext, path);
                media.add(init);
            }
            //return null;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SaveListDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SaveListDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveListDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return media;
    }
}
