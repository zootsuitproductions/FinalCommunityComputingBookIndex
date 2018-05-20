package packTwo;

import java.io.IOException;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PDFParser {
	
	private String fullText = "";
	private ArrayList<Integer> pageStartLocation = new ArrayList<Integer>();
	private ArrayList<Integer> truePageNums = new ArrayList<Integer>();
	
    public void parse(File myFile) throws IOException {
    	
    	//code inspired by https://radixcode.com/pdfbox-example-code-how-to-extract-text-from-pdf-file-with-java

       PDFManager pdfManager = new PDFManager();
       pdfManager.setFile(myFile);  
       String[] byPage = pdfManager.ToText();
       int difference = getDifference(byPage,0); 

       int charactersSeen = 0;
       for (int i = 0; i < byPage.length; i ++) {
    	   this.pageStartLocation.add(charactersSeen);
    	   		charactersSeen += byPage[i].length();
    	   		this.truePageNums.add(i+1+difference);
    	   		this.fullText += byPage[i];
    	   	//fullText += "<p"+(i+1+difference)+">";
       }
}
    
    public String getText() {
    		return this.fullText;
    }
    
    public ArrayList<Integer> getPageLocations() {
    		return this.pageStartLocation;
    }
    
    public ArrayList<Integer> getTrueNumbers() {
		return this.truePageNums;
    }
    
    private int getDifference(String[] byPage, int tries) {
    	
    		if (byPage.length < 16) {
    			return 0;
    		}
    	
    		Random rand = new Random();
    		int  n = rand.nextInt(byPage.length-byPage.length/2) + byPage.length/4;
    		
    	 	String str0 = byPage[n-1];      
        str0 = str0.replaceAll("[^0-9]+", " ");
        String[] strs0 = str0.trim().split(" ");
        
        
        String str1 = byPage[n];      
        str1 = str1.replaceAll("[^0-9]+", " ");
        String[] strs1 = str1.trim().split(" ");
         
        String str2 = byPage[n+1];
        str2 = str2.replaceAll("[^0-9]+", " ");
        String[] strs2 = str2.trim().split(" ");
         
        int firstIndex = 0;
        while (tryParsing(strs2,firstIndex) == tryParsing(strs1,firstIndex)) {
        		firstIndex ++;
        }
        
        		if (tryParsing(strs2,firstIndex)- tryParsing(strs1,firstIndex)==1 &&  tryParsing(strs1,firstIndex)- tryParsing(strs0,firstIndex)==1) {
           	   //top of page
           	   	return checkWhichIsHigherAndReturn(n,n- tryParsing(strs0,firstIndex));
             } else if (tryParsing(strs2,strs2.length-1)-tryParsing(strs1,strs1.length-1)==1 && tryParsing(strs1,strs1.length-1)-tryParsing(strs0,strs0.length-1)==1) {
           	   //bottom of page
            	 
           	   	return checkWhichIsHigherAndReturn(n,n-Integer.parseInt(strs0[strs0.length-1]));
             } else if (tries < 10) {
             		return getDifference(byPage, tries+1);
             } else {
             		return 0;
             }
          
    }
    
    private int checkWhichIsHigherAndReturn(int pdfPage, int prospectiveReturn) {
    		if (pdfPage < prospectiveReturn) {
    			return prospectiveReturn;
    		} else {
    			return -prospectiveReturn;
    		}
    }
    
    private int tryParsing(String[] theStuff, int index) {
    		int toReturn = -30;
    		try {
    			toReturn = Integer.parseInt(theStuff[index]);
    		} catch (NumberFormatException e) {	
    		}
    		return toReturn;
    }
    
}
