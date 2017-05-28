/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava.Model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
public class SaveList {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SaveList.class);

    public static void saveList(PlayItem item) {

        logger.info(item.toString());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        
        File file = new File("output.xml");
        if (!file.exists() && !file.isDirectory()) {
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer;

            try {
                transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                Document doc = builder.newDocument();
                
                Element root=doc.createElement("MediaElements");
                doc.appendChild(root);
                Element mediaElements=doc.createElement("Media");
                root.appendChild(mediaElements);
                
                Element title = doc.createElement("Title");
                Text txtTitle=doc.createTextNode(item.getTitle());
                title.appendChild(txtTitle);
                mediaElements.appendChild(title);
                
                Element length = doc.createElement("Length");
                Text txtLength=doc.createTextNode(item.getLenght());
                length.appendChild(txtLength);
                mediaElements.appendChild(length);
                
                Element path = doc.createElement("Path");
                Text txtPath=doc.createTextNode(item.getUriPath());
                path.appendChild(txtPath);
                mediaElements.appendChild(path);
                
                Element ext = doc.createElement("Extension");
                Text txtExt=doc.createTextNode(item.getExtension());
                ext.appendChild(txtExt);
                mediaElements.appendChild(ext);
                
                DOMSource source = new DOMSource(doc);
                StreamResult fileRsult = new StreamResult("output.xml");
                
                transformer.transform(source, fileRsult);
                
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(SaveList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(SaveList.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
           
        }
    }

}
