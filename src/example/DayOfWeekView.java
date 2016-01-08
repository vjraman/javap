package example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by rags on 1/2/2016.
 */
public class DayOfWeekView extends JPanel
{
    private GridLayout horzLayout = new GridLayout(1,4);
    private Element supercontainer;

    ListButtonPanel [] mealPanel = new ListButtonPanel[4];

    public DayOfWeekView(Element supercontainer, Document document)// Element is day
    {
        super();
        this.supercontainer = supercontainer;
        Random rand = new Random();
        this.setBorder(new LineBorder(Color.CYAN));
        this.setLayout(horzLayout);


        for(int i=0; i<4;i++)
        {
            mealPanel[i] = new ListButtonPanel(document,true);
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            mealPanel[i].setBackground(new Color(r,g,b));
            mealPanel[i].setBorder(new EmptyBorder(5, 5, 5, 5));
            mealPanel[i].setBounds(100 + i * 450, 100, 400, 300);
            this.add(mealPanel[i]);
        }
        this.update(this.supercontainer);

    }

    public void update(Element day)
    {
        NodeList mealnodes = day.getChildNodes(); // add panels for childnode meals
        for(int i=0; i< mealnodes.getLength();++i)
        {
            Element mealnode = (Element) mealnodes.item(i); // meal
            mealPanel[i].update(mealnode);
        }
    }


}