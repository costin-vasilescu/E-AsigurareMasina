package proiect_oop;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.*;
import java.io.FileInputStream;

public class PrinterText extends Thread{
    private PrintRequestAttributeSet pras;
    private DocFlavor flavor;

    public PrinterText(PrintRequestAttributeSet pras, DocFlavor flavor) {
        this.pras = pras;
        this.flavor = flavor;
    }

    public void run() {
        try{
            PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            PrintService service = ServiceUI.printDialog(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(), 200, 200,
                    printService, defaultService, flavor, pras);
            if (service != null) {
                DocPrintJob job = service.createPrintJob();
                FileInputStream fis = new FileInputStream("contract.html");
                DocAttributeSet das = new HashDocAttributeSet();
                Doc document = new SimpleDoc(fis, flavor, das);
                job.print(document, pras);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
