package imageProcessing;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Graphics;
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

		String imagePATH = "Directory/Samples/t";
		String backgroundPath = "Directory/Background/";
		String ext = ".bmp";

		int imageNum = 1;

		//while(true){
		try {
			BufferedImage img = ImageIO.read(new File(imagePATH + String.valueOf(imageNum) + ext));
			
			frame.add(new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 0, 0, null);
				}
			});
		} catch (IOException e) {
			System.out.print("Image not Found");
		}
		//}
	}

	private static JFrame buildFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(150, 150);
		frame.setVisible(true);
		return frame;
	}

}
