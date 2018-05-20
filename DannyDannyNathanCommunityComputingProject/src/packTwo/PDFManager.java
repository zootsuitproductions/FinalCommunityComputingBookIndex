package packTwo;

import java.io.File;
import java.util.Random;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFManager {
    
   private PDFParser parser;
   private PDFTextStripper pdfStripper;
   private PDDocument pdDoc ;
   private COSDocument cosDoc ;
   
   private String Text ;
   //private File filePath;
   private File file;

    public PDFManager() {
        
    }
    //some code from https://radixcode.com/pdfbox-example-introduction-setup-dev-environment
   public String[] ToText() throws IOException
   {
       this.pdfStripper = null;
       this.pdDoc = null;
       this.cosDoc = null;
       
       //file = new File(filePath);
       parser = new PDFParser(new RandomAccessFile(file,"r")); // update for PDFBox V 2.0
       
       parser.parse();
       cosDoc = parser.getDocument();
       pdfStripper = new PDFTextStripper();
       pdDoc = new PDDocument(cosDoc);
       pdDoc.getNumberOfPages();
       
       System.out.println(pdDoc.getNumberOfPages());
       
       String[] toReturn = new String[pdDoc.getNumberOfPages()];
       
       for (int i = 0; i < pdDoc.getNumberOfPages(); i ++) {
    	   	pdfStripper.setStartPage(i+1);
    	    pdfStripper.setEndPage(i+1);
    	   	toReturn[i] = pdfStripper.getText(pdDoc);	
       }
       return toReturn;
   }

    public void setFile(File theFile) {
        this.file = theFile;
    }
   
}
