package example; /**
 * Created by rags on 12/1/2015.
 */
import javax.xml.bind.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import example.Customer;
import example.PhoneNumber;
import org.w3c.dom.*;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class Demo
{
    public static String trim(String input)
    {
        BufferedReader reader = new BufferedReader(new StringReader(input));
        StringBuffer result = new StringBuffer();
        try {
            String line;
            while ( (line = reader.readLine() ) != null)
                result.append(line.trim());
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse("src/weeklymenu.xml");

       /* XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();

        JAXBContext jc = JAXBContext.newInstance(CustomerList.class);


        Binder<Node> binder = jc.createBinder();
        binder.unmarshal(document); */
        Element e = document.getDocumentElement();


        /*  CustomerList cl =(CustomerList)binder.getJAXBNode(e);
        List<Customer> cst = cl.getCustomers();

        for(Customer cus: cst)
        {
            System.out.println(cus.getName());
            List<PhoneNumber> pn = cus.getPhoneNumbers();

            for(PhoneNumber s:  pn)
                System.out.println(s.getNumber());

        }*/

         NodeList nl = e.getFirstChild().getChildNodes();

         for(int i = 0;i<nl.getLength();i++)
         {
             Node n = nl.item(i);
             if(!(n instanceof Text))
             {
                 System.out.print(i);
                 System.out.println(nl.item(i).getNodeName());
                 System.out.println(nl.item(i).getAttributes().item(0).getNodeValue());
             }

         }





       // System.out.println(e.getTagName());
        /* Use Node to Get Object
        Node phoneNumberElement = (Node) xpath.evaluate("/customer/phoneNumbers/phoneNumber[2]", document, XPathConstants.NODE);
        PhoneNumber phoneNumber = (PhoneNumber) binder.getJAXBNode(phoneNumberElement);

        // Modify Object to Update DOM
        phoneNumber.setNumber("555-2OBJ");
        binder.updateXML(phoneNumber);
        System.out.println(xpath.evaluate("/customer/phoneNumbers/phoneNumber[2]", document, XPathConstants.STRING));

        // Modify DOM to Update Object
        phoneNumberElement.setTextContent("555-2DOM");
        binder.updateJAXB(document.getDocumentElement());
        System.out.println(phoneNumber.getNumber()); */
    }

}