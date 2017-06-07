import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Editor extends JFrame
{
	private String fileName;
	private MenuBar mb;
	private Menu mFile;
	private MenuItem miNew, miOpen, miSave, miSaveAs, miMakeJava, miExit;

	private JToolBar tb;
	private JButton mtNew, mtOpen, mtSave, mtSaveAs, mtMakeJava, mtExit;
	private JButton bChange, bDelete;

	private JPanel attPane, selected;
	private MyPanel editPane;
	private JLabel xyPos, w_h, attValue, comType, varName;
	private JTextField tfXYPos, tfW_H, tfAttValue, tfVarName;
	private JComboBox cbComType;
	private MyHandler handler;

	private MyArrayList myModelList;

	public Editor(String title)
	{
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setSize(1500, 1000);
		this.initMenuBar();
		this.initToolBar();
		this.initAttPane();
		this.initEditPane();
		this.addActions();
		this.mySetFont();

		tb.setFloatable(false);

		add(tb);
		add(attPane);
		add(editPane);

		setVisible(true);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frm = this.getSize();
		int xpos = (int) (screen.getWidth() / 2 - frm.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - frm.getHeight() / 2 - 20);
		this.setLocation(xpos, ypos);
		this.setResizable(false);

		myModelList = new MyArrayList();
	}
	private void initMenuBar()
	{
		mb = new MenuBar();
		mFile = new Menu("Menu");
		miNew = new MenuItem("새로 만들기");
		miOpen = new MenuItem("열기");
		miSave = new MenuItem("저장");
		miSaveAs = new MenuItem("다른 이름으로 저장");
		miMakeJava = new MenuItem(".java 파일 생성");
		miExit = new MenuItem("닫기");

		mFile.add(miNew);
		mFile.add(miOpen);
		mFile.add(miSave);
		mFile.add(miSaveAs);
		mFile.add(miMakeJava);
		mFile.addSeparator();
		mFile.add(miExit);

		mb.add(mFile);
		setMenuBar(mb);
	}
	private void initToolBar()
	{
		final int R = 23;
		final int G = 89;
		final int B = 149;
		final int R2 = 37;
		final int G2 = 104;
		final int B2 = 159;

		tb = new JToolBar();
		mtNew = new JButton("새로 만들기");
		mtOpen = new JButton("열기");
		mtSave = new JButton("저장");
		mtSaveAs = new JButton("다른 이름으로 저장");
		mtMakeJava = new JButton(".java 파일 생성");
		mtExit = new JButton("닫기");

		tb.setSize(this.getWidth(), 30);

		tb.setBackground(new Color(R, G, B));
		mtNew.setBackground(new Color(R2, G2, B2));
		mtOpen.setBackground(new Color(R2, G2, B2));
		mtSave.setBackground(new Color(R2, G2, B2));
		mtSaveAs.setBackground(new Color(R2, G2, B2));
		mtMakeJava.setBackground(new Color(R2, G2, B2));
		mtExit.setBackground(new Color(R2, G2, B2));

		tb.add(mtNew);
		tb.add(mtOpen);
		tb.add(mtSave);
		tb.add(mtSaveAs);
		tb.add(mtMakeJava);
		tb.add(mtExit);
	}
	private void initAttPane()
	{
		final int LB_WIDTH = 100;
		final int LB_HEIGHT = 30;
		final int TF_WIDTH = 200;
		final int TF_HEIGHT = 30;

		attPane = new JPanel();
		attPane.setLayout(null);
		attPane.setBackground(new Color(225, 225, 225));
		attPane.setSize(this.getWidth() / 3, this.getHeight());
		attPane.setLocation(0, 10);

		xyPos = new JLabel("시작 x,y 좌표   :");
		w_h = new JLabel("    너비, 높이   :");
		attValue = new JLabel("컴포넌트의 텍스트 속성값   :");
		comType = new JLabel("  컴포넌트 타입  :");
		varName = new JLabel(" 컴포넌트 변수명   :");

		tfXYPos = new JTextField();
		tfW_H = new JTextField();
		tfAttValue = new JTextField();
		tfVarName = new JTextField();

		String[] types =
			{
					"JPanel",
					"JLabel",
					"JButton"
			};

		cbComType = new JComboBox(types);

		bChange = new JButton("변경");
		bDelete = new JButton("삭제");

		xyPos.setSize(LB_WIDTH, LB_HEIGHT);
		w_h.setSize(LB_WIDTH, LB_HEIGHT);
		attValue.setSize(LB_WIDTH + 100, LB_HEIGHT);
		comType.setSize(LB_WIDTH, LB_HEIGHT);
		varName.setSize(LB_WIDTH + 100, LB_HEIGHT);

		tfXYPos.setSize(TF_WIDTH, TF_HEIGHT);
		tfW_H.setSize(TF_WIDTH, TF_HEIGHT);
		tfAttValue.setSize(TF_WIDTH, TF_HEIGHT);
		tfVarName.setSize(TF_WIDTH, TF_HEIGHT);

		cbComType.setSize(TF_WIDTH, TF_HEIGHT);

		bChange.setSize(70, 30);
		bDelete.setSize(70, 30);

		final int REF_XPOS = 100;
		final int REF_YPOS = 150;
		xyPos.setLocation(REF_XPOS, REF_YPOS);
		w_h.setLocation(REF_XPOS, xyPos.getY() + 130);
		attValue.setLocation(REF_XPOS - 80, w_h.getY() + 130);
		comType.setLocation(REF_XPOS - 20, attValue.getY() + 130);
		varName.setLocation(REF_XPOS - 30, comType.getY() + 130);

		final int REF_XPOS2 = REF_XPOS + 100;
		tfXYPos.setLocation(REF_XPOS2, xyPos.getY());
		tfW_H.setLocation(REF_XPOS2, w_h.getY());
		tfAttValue.setLocation(REF_XPOS2, attValue.getY());
		tfVarName.setLocation(REF_XPOS2, varName.getY());

		cbComType.setLocation(REF_XPOS2, comType.getY());

		bChange.setLocation(REF_XPOS2 + 30, comType.getY() + 230);
		bDelete.setLocation(REF_XPOS2 + 130, comType.getY() + 230);

		bDelete.addActionListener(new MyButtonListener());
		bChange.addActionListener(new MyButtonListener());

		attPane.add(xyPos);
		attPane.add(w_h);
		attPane.add(attValue);
		attPane.add(comType);
		attPane.add(varName);
		attPane.add(tfXYPos);
		attPane.add(tfW_H);
		attPane.add(tfAttValue);
		attPane.add(tfVarName);
		attPane.add(cbComType);
		attPane.add(bChange);
		attPane.add(bDelete);
	}
	private void initEditPane()
	{
		editPane = new MyPanel();
		editPane.setLayout(null);
		editPane.setBackground(Color.WHITE);
		editPane.setSize(this.getWidth() / 3 * 2, this.getHeight());
		editPane.setLocation(attPane.getWidth(), 30);
	}
	private void addActions()
	{

		handler = new MyHandler(this);

		miNew.addActionListener(handler);
		miOpen.addActionListener(handler);
		miSave.addActionListener(handler);
		miSaveAs.addActionListener(handler);
		miMakeJava.addActionListener(handler);
		miExit.addActionListener(handler);
		mtNew.addActionListener(handler);
		mtOpen.addActionListener(handler);
		mtSave.addActionListener(handler);
		mtSaveAs.addActionListener(handler);
		mtMakeJava.addActionListener(handler);
		mtExit.addActionListener(handler);

	}
	private void mySetFont()
	{
		Font f = new Font("맑은 고딕", Font.PLAIN, 13);
		Font f2 = new Font("맑은 고딕", Font.BOLD, 13);

		mFile.setFont(f);
		mtNew.setFont(f);
		mtOpen.setFont(f);
		mtSave.setFont(f);
		mtSaveAs.setFont(f);
		mtMakeJava.setFont(f);
		mtExit.setFont(f);

		bChange.setFont(f2);
		bDelete.setFont(f2);

		mtNew.setForeground(Color.WHITE);
		mtOpen.setForeground(Color.WHITE);
		mtSave.setForeground(Color.WHITE);
		mtSaveAs.setForeground(Color.WHITE);
		mtMakeJava.setForeground(Color.WHITE);
		mtExit.setForeground(Color.WHITE);

	}
	public MyPanel getEditPane() {
		return this.editPane;
	}
	public static void main(String[] args)
	{
		Editor mainWin = new Editor("GUI EDITOR");
	}
	class MyPanel extends JPanel{
		int xPos, yPos, width, height;
		MyMouseListener listener = new MyMouseListener();

		public MyPanel() {
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}	
		public JPanel addPanel(int x, int y, int width, int height, Color color){
			repaint();
			JPanel panel = new JPanel();
			panel.setBackground(color);
			panel.setSize(width, height);
			panel.setLocation(x, y);
			tfXYPos.setText(panel.getX() + "," + panel.getY());
			tfW_H.setText(panel.getWidth() + "," + panel.getHeight());
			add(panel);
			return panel;
		}
		public JPanel searchPanel(int x,int y){
			JPanel panel = (JPanel)this.getComponentAt(x, y);
			return panel;
		}
		public void removePanel(int x,int y){
			JPanel target = this.searchPanel(x, y);
			this.remove(target);
		}
	}
	class MyMouseListener implements MouseListener, MouseMotionListener {
		private int onX, onY, offX, offY, beforeX, beforeY;
		private boolean isSelected, isSized;
		private int xCk = 0, yCk = 0;
		private MyModel selectedMyModel;
		public void mousePressed(MouseEvent e) {
			onX = e.getX();
			onY = e.getY();
			if(isSelected){ 
				if (!selected.equals(editPane)){
					if (onX >= selected.getX() + selected.getWidth() - 15 && onX <= selected.getX() + selected.getWidth())
					{
						isSized = true;
						xCk = 1;
					}
					else if (onX >= selected.getX() && onX <= selected.getX() + 15)
					{
						isSized = true;
						xCk = -1;
					}
					if (onY >= selected.getY() + selected.getHeight() - 15 && onY <= selected.getY() + selected.getHeight())
					{
						isSized = true;
						yCk = 1;
					}
					else if (onY >= selected.getY() && onY <= selected.getY() + 15)
					{
						isSized = true;
						yCk = -1;
					}
				}
				if(!selected.equals(editPane)){
					selected.setBackground(Color.LIGHT_GRAY);
					tfXYPos.setText("");
					tfW_H.setText("");
					tfAttValue.setText("");
					tfVarName.setText("");
					cbComType.setSelectedItem("");
				}
			}
			selected = editPane.searchPanel(onX,onY);

			if(!selected.equals(editPane)){
				selected.setBackground(Color.blue);
				selectedMyModel = myModelList.find(selected.getX(), selected.getY());
				tfXYPos.setText(selectedMyModel.getX() + "," + selected.getY());
				tfW_H.setText(selected.getWidth() + "," + selected.getHeight());
				try{
					tfAttValue.setText(selectedMyModel.getAttValue());
					tfVarName.setText(selectedMyModel.getVarName());
					cbComType.setSelectedItem(selectedMyModel.getComType());
				}
				catch(NullPointerException ex){
					tfVarName.setText("");
					tfAttValue.setText("");
				}	
				beforeX=onX; beforeY=onY;
				isSelected=true;
			}
			else{
				tfXYPos.setText("");
				tfW_H.setText("");
				tfAttValue.setText("");
				tfVarName.setText("");
				isSelected=false;
			}
		}
		public void mouseReleased(MouseEvent e) {
			offX = e.getX();
			offY = e.getY();
			if(isSelected){ //옮기기 & 크기변경
				if(!isSized)
					selected = editPane.searchPanel(offX, offY);
				if(!selected.equals(editPane)){
					selected.setBackground(Color.blue);
				}
				MyModel newMyModel = new MyModel(selected.getX(),selected.getY(),selected.getWidth(),selected.getHeight());
				try{
					newMyModel.setAttValue(selectedMyModel.getAttValue());
					newMyModel.setVarName(selectedMyModel.getVarName());
					newMyModel.setComType(selectedMyModel.getComType());
				}catch(NullPointerException ex){
					System.out.println("Null");
				}
				myModelList.remove(selectedMyModel);
				myModelList.add(newMyModel);
				selectedMyModel=null;
				beforeX=selected.getX()+selected.getWidth()/2;
				beforeY=selected.getY()+selected.getHeight()/2;
			}
			else{ //처음 그려질 때
				if(offX-onX>0 && offY-onY>0){
					editPane.removePanel(onX, onY);
					JPanel panel = editPane.addPanel(onX,onY, offX - onX, offY - onY,Color.LIGHT_GRAY);
					MyModel newMyModel = new MyModel(panel.getX(),panel.getY(),panel.getWidth(),panel.getHeight());
					myModelList.add(newMyModel);
				}
			}
			xCk = yCk = 0;
			isSized = false;
		}
		public void mouseDragged(MouseEvent me){
			if(isSelected){
				int panelX=selected.getX(), panelY=selected.getY();
				int panelWidth=selected.getWidth(), panelHeight = selected.getHeight();
				editPane.remove(selected);
				if (isSized)
				{	
					if (xCk > 0)
					{
						selected = editPane.addPanel(panelX, panelY, me.getX() - panelX, panelHeight, Color.YELLOW);
		
					}
					else if (xCk < 0)
					{
						selected = editPane.addPanel(me.getX(), panelY, panelWidth + (panelX - me.getX()), panelHeight, Color.YELLOW);
					}	
					else if (yCk > 0)
					{
						selected = editPane.addPanel(panelX, panelY, panelWidth, me.getY()-panelY, Color.yellow);
					}
					else if (yCk < 0)
					{
						selected = editPane.addPanel(panelX, me.getY(), panelWidth, panelHeight + (panelY - me.getY()), Color.yellow);
					}
				}
				else{
					selected = editPane.addPanel(panelX+(me.getX()-beforeX), panelY+(me.getY()-beforeY),panelWidth,panelHeight, Color.yellow);
					beforeX=me.getX(); beforeY=me.getY();
				}
			}
			else{
				editPane.removePanel(onX, onY);
				editPane.addPanel(onX, onY, me.getX()-onX, me.getY()-onY, Color.yellow);
			}
		}
		public void mouseMoved(MouseEvent me){}
		public void mouseClicked(MouseEvent me){}
		public void mouseEntered(MouseEvent me){}
		public void mouseExited(MouseEvent me){}
	}
	class MyButtonListener implements ActionListener {
		JPanel removePanel;
		String changePos, changeSize;
		int changeX, changeY, changeWidth, changeHeight;
		int removeX, removeY;
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("삭제")) {
				myModelList.remove(selected.getX(), selected.getY());
				if(myModelList.isEmpty())
					System.out.println("this is empty.");
				editPane.removePanel(selected.getX(),selected.getY());
				editPane.repaint();
			}
			else if(command.equals("변경")) {
				String attValue = tfAttValue.getText();
				String varName = tfVarName.getText();
				String comType = (String) cbComType.getSelectedItem();
				MyModel tmp = myModelList.find(selected.getX(), selected.getY());
				tmp.setAttValue(attValue);
				tmp.setComType(comType);
				tmp.setVarName(varName);
				changePos = tfXYPos.getText();
				changeSize = tfW_H.getText();
				String pos[] = changePos.split(",");
				String size[] = changeSize.split(",");
				changeX = Integer.parseInt(pos[0]);
				changeY = Integer.parseInt(pos[1]);
				changeWidth = Integer.parseInt(size[0]);
				changeHeight = Integer.parseInt(size[1]);
				if(changeX != selected.getX() || changeY != selected.getY() || changeWidth != selected.getWidth() || changeHeight != selected.getHeight()) {
					editPane.removePanel(selected.getX(),selected.getY());
					editPane.repaint();
					redraw(changeX, changeY, changeWidth, changeHeight);
					editPane.repaint();
					tmp.setX(changeX);
					tmp.setY(changeY);
					tmp.setWidth(changeWidth);
					tmp.setHeight(changeHeight);
				}
			}
		}
		public void redraw(int x, int y, int width, int height) {
			editPane.addPanel(x, y, width, height, Color.LIGHT_GRAY);
		}
	}
	
	class MyJSON extends JSONObject {
		String fileName;
		JSONObject jObj = new JSONObject();
		int jIndex = 1;
		JSONArray jModelList = new JSONArray();
		Iterator<MyModel> it = myModelList.iterator();
		public MyJSON() {
			fileName = "c:\\MyJSON.json";	
		}
		
		public MyJSON(String fileName) {
			this.fileName = fileName + ".json";
		}
		
		public void makeFile() {
				while(it.hasNext()) {
					MyModel tmp = (MyModel)it.next();
					if(tmp.getAttValue() != null && tmp.getVarName() != null && tmp.getComType() != null) {
						JSONArray jModelList = new JSONArray();
						jModelList.add(tmp.getX() + "");
						jModelList.add(tmp.getY() + "");
						jModelList.add(tmp.getWidth() + "");
						jModelList.add(tmp.getHeight() + "");
						jModelList.add(tmp.getAttValue());
						jModelList.add(tmp.getVarName());
						jModelList.add(tmp.getComType());
						jObj.put(jIndex + "번째 모델", jModelList);
						jIndex++;
					}
					else
						return;
				}
				
			try {
				FileWriter file = new FileWriter(fileName);
				file.write(jObj.toJSONString());
				file.flush();
				file.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("Created JSON Object" + jObj);
		}
		
		
		
	}
	public JTextField getTextField(String c) {
		if(c.equals("XYPos")) {
				return tfXYPos;
		} else if(c.equals("W_H")) {
				return tfW_H;
		} else 
			return null;
	}
	public MyArrayList getMyModelList() {
		return myModelList;
	}
}


class MyHandler implements ActionListener
{
	String fileName;
	Editor editor;
	ArrayList<MyModel> myModelList;
	public MyHandler(Editor editor) {
		this.editor = editor;
	}
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();

		if (command.equals("새로 만들기"))
		{
			Container newPane = editor.getEditPane();
			newPane.removeAll();
			newPane.repaint();
			myModelList = editor.getMyModelList();
			myModelList.clear();
		}
		else if (command.equals("열기"))
		{
			Container newPane = editor.getEditPane();
			newPane.removeAll();
			newPane.repaint();
			myModelList = editor.getMyModelList();
			myModelList.clear();
			FileDialog fileOpen =
					new FileDialog(editor, "파일열기", FileDialog.LOAD);
			fileOpen.setVisible(true);
			fileName = fileOpen.getDirectory() + fileOpen.getFile();
			System.out.println(fileName);
			open(fileName);
		}
		else if (command.equals("저장"))
		{
			editor.new MyJSON().makeFile();
		}
		else if (command.equals("다른 이름으로 저장"))
		{
			FileDialog fileSave =
					new FileDialog(editor, "파일저장", FileDialog.SAVE);
			fileSave.setVisible(true);
			fileName = fileSave.getDirectory() + fileSave.getFile();
			editor.new MyJSON(fileName).makeFile();
			System.out.println(fileName);
		}
		else if (command.equals(".java 파일 생성"))
		{
			System.exit(0);
		}
		else if (command.equals("닫기"))
		{
			System.exit(0);
		}
	}
	
	public void open(String fileName) {
		JSONParser parser = new JSONParser();
		int jIndex = 1;
        try {
               // myJson.json파일을 읽어와 Object로 파싱
               Object obj = parser.parse(new FileReader(fileName));
               
               JSONObject jObject =(JSONObject) obj;
               
               	try {
               		while(true) {
               			JSONArray jModelList = (JSONArray)jObject.get(jIndex+"번째 모델");
               			Iterator<String> it = jModelList.iterator();
               			while(it.hasNext()) {
               				int x = Integer.parseInt(it.next());
               				int y = Integer.parseInt(it.next());
               				int width = Integer.parseInt(it.next());
               				int height = Integer.parseInt(it.next());
               				String attValue = it.next();
               				String varName = it.next();
               				String comType = it.next();
               				openedFilePaint(x, y, width, height, attValue, varName, comType);
            	   		}
               			jIndex++;
               		}
               	} catch (Exception e) {
               		return;
               	}
            	   
        } catch (Exception e) {
               e.printStackTrace();
        }
	}
	
	public void openedFilePaint(int x, int y, int width, int height, String attValue, String varName, String comType) {
		Container editPane = editor.getEditPane();
		myModelList = editor.getMyModelList();
		MyModel newMyModel = new MyModel(x, y, width, height, attValue, varName, comType);
		JTextField tfXYPos = editor.getTextField("XYPos");
		JTextField tfW_H = editor.getTextField("W_H");
		JPanel newPanel = new JPanel();
		newPanel.setBackground(Color.lightGray);
		newPanel.setSize(width, height);
		newPanel.setLocation(x,y);
		tfXYPos.setText(x + "," + y);
		tfW_H.setText(width + "," + height);
		editPane.add(newPanel);
		editPane.repaint();
		myModelList.add(newMyModel);
	}
	
}

class MyModel
{
	private int x;
	private int y;
	private int width;
	private int height;
	private String attValue;
	private String varName;
	private String comType;

	public MyModel(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.comType = new String("JPanel");
	}
	
	public MyModel(int x, int y, int width, int height, String attValue, String varName, String comType) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.attValue = attValue;
		this.varName = varName;
		this.comType = comType;
		
	}

	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getWidth(){ return width; }
	public int getHeight() {return height;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setWidth(int width) {this.width = width;}
	public void setHeight(int height) {this.height = height;}
	public void setAttValue(String attValue) {this.attValue = new String(attValue);}
	public void setVarName(String varName) {this.varName = new String(varName);}
	public void setComType(String comType) {this.comType = new String(comType);}
	public String getAttValue() {return attValue;}
	public String getVarName() {return varName;}
	public String getComType() {return comType;}
}

class MyArrayList extends ArrayList<MyModel>{
	public MyModel find(int x,int y){
		int i;
		for(i=0; i<this.size(); i++){
			MyModel tmp = this.get(i);
			if(tmp.getX()==x && tmp.getY() == y)
				return this.get(i);

		}
		return null;
	}
	public boolean remove(int x,int y){
		MyModel tmp = this.find(x,y);
		if(tmp==null)
			return false;
		else{
			this.remove(tmp);
			return true;
		}
	}
}
