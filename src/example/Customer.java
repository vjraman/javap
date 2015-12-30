package example; /**
 * Created by rags on 12/1/2015.
 */
import java.util.*;
import javax.xml.bind.annotation.*;


public class Customer
{
    private String name;

    private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
    @XmlAttribute
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    @XmlElementWrapper
    @XmlElement(name="phoneNumber")
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

}
