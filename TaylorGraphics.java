import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class TaylorGraphics extends JPanel{
	
	private Timer timer = new Timer(33, new TimerListener());
	private BufferedImage bi = new BufferedImage(900, 600, BufferedImage.TYPE_INT_RGB);  
	private Polygon p;
	
	private final int DRAWNUM = 220;
	private int[] xPoints = new int[DRAWNUM];
	private int[] yPoints= new int[DRAWNUM];
	private int nPoints;
	
	private double xAdj = 10;
	private double yAdj = 300;
	private double xPlot = 400;
	private double yPlot = 400;
	private int scale = 40;
	
	private Color myColor = new Color(100, 100, 100, 150);
	private float lineThickness = 2.0f;
	
	private double x = 0;
	private double xStep = 0.1;
	private double a = 0;
	
	private int degree;
	  
		
	public TaylorGraphics(int degree){
		if (degree < 0 || degree > 12){
			System.out.println("degree must be [0 - 12]");
			System.exit(0);
		}
		this.degree = degree;
		timer.start();
	}
	  
	
	  
	  private void go(){
		  clearImage();
		  myColor = new Color(100, 100, 100, 150);
		  x = 0;
		  p = new Polygon();
		  makeSine();
		  printImage();
		  repaint();
		  
		  myColor = new Color(50, 50, 50, 250);
		  x = 0;
		  p = new Polygon();
		  makeTaylor();
		  printImage();
		  repaint();
		  
		  a += 0.04;
		 
	  }
	  
	  private void makeSine(){
		  for(int i = 0; i < DRAWNUM; i++){
			  p.addPoint((int)xPlot, (int)yPlot);  
			  if (i < DRAWNUM - 1)
				  sinEQ();
		  }
		  p.xpoints[0] = p.xpoints[1];
		  p.ypoints[0] = p.ypoints[1];
		  
		  xPoints=p.xpoints;
		  yPoints=p.ypoints;
		  nPoints=p.npoints;
	  }
	  
	  private void sinEQ(){
		  
		  xPlot = x;
		  yPlot = Math.sin(x);
		  
		  xPlot *= scale;
		  yPlot *= scale;
		  xPlot += xAdj;
		  yPlot += yAdj;
		  
		  x += xStep;
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
		  
		  /*yPlot =       			Math.sin(a)
				  + 				Math.cos(a) * (x - a)
				  - (1 / 2.0) *		Math.sin(a) * Math.pow((x - a), 2)
				  - (1 / 6.0) *  	Math.cos(a) * Math.pow((x - a), 3)
				  + (1 / 24.0) * 	Math.sin(a) * Math.pow((x - a), 4)
		  		  + (1 / 120.0) * 	Math.cos(a) * Math.pow((x - a), 5)
		  		  - (1 / 720.0) * 	Math.sin(a) * Math.pow((x - a), 6)
		  		  - (1 / 5040.0) * 	Math.cos(a) * Math.pow((x - a), 7);*/
		
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
	  
	  private int getCentX(){
		  return (int)((a * scale) + xAdj);
	  }
	  
	  private int getCentY(){
		  double temp = Math.sin(a);
		  temp *= scale;
		  return (int)(temp += yAdj);
	  }
	  
	  private void clearImage(){
		  Graphics2D g2d = bi.createGraphics();
		  g2d.setBackground(Color.white);
		  g2d.clearRect(0, 0, 900, 600);
		  g2d.dispose();
	  }
	  
	  @Override
	  public void paintComponent(Graphics g) {
		  Graphics2D g2d = (Graphics2D) g;
		  g2d.drawImage(bi, null, 0, 0);
		  g2d.dispose();
	  }
	  
	  private void printImage(){
		  Graphics2D g2 = bi.createGraphics();
		  g2.setColor(myColor);
		  g2.setStroke(new BasicStroke(lineThickness));
		  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		  g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		  g2.drawPolyline(xPoints, yPoints, nPoints);

		  g2.setColor(Color.RED);
		  g2.fillOval(getCentX() - 5, getCentY() - 5, 10, 10);
		  g2.dispose();    
	  }	
	  
	  private class TimerListener implements ActionListener{
		  public void actionPerformed(ActionEvent e){
			 go();
		  } 	  
	}
}
