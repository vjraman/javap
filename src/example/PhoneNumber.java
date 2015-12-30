package example; /**
 * Created by rags on 12/1/2015.
 */
import javax.xml.bind.annotation.*;

public class PhoneNumber {

    private String type;
    private String number;

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlValue
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
