import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Base {
	
	//these variables used for JArus XPlorer
	private JFrame mainFrame;
	private JPanel panel;
	private JLabel ipFileLabel, opFileLabel, ipFileStatus, opFileNote, Status;
	private JTextField opText;
	
	//drag n drop buttons along with their dragListener
	private JButton[] buttonArray;
	private DragListener[] dl;
	private String[] definitions;
	
	//used in writing the output
	private String[] mainArray;
	private int index=0;
	
	//input and output files
	private File ipFile=null, opFile=null;
	
	
	
	public Base(){
		URL u = getClass().getProtectionDomain().getCodeSource().getLocation();
	    File f = null;
		try {
			f = new File(u.toURI());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    ipFile=new File(f.getParent()+"/input.txt");
		
		mainFrame=new JFrame("JArus XPlorer");
		
		panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		GridBagConstraints panelConstraints = new GridBagConstraints();
		
		JPanel p=new JPanel();
		p.setLayout(new GridLayout(3,1));
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		ipFileLabel=new JLabel("File Selected : " 
	               + ipFile.getName(), JLabel.CENTER);
		ipFileStatus=new JLabel("*note: for new input file, select here", JLabel.CENTER);
		final JFileChooser  ipfileChoose = new JFileChooser();
	    JButton ipFileSelect = new JButton("Select File");
	    ipFileSelect.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            int returnVal = ipfileChoose.showOpenDialog(p);
	            Status.setText("");
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	               ipFile = ipfileChoose.getSelectedFile();
	               ipFileLabel.setText("File Selected : " 
	               + ipFile.getName());
	               ipFileStatus.setForeground(Color.GREEN.darker());
           		ipFileStatus.setText("File has been selected :)" );
	            }
	            else{
	            	if(ipFile==null){
	            		ipFileStatus.setForeground(Color.RED.darker());
	            		ipFileStatus.setText("File not selected :|" );
	            	}           
	            }      
	         }
	      });
	    
	    p.add(ipFileLabel);
	    p.add(ipFileSelect);
	    p.add(ipFileStatus);
	    
	    JPanel p1=new JPanel();
		p1.setLayout(new GridLayout(3,1));
		p1.setBorder(new EmptyBorder(5, 5, 5, 5));
		opFileLabel=new JLabel("mention the output  file name", JLabel.CENTER);
		opFileNote=new JLabel("*note: it will be created in input file directory", JLabel.CENTER);
		opText=new JTextField();
		
	    p1.add(opFileLabel);
	    p1.add(opText);
	    p1.add(opFileNote);
	    
	    JPanel p2=new JPanel();
	    p2.setBorder(new EmptyBorder(5, 5, 5, 5));
	    JButton go=new JButton("Go");
	    go.setBackground(Color.GREEN);
	    
	    go.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) {
	    		  //System.out.println("go pressed!");
	  
	    		  if(ipFile!=null){
	    			  if(!opText.getText().equals("")){
	    				  Status.setText("");
	    				  mainWorker();
	    			  }else{
	    				  Status.setForeground(Color.RED.darker());
	    				  Status.setText("output File not mentioned :/");
	    			  }
	    		  }else{
	    			  Status.setForeground(Color.RED.darker());
	    			  Status.setText("input File not selected :/");
	    		  }
	    	  } 
	    	});
	    
	    p2.add(go);
	    
	    JPanel p3=new JPanel();
	    p3.setBorder(new EmptyBorder(5, 5, 5, 5));
	    Status=new JLabel("", JLabel.CENTER);
		Status.setForeground(Color.GREEN.darker());
		p3.add(Status);
	    
		panelConstraints.weightx=0.8;
		
		panelConstraints.gridx = 0;
		panelConstraints.fill = GridBagConstraints.BOTH;
        panel.add(p, panelConstraints);

        panelConstraints.gridx = 1;
        panel.add(p1, panelConstraints);
        
        panelConstraints.fill = GridBagConstraints.HORIZONTAL;
	    panelConstraints.gridwidth=2;
	    panelConstraints.weighty=0.2;
	    panelConstraints.gridx = 0;
	    panelConstraints.gridy = 1;
	    panel.add(p2, panelConstraints);
	    
	    panelConstraints.fill = GridBagConstraints.HORIZONTAL;
	    panelConstraints.gridwidth=2;
	    panelConstraints.weighty=0.1;
	    panelConstraints.gridx = 0;
	    panelConstraints.gridy = 2;
	    panel.add(p3, panelConstraints);
		
		mainFrame.setSize(500, 200);
		mainFrame.add(panel);
		mainFrame.setVisible(true);
	}
	
	private void mainWorker(){
		JFrame circlesFrame=new JFrame("JArus DnD");
		  
		Circles circles=null;
		try{
			BufferedReader buf=new BufferedReader(new FileReader(ipFile));
			String line=null;
			LinkedList<String> ll=new LinkedList<String>();
			
			while((line=buf.readLine())!=null)
				ll.add(line);
			
			String[] sarr=Arrays.copyOf(ll.toArray(), ll.toArray().length, String[].class);
			
			definitions=new String[sarr.length];
			for(int i=0;i<sarr.length;i++){
				if(sarr[i].contains(":")){
					String[] temp=sarr[i].split(":");
					sarr[i]=temp[0];
					definitions[i]=temp[1];
				}else{
					definitions[i]=null;
				}
			}
			
			mainArray=new String[sarr.length];
			for(int i=0;i<sarr.length;i++)
				mainArray[i]="";
			
			circles=dragStuff(sarr, circlesFrame);
			
			buf.close();
		}catch(Exception e){
			e.printStackTrace();
			Status.setForeground(Color.RED.darker());
			Status.setText("well it is embarassing! something went wrong :(");
		}
		circlesFrame.add(circles);
		  
		circlesFrame.setSize(1000, 700);
		circlesFrame.setVisible(true);
	}
	
	public Circles dragStuff(String[] sarr, JFrame circlesFrame){
		if(index>=sarr.length){
			circlesFrame.dispatchEvent(new WindowEvent(circlesFrame, WindowEvent.WINDOW_CLOSING));
			
			try{
				opFile=new File(ipFile.getParent()+"/"+opText.getText()+".infl.txt");
				PrintWriter writer = new PrintWriter(opFile, "UTF-8");
				
				writer.println("data");
				writer.flush();
				writer.println("distances");
				writer.flush();
				writer.println(sarr.length+" items");
				writer.flush();
				writer.println("JTarget");
				writer.flush();
				writer.println("0 minimum");
				writer.flush();
				writer.println("3 maximum");
				writer.flush();
				writer.println("full matrix:");
				writer.flush();
				
				for(int i=0;i<sarr.length;i++){
					writer.println(mainArray[i]);
					writer.flush();
				}
				writer.close();
			}catch(Exception e){
				e.printStackTrace();
				Status.setForeground(Color.RED.darker());
				Status.setText("well it is embarassing! something went wrong :(");
			}
			
			Status.setForeground(Color.GREEN.darker());
			Status.setText(opText.getText()+".infl.txt file has been created :)");
			index=0;
			opText.setText("");
			return null;
		}
		
		Circles circles=new Circles(sarr[index],definitions[index]);
		circles.setLayout(new BoxLayout(circles, BoxLayout.Y_AXIS));
		
		JButton centerButton=circles.getCenterButton();
		centerButton.addMouseListener(new MouseInputAdapter(){
			public void mousePressed(MouseEvent me){
				if(me.getButton()==MouseEvent.BUTTON3){
		        	circles.getCenterPopup().show(me.getComponent(), me.getX(), me.getY()+20);
		        }
			}
		});
		
		JButton nextButton=circles.getNextButton();
		
		nextButton.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) {
	    		  //System.out.println("nextButton pressed!");
	    		  //System.out.println(buttonArray[1].getHeight()+"  "+buttonArray[1].getWidth());
	    		  
	    		  for(int i=0;i<sarr.length;i++){
	    			  if(i==index){
	    				  mainArray[index]=mainArray[index]+" 0";
	    			  }else{
	    				  double devx=buttonArray[i].getWidth(), devy=buttonArray[i].getHeight();
		    			  double ix=dl[i].getX()+(devx/2), iy=dl[i].getY()+(devy/2);
		    			  int cx=circles.getCenterX(), cy=circles.getCenterY();
		    			  
		    			  double res=(((cx-ix)*(cx-ix))+((cy-iy)*(cy-iy)))/(10000);
		    			  
		    			  int flag=0;
		    			  for(int j=1;j<=3;j++){
		    				  if((j*j)>=res){
		    					  flag=1;
		    					  mainArray[index]=mainArray[index]+" "+j;
		    					  break;
		    				  }
		    			  }
		    			  if(flag==0)
		    				  mainArray[index]=mainArray[index]+" 4";
	    			  }
	    		  }
	    		  
	    		  index++;
	    		  
	    		  circlesFrame.remove(circles);
	    		  Circles temp=dragStuff(sarr, circlesFrame);
	    		  if(temp!=null){
	    			  circlesFrame.add(temp);
		    		  circlesFrame.setVisible(true);
	    		  }
	    	  } 
	    	});
		
		buttonArray=new JButton[sarr.length];
		dl=new DragListener[sarr.length];
		for(int i=0;i<sarr.length;i++){
			if(i==index)
				continue;
			buttonArray[i]=new JButton(sarr[i]);
			buttonArray[i].setBackground(Color.ORANGE);
			buttonArray[i].setMargin(new Insets(0, 0, 0, 0));
			
			if(definitions[i]==null)
				dl[i]=new DragListener();
			else
				dl[i]=new DragListener(definitions[i]);
			buttonArray[i].addMouseListener(dl[i]);
			buttonArray[i].addMouseMotionListener(dl[i]);
			
			circles.add(buttonArray[i]);
		}
		
		return circles;
	}
	
	public static void main(String[] args){
		new Base();
	}
	
	public class DragListener extends MouseInputAdapter
	{
	    Point location;
	    MouseEvent pressed;
	    int x,y;
	    
	    JPopupMenu buttonPopup = new JPopupMenu("Popup");
	    
	    public DragListener(){
	    	buttonPopup.add(new JLabel("no definition..."));
	    }
	    public DragListener(String def){
	    	buttonPopup.add(new JLabel(def));
	    }
	 
	    public void mousePressed(MouseEvent me){
	        pressed = me;
	        Component component = me.getComponent();
	        component.setBackground(Color.CYAN);
	        if(me.getButton()==MouseEvent.BUTTON3){
	        	buttonPopup.show(me.getComponent(), me.getX(), me.getY()+10);
	        }
	    }
	 
	    public void mouseDragged(MouseEvent me){
	        Component component = me.getComponent();
	        location = component.getLocation(location);
	        x = location.x - pressed.getX() + me.getX();
	        y = location.y - pressed.getY() + me.getY();
	        component.setLocation(x, y);
	     }
	    
	    public int getX(){
	    	return x;
	    }
	    public int getY(){
	    	return y;
	    }
	}
}
