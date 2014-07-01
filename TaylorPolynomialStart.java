import javax.swing.JFrame;

public class TaylorPolynomialStart {
  public static void main(String[] args){
	 int degree = 4; 
	  
	 JFrame frame = new JFrame();
	 TaylorGraphics tg = new TaylorGraphics(degree);			//basic animation
	 //TaylorGraphics2 tg = new TaylorGraphics2(degree);		//animate layers
	 
	 frame.add(tg);
	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 frame.setSize(900,600);
	 frame.setVisible(true);
	 
	 //TaylorPrint tg = new TaylorPrint();						//for exporting images
  }
}
