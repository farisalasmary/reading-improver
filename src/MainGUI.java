
/**
 * 
 * @author Faris_Alasmary
 * @Version : 1.0
 * 
 * */


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 5765267086570271159L;

	private static final double VERSION = 1.0;
	private static final String PROGRAM_NAME = "Reading Improver";
	
	protected JScrollPane textAreaScrollable;
	protected JTextArea   theText;
	protected JButton     startBtn, clearBtn;
	protected JPanel      textPanel; 
	
	protected JMenuBar menuBar;
	protected JMenuItem exitItm, openItm, aboutItm, RTLitm, LTRitm, flashItm, wordsItm; // RTLitm = Right to left item
	protected JMenu  fileMenu, modeMenu;
	protected MainGUI mainGUI = this;
	
	public MainGUI() {
		super(PROGRAM_NAME + " " + VERSION);
		this.setSize(530, 400);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){exit();}});

		constructMenus();
		constructPanels();
		constructButtons();
		constructTextArea();
		
		this.setVisible(true);

	}
	
	private void constructMenus(){
		this.menuBar = new JMenuBar();
		
		this.fileMenu = new JMenu("File");
		this.modeMenu = new JMenu("Mode");
		
		this.aboutItm = new JMenuItem("About...");
		this.openItm  = new JMenuItem("Open.."); 
		this.exitItm  = new JMenuItem("Exit");
		
		this.RTLitm = new JRadioButtonMenuItem("Right to left");
		this.LTRitm = new JRadioButtonMenuItem("Left to Right");
		
		this.flashItm = new JRadioButtonMenuItem("Flash");
		this.wordsItm = new JRadioButtonMenuItem("Word by word");
		
		this.aboutItm.addActionListener(this);
		this.openItm.addActionListener(this);
		this.flashItm.addActionListener(this);
		this.wordsItm.addActionListener(this);
		this.RTLitm.addActionListener(this);
		this.LTRitm.addActionListener(this);
		this.exitItm.addActionListener(this);

		
		this.fileMenu.add(this.openItm);
		this.fileMenu.addSeparator();

		this.flashItm.setSelected(true);
		this.wordsItm.setSelected(false);
		this.modeMenu.add(this.flashItm);
		this.modeMenu.add(this.wordsItm);
		this.fileMenu.add(this.modeMenu);
		this.fileMenu.addSeparator();
		
		this.RTLitm.setSelected(false);
		this.LTRitm.setSelected(true);
		this.fileMenu.add(this.RTLitm);
		this.fileMenu.add(this.LTRitm);
		
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.exitItm);
		
		this.menuBar.add(this.fileMenu);
		this.menuBar.add(this.aboutItm);
		
		this.setJMenuBar(this.menuBar);
		
	}
	
	private void constructButtons(){
		this.startBtn = new JButton("Start");
		this.startBtn.setBounds((this.textPanel.getSize().width / 2) - (this.startBtn.getPreferredSize().width / 2) - 35,
						this.textPanel.getSize().height + 5, this.startBtn.getPreferredSize().width, 
						this.startBtn.getPreferredSize().height);
		
		this.clearBtn = new JButton("Clear");
		this.clearBtn.setBounds(this.startBtn.getX() + this.startBtn.getPreferredSize().width + 5,
								this.startBtn.getY(), this.clearBtn.getPreferredSize().width,
								this.clearBtn.getPreferredSize().height);
		this.startBtn.addActionListener(this);
		this.clearBtn.addActionListener(this);
										
		this.add(startBtn);
		this.add(clearBtn);
	}
	
	private void constructPanels(){
		this.textPanel = new JPanel(new FlowLayout());
		this.textPanel.setBounds(0, 0, this.getSize().width, this.getSize().height - 60);
		this.textPanel.setBorder(BorderFactory.createTitledBorder("The Text"));
		this.add(textPanel);
	}
	
	private void constructTextArea(){
		this.theText = new JTextArea(20, 45);
		this.textAreaScrollable = new JScrollPane(theText);
		this.theText.setWrapStyleWord(true);
		this.theText.setLineWrap(true);
		this.textAreaScrollable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.textAreaScrollable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.textPanel.add(textAreaScrollable);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == this.startBtn){
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run(){
					new Display(mainGUI);				
				}
			});
			
		}else if(e.getSource() == this.clearBtn){
			this.theText.setText("");
			
		}else if(e.getSource() == this.openItm){
			ReadFromFile();
			
		}else if(e.getSource() == this.exitItm){
			exit();
			
		}else if(e.getSource() == this.RTLitm){
			RightToLeft();
			
		}else if(e.getSource() == this.LTRitm){
			LeftToRight();
			
		}else if(e.getSource() == this.flashItm){
			this.flashItm.setSelected(true);
			this.wordsItm.setSelected(false);
			
		}else if(e.getSource() == this.wordsItm){
			this.flashItm.setSelected(false);
			this.wordsItm.setSelected(true);
			
		}else if(e.getSource() == this.aboutItm){
			String info = PROGRAM_NAME + "\n"		  +
					  "Version : " + VERSION + "\n\n" +
					  "Developed By :\n" 	 		  +
					  "Faris Abdullah Alasmary\n\n"   +
					  "Email :\n" 					  +
					  "farisalasmary@gmail.com";
		
		JOptionPane.showMessageDialog(null, info, "About...", JOptionPane.INFORMATION_MESSAGE);
		
		}
		
	}
	
	private void exit() {
    	int choice = JOptionPane.showConfirmDialog(null, "Are you sure ??", "Info",
				 JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	   	if(choice == JOptionPane.YES_OPTION)
	       	System.exit(0);		
	}


	private void ReadFromFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		fileChooser.setVisible(true);
		
		try {
			if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
		//		FileInputStream input = new FileInputStream(fileChooser.getSelectedFile());
				Scanner scan = new Scanner(fileChooser.getSelectedFile());
				String str = "";
				while(scan.hasNextLine()){
					str += scan.nextLine();
					str += "\n";
				}
				scan.close();
				this.theText.setText(str);
			}else{
				return; // No need to continue 
			}
		}catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "No such a file!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void RightToLeft(){
		this.textAreaScrollable.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		this.theText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		this.RTLitm.setSelected(true);
		this.LTRitm.setSelected(false);
	}
	
	private void LeftToRight(){
		this.textAreaScrollable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.theText.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.RTLitm.setSelected(false);
		this.LTRitm.setSelected(true);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				new MainGUI();				
			}
		});
	}
}

