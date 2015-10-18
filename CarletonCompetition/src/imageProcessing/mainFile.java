package imageProcessing;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class mainFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainFile main = new mainFile();
		main.init();
	}



	private void init(){
		JFrame frame = buildFrame();
		JPanel pane = null;

		String imagePATH = "Directory/Samples/t";
		String backgroundPath = "Directory/Background/";
		String ext = ".bmp";

		int imageNum = 1;

		//while(true){
		try {
			System.out.print("Works?");
			BufferedImage img = ImageIO.read(new File(imagePATH + String.valueOf(imageNum) + ext));
			
			pane = new JPanel(){
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D)g;
					g2.drawLine(0, 0, frame.getWidth(), frame.getHeight());
					//g2.drawImage(img, 0, 0, null);
					
					g2.setColor(Color.RED);
					g2.fillOval(0, 0, 30, 30);
					g2.drawOval(0, 50, 30, 30);		
					g2.fillRect(50, 0, 30, 30);
					g2.drawRect(50, 50, 30, 30);
				}
			};
			
			
		} catch (IOException e) {
			System.out.print("Image not Found");
		}
		//}
		
		frame.add(pane);
	
	}

	private static JFrame buildFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		return frame;
	}

}