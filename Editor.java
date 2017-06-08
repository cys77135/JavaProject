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

    private JPanel attPane, selected;
    private MyPanel editPane;
    private JLabel xyPos, width_height, attValue, comType, varName;
    private JTextField tfXYPos, tfWidth_Height, tfAttValue, tfVarName;
    private JComboBox cbComType;
    private MyHandler handler;

    private MyArrayList myModelList;

    public Editor(String title) {
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

    private void initMenuBar() {
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

    private void initToolBar() {
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

    private void initAttPane() {
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
        width_height = new JLabel("    너비, 높이   :");
        attValue = new JLabel("컴포넌트의 텍스트 속성값   :");
        comType = new JLabel("  컴포넌트 타입  :");
        varName = new JLabel(" 컴포넌트 변수명   :");

        tfXYPos = new JTextField();
        tfWidth_Height = new JTextField();
        tfAttValue = new JTextField();
        tfVarName = new JTextField();

        String[] types =
                {
                        "JLabel",
                        "JButton",
                        "JTextField",
                        "JCheckBox"
                };

        cbComType = new JComboBox(types);

        bChange = new JButton("변경");
        bDelete = new JButton("삭제");

        xyPos.setSize(LB_WIDTH, LB_HEIGHT);
        width_height.setSize(LB_WIDTH, LB_HEIGHT);
        attValue.setSize(LB_WIDTH + 100, LB_HEIGHT);
        comType.setSize(LB_WIDTH, LB_HEIGHT);
        varName.setSize(LB_WIDTH + 100, LB_HEIGHT);

        tfXYPos.setSize(TF_WIDTH, TF_HEIGHT);
        tfWidth_Height.setSize(TF_WIDTH, TF_HEIGHT);
        tfAttValue.setSize(TF_WIDTH, TF_HEIGHT);
        tfVarName.setSize(TF_WIDTH, TF_HEIGHT);

        cbComType.setSize(TF_WIDTH, TF_HEIGHT);

        bChange.setSize(70, 30);
        bDelete.setSize(70, 30);

        final int REF_XPOS = 100;
        final int REF_YPOS = 150;
        xyPos.setLocation(REF_XPOS, REF_YPOS);
        width_height.setLocation(REF_XPOS, xyPos.getY() + 130);
        attValue.setLocation(REF_XPOS - 80, width_height.getY() + 130);
        comType.setLocation(REF_XPOS - 20, attValue.getY() + 130);
        varName.setLocation(REF_XPOS - 30, comType.getY() + 130);

        final int REF_XPOS2 = REF_XPOS + 100;
        tfXYPos.setLocation(REF_XPOS2, xyPos.getY());
        tfWidth_Height.setLocation(REF_XPOS2, width_height.getY());
        tfAttValue.setLocation(REF_XPOS2, attValue.getY());
        tfVarName.setLocation(REF_XPOS2, varName.getY());

        cbComType.setLocation(REF_XPOS2, comType.getY());

        bChange.setLocation(REF_XPOS2 + 30, comType.getY() + 230);
        bDelete.setLocation(REF_XPOS2 + 130, comType.getY() + 230);

        bDelete.addActionListener(new MyButtonListener());
        bChange.addActionListener(new MyButtonListener());

        attPane.add(xyPos);
        attPane.add(width_height);
        attPane.add(attValue);
        attPane.add(comType);
        attPane.add(varName);
        attPane.add(tfXYPos);
        attPane.add(tfWidth_Height);
        attPane.add(tfAttValue);
        attPane.add(tfVarName);
        attPane.add(cbComType);
        attPane.add(bChange);
        attPane.add(bDelete);
    }

    private void initEditPane() {
        editPane = new MyPanel();
        editPane.setLayout(null);
        editPane.setBackground(Color.WHITE);
        editPane.setSize(this.getWidth() / 3 * 2, this.getHeight());
        editPane.setLocation(attPane.getWidth(), 30);
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
        new Editor("GUI EDITOR");
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
                    tfAttValue.setText("");
                    tfVarName.setText("");
                    cbComType.setSelectedItem("");
                }
            }
            selected = editPane.searchPanel(onX, onY);

            if (!selected.equals(editPane)) {
                selected.setBackground(Color.blue);
                selectedMyModel = myModelList.find(selected.getX(), selected.getY());
                tfXYPos.setText(selectedMyModel.getX() + "," + selected.getY());
                tfWidth_Height.setText(selected.getWidth() + "," + selected.getHeight());
                try {
                    tfAttValue.setText(selectedMyModel.getAttValue());
                    tfVarName.setText(selectedMyModel.getVarName());
                    cbComType.setSelectedItem(selectedMyModel.getComType());
                }
                catch (NullPointerException ex) {
                    tfVarName.setText("");
                    tfAttValue.setText("");
                }
                beforeX = onX;
                beforeY = onY;
                isSelected = true;
            }
            else {
                tfXYPos.setText("");
                tfWidth_Height.setText("");
                tfAttValue.setText("");
                tfVarName.setText("");
                isSelected = false;
            }
        }

        public void mouseReleased(MouseEvent e) {
            offX = e.getX();
            offY = e.getY();
            if (isSelected) { //옮기기 & 크기변경
                if (!isSized)
                    selected = editPane.searchPanel(offX, offY);
                if (!selected.equals(editPane)) {
                    selected.setBackground(Color.blue);
                }
                MyModel newMyModel = new MyModel(selected.getX(), selected.getY(), selected.getWidth(), selected.getHeight());
                try {
                    newMyModel.setAttValue(selectedMyModel.getAttValue());
                    newMyModel.setVarName(selectedMyModel.getVarName());
                    newMyModel.setComType(selectedMyModel.getComType());
                }
                catch (NullPointerException ex) {
                    System.out.println("Null");
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

                MyModel changeModel = myModelList.find(selected.getX(), selected.getY());
                changeAttributes(changeModel);

                changePos = tfXYPos.getText();
                changeSize = tfWidth_Height.getText();
                String pos[] = changePos.split(",");
                String size[] = changeSize.split(",");
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
            String attValue = tfAttValue.getText();
            String varName = tfVarName.getText();
            String comType = (String) cbComType.getSelectedItem();
            try {
                changeModel.setAttValue(attValue);
                changeModel.setComType(comType);
                changeModel.setVarName(varName);
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
            	String tmp[] = fileName.split(".");
            	this.fileName = tmp[0] + ".json";
            } catch (Exception e) {
            	this.fileName = fileName + ".json";
            }
        }

        public void makeJSONFile() {
            while (it.hasNext()) {
                MyModel tmp = it.next();
                if (tmp.getAttValue() != null && tmp.getVarName() != null && tmp.getComType() != null) {
                    JSONArray jModelList = new JSONArray();
                    jModelList.add(tmp.getX() + "");
                    jModelList.add(tmp.getY() + "");
                    jModelList.add(tmp.getWidth() + "");
                    jModelList.add(tmp.getHeight() + "");
                    jModelList.add(tmp.getAttValue());
                    jModelList.add(tmp.getVarName());
                    jModelList.add(tmp.getComType());
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
            System.out.println("Created JSON Object" + jObj);
        }
    }

    public JTextField getTextField(String c) {
        if (c.equals("XYPos")) {
            return tfXYPos;
        }
        else if (c.equals("W_H")) {
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

        if (command.equals("새로 만들기")) {
            Container newPane = editor.getEditPane();
            newPane.removeAll();
            newPane.repaint();
            myModelList = editor.getMyModelList();
            myModelList.clear();
        }
        else if (command.equals("열기")) {
            Container newPane = editor.getEditPane();
            newPane.removeAll();
            newPane.repaint();
            myModelList = editor.getMyModelList();
            myModelList.clear();
            try {
            FileDialog fileOpen =
                    new FileDialog(editor, "파일열기", FileDialog.LOAD);
            fileOpen.setVisible(true);
            fileName = fileOpen.getDirectory() + fileOpen.getFile();
            System.out.println(fileName);
            fileOpen(fileName);
            } catch (Exception ex) {
            	return;
            }
        }
        else if (command.equals("저장")) {
            editor.new MyJSON().makeJSONFile();
        }
        else if (command.equals("다른 이름으로 저장")) {
        	try {
            FileDialog fileSave =
                    new FileDialog(editor, "파일저장", FileDialog.SAVE);
            fileSave.setVisible(true);
            fileName = fileSave.getDirectory() + fileSave.getFile();
            editor.new MyJSON(fileName).makeJSONFile();
            System.out.println(fileName);
            } catch (Exception ex) {
            	return;
            }
        }
        else if (command.equals(".java 파일 생성")) {
            try {
                javaFile();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else if (command.equals("닫기")) {
            System.exit(0);
        }
    }

    public void fileOpen(String fileName) {
        JSONParser parser = new JSONParser();
        int jModelIndex = 1;
        try {
            // myJson.json파일을 읽어와 Object로 파싱
            Object obj = parser.parse(new FileReader(fileName));

            JSONObject jObject = (JSONObject) obj;

            try {
                while (true) {
                    JSONArray jModelList = (JSONArray) jObject.get(jModelIndex + "번째 모델");
                    Iterator<String> it = jModelList.iterator();
                    while (it.hasNext())
                    {
                        int x = Integer.parseInt(it.next());
                        int y = Integer.parseInt(it.next());
                        int width = Integer.parseInt(it.next());
                        int height = Integer.parseInt(it.next());
                        String attValue = it.next();
                        String varName = it.next();
                        String comType = it.next();
                        openedFilePaint(x, y, width, height, attValue, varName, comType);
                    }
                    jModelIndex++;
                }
            }
            catch (Exception e) {
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openedFilePaint(int x, int y, int width, int height, String attValue, String varName, String comType) {
        Container editPane = editor.getEditPane();
        MyModel newMyModel = new MyModel(x, y, width, height, attValue, varName, comType);
        JTextField tfXYPos = editor.getTextField("XYPos");
        JTextField tfW_H = editor.getTextField("W_H");
        JPanel newPanel = new JPanel();

        myModelList = editor.getMyModelList();

        newPanel.setBackground(Color.lightGray);
        newPanel.setSize(width, height);
        newPanel.setLocation(x, y);
        tfXYPos.setText(x + "," + y);
        tfW_H.setText(width + "," + height);
        editPane.add(newPanel);
        editPane.repaint();

        myModelList.add(newMyModel);
    }

    public void javaFile() throws IOException {
        FileWriter fw;
        BufferedWriter bw;
        try {
            fw = new FileWriter(".\\Test.java");
            bw = new BufferedWriter(fw);
            bw.write("import javax.swing.*;\n" + "import java.awt.*;\n");
            bw.write("\n" + "public class Test extends JFrame\n{\n");

            bw.write("    Test()\n" +
                    "    {\n" +
                    "        setTitle(\"My GUI\");\n" +
                    "        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" +
                    "        setBackground(Color.WHITE);\n" +
                    "        setLayout(null);\n" +
                    "        setSize(1000, 1000);\n" +
                    "        setVisible(true);\n" +
                    "\n");
            for (int i = 0; i < editor.getMyModelList().size(); i++) {
                MyModel tmp = myModelList.get(i);
                bw.write("        " + tmp.getComType() + " " + tmp.getVarName()
                        + " = new " + tmp.getComType() + "();\n");
                bw.write("        " + tmp.getVarName() + ".setBounds("
                        + tmp.getX() + ", " + tmp.getY() + ", "
                        + tmp.getWidth() + ", " + tmp.getHeight() + ");\n");
                bw.write("        " + tmp.getVarName() + ".setBackground(Color.LIGHT_GRAY);\n");
                bw.write("        " + tmp.getVarName() + ".setText(\"" + tmp.getAttValue() + "\");\n");
                bw.write("        add(" + tmp.getVarName() + ");\n");
            }
            bw.write("        repaint();\n");
            bw.write("    }\n" +
                    "\n" +
                    "    public static void main(String[] args)\n" +
                    "    {\n" +
                    "        new Test();\n" +
                    "    }\n" +
                    "}");
            bw.close();
            fw.close();
            System.out.println("sample.txt 파일이 성공적으로 생성되었습니다.");
        }
        catch (Exception e) { }
    }
}

class MyModel {
    private int x;
    private int y;
    private int width;
    private int height;
    private String attValue;
    private String varName;
    private String comType;

    public MyModel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.comType = new String("JLabel");
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

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public String getAttValue() {return attValue;}
    public String getVarName() {return varName;}
    public String getComType() {return comType;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}
    public void setAttValue(String attValue) {this.attValue = new String(attValue);}
    public void setVarName(String varName) {this.varName = new String(varName);}
    public void setComType(String comType) {this.comType = new String(comType);}

    
}

class MyArrayList extends ArrayList<MyModel> {
    public MyModel find(int x, int y) {
        int i;
        for (i = 0; i < this.size(); i++) {
            MyModel tmp = this.get(i);
            if (tmp.getX() == x && tmp.getY() == y)
                return this.get(i);

        }
        return null;
    }

    public boolean remove(int x, int y) {
        MyModel tmp = this.find(x, y);
        if (tmp == null)
            return false;
        else {
            this.remove(tmp);
            return true;
        }
    }
}