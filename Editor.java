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
import java.io.BufferedWriter;
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



public class Editor extends JFrame {
	private MenuBar mb;
	private Menu mFile;
	private MenuItem miNew, miOpen, miSave, miSaveAs, miMakeJava, miExit;

	private JToolBar tb;
	private JButton mtNew, mtOpen, mtSave, mtSaveAs, mtMakeJava, mtExit;
	private JButton bChange, bDelete;

	private JPanel attributePane, selected;
	private MyPanel editPane;
	private JLabel xyPos, width_height, attributeValue, componentType, variableName;
	private JTextField tfXYPos, tfWidth_Height, tfAttributeValue, tfVariableName;
	private JComboBox cbComponentType;
	private MyHandler handler;

	private MyArrayList myModelList;

	public Editor(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setSize(1500, 1000);
		this.initMenuBar();
		this.initToolBar();
		this.initAttributePane();
		this.initEditPane();
		this.addActions();
		this.mySetFont();

		tb.setFloatable(false);

		add(tb);
		add(attributePane);
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

	private void initMenuBar() {
		mb = new MenuBar();
		mFile = new Menu("Menu");
		miNew = new MenuItem("New");
		miOpen = new MenuItem("Open");
		miSave = new MenuItem("Save");
		miSaveAs = new MenuItem("Save As");
		miMakeJava = new MenuItem("Create .java file");
		miExit = new MenuItem("Close");

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

	private void initToolBar() {
		final int R = 23;
		final int G = 89;
		final int B = 149;
		final int R2 = 37;
		final int G2 = 104;
		final int B2 = 159;

		tb = new JToolBar();
		mtNew = new JButton("New");
		mtOpen = new JButton("Open");
		mtSave = new JButton("Save");
		mtSaveAs = new JButton("Save As");
		mtMakeJava = new JButton("Create .java file");
		mtExit = new JButton("Close");

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

	private void initAttributePane() {
		final int LB_WIDTH = 100;
		final int LB_HEIGHT = 30;
		final int TF_WIDTH = 200;
		final int TF_HEIGHT = 30;

		attributePane = new JPanel();
		attributePane.setLayout(null);
		attributePane.setBackground(new Color(225, 225, 225));
		attributePane.setSize(this.getWidth() / 3, this.getHeight());
		attributePane.setLocation(0, 10);

		xyPos = new JLabel("시작 x,y 좌표   :");
		width_height = new JLabel("    너비, 높이   :");
		attributeValue = new JLabel("컴포넌트의 텍스트 속성값   :");
		componentType = new JLabel("  컴포넌트 타입  :");
		variableName = new JLabel(" 컴포넌트 변수명   :");

		tfXYPos = new JTextField();
		tfWidth_Height = new JTextField();
		tfAttributeValue = new JTextField();
		tfVariableName = new JTextField();

		String[] types =
			{
					"JLabel",
					"JButton",
					"JTextField",
					"JCheckBox"
			};

		cbComponentType = new JComboBox(types);

		bChange = new JButton("변경");
		bDelete = new JButton("삭제");

		xyPos.setSize(LB_WIDTH, LB_HEIGHT);
		width_height.setSize(LB_WIDTH, LB_HEIGHT);
		attributeValue.setSize(LB_WIDTH + 100, LB_HEIGHT);
		componentType.setSize(LB_WIDTH, LB_HEIGHT);
		variableName.setSize(LB_WIDTH + 100, LB_HEIGHT);

		tfXYPos.setSize(TF_WIDTH, TF_HEIGHT);
		tfWidth_Height.setSize(TF_WIDTH, TF_HEIGHT);
		tfAttributeValue.setSize(TF_WIDTH, TF_HEIGHT);
		tfVariableName.setSize(TF_WIDTH, TF_HEIGHT);

		cbComponentType.setSize(TF_WIDTH, TF_HEIGHT);

		bChange.setSize(70, 30);
		bDelete.setSize(70, 30);

		final int REF_XPOS = 100;
		final int REF_YPOS = 150;
		xyPos.setLocation(REF_XPOS, REF_YPOS);
		width_height.setLocation(REF_XPOS, xyPos.getY() + 130);
		attributeValue.setLocation(REF_XPOS - 80, width_height.getY() + 130);
		componentType.setLocation(REF_XPOS - 20, attributeValue.getY() + 130);
		variableName.setLocation(REF_XPOS - 30, componentType.getY() + 130);

		final int REF_XPOS2 = REF_XPOS + 100;
		tfXYPos.setLocation(REF_XPOS2, xyPos.getY());
		tfWidth_Height.setLocation(REF_XPOS2, width_height.getY());
		tfAttributeValue.setLocation(REF_XPOS2, attributeValue.getY());
		tfVariableName.setLocation(REF_XPOS2, variableName.getY());

		cbComponentType.setLocation(REF_XPOS2, componentType.getY());

		bChange.setLocation(REF_XPOS2 + 30, componentType.getY() + 230);
		bDelete.setLocation(REF_XPOS2 + 130, componentType.getY() + 230);

		bDelete.addActionListener(new MyButtonListener());
		bChange.addActionListener(new MyButtonListener());

		attributePane.add(xyPos);
		attributePane.add(width_height);
		attributePane.add(attributeValue);
		attributePane.add(componentType);
		attributePane.add(variableName);
		attributePane.add(tfXYPos);
		attributePane.add(tfWidth_Height);
		attributePane.add(tfAttributeValue);
		attributePane.add(tfVariableName);
		attributePane.add(cbComponentType);
		attributePane.add(bChange);
		attributePane.add(bDelete);
	}

	private void initEditPane() {
		editPane = new MyPanel();
		editPane.setLayout(null);
		editPane.setBackground(Color.WHITE);
		editPane.setSize(this.getWidth() / 3 * 2, this.getHeight());
		editPane.setLocation(attributePane.getWidth(), 30);
	}

	private void addActions() {

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

	private void mySetFont() {
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

	public static void main(String[] args) {
		new Editor("MY GUI BUILDER");
	}

	class MyPanel extends JPanel {
		MyMouseListener listener = new MyMouseListener();

		public MyPanel() {
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}

		public JPanel addPanel(int x, int y, int width, int height, Color color) {
			repaint();
			JPanel panel = new JPanel();
			panel.setBackground(color);
			panel.setSize(width, height);
			panel.setLocation(x, y);
			tfXYPos.setText(panel.getX() + "," + panel.getY());
			tfWidth_Height.setText(panel.getWidth() + "," + panel.getHeight());
			add(panel);
			return panel;
		}

		public JPanel searchPanel(int x, int y) {
			JPanel panel = (JPanel) this.getComponentAt(x, y);
			return panel;
		}

		public void removePanel(int x, int y) {
			JPanel target = this.searchPanel(x, y);
			this.remove(target);
		}
	}

	class MyMouseListener implements MouseListener, MouseMotionListener {
		private int onX, onY, offX, offY, beforeX, beforeY;
		private boolean isSelected, isSized;
		private int xSized = 0, ySized = 0;
		private MyModel selectedMyModel;

		public void mousePressed(MouseEvent e) {
			onX = e.getX();
			onY = e.getY();
			if (isSelected) {
				if (!selected.equals(editPane)) {
					if (onX >= selected.getX() + selected.getWidth() - 15 && onX <= selected.getX() + selected.getWidth()) {
						isSized = true;
						xSized = 1;
					}
					else if (onX >= selected.getX() && onX <= selected.getX() + 15) {
						isSized = true;
						xSized = -1;
					}
					if (onY >= selected.getY() + selected.getHeight() - 15 && onY <= selected.getY() + selected.getHeight()) {
						isSized = true;
						ySized = 1;
					}
					else if (onY >= selected.getY() && onY <= selected.getY() + 15) {
						isSized = true;
						ySized = -1;
					}
				}
				if (!selected.equals(editPane)) {
					selected.setBackground(Color.LIGHT_GRAY);
					tfXYPos.setText("");
					tfWidth_Height.setText("");
					tfAttributeValue.setText("");
					tfVariableName.setText("");
					cbComponentType.setSelectedItem("");
				}
			}
			selected = editPane.searchPanel(onX, onY);

			if (!selected.equals(editPane)) {
				selected.setBackground(Color.blue);
				selectedMyModel = myModelList.find(selected.getX(), selected.getY());
				tfXYPos.setText(selected.getX() + "," + selected.getY());
				tfWidth_Height.setText(selected.getWidth() + "," + selected.getHeight());
				try {
					tfAttributeValue.setText(selectedMyModel.getAttributeValue());
					tfVariableName.setText(selectedMyModel.getVariableName());
					cbComponentType.setSelectedItem(selectedMyModel.getComponentType());
				}
				catch (NullPointerException ex) {
					tfVariableName.setText("");
					tfAttributeValue.setText("");
				}
				beforeX = onX;
				beforeY = onY;
				isSelected = true;
			}
			else {
				tfXYPos.setText("");
				tfWidth_Height.setText("");
				tfAttributeValue.setText("");
				tfVariableName.setText("");
				isSelected = false;
			}
		}

		public void mouseReleased(MouseEvent e) {
			offX = e.getX();
			offY = e.getY();
			if (isSelected) { //옮기기 & 크기변경
				if (!selected.equals(editPane)) {
					selected.setBackground(Color.blue);
				}
				MyModel newMyModel = new MyModel(selected.getX(), selected.getY(), selected.getWidth(), selected.getHeight());
				try {
					newMyModel.setAttributeValue(selectedMyModel.getAttributeValue());
					newMyModel.setVariableName(selectedMyModel.getVariableName());
					newMyModel.setComponentType(selectedMyModel.getComponentType());
				}
				catch (NullPointerException ex) {

				}
				myModelList.remove(selectedMyModel);
				myModelList.add(newMyModel);
				selectedMyModel = null;
				beforeX = selected.getX() + selected.getWidth() / 2;
				beforeY = selected.getY() + selected.getHeight() / 2;
			}
			else { //처음 그려질 때
				if (offX - onX > 0 && offY - onY > 0) {
					editPane.removePanel(onX, onY);
					JPanel panel = editPane.addPanel(onX, onY, offX - onX, offY - onY, Color.LIGHT_GRAY);
					MyModel newMyModel = new MyModel(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
					myModelList.add(newMyModel);
				}
			}
			xSized = ySized = 0;
			isSized = false;
		}

		public void mouseDragged(MouseEvent me) {
			if (isSelected) {
				int panelX = selected.getX(), panelY = selected.getY();
				int panelWidth = selected.getWidth(), panelHeight = selected.getHeight();
				editPane.remove(selected);
				if (isSized) {
					if (xSized > 0) {
						selected = editPane.addPanel(panelX, panelY, me.getX() - panelX, panelHeight, Color.YELLOW);

					}
					else if (xSized < 0) {
						selected = editPane.addPanel(me.getX(), panelY, panelWidth + (panelX - me.getX()), panelHeight, Color.YELLOW);
					}
					else if (ySized > 0) {
						selected = editPane.addPanel(panelX, panelY, panelWidth, me.getY() - panelY, Color.yellow);
					}
					else if (ySized < 0) {
						selected = editPane.addPanel(panelX, me.getY(), panelWidth, panelHeight + (panelY - me.getY()), Color.yellow);
					}
				}
				else {
					selected = editPane.addPanel(panelX + (me.getX() - beforeX), panelY + (me.getY() - beforeY), panelWidth, panelHeight, Color.yellow);
					beforeX = me.getX();
					beforeY = me.getY();
				}
			}
			else {
				editPane.removePanel(onX, onY);
				editPane.addPanel(onX, onY, me.getX() - onX, me.getY() - onY, Color.yellow);
			}
		}

		public void mouseMoved(MouseEvent me) { }
		public void mouseClicked(MouseEvent me) { }
		public void mouseEntered(MouseEvent me) { }
		public void mouseExited(MouseEvent me) { }
	}

	class MyButtonListener implements ActionListener {
		String changePos, changeSize;
		int changeX, changeY, changeWidth, changeHeight;

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("삭제")) {
				deletePanel();
			}
			else if (command.equals("변경")) {
				try {
					MyModel changeModel = myModelList.find(selected.getX(), selected.getY());
					changeAttributes(changeModel);

					changePos = tfXYPos.getText();
					changeSize = tfWidth_Height.getText();
					String pos[] = changePos.split(",");
					String size[] = changeSize.split(",");
					pos[0] = pos[0].trim();
					pos[1] = pos[1].trim();
					size[0] = size[0].trim();
					size[1] = size[1].trim();
					changeX = Integer.parseInt(pos[0]);
					changeY = Integer.parseInt(pos[1]);
					changeWidth = Integer.parseInt(size[0]);
					changeHeight = Integer.parseInt(size[1]);

					if (changeX != selected.getX() || changeY != selected.getY() || changeWidth != selected.getWidth() || changeHeight != selected.getHeight()) {
						editPane.removePanel(selected.getX(), selected.getY());
						editPane.repaint();
						if (changeWidth > 0 && changeHeight > 0) {
							redraw(changeX, changeY, changeWidth, changeHeight, changeModel);
							editPane.repaint();
						}
						else {
							deletePanel();
						}

					}
				} catch(Exception ex) {}
			}
		}	

		public void deletePanel() {
			myModelList.remove(selected.getX(), selected.getY());
			if (myModelList.isEmpty())
				System.out.println("this is empty.");
			editPane.removePanel(selected.getX(), selected.getY());
			editPane.repaint();
		}

		public void changeAttributes(MyModel changeModel) {
			String attributeValue = tfAttributeValue.getText();
			String variableName = tfVariableName.getText();
			String componentType = (String) cbComponentType.getSelectedItem();
			try {
				changeModel.setAttributeValue(attributeValue);
				changeModel.setComponentType(componentType);
				changeModel.setVariableName(variableName);
			}
			catch (Exception e) {
				return;
			}
		}

		public void redraw(int x, int y, int width, int height, MyModel changeModel) {
			try {
				editPane.addPanel(x, y, width, height, Color.LIGHT_GRAY);
				changeModel.setX(x);
				changeModel.setY(y);
				changeModel.setWidth(width);
				changeModel.setHeight(height);
			} catch(Exception e) {
				return;
			}
		}
	}

	class MyJSON extends JSONObject {
		String fileName;
		JSONObject jObj = new JSONObject();
		int jModelIndex = 1;
		Iterator<MyModel> it = myModelList.iterator();

		public MyJSON() {
			fileName = ".\\MyJSON.json";
		}

		public MyJSON(String fileName) {
			try {
				if(fileName != null) {
					String tmp[] = fileName.split(".");
					this.fileName = tmp[0] + ".json";
				}
			} catch (Exception e) {
				if(fileName != null) {
					this.fileName = fileName + ".json";
				}
			}
		}

		public void createJSONFile() {
			while (it.hasNext()) {
				MyModel sourceModel = it.next();
				if (sourceModel.getAttributeValue() != null && sourceModel.getVariableName() != null && sourceModel.getComponentType() != null) {
					JSONArray jModelList = new JSONArray();
					jModelList.add(sourceModel.getX() + "");
					jModelList.add(sourceModel.getY() + "");
					jModelList.add(sourceModel.getWidth() + "");
					jModelList.add(sourceModel.getHeight() + "");
					jModelList.add(sourceModel.getAttributeValue());
					jModelList.add(sourceModel.getVariableName());
					jModelList.add(sourceModel.getComponentType());
					jObj.put(jModelIndex + "번째 모델", jModelList);
					jModelIndex++;
				}
				else
					return;
			}

			try {
				FileWriter file = new FileWriter(fileName);
				file.write(jObj.toJSONString());
				file.flush();
				file.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public JTextField getTextField(String c) {
		if (c.equals("XYPos")) {
			return tfXYPos;
		}
		else if (c.equals("Width_Height")) {
			return tfWidth_Height;
		}
		else
			return null;
	}

	public MyArrayList getMyModelList() {
		return myModelList;
	}
}


class MyHandler implements ActionListener {
	String fileName;
	Editor editor;
	ArrayList<MyModel> myModelList;

	public MyHandler(Editor editor) {
		this.editor = editor;
		this.myModelList = editor.getMyModelList();
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("New")) {
			Container newPane = editor.getEditPane();
			newPane.removeAll();
			newPane.repaint();
			myModelList = editor.getMyModelList();
			myModelList.clear();
		}
		else if (command.equals("Open")) {
			Container newPane = editor.getEditPane();
			newPane.removeAll();
			newPane.repaint();
			myModelList = editor.getMyModelList();
			myModelList.clear();

			FileDialog fileOpen =
					new FileDialog(editor, "Open File", FileDialog.LOAD);
			fileOpen.setVisible(true);
			fileName = fileOpen.getDirectory() + fileOpen.getFile();

			try {
				fileOpen(fileName);
			} catch (Exception ex) {
				return;
			}
		}
		else if (command.equals("Save")) {
			editor.new MyJSON().createJSONFile();
		}
		else if (command.equals("Save As")) {
			try {
				FileDialog fileSave =
						new FileDialog(editor, "Save File", FileDialog.SAVE);
				fileSave.setVisible(true);
				fileName = fileSave.getDirectory() + fileSave.getFile();
				editor.new MyJSON(fileName).createJSONFile();

			} catch (Exception ex) {
				return;
			}
		}
		else if (command.equals("Create .java file")) {
			try {
				createJavaFile();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if (command.equals("Close")) {
			System.exit(0);
		}
	}

	public void fileOpen(String fileName) {
		JSONParser parser = new JSONParser();
		int jModelIndex = 1;
		try {
			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jObject = (JSONObject) obj;	

			while (true) {
				JSONArray jModelList = (JSONArray) jObject.get(jModelIndex + "번째 모델");
				Iterator<String> it = jModelList.iterator();
				while (it.hasNext()) {
					int x = Integer.parseInt(it.next());
					int y = Integer.parseInt(it.next());
					int width = Integer.parseInt(it.next());
					int height = Integer.parseInt(it.next());
					String attributeValue = it.next();
					String variableName = it.next();
					String componentType = it.next();
					openedFilePaint(x, y, width, height, attributeValue, variableName, componentType);
				}
				jModelIndex++;
			}
		}
		catch (Exception e) {
			return;
		}
	}

	public void openedFilePaint (int x, int y, int width, int height, String attributeValue, String variableName, String componentType) {
		Container editPane = editor.getEditPane();
		MyModel newMyModel = new MyModel(x, y, width, height, attributeValue, variableName, componentType);
		JTextField tfXYPos = editor.getTextField("XYPos");
		JTextField tfWidth_Height = editor.getTextField("Width_Height");
		JPanel newPanel = new JPanel();

		myModelList = editor.getMyModelList();

		newPanel.setBackground(Color.lightGray);
		newPanel.setSize(width, height);
		newPanel.setLocation(x, y);
		tfXYPos.setText(x + "," + y);
		tfWidth_Height.setText(width + "," + height);
		editPane.add(newPanel);
		editPane.repaint();

		myModelList.add(newMyModel);
	}

	public void createJavaFile() throws IOException {
		FileWriter fw;
		BufferedWriter bw;
		ArrayList<MyModel> list = editor.getMyModelList();
		Iterator<MyModel> it = list.iterator();
		try {
			fw = new FileWriter(".\\MyGUI.java");
			bw = new BufferedWriter(fw);
			bw.write("import javax.swing.*;\n" + "import java.awt.*;\n");
			bw.write("\n" + "public class MyGUI extends JFrame\n{\n");

			bw.write("    MyGUI()\n" +
					"    {\n" +
					"        setTitle(\"MyGUI\");\n" +
					"        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" +
					"        setBackground(Color.WHITE);\n" +
					"        setLayout(null);\n" +
					"        setSize(1000, 1000);\n" +
					"        setVisible(true);\n" +
					"\n");
			while(it.hasNext()) {
				MyModel sourceModel = it.next();
				if(sourceModel.getComponentType() != null && sourceModel.getVariableName() != null && sourceModel.getAttributeValue() != null) {
					bw.write("        " + sourceModel.getComponentType() + " " + sourceModel.getVariableName()
					+ " = new " + sourceModel.getComponentType() + "();\n");
					bw.write("        " + sourceModel.getVariableName() + ".setBounds("
							+ sourceModel.getX() + ", " + sourceModel.getY() + ", "
							+ sourceModel.getWidth() + ", " + sourceModel.getHeight() + ");\n");
					bw.write("        " + sourceModel.getVariableName() + ".setBackground(Color.LIGHT_GRAY);\n");
					bw.write("        " + sourceModel.getVariableName() + ".setText(\"" + sourceModel.getAttributeValue() + "\");\n");
					bw.write("        add(" + sourceModel.getVariableName() + ");\n");
				}
				else
					System.out.println(".java 파일을 생성할 수 없습니다.");
			}     
			bw.write("        repaint();\n");
        	bw.write("    }\n" +
                "\n" +
                "    public static void main(String[] args)\n" +
                "    {\n" +
                "        new MyGUI();\n" +
                "    }\n" +
                "}");
        	bw.close();
        	fw.close();
        	System.out.println(".java 파일이 성공적으로 생성되었습니다.");
		}
		catch (Exception e) { }
	}
}

class MyModel {
	private int x;
	private int y;
	private int width;
	private int height;
	private String attributeValue;
	private String variableName;
	private String componentType;

	public MyModel(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.componentType = new String("JLabel");
	}

	public MyModel(int x, int y, int width, int height, String attributeValue, String variableName, String componentType) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.attributeValue = attributeValue;
		this.variableName = variableName;
		this.componentType = componentType;
	}

	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public String getAttributeValue() {return attributeValue;}
	public String getVariableName() {return variableName;}
	public String getComponentType() {return componentType;}

	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setWidth(int width) {this.width = width;}
	public void setHeight(int height) {this.height = height;}
	public void setAttributeValue(String attributeValue) {this.attributeValue = new String(attributeValue);}
	public void setVariableName(String variableName) {this.variableName = new String(variableName);}
	public void setComponentType(String componentType) {this.componentType = new String(componentType);}


}

class MyArrayList extends ArrayList<MyModel> {
	public MyModel find(int x, int y) {
		int i;
		for (i = 0; i < this.size(); i++) {
			MyModel deleteModel = this.get(i);
			if (deleteModel.getX() == x && deleteModel.getY() == y)
				return this.get(i);

		}
		return null;
	}

	public boolean remove(int x, int y) {
		MyModel deleteModel = this.find(x, y);
		if (deleteModel == null)
			return false;
		else {
			this.remove(deleteModel);
			return true;
		}
	}
}
