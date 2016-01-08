package example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.awt.*;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 * Created by rags on 12/28/2015.
 */

//EventListener myModificationListener =


class ModListener implements EventListener,Serializable
{

@Override
public void handleEvent(org.w3c.dom.events.Event e) {
        if (e instanceof MutationEvent) {
        MutationEvent me = (MutationEvent) e;
        System.out.println("type: " + me.getType()
        + ", dest: " + me.getTarget());
        }


  }

 };

class NewDish implements ActionListener
{
    private Document document;
    private ListButtonPanel meallist;

    public NewDish(Document document, ListButtonPanel meallist)
    {
        this.document=document;
        this.meallist=meallist;
    }

    public void actionPerformed(ActionEvent e) {

        JTextField xField = new JTextField(5);
        JTextArea yField = new JTextArea(20,20);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("dish title:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("dish description:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Dish", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                XPathFactory xpf = XPathFactory.newInstance();
                XPath xpath = xpf.newXPath();
                Element sampledishnode = (Element) xpath.evaluate("/weeklymenu/sample/dish", document, XPathConstants.NODE);
                sampledishnode.setAttribute("title", xField.getText());
                sampledishnode.appendChild(document.createTextNode(yField.getText()));
                meallist.addElement(sampledishnode, true);
                meallist.getParent().validate();
            } catch (Exception ex) {
                System.out.println("Exception " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}

class EditDish implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {

    }

}

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
            FileOutputStream fos= new java.io.FileOutputStream("src/weeklymenuful.xml");
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

class Scroll implements ActionListener
{
    private Document document;
    private WeeklyMenuPlan wmp;

    public Scroll(Document doc, WeeklyMenuPlan wmp)
    {
        this.document=doc;
        this.wmp=wmp;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            if (wmp.currentweek == 8)
              wmp.updateWeek(1);
            else
              wmp.updateWeek(1 + wmp.currentweek);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void setDocument(Document doc)
    {
        this.document=document;
    }
}


public class WeeklyMenuPlan extends JFrame
{
    public int currentweek = 1;
    private Document document;

    private JPanel contentPane;
    //private GridLayout vertLayout = new GridLayout(1,4);

    private FlowLayout vertLayout = new FlowLayout(0);
    // content pane with vertical ordering for each day of the week
    private String [] daysofweek = {"daysofweek", "Monday", "Tuesday", "Wednesday", "Thursday","Friday", "Saturday", "Sunday"};
    private JPanel DOWP = new JPanel();
    private JPanel TRSP = new JPanel();
    private JPanel MEALP = new JPanel();
    private JPanel RESP = new JPanel();
    public ListButtonPanel meallist = null;


    public WeeklyMenuPlan(Document document)
    {
        this.document=document;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,1800,900);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(vertLayout);


        try
        {
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            Node userElement = (Node) xpath.evaluate("/weeklymenu/user", document, XPathConstants.NODE);
            String weekno = userElement.getChildNodes().item(0).getNodeValue();
            updateWeek(Integer.valueOf(weekno));
            this.meallist=new ListButtonPanel((Element)xpath.evaluate("/weeklymenu/dishes",document, XPathConstants.NODE),document,false);
            MEALP.add(meallist);
        }
        catch(Exception ex)
        {
            System.out.println("Exception " + ex.getMessage() );
            ex.printStackTrace();
        }


        TRSP.setBounds(100, 100, 200, 800);
        TRSP.setMaximumSize(new Dimension(200,800));
        TRSP.setPreferredSize(new Dimension(200,800));
        TRSP.setBorder(new LineBorder(Color.WHITE, 5));
        TRSP.setLayout(new GridLayout(2,1));


        JPanel buttons = new JPanel();
        JButton saveB = new JButton("Save");
        saveB.addActionListener(new Save(document));
        JButton scrollB = new JButton("Scroll");
        scrollB.addActionListener(new Scroll(document,this));

        JButton newDishB = new JButton("New Dish");
        newDishB.addActionListener(new NewDish(document,meallist));


        buttons.add(saveB);
        buttons.add(scrollB);
        buttons.add(newDishB );



        TRSP.add(buttons);

        ListButtonPanel  tlp =new ListButtonPanel((Element)document.getDocumentElement().getChildNodes().item(0),document, true);
        tlp.setBorder(new LineBorder(Color.RED, 5));
        TRSP.add(tlp);

        //System.out.println(((Element)document.getDocumentElement().getChildNodes().item(0)).getNodeName());

        DOWP.setBorder(new LineBorder(Color.WHITE, 5));
        DOWP.setLayout(new GridLayout(7, 1));
        DOWP.setBounds(350, 100, 1000, 800);
        DOWP.setPreferredSize(new Dimension(900,800));

        MEALP.setBounds(1400, 100, 800, 800);
        MEALP.setBorder(new LineBorder(Color.BLUE, 5));
        MEALP.setLayout(new GridLayout(7, 1));
        MEALP.setPreferredSize(new Dimension(600,800));
        //RESP.setBounds(2250, 100, 400,800);
        //RESP.setBorder(new LineBorder(Color.WHITE, 5));

        contentPane.add(TRSP);
        contentPane.add(DOWP);
        contentPane.add(MEALP);
        //contentPane.add(RESP);



        setContentPane(contentPane);




    }

    public void updateWeek(int weeknum)
    {

        setTitle("Weekly Menu Sample number " + new Integer(weeknum).toString());
        currentweek=weeknum;
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        String accessStr = "/weeklymenu/weeks/week["+ currentweek +"]";
        Node weeknode = null;
        try {
            weeknode = (Node) xpath.evaluate(accessStr, document, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        NodeList daynodes = weeknode.getChildNodes();
        this.DOWP.removeAll();
        for(int i=0;i<daynodes.getLength();i++)
        {
            Element day = (Element)daynodes.item(i);

            this.DOWP.add(new DayOfWeekView(day,this.document));
        }
        validate();
    }


    public static Document getDocument(String filename)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(filename);

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

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

            Node weeksnode = (Node)xpath.evaluate("/weeklymenu/weeks/week[1]", document, XPathConstants.NODE);

            System.out.println(weeksnode.getNodeName());
            ModListener myModificationListener = new ModListener();
            ((EventTarget) weeksnode).addEventListener("DOMSubtreeModified",myModificationListener, false);


            return document;

        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }


        return null;
    }


    public static void main(String[] args)
    {
            final Document docroot = getDocument("src/weeklymenuful.xml");

            EventQueue.invokeLater(
                    new Runnable()
                    {
                         public void run()
                         {
                          try
                          {
                              new WeeklyMenuPlan(docroot).setVisible(true);
                          } catch (Exception e)
                          {
                            e.printStackTrace();
                          }
                         }
                    }
            );


    }


}
