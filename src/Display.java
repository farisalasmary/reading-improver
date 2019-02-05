
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

public class Display extends JFrame implements ActionListener, Runnable{

	private static final long serialVersionUID = 3421576252605228469L;
	
	private JPanel textPanel, controlPanel;
	private JLabel timeLbl, numberOfWordsLbl;
	private MainGUI mainGUI;
	private JButton startBtn, pauseBtn, stopBtn;
	private JSpinner timeSpnr, numberOfWordsSpnr;
	private JTextArea display;

	private boolean isRunning = false;
	private boolean isPaused  = false;
	private boolean isEnabled = true;
	private Scanner scan, reader;
	private JScrollPane textAreaScrollable;
	
	public Display(MainGUI mainGUI){
		super("Display");
		this.setSize(500, 350);
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.addWindowListener( new WindowAdapter(){
			   public void windowClosing(WindowEvent e){
				   isRunning = false; // to stop the thread when exit
			   }});
		
		if(mainGUI != null)
			this.mainGUI = mainGUI;
		else
			throw new NullPointerException("There is no MainGUI!!");
		
		constructPanels();
		constructLabels();
		constructTextFields();
		constructButtons();
				
		this.setResizable(false);
		this.setVisible(true);
	}

	private void constructPanels(){
		this.textPanel = new JPanel(null);
		this.controlPanel = new JPanel(null);
		
		this.textPanel.setBounds(0, 0, this.getSize().width, this.getSize().height - 120);
		this.textPanel.setBorder(BorderFactory.createTitledBorder("The Text"));
		
		this.controlPanel.setBounds(0, this.textPanel.getSize().height, this.getSize().width, 100);
		this.controlPanel.setBorder(BorderFactory.createTitledBorder("Control Panel"));
		
		this.add(textPanel);
		this.add(controlPanel);
	}
	
	private void constructLabels(){
		this.timeLbl = new JLabel("Time (ms) : ");
		this.numberOfWordsLbl = new JLabel("# of Words per one iteration : ");
		
		this.timeLbl.setBounds(10, 20, this.timeLbl.getPreferredSize().width,
							   this.timeLbl.getPreferredSize().height);
		this.numberOfWordsLbl.setBounds(this.timeLbl.getX() + 200, this.timeLbl.getY(), this.numberOfWordsLbl.getPreferredSize().width,
										this.numberOfWordsLbl.getPreferredSize().height);
		
		this.controlPanel.add(timeLbl);
		this.controlPanel.add(numberOfWordsLbl);
		
	}
	
