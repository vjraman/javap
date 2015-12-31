package example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by rags on 12/28/2015.
 */

class Save implements ActionListener
{
    private Document doc;

    public Save(Document doc)
    {
        this.doc=doc;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try {


            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(this.doc);
            FileOutputStream fos= new java.io.FileOutputStream("src/weeklymenu.xml");
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
            fos.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void setDocument(Document doc)
    {
        this.doc=doc;
    }
}
public class WeeklyMenuPlan extends JFrame
{
    public WeeklyMenuPlan()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,1800,500);
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(1,3));
        setContentPane(jp);

       try {

           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
           dbf.setNamespaceAware(true);
           DocumentBuilder db = dbf.newDocumentBuilder();
           Document document = db.parse("src/weeklymenu.xml");

           XPathFactory xpathFactory = XPathFactory.newInstance();
           // XPath to find empty text nodes.
           XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");
           NodeList emptyTextNodes = (NodeList)xpathExp.evaluate(document, XPathConstants.NODESET);

           // Remove each empty text node from document.
           for (int i = 0; i < emptyTextNodes.getLength(); i++)
           {
               Node emptyTextNode = emptyTextNodes.item(i);
               emptyTextNode.getParentNode().removeChild(emptyTextNode);
           }

        Element dishE = (Element) document.getDocumentElement().getChildNodes().item(0);
        Element mealE = (Element) document.getDocumentElement().getChildNodes().item(1);

        System.out.println(mealE.getNodeName());


        ListButtonPanel dishP = new ListButtonPanel(dishE,document, false);
        dishP.setBounds(100, 100, 200, 400);
        dishP.setMaximumSize(new Dimension(200,400));
        dishP.setBorder(new LineBorder(Color.WHITE, 5));

        ListButtonPanel mealP = new ListButtonPanel(mealE,document,true);
        mealP.setBounds(350, 100, 1000, 400);
        mealP.setMaximumSize(new Dimension(200,400));
        mealP.setBorder(new LineBorder(Color.WHITE, 5));

        JButton saveB = new JButton("Save");
        saveB.addActionListener(new Save(document));
        jp.add(saveB);

        jp.add(dishP);
        jp.add(mealP);

       }
       catch(Exception ex)
       {

       }

    }


    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new WeeklyMenuPlan().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
