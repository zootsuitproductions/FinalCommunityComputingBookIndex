package packTwo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class PageTagger
{
	private ArrayList<Integer> pageNumbers;
	private ArrayList<Integer> startLocations;

	public void findPageNumbers(String filePath, ArrayList<Integer> pageXML, ArrayList<Integer> locationXML)
	{
		String inputFileContents = readFileIntoString(filePath);

		ArrayList<Integer> foundNumbers = new ArrayList<Integer>();		//array for all numbers in the text file
		ArrayList<Integer> location = new ArrayList<Integer>();		//corresponding array for the number's location

		int counter = 0;
		String foundDigits = "";		//stores consecutive digits found in the text. it must cleared for each new number found
		while(counter < inputFileContents.length())
		{
			if(isDigit(inputFileContents.charAt(counter)))
			{
				foundDigits += inputFileContents.charAt(counter);
			}
			else
			{
				//if the character is not a digit, the number must have ended. in that case, record foundDigits if a number was found and clear foundDigits
				if(foundDigits.length() != 0)
				{
					foundNumbers.add(Integer.parseInt(foundDigits));
					location.add(counter - foundDigits.length());
				}
				foundDigits = "";
			}
			counter++;
		}
		orderPages(foundNumbers, location, pageXML, locationXML);
	}

	// returns the text of the file
	// code comes from
	// https://howtodoinjava.com/core-java/io/java-read-file-to-string-examples/
	private static String readFileIntoString(String filePath)
	{
		String content = "";
		try
		{
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}

	//method to figure out which numbers are page numbers
	public void orderPages(ArrayList<Integer> foundNumbers, ArrayList<Integer> numberLocations, ArrayList<Integer> pageXML, ArrayList<Integer> locationXML)
	{
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();		//key is page offset and value is number of pages with a digit with that offset
		int counter = 0;
		for(int i = 0; i < pageXML.size(); i++)
		{
			ArrayList<Integer> dif = new ArrayList<Integer>();
			while(counter < numberLocations.size() && numberLocations.get(counter) < locationXML.get(i))
			{
				dif.add(foundNumbers.get(counter) - pageXML.get(i));
				counter++;
			}

			//remove duplicates in dif
			Collections.sort(dif);
			int counter2 = 0;
			while(counter2 + 1 < dif.size())
			{
				if(dif.get(counter2) == dif.get(counter2 + 1))
				{
					dif.remove(counter2);
				}
				else
				{
					counter2++;
				}
			}

			//store values in dif in hm
			for(int o = 0; o < dif.size(); o++)
			{
				if(hm.get(dif.get(o)) == null)
				{
					hm.put(dif.get(o), 0);
				}
				hm.put(dif.get(o), hm.get(dif.get(o)) + 1);
			}
		}

		//find most frequent offset
		Integer maxOffsetFreq = 0;
		Integer offset = 0;
		Integer[] keys = hm.keySet().toArray(new Integer[0]);
		for(int i = 0; i < keys.length; i++)
		{
			Integer offsetFreq = hm.get(keys[i]);
			if(offsetFreq > maxOffsetFreq)
			{
				maxOffsetFreq = offsetFreq;
				offset = keys[i];
			}
		}

		//format output as instance variables
		ArrayList<Integer> outputPage = new ArrayList<Integer>();
		for(int i = 0; i < pageXML.size(); i++)
		{
			outputPage.add(pageXML.get(i) + offset);
		}
		pageNumbers = outputPage;
		startLocations = locationXML;
	}

	public ArrayList<Integer> getPageNumbers()
	{
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < pageNumbers.size(); i++)
		{
			output.add(pageNumbers.get(i));
		}
		return output;
	}

	public ArrayList<Integer> getStartLocations()
	{
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < startLocations.size(); i++)
		{
			output.add(startLocations.get(i));
		}
		return output;
	}

	private static boolean isDigit(char input)
	{
		return ('0' <= input) && (input <= '9');
	}
}