	private void constructTextFields(){
		
		if(this.mainGUI.flashItm.isSelected()){ // if it is flash mode then use those settings
			this.display = new JTextArea(10, 30);
			
			int width = 460, height = 90;
			
			this.display.setBounds(this.textPanel.getSize().width / 2 - width / 2, this.textPanel.getSize().height / 2 - height / 2, width, height);
			this.display.setFont( new Font("Arial", 0, 70));
			this.display.setComponentOrientation(this.mainGUI.theText.getComponentOrientation());
			this.display.setEditable(false);
			this.display.setBackground(this.getBackground());
			this.display.setBorder(BorderFactory.createLineBorder(Color.black));
			this.textPanel.add(display);
		
		}else{
			this.display = new JTextArea(10, 30);
			
			int width = 480, height = 200;
			
			//this.display.setBounds(this.textPanel.getSize().width / 2 - width / 2, this.textPanel.getSize().height / 2 - height / 2, width, height);
			this.display.setFont(new Font("Arial", 0, 20));
			this.display.setComponentOrientation(this.mainGUI.theText.getComponentOrientation());
			this.display.setEditable(false);
			this.display.setBackground(this.getBackground());
			this.display.setBorder(BorderFactory.createLineBorder(Color.black));
			((DefaultCaret)this.display.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // this line is to auto scroll the textarea after it get updated
			this.textAreaScrollable = new JScrollPane(this.display);
			this.display.setWrapStyleWord(true);
			this.display.setLineWrap(true);
			this.textAreaScrollable.setBounds(this.textPanel.getSize().width / 2 - width / 2, this.textPanel.getSize().height / 2 - height / 2, width, height);
			this.textAreaScrollable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.textAreaScrollable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.textAreaScrollable.setComponentOrientation(this.mainGUI.textAreaScrollable.getComponentOrientation());
			this.display.setComponentOrientation(this.mainGUI.theText.getComponentOrientation());
			this.textPanel.add(this.textAreaScrollable);
		}
		
				
        this.timeSpnr = makeDigitsOnlySpinnerUsingDocumentFilter(2, 1, 20000, 10);
        this.timeSpnr.setBounds(this.timeLbl.getX() + this.timeLbl.getPreferredSize().width, this.timeLbl.getY(), timeSpnr.getPreferredSize().width, timeSpnr.getPreferredSize().height);
        
        this.numberOfWordsSpnr = makeDigitsOnlySpinnerUsingDocumentFilter(2, 1, 10, 1);
        this.numberOfWordsSpnr.setBounds(this.numberOfWordsLbl.getX() + this.numberOfWordsLbl.getPreferredSize().width, this.numberOfWordsLbl.getY(), numberOfWordsSpnr.getPreferredSize().width, numberOfWordsSpnr.getPreferredSize().height);
        
        // Here, I'm reseting the values because it doesn't show up when the jframe is created and they have to be different
        //than the original values provided in "makeDigitsOnlySpinnerUsingDocumentFilter()" method
		this.timeSpnr.setValue(1000);
		this.numberOfWordsSpnr.setValue(1);
        
        this.controlPanel.add(timeSpnr);
        this.controlPanel.add(numberOfWordsSpnr);
		
	}
	

	private void constructButtons(){
		this.startBtn = new JButton("Start");
		this.pauseBtn = new JButton("Pause");
		this.stopBtn  = new JButton("Stop");
		
		this.startBtn.setBounds(130, 60, this.startBtn.getPreferredSize().width, this.startBtn.getPreferredSize().height);
		this.startBtn.addActionListener(this);
		this.startBtn.setEnabled(isEnabled);
		this.controlPanel.add(startBtn);
		
		int width = 98; // this is the preferred width for the word "Continue"
		
		this.pauseBtn.setBounds(this.startBtn.getX() + this.startBtn.getPreferredSize().width + 5, this.startBtn.getY(),
								width, this.pauseBtn.getPreferredSize().height);
		this.pauseBtn.addActionListener(this);
		this.pauseBtn.setEnabled(!isEnabled);
		this.controlPanel.add(pauseBtn);
		
		this.stopBtn.setBounds(this.pauseBtn.getX() + width + 5, this.pauseBtn.getY(),
							   this.stopBtn.getPreferredSize().width, this.stopBtn.getPreferredSize().height);
		this.stopBtn.addActionListener(this);
		this.stopBtn.setEnabled(!isEnabled);
		this.controlPanel.add(stopBtn);
	}

    private JSpinner makeDigitsOnlySpinnerUsingDocumentFilter(int value, int start, int stop, int step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, start, stop, step));
        
