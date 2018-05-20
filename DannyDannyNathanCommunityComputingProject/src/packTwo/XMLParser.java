//code comes from https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
//XXX: it's necessary that all line separators have the same character length, so the line separator string is a final static variable with value "\n"

package packTwo;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class XMLParser
{
	final static String LINE_SEPARATOR = "\n";

	private ArrayList<Integer> pageLocations;

	public XMLParser()
	{
		pageLocations = new ArrayList<Integer>();
	}

	public void parseBookXML(File input, File outputFile)
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(input);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			PrintWriter pw = new PrintWriter(outputFile); // storing the text of a book in a String is much slower than
															// writing it to a file then reading a file
			ArrayList<Integer> pageStartCharacterIndices = new ArrayList<Integer>();
			int charactersSeen = 0;

			NodeList nList = doc.getElementsByTagName("page");
			for(int i = 0; i < nList.getLength(); i++)
			{
				pageStartCharacterIndices.add(charactersSeen);

				Node nNode = nList.item(i);
				Element nNodeElement = (Element) nNode;
				NodeList nListTwo = nNodeElement.getElementsByTagName("block");
				for(int j = 0; j < nListTwo.getLength(); j++)
				{
					Node nNodeTwo = nListTwo.item(j);
					Element nNodeTwoElement = (Element) nNodeTwo;
					NodeList nListThree = nNodeTwoElement.getElementsByTagName("text");
					for(int k = 0; k < nListThree.getLength(); k++)
					{
						Node nNodeThree = nListThree.item(k);
						Element nNodeThreeElement = (Element) nNodeThree;
						NodeList nListFour = nNodeThreeElement.getElementsByTagName("line");
						for(int l = 0; l < nListFour.getLength(); l++)
						{
							Node nNodeFour = nListFour.item(l);
							Element nNodeFourElement = (Element) nNodeFour;
							NodeList nListFive = nNodeFourElement.getElementsByTagName("charParams");
							for(int m = 0; m < nListFive.getLength(); m++)
							{
								Node nNodeFive = nListFive.item(m);
								if(nNodeFive.getNodeType() == Node.ELEMENT_NODE)
								{
									Element eElement = (Element) nNodeFive;
									pw.print(eElement.getTextContent());
									charactersSeen += eElement.getTextContent().length();
								}
							}
							pw.print(LINE_SEPARATOR);
							charactersSeen += LINE_SEPARATOR.length();
						}
					}
				}
			}
			pw.flush();
			pw.close();
			pageLocations = pageStartCharacterIndices;
			// System.out.println("done parsing");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> getPageLocations()
	{
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < pageLocations.size(); i++)
		{
			output.add(pageLocations.get(i));
		}
		return output;
	}

}