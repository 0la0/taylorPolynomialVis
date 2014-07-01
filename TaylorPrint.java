import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class TaylorPrint extends JPanel{
	private final int IMAGE_HEIGHT = 1800;
	private final int IMAGE_WIDTH = 2700;
	private final int DISPLAY_HEIGHT = 600;
	private final int DISPLAY_WIDTH = 900;
	private BufferedImage bi = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);  
	private Polygon p;
	
	private final int DRAWNUM = 990;
	private int[] xPoints = new int[DRAWNUM];
	private int[] yPoints= new int[DRAWNUM];
	private int nPoints;
	
	private double xAdj = 10;
	private double yAdj = 900;
	private double xPlot = 400;
	private double yPlot = 400;
	private int scale = 40;
	
	private Color myColor = new Color(0, 0, 0, 50);
	private float lineThickness = 0.5f;
	
	private double x = 0;
	private double xStep = 0.1;
	private double a = 0;
	private int printNum = 0;
	private int ctr = 0;
	
	private int degree = 9;
	  
		
	public TaylorPrint(){
		clearImage();
		for (int i = 0; i < 5000; i++)
			go();
		repaint();
		saveImage();
	}
	  
	
	  
	  private void go(){
		  x = 0;
		  p = new Polygon();
		  makeTaylor();
		  printImage();
		  
		  a += 0.01;
	  }

	  private void makeTaylor(){
		  for(int i = 0; i < DRAWNUM; i++){
			  p.addPoint((int)xPlot, (int)yPlot);  
			  if (i < DRAWNUM - 1)
				  taylorEQ();
		  }
		  p.xpoints[0] = p.xpoints[1];
		  p.ypoints[0] = p.ypoints[1];
		  
		  xPoints=p.xpoints;
		  yPoints=p.ypoints;
		  nPoints=p.npoints;
	  }
	  
	  private void taylorEQ(){
		  yPlot = 0;
		  xPlot = x;
		
		  for (int i = 0; i <= degree; i++){
			  int posNeg = 1;
			  if (i == 2 || i == 3 || i == 6 || i == 7 || i == 10 || i == 11 || i == 14 || i == 15)
				  posNeg = -1;
			  if ((i % 2) == 0)
				  yPlot += (posNeg / factorial(i)) * Math.sin(a) * Math.pow((x - a), i);
			  else
				  yPlot += (posNeg / factorial(i)) * Math.cos(a) * Math.pow((x - a), i);
		  }
		  
		  xPlot *= scale;
		  yPlot *= scale;
		  xPlot += xAdj;
		  yPlot += yAdj;
		  
		  x += xStep;
	  }	  
	  
	  private static double factorial(double n){
			if (n == 0)
				return 1;
			else
				return n * factorial(n - 1);
	  }
	  
	  private void clearImage(){
		  Graphics2D g2d = bi.createGraphics();
		  g2d.setBackground(Color.white);
		  g2d.clearRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		  g2d.dispose();
	  }
	  
	  @Override
	  public void paintComponent(Graphics g) {
		  Graphics2D g2d = (Graphics2D) g;
		  g2d.drawImage(bi, 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, null);
		  g2d.dispose();
	  }
	  
	  private void printImage(){
		  Graphics2D g2 = bi.createGraphics();
		  g2.setColor(myColor);
		  g2.setStroke(new BasicStroke(lineThickness));
		  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		  g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		  g2.drawPolyline(xPoints, yPoints, nPoints);
		  g2.dispose();    
	  }	
	  
	  public void saveImage(){
		    String fileName = "files/exports/" + "taylor_degree_" + degree + ".png"; 	
			try{
				File outputFile = new File(fileName);
				ImageIO.write(bi, "png", outputFile);
				JOptionPane.showMessageDialog(null, "Image has been saved in the files/exports folder.");
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Image could not be saved.");
				System.exit(1);
			}
			printNum++;
	 }
	 
}
