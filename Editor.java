import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class Editor extends JFrame
{
    private String fileName;
    private MenuBar mb;
    private Menu mFile;
    private MenuItem miNew, miOpen, miSave, miSaveAs, miMakeJava, miExit;

    private JToolBar tb;
    private JButton mtNew, mtOpen, mtSave, mtSaveAs, mtMakeJava, mtExit;
    private JButton bChange, bDelete;

    private JPanel attPane, editPane;
    private JLabel xyPos, w_h, attValue, comType, varName;
    private JTextField tfXYPos, tfW_H, tfAttValue, tfVarName;
    private JComboBox cbComType;
    private MyHandler handler;

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

    }

    private void initMenuBar()
    {
        mb = new MenuBar();
        mFile = new Menu("Menu");
        miNew = new MenuItem("���� �����");
        miOpen = new MenuItem("����");
        miSave = new MenuItem("����");
        miSaveAs = new MenuItem("�ٸ� �̸����� ����");
        miMakeJava = new MenuItem(".java ���� ����");
        miExit = new MenuItem("�ݱ�");

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
        mtNew = new JButton("���� �����");
        mtOpen = new JButton("����");
        mtSave = new JButton("����");
        mtSaveAs = new JButton("�ٸ� �̸����� ����");
        mtMakeJava = new JButton(".java ���� ����");
        mtExit = new JButton("�ݱ�");

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

        xyPos = new JLabel("���� x,y ��ǥ   :");
        w_h = new JLabel("    �ʺ�, ����   :");
        attValue = new JLabel("������Ʈ�� �ؽ�Ʈ �Ӽ���   :");
        comType = new JLabel("  ������Ʈ Ÿ��  :");
        varName = new JLabel(" ������Ʈ ������   :");

        tfXYPos = new JTextField();
        tfW_H = new JTextField();
        tfAttValue = new JTextField();
        tfVarName = new JTextField();

        String[] types =
                {
                        "JPanel",
                        "JLabel",
                        "JButton",
                        "JComboBox",
                        "JScrollBar",
                        "JToolBar",
                        "JMenuBar"
                };

        cbComType = new JComboBox(types);

        bChange = new JButton("����");
        bDelete = new JButton("����");
        
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
        Font f = new Font("���� ���", Font.PLAIN, 13);
        Font f2 = new Font("���� ���", Font.BOLD, 13);

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

    public Container getEditPane() {
    	return this.editPane;
    }
    
    public static void main(String[] args)
    {
        Editor mainWin = new Editor("GUI EDITOR");
    }

    class MyPanel extends JPanel
    {
        int xPos, yPos, width, height;
        MyMouseListener listener = new MyMouseListener();

        public MyPanel()
        {
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(xPos, yPos, width, height);
        }
    }

    class MyMouseListener implements MouseListener, MouseMotionListener
    {
        private int onX, onY, offX, offY, selX, selY;
        private boolean isSelected, isSized;
        private int xCk = 0, yCk = 0;
        private JPanel selected;

        public void mousePressed(MouseEvent e)
        {
            onX = e.getX();
            onY = e.getY();
            if (isSelected)
            {
                if (!selected.equals(editPane))
                {
                    selected.setBackground(Color.LIGHT_GRAY);
                    if (onX >= selected.getX() + selected.getWidth() - 10 && onX <= selected.getX() + selected.getWidth())
                    {
                        isSized = true;
                        xCk = 1;
                    }
                    else if (onX >= selected.getX() && onX <= selected.getX() + 10)
                    {
                        isSized = true;
                        xCk = -1;
                    }
                    if (onY >= selected.getY() + selected.getHeight() - 10 && onY <= selected.getY() + selected.getHeight())
                    {
                        isSized = true;
                        yCk = 1;
                    }
                    else if (onY >= selected.getY() && onY <= selected.getY() + 10)
                    {
                        isSized = true;
                        yCk = -1;
                    }
                }
            }
            selected = (JPanel) editPane.getComponentAt(onX, onY);

            if (!selected.equals(editPane))
            {
                selected.setBackground(Color.blue);
                selX = onX;
                selY = onY;
                isSelected = true;
            }
            else
            {
                isSelected = false;
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            offX = e.getX();
            offY = e.getY();
            repaint();
            if (isSelected)
            {
                if (isSized)
                {
                    editPane.remove(selected);
                    if (xCk > 0)
                    {
                        selected.setSize(e.getX() - selected.getX(), selected.getHeight());
                        selected.setLocation(selected.getX(), selected.getY());
                    }
                    else if (xCk < 0)
                    {
                        selected.setSize(selected.getX() + selected.getWidth() - e.getX(), selected.getHeight());
                        selected.setLocation(e.getX(), selected.getY());
                    }
                    if (yCk > 0)
                    {
                        selected.setSize(selected.getWidth(), e.getY() - selected.getY());
                        selected.setLocation(selected.getX(), selected.getY());
                    }
                    else if (yCk < 0)
                    {
                        selected.setSize(selected.getWidth(), selected.getY() + selected.getHeight() - e.getY());
                        selected.setLocation(selected.getX(), e.getY());
                    }
                    selected.setBackground(Color.blue);
                    tfXYPos.setText(selected.getX() + "," + selected.getY());
                    tfW_H.setText(selected.getWidth() + "," + selected.getHeight());
                    editPane.add(selected);
                }
                else
                {
                    editPane.remove(selected);
                    selected.setSize(selected.getWidth(), selected.getHeight());
                    selected.setLocation(selected.getX() + (offX - onX), selected.getY() + (offY - onY));
                    selected.setBackground(Color.blue);
                    tfXYPos.setText(selected.getX() + "," + selected.getY());
                    tfW_H.setText(selected.getWidth() + "," + selected.getHeight());
                    selX = offX;
                    selY = offY;
                    editPane.add(selected);
                }
            }
            else
            {
                JPanel panel = new JPanel();
                panel.setBackground(Color.LIGHT_GRAY);
                panel.setSize(offX - onX, offY - onY);
                panel.setLocation(onX, onY);
                tfXYPos.setText(panel.getX() + "," + panel.getY());
                tfW_H.setText(panel.getWidth() + "," + panel.getHeight());
                editPane.add(panel);
            }
            xCk = yCk = 0;
            isSized = false;
        }

        public void mouseDragged(MouseEvent me)
        {
            /*int x,y;
            x=me.getX();
			y=me.getY();
			repaint();
			JPanel panel = new JPanel();
			editPane.remove(editPane.getComponentAt(x, y));
			if(isSelected){
				panel.setBackground(Color.yellow);
				panel.setSize(selected.getWidth(), selected.getHeight());
				panel.setLocation(selected.getX()+(x-onX), selected.getY()+(y-onY));
			}
			else{
				panel.setBackground(Color.YELLOW);
				panel.setSize(x- onX, y - onY);
				panel.setLocation(onX, onY);
			}
			tfXYPos.setText(panel.getX() + "," + panel.getY());
			tfW_H.setText(panel.getWidth() + "," + panel.getHeight());
			editPane.add(panel);*/
        }
        
        public void mouseMoved(MouseEvent me){}
        public void mouseClicked(MouseEvent me){}
        public void mouseEntered(MouseEvent me){}
        public void mouseExited(MouseEvent me) {}
                     
    }
    class MyButtonListener implements ActionListener {
    	JPanel removePanel;
    	int removeX, removeY;
    	public void actionPerformed(ActionEvent e) {
    		String command = e.getActionCommand();
    		if(command.equals("����")) {
    			
    			String tmp = tfXYPos.getText();
        		String pos[] = tmp.split(",");
        		removeX = Integer.parseInt(pos[0]);
        		removeY = Integer.parseInt(pos[1]);
        		editPane.remove(editPane.getComponentAt(removeX,removeY));
        		editPane.repaint();
    		}
    	}
    }
    
}

class MyHandler implements ActionListener
{
	String fileName;
	Editor editor;
	
	public MyHandler(Editor editor) {
		this.editor = editor;
	}
	
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("���� �����"))
        {
            Container newPane = editor.getEditPane();
            newPane.removeAll();
            newPane.repaint();
        }
        else if (command.equals("����"))
        {
            System.exit(0);
        }
        else if (command.equals("����"))
        {
            System.exit(0);
        }
        else if (command.equals("�ٸ� �̸����� ����"))
        {
            FileDialog fileSave =
                    new FileDialog(editor, "��������", FileDialog.SAVE);
            fileSave.setVisible(true);
            fileName = fileSave.getDirectory() + fileSave.getFile();
            System.out.println(fileName);
        }
        else if (command.equals(".java ���� ����"))
        {
            System.exit(0);
        }
        else if (command.equals("�ݱ�"))
        {
            System.exit(0);
        }
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
    }
}