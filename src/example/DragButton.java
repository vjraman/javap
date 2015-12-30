package example;

import org.w3c.dom.Element;

import javax.swing.*;

/**
 * Created by rags on 12/25/2015.
 */
public class DragButton extends JButton {
    private Element element;
    private String dragPanelId;

    public DragButton(Element element, String dragPanelId)
    {

        this.element=element;
        setText(element.getAttribute("title"));
        this.dragPanelId=dragPanelId;
    }

    public void setDragPanelId(String dragPanelId)
    {
        this.dragPanelId=dragPanelId;
    }

    public String getDragPanelId()
    {
        return this.dragPanelId;
    }

    public Element getElement()
    {
        return element;
    }

    public void setElement(Element element)
    {
        this.element=element;
    }

}
