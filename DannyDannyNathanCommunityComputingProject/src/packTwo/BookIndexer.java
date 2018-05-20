// TODO: Add documentation of the class LOL

package packTwo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BookIndexer
{
	private String classifierLocation = "classifiers/english.muc.7class.distsim.crf.ser"; // the location of the classifier to be used by ner
	private File inputFile;		//the file that was last selected to be the input to the program
	
	public void selectInputFile(File selection)
	{
		inputFile = selection;
	}
	
	// TODO: handle case where inputFile is null
	public void processFile() throws UnsupportedEncodingException
	{
		if(inputFile != null)
		{
			NERProcessor ner = new NERProcessor();
			ner.initializeClassifier(classifierLocation);
			
			PDFParser coolName = new PDFParser();
			try {
				coolName.parse(inputFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String bookPlaintext = coolName.getText();
			ner.processText(bookPlaintext);
			
			ArrayList<String> termList = ner.getTermList();
			ArrayList<String> categoryList = ner.getCategoryList();
			ArrayList<Integer> termStartingIndices = ner.getTermStartingIndices();
			
			ArrayList<Integer> truePageNums = coolName.getTrueNumbers();
			ArrayList<Integer> truePageLocs = coolName.getPageLocations();
			HashMap<String, ArrayList<Integer>> termsIndex = new HashMap<String, ArrayList<Integer>>();
			int currentXMLPage = 0;
			for(int i = 0; i < termList.size(); i++)
			{
				String term = termList.get(i);
				String termNoNewLine = term.replace("\n", " ");
				
				// add term to termsIndex
				while(currentXMLPage + 1 < truePageLocs.size() && truePageLocs.get(currentXMLPage + 1) <= termStartingIndices.get(i))
				{
					currentXMLPage++;
				}
				int numberToAdd = truePageNums.get(currentXMLPage);
				
				// initialize termsIndex.get(termNoNewLine) to avoid a NullPointerException
				if(termsIndex.get(termNoNewLine) == null)
				{
					termsIndex.put(termNoNewLine, new ArrayList<Integer>());
				}
				
				//check for duplicate index entry
				boolean RECORD_DUPLICATE_TERMS_ON_SAME_PAGE = false;
				if(RECORD_DUPLICATE_TERMS_ON_SAME_PAGE || termsIndex.get(termNoNewLine).contains(numberToAdd) == false)
				{
					termsIndex.get(termNoNewLine).add(numberToAdd);
				}
			}
			
			// write termsIndex to a file
			try
			{
				String filePathOfInput = inputFile.getAbsolutePath();
				while (filePathOfInput.charAt(filePathOfInput.length()-1) != '/') {
					filePathOfInput = filePathOfInput.substring(0, filePathOfInput.length() - 1);
				}
				
				System.out.println(filePathOfInput);
				
				String docName = inputFile.getAbsolutePath().substring(filePathOfInput.length(),inputFile.getAbsolutePath().length()-4);
				
				PrintWriter pw = new PrintWriter(filePathOfInput+docName+"BookIndex.txt", "UTF-8");
				String[] keys = termsIndex.keySet().toArray(new String[0]);
				Arrays.sort(keys);
				for(int i = 0; i < keys.length; i++)
				{
					ArrayList<Integer> pageNumbers = termsIndex.get(keys[i]);
					pw.println(keys[i]);
					for(int j = 0; j < pageNumbers.size(); j++)
					{
						pw.println("\t" + pageNumbers.get(j));
					}
				}
				pw.flush();
				pw.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
}
