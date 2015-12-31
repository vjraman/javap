package example;

import org.w3c.dom.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * Created by rags on 12/19/2015.
 *
 *
 */

class DraggedElementListener implements DragGestureListener
{
    @Override
    public void dragGestureRecognized(DragGestureEvent event)
    {
        Cursor cursor = null;
        DragButton dragButton = (DragButton) event.getComponent();

        if (event.getDragAction() == DnDConstants.ACTION_MOVE)
        {
            cursor = DragSource.DefaultCopyDrop;
        }

        event.startDrag(cursor, new TransferableElement(dragButton));
    }
}


class DroppedElementListener extends DropTargetAdapter implements DropTargetListener
{
    private DropTarget dropTarget;
    private ListButtonPanel jpanel;
    DataFlavor dataFlavor = new DataFlavor(DragButton.class, DragButton.class.getSimpleName());

   public DroppedElementListener(ListButtonPanel panel)
   {
       this.jpanel= panel;
       this.dropTarget=new DropTarget(jpanel, DnDConstants.ACTION_MOVE,this,true,null);
   }

    public void drop(DropTargetDropEvent event)
    {
        try
        {
            Transferable tr = event.getTransferable();
            DragButton jbmt = (DragButton) tr.getTransferData(dataFlavor);
            Element m = jbmt.getElement();
            if (event.isDataFlavorSupported(dataFlavor))
            {
                event.acceptDrop(DnDConstants.ACTION_MOVE);
                this.jpanel.addElement(m,true);
                if(jpanel.isMove())
                {
                    ((ListButtonPanel)ListButtonPanel.dragPanels.get(jbmt.getDragPanelId())).removeElement(m);
                }
                event.dropComplete(true);
                this.jpanel.validate();
                return;
            }
            event.rejectDrop();
        } catch (Exception e)
        {
            e.printStackTrace();
            event.rejectDrop();
        }
    }
}



public class ListButtonPanel extends JPanel
{
    private Node container;
    private boolean move = false;
    public static Hashtable dragPanels = new Hashtable();
    private String dragPanelId = Integer.toString(this.hashCode());
    private Document document;

    private DragSource dragSource = new DragSource();
    private DraggedElementListener draggedElementListener = new DraggedElementListener();

    public boolean isMove()
    {
        return move;
    }

    public  ListButtonPanel(Element el, Document document, boolean move)
    {
        this.document = document;
        this.move=move;
        dragPanels.put(dragPanelId,this);
        container = el.getChildNodes().item(0);
        NodeList nl  = container.getChildNodes();
        for(int i = 0;i<nl.getLength();i++)
        {
            Node n = nl.item(i);
            if(!(n instanceof Text))
            {
                Element comp = (Element) n;
                addElement(comp,false);
            }

        }
        new DroppedElementListener(this);
    }

    public void addElement(Element element, boolean clone)
    {
       Element dupl = element;
       if(clone)
       {
           dupl = (Element) element.cloneNode(true);
           dupl = (Element) document.importNode(dupl,true);
           container.appendChild(dupl);
       }
        DragButton dragButton = new DragButton(dupl,dragPanelId);
        add(dragButton);
        dragSource.createDefaultDragGestureRecognizer(dragButton,DnDConstants.ACTION_MOVE, draggedElementListener);
    }

    public void removeElement(Element element)
    {
        String nodename = element.getTagName();
        Component[] ca = getComponents();
        for(int i=0;i<ca.length;i++)
        {
            DragButton jb = (DragButton) ca[i];
            if (jb.getElement().getAttribute("title").equals(element.getAttribute("title")))
            {
                remove(jb);
            }

            repaint();
        }
        NodeList nl = container.getChildNodes();

        for(int i=0;i<nl.getLength();i++)
        {
            Node n = nl.item(i);
            if(n.getNodeName().equals(nodename))
            {
                Element removeEl = (Element)n;
                if(removeEl.getAttribute("title").equals(element.getAttribute("title")))
                {
                    container.removeChild(removeEl);
                }
            }

        }
    }
}
