// TODO: Rename the Java Project (ProcessingUI) and the package (packTwo) to
// something more relevant

package packTwo;

import java.io.File;
import java.io.UnsupportedEncodingException;

import processing.core.PApplet;

public class GUI extends PApplet
{
	//this class uses processing code
	
	private Boolean hasUploadedFile = false;
	private String errorMessage;
	private BookIndexer bookProcessor;

	public static void main(String[] args)
	{
		PApplet.main("packTwo.GUI");
	}

	@Override
	public void settings()
	{
		size(800, 450);
	}

	@Override
	public void setup()
	{
		errorMessage = "Program loaded.";
		bookProcessor = new BookIndexer();
	}

	@Override
	public void draw()
	{
		//makes the interface
		background(255, 255, 255);

		stroke(255, 0, 0);
		noFill();
		rect((float) (0.1 * width), (float) (0.1 * height), (float) (0.8 * width), (float) (0.3 * height));
		rect((float) (0.1 * width), (float) (0.5 * height), (float) (0.8 * width), (float) (0.4 * height));

		noStroke();
		fill(0, 0, 0);
		textSize((float) (height * 0.05));
		textAlign(RIGHT, BOTTOM);
		text(errorMessage, (float) (width - 5), (float) (height - 5));

		textSize((float) (height * 0.08));
		textAlign(LEFT, BOTTOM);
		text("Select a PDF file", (float) (width * 0.1), (float) (height * 0.2));

		textAlign(LEFT, BOTTOM);
		text("Click to generate the index", (float) (width * 0.1), (float) (height * 0.6));
	}

	@Override
	public void mousePressed()
	{
		if(((0.1 * width) < mouseX) && (mouseX < (0.9 * width)) && ((0.1 * height) < mouseY) && (mouseY < (0.4 * height))) {
			selectInput("Select the downloaded XML file", "selectPDF");
		} else if(((0.1 * width) < mouseX) && (mouseX < (0.9 * width)) && ((0.5 * height) < mouseY) && (mouseY < (0.9 * height))) {
			processBook();
		}
	}

	public void selectPDF(File selection)
	{
		if(selection == null)
		{
			println("You didn't select an acceptable xml file.");
			errorMessage = "File selection error.";
		}
		else
		{
			if (selection.getName().substring(selection.getName().length()-4, selection.getName().length()).contentEquals(".pdf")) {
				
				println("You selected the file " + selection.getAbsolutePath());
				bookProcessor.selectInputFile(selection);
				errorMessage = "Selecting a PDF File succeeded.";
				hasUploadedFile = true;
			} else {
				errorMessage = "That's no PDF File!";
			}
		}
	}

	public void processBook()
	{
		errorMessage = "Starting generating an index.";
		try {
			bookProcessor.processFile();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (hasUploadedFile) {
			errorMessage = "An index has been generated in the same folder that your PDF file is in.";
		} else {
			errorMessage = "There was an error.";
		}
	}
}
