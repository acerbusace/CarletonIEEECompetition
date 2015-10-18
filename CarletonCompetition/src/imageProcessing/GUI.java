package imageProcessing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class GUI extends JFrame implements Runnable {

	int width = 600; //sets the width of the window/JFrame
	int height = 600; //sets the height of the window/JFrame

	String imagePATH = "Directory/Samples/t";
	String backgroundPath = "Directory/Background/";
	String ext = ".bmp";

	int imageNum = 1;
	BufferedImage background = null;
	BufferedImage img = null;
	int radius = 12;
	int radiusEnd = 16;
	int offset = 0;


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


		//try and catch statement, if the image file could not be found
		try {

			//the following lines of code take the names of the buffered images above and relate them to the images actually found
			//in the game files using the ImageIO.read command to 'read' these images and incorporate them into the program. as mentioned
			//above, most of these screens are externally developed (Microsoft Paint) to suit the design of the game.

			background = ImageIO.read(new File(backgroundPath + "th_DSC_0258" + ext));
			img = ImageIO.read(new File(imagePATH + String.valueOf(imageNum) + ext));
			//img = ImageIO.read(new File(backgroundPath + "th_DSC_0258" + ext));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not load the image files.");
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


		if (!once){
			System.out.println("Once");
			for (int i = 0; i < background.getWidth(); i++){

				//System.out.println("Outer Loop");
				for (int j = 0; j < background.getHeight(); j++){
					//System.out.println("Inner Loop");
					System.out.println(Math.abs(background.getRGB(i, j) - img.getRGB(i, j)));
					if (Math.abs(background.getRGB(i, j) - img.getRGB(i, j)) < 999999){
						//System.out.println(":D:D:D");
						img.setRGB(i, j, new Color(255, 255, 255).getRGB());
						//img.set
					}
				}
			}
			
			boolean washerGood = false;
			for (int i = radius; i < img.getWidth() - radius; i++){
				for (int j = radius + offset; j < img.getHeight() - radius - offset; j++){
					boolean correct = true;
					
					for (int k = 0; k <= radius; k++){
						int y = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow(k, 2));
						if (img.getRGB(i + k, j + y + offset) == new Color(255, 255, 255).getRGB() || img.getRGB(i + k, j - y - offset) == new Color(255, 255, 255).getRGB() || img.getRGB(i - k, j + y + offset) == new Color(255, 255, 255).getRGB() || img.getRGB(i - k, j - y - offset) == new Color(255, 255, 255).getRGB()){
							System.out.println("Not Correct!!!");
							correct = false;
							break;
						}
					}
					
					if (correct){
						washerGood = true;
						
						for (int k = 0; k <= radius; k++){
							int y = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow(k, 2));
							img.setRGB(i + k, j + y + offset, new Color(255, 0, 0).getRGB());
							img.setRGB(i + k, j - y - offset, new Color(255, 0, 0).getRGB());
							img.setRGB(i - k, j + y + offset, new Color(255, 0, 0).getRGB());
							img.setRGB(i - k, j - y - offset, new Color(255, 0, 0).getRGB());
						}
						
						
						
						break;
					}
				}
				if (washerGood){
					break;
				}
			}
			
			if (washerGood){
				System.out.println("Good Washer!!!");
			}

			once = true;
		}
	}




	//create a component that you can actually draw on.
	class DrawPane extends JPanel
	{
		public void paintComponent (Graphics g)
		{
			//draw on g here e.g.
			Graphics2D g2 = (Graphics2D) g;

			g2.drawImage(img, 0, 0, null);



		}
	}




}