        JSpinner.NumberEditor jsEditor = new JSpinner.NumberEditor(spinner, "#");
        spinner.setEditor(jsEditor);
        final Document jsDoc = jsEditor.getTextField().getDocument();
        
        
        if (jsDoc instanceof PlainDocument) {
            AbstractDocument doc = new PlainDocument() {

                private static final long serialVersionUID = 1L;

                @Override
                public void setDocumentFilter(DocumentFilter filter) {
                    if (filter instanceof MyDocumentFilter) {
                        super.setDocumentFilter(filter);
                    }
                }
            };
            doc.setDocumentFilter(new MyDocumentFilter());
            jsEditor.getTextField().setDocument(doc);
        }
        return spinner;
    }

    public void run(){
    	
    	this.scan = new Scanner(this.mainGUI.theText.getText() + "\n\n"); // adding two newlines to avoid missing the last line or missing half of it
    	boolean isFlashMode = this.mainGUI.flashItm.isSelected();
    	this.display.setText(""); // to clear the screen
    	
    	while(isRunning){
    		if(!isPaused)
    			if(isFlashMode)
    				FlashMode();
    			else
    				WordsMode();
    		
    		try{
				Thread.sleep((int)this.timeSpnr.getValue());
			}catch(InterruptedException e) {
				System.exit(-1);
			}
    	
    	}
    	
		this.startBtn.setEnabled(true );
		this.pauseBtn.setEnabled(false);
		this.stopBtn .setEnabled(false);
		
		this.isRunning = false;
		this.pauseBtn.setText("Pause");
    	
    }
	
	private void WordsMode(){
		String str = "";
		
		str += this.display.getText();
		
		if(this.reader == null){
			this.reader = new Scanner(scan.nextLine());
		}else if(this.scan.hasNextLine() && !this.reader.hasNext()){
			this.reader = new Scanner(scan.nextLine());
			str += "\n";
		}
		
		for(int i = 0; i < (int)this.numberOfWordsSpnr.getValue() && this.reader.hasNext(); i++)
			str += (reader.next() + " ");
				
		this.display.setText(str);
				
		if(!scan.hasNextLine()) // to destroy the thread
			this.isRunning = false;
	}

	private void FlashMode(){
		String str = "";
		
		if(this.reader == null){
			this.reader = new Scanner(scan.nextLine());
		}else if(this.scan.hasNextLine() && !this.reader.hasNext()){
			this.reader = new Scanner(scan.nextLine());
			return; // we must exit the method because the line has reached the end
		}
		
		for(int i = 0; i < (int)this.numberOfWordsSpnr.getValue() && this.reader.hasNext(); i++)
			str += (reader.next() + " ");
		
		// Change the font size to contain the specified number of words in the "display" textarea
		if((int)this.numberOfWordsSpnr.getValue() >= 6)
			this.display.setFont(new Font("Arial", 0, 20));
		else if((int)this.numberOfWordsSpnr.getValue() >= 4)
			this.display.setFont(new Font("Arial", 0, 30));
		else
			this.display.setFont(new Font("Arial", 0, 40));
		
		this.display.setText(str);
				
		if(!scan.hasNextLine()) // to destroy the thread
			this.isRunning = false;
		repaint(); // to remove some dots that are leaved after changing the "display" text
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.startBtn){
			if(!isRunning){
				this.startBtn.setEnabled(false);
				this.pauseBtn.setEnabled(true);
				this.stopBtn.setEnabled(true);
				this.isRunning = true;
				this.isPaused = false;
				new Thread(this).start();
			}
		
		}else if(e.getSource() == this.pauseBtn){			
			if(isPaused){
				this.isPaused = false;
				this.pauseBtn.setText("Pause");
			}else{
				this.isPaused = true;
				this.pauseBtn.setText("Continue");
			}
		}else if(e.getSource() == this.stopBtn){
			this.startBtn.setEnabled(true);
			this.pauseBtn.setEnabled(false);
			this.stopBtn.setEnabled(false);
			
			this.isRunning = false;
			this.pauseBtn.setText("Pause");
		}
		
	}

	private static class MyDocumentFilter extends DocumentFilter {

		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			if (stringContainsOnlyDigits(string)) {
				super.insertString(fb, offset, string, attr);
			}
		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			super.remove(fb, offset, length);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (stringContainsOnlyDigits(text)) {
	                super.replace(fb, offset, length, text, attrs);
			}
		}

		private boolean stringContainsOnlyDigits(String text) {
			for (int i = 0; i < text.length(); i++) {
				if (!Character.isDigit(text.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	}
}

