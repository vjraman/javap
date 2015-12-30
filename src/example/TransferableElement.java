package example;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by rags on 12/27/2015.
 */
public class TransferableElement implements Transferable
{

    DataFlavor dataFlavor = new DataFlavor(DragButton.class, DragButton.class.getSimpleName());
    private DragButton dragButton;
    private String panel;

    public TransferableElement(DragButton dragButton)
    {
        this.dragButton = dragButton;

    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { dataFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(dataFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor)  throws UnsupportedFlavorException, IOException
    {

        if (flavor.equals(dataFlavor))
            return dragButton;
        else
            throw new UnsupportedFlavorException(flavor);
    }

}
