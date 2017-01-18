import java.awt.*;

import javax.swing.*;
 
public class Circles extends JPanel{
	
	private JButton centerButton, nextButton=new JButton("next");
	private JLabel[] labelArray=null;
	private int xPoint, yPoint;
	
	public Circles(String bstr){
		centerButton=new JButton(bstr);
		centerButton.setBackground(Color.RED);
		centerButton.setForeground(Color.WHITE);
	}
	
	public int getCenterX(){
		return xPoint;
	}
	public int getCenterY(){
		return yPoint;
	}
	public JButton getNextButton(){
		return nextButton;
	}
     
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if(labelArray!=null){
        	for(int i=0;i<4;i++)
        		this.remove(labelArray[i]);
        }
        labelArray=null;
         
        int width = getWidth();
        int height = getHeight();       
        //System.out.printf("Width is %d \t and height is %d\n", width, height );
         
        xPoint = width / 2;
        yPoint = height / 2;
        
        labelArray=new JLabel[4];
        labelArray[0]=new JLabel("most influential",JLabel.CENTER);
        labelArray[1]=new JLabel("moderately influential",JLabel.CENTER);
        labelArray[2]=new JLabel("somewhat influential",JLabel.CENTER);
        labelArray[3]=new JLabel("not at all influential",JLabel.CENTER);
         
        for(int i = 3; i >= 0; i--){
        	g.drawOval(xPoint - (i * 100), yPoint - (i * 100), (i * 200), (i * 200));
        	this.add(labelArray[i]);
            labelArray[i].setBounds(xPoint-((i+1)*70), yPoint-((i+1)*70), 150, 30);
        }
        
        this.add(centerButton);
        centerButton.setBounds(xPoint-50, yPoint-10, 100, 20);
        
        this.add(nextButton);
        nextButton.setBounds(width-80, height-40, 70, 30);
        nextButton.setBackground(Color.GREEN);
    }
}