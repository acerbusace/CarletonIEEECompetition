package imageProcessing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI extends JFrame implements Runnable {

	int fontSize = 20;
	
	int width = 600; //sets the width of the window/JFrame
	int height = 600 + fontSize; //sets the height of the window/JFrame

	long startingTime = System.currentTimeMillis();

	//PrintWriter = new ("Directory/");

	String imagePATH = "Directory/Samples/";
	String backgroundPath = "Directory/Background/";
	String ext = ".bmp";

	int imageNum = 1;
	BufferedImage background = null;
	BufferedImage background2 = null;
	BufferedImage img = null;
	BufferedImage img2 = null;
	int radius = 13;
	int radiusEnd = 16;
	int offset = 4;

	Font font = new Font("Calibri",Font.BOLD,fontSize);
	String print = "";

	boolean once = false;
	boolean running; //keeps the while loop inside the "run()" method running


	public GUI ()
	{
		super ("Pixels!"); //changes the name of the top of the window to "Missile Command"
		setSize (width, height); //Sets the size of the JFrame
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); //Sets the close operation for the JFrame

		setContentPane (new DrawPane ());

		//sets the layout to null
		setLayout (null);
		//sets it so, that the window/JFrame does not resize when it reaches the edges of the screen
		setLocationRelativeTo (null);

		//sets the windows/JFrame to visible
		setVisible (true);

		try {

			//the following lines of code take the names of the buffered images above and relate them to the images actually found
			//in the game files using the ImageIO.read command to 'read' these images and incorporate them into the program. as mentioned
			//above, most of these screens are externally developed (Microsoft Paint) to suit the design of the game.

			background = ImageIO.read(new File(backgroundPath + "th_DSC_0258" + ext));
			background2 = ImageIO.read(new File(backgroundPath + "th_DSC_0261" + ext));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could Not Find the Background Image.");
		}

		running = true;
	}


	public static void main (String args[])
	{
		new GUI ().run (); //creates a new instance of the GUI
	}


	@ Override
	public void run ()
	{
		// TODO Auto-generated method stub

		//Calculates when the game should be updated and when the window/JFrame should be updates

		long lastTime = System.nanoTime ();
		double nsPerCalc = 1000000000D / 60D;

		int calcs = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis ();
		double delta = 0;


		while (running)
		{

			long now = System.nanoTime ();
			delta += (now - lastTime) / nsPerCalc;
			lastTime = now;

			boolean shouldRender = true;

			while (delta >= 1)
			{
				calcs++;

				//Program's Logic
				calc ();

				delta -= 1;
				shouldRender = true;
			}

			if (shouldRender)
			{
				frames++;

				//drawing method
				revalidate ();
				repaint ();
			}

			try
			{
				Thread.sleep (2);
			}
			//throws an InterruptedException
			catch (InterruptedException e)
			{
				e.printStackTrace ();
			}

			if ((System.currentTimeMillis () - lastTimer) > 1000)
			{
				lastTimer += 1000;
				//System.out.println("FPS: " + calcs);
				frames = 0;
				calcs = 0;
			}
		}
	}




	private void calc() {
		// TODO Auto-generated method stub

		//try and catch statement, if the image file could not be found
		try {

			//the following lines of code take the names of the buffered images above and relate them to the images actually found
			//in the game files using the ImageIO.read command to 'read' these images and incorporate them into the program. as mentioned
			//above, most of these screens are externally developed (Microsoft Paint) to suit the design of the game.

			//Works every half a second
			if (System.currentTimeMillis() - startingTime > 1000){
				//reads the images
				img = ImageIO.read(new File(imagePATH + "t" + String.valueOf(imageNum) + ext));
				img2 = ImageIO.read(new File(imagePATH + "t" + String.valueOf(imageNum) + ext));

				double currentTime = System.currentTimeMillis();

				//detects the background pixels and sets the color to white
				for (int i = 0; i < background.getWidth(); i++){

					for (int j = 0; j < background.getHeight(); j++){
						if (Math.abs(background.getRGB(i, j) - img.getRGB(i, j)) < 777777){
							img.setRGB(i, j, new Color(255, 255, 255).getRGB());
						}
						
						if (Math.abs(background2.getRGB(i, j) - img2.getRGB(i, j)) < 777777){
							img2.setRGB(i, j, new Color(255, 255, 255).getRGB());
						}
					}
				}

				//checks if the washer is good or not
				boolean washerGood = false;
				//loops over every pixel
				for (int i = radiusEnd; i < img.getWidth() - radiusEnd; i++){
					for (int j = radiusEnd + offset; j < img.getHeight() - radiusEnd - offset; j++){
						boolean correct = true;

						//checks if the washer is good by detection the a circular radius around a specific point
						for (int r = radius; r <= radiusEnd; r++){
							for (int k = 0; k <= r; k++){
								int y = (int) Math.sqrt(Math.pow(r, 2) - Math.pow(k, 2));
								if (img.getRGB(i + k, j + y + offset) == new Color(255, 255, 255).getRGB() || img.getRGB(i + k, j - y - offset) == new Color(255, 255, 255).getRGB() || img.getRGB(i - k, j + y + offset) == new Color(255, 255, 255).getRGB() || img.getRGB(i - k, j - y - offset) == new Color(255, 255, 255).getRGB()){
									correct = false;
									break;
								}
							}
						}

						if (correct){
							washerGood = true;

							//draws the detection circle
							for (int k = 0; k <= radius; k++){
								int y = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow(k, 2));
								img.setRGB(i + k, j + y + offset, new Color(255, 0, 0).getRGB());
								img.setRGB(i + k, j - y - offset, new Color(255, 0, 0).getRGB());
								img.setRGB(i - k, j + y + offset, new Color(255, 0, 0).getRGB());
								img.setRGB(i - k, j - y - offset, new Color(255, 0, 0).getRGB());
							}

							break;
						} else {
							correct = true;
							
							for (int r = radius; r <= radiusEnd; r++){
								for (int k = 0; k <= r; k++){
									int y = (int) Math.sqrt(Math.pow(r, 2) - Math.pow(k, 2));
									if (img2.getRGB(i + k, j + y + offset) == new Color(255, 255, 255).getRGB() || img2.getRGB(i + k, j - y - offset) == new Color(255, 255, 255).getRGB() || img2.getRGB(i - k, j + y + offset) == new Color(255, 255, 255).getRGB() || img2.getRGB(i - k, j - y - offset) == new Color(255, 255, 255).getRGB()){
										correct = false;
										break;
									}
								}
							}
							
							if (correct){
								washerGood = true;

								for (int k = 0; k <= radius; k++){
									int y = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow(k, 2));
									img2.setRGB(i + k, j + y + offset, new Color(255, 0, 0).getRGB());
									img2.setRGB(i + k, j - y - offset, new Color(255, 0, 0).getRGB());
									img2.setRGB(i - k, j + y + offset, new Color(255, 0, 0).getRGB());
									img2.setRGB(i - k, j - y - offset, new Color(255, 0, 0).getRGB());
								}

								break;
							}
						}
					}
					if (washerGood){
						break;
					}
				}

				//prints out the washer information in the csv file
				double timeTaken = System.currentTimeMillis() - currentTime;
				if (washerGood){
					print = "t" + String.valueOf(imageNum) + ", " + LocalTime.now().toString() + ", G, " + String.valueOf(timeTaken) + ", " + ext;
					addToCSV("t" + String.valueOf(imageNum), LocalTime.now(), "G", String.valueOf(timeTaken), ext);
				} else {
					print = "t" + String.valueOf(imageNum) + ", " + LocalTime.now().toString() + ", B, " + String.valueOf(timeTaken) + ", " + ext;
					addToCSV("t" + String.valueOf(imageNum), LocalTime.now(), "D", String.valueOf(timeTaken), ext);
				}

				imageNum++;
				startingTime = System.currentTimeMillis();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No New Image File Detected: " + imagePATH + String.valueOf(imageNum) + ext);
		}
	}


	//adds the given information into the csv file
	public void addToCSV(String fileName,LocalTime localTime,String classification, String timTakenToClassify, String ext)
	{
		try {
			FileWriter resultsFile = new FileWriter("results.csv",true);
			resultsFile.append(fileName + ext);
			resultsFile.append(",");
			resultsFile.append(localTime.toString());
			resultsFile.append(",");
			resultsFile.append(classification);
			resultsFile.append(",");
			resultsFile.append(timTakenToClassify);
			resultsFile.append("\n");

			resultsFile.flush();
			resultsFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//create a component that you can actually draw on.
	class DrawPane extends JPanel
	{
		public void paintComponent (Graphics g)
		{
			//draw on g here e.g.
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(font);//sets the following text to be in a previously declared font
			g2.setColor(Color.RED);//sets the color for the following text to be black

			//draws the waasher picture
			g2.drawImage(img, 0, 0, this.getWidth(), this.getHeight() - fontSize, null);
			//prints out the washer information
			g2.drawString(print, this.getWidth()/4, this.getHeight());
		}
	}




}


