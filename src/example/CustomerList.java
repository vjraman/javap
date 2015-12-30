package example;

import com.sun.xml.internal.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 12/4/2015.
 */
@XmlRootElement
public class CustomerList
{
    private List<Customer> customers = new ArrayList<Customer>();
    @XmlElementWrapper
    @javax.xml.bind.annotation.XmlElement(name="customer")
    public List<Customer> getCustomers(){return customers;}
}
