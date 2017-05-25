import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class Editor extends JFrame
{
    private String fileName;
    private MenuBar mb;
    private Menu mFile;
    private MenuItem miNew, miOpen, miSave, miSaveAs, miMakeJava, miExit;

    private JToolBar tb;
    private JButton mtNew, mtOpen, mtSave, mtSaveAs, mtMakeJava, mtExit;
    private JButton pChange;

    private JPanel attPane, editPane;
    private JLabel xyPos, w_h, attValue, comType, varName;
    private JTextField tfXYPos, tfW_H, tfAttValue, tfVarName;
    private JComboBox cbComType;

    public Editor(String title)
    {
        super(title);
        setLayout(null);
        setSize(1500, 1000);

        this.initMenuBar();
        this.initToolBar();
        this.initAttPane();
        this.initEditPane();
        this.addActions();
        this.mySetFont();


        //ToolBar 위치 고정
        tb.setFloatable(false);

        add(tb);
        add(attPane);
        add(editPane);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        //Frame 위치 지정
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
        comType = new JLabel("  컴포넌트 타입   :");
        varName = new JLabel(" 컴포넌트 변수명   :");

        tfXYPos = new JTextField();
        tfW_H = new JTextField();
        tfAttValue = new JTextField();
        tfVarName = new JTextField();

        String[] types =
                {
                        "Rect",
                        "Circle",
                        "Oval",
                        "Arc",
                        "Polygon"
                };

        cbComType = new JComboBox(types);

        pChange = new JButton("변경");

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

        pChange.setSize(70, 30);

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

        pChange.setLocation(REF_XPOS2 + 130, comType.getY() + 230);

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
        attPane.add(pChange);
    }

    private void initEditPane()
    {
        //editPane = new JPanel();
        editPane = new MyPanel();
        editPane.setLayout(null);
        editPane.setBackground(Color.red);
        editPane.setSize(this.getWidth() / 3 * 2, this.getHeight());
        editPane.setLocation(attPane.getWidth(), 30);
    }

    private void addActions()
    {
        MyHandler handler = new MyHandler();
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

        pChange.setFont(f2);

        mtNew.setForeground(Color.WHITE);
        mtOpen.setForeground(Color.WHITE);
        mtSave.setForeground(Color.WHITE);
        mtSaveAs.setForeground(Color.WHITE);
        mtMakeJava.setForeground(Color.WHITE);
        mtExit.setForeground(Color.WHITE);
    }

    public static void main(String[] args)
    {
        Editor mainWin = new Editor("GUI EDITOR");
    }

    class MyHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();

            if (command.equals("새로 만들기"))
            {
                System.exit(0);
            }
            else if (command.equals("열기"))
            {
                System.exit(0);
            }
            else if (command.equals("저장"))
            {
                System.exit(0);
            }
            else if (command.equals("다른 이름으로 저장"))
            {
                FileDialog fileSave =
                        new FileDialog(Editor.this, "파일저장", FileDialog.SAVE);
                fileSave.setVisible(true);
                fileName = fileSave.getDirectory() + fileSave.getFile();
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
    }
}

class JPanelEX extends JFrame
{
    Container contentPane;

    JPanelEX()
    {
        setTitle("JPanel paintComponent 예제");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = getContentPane();
        MyPanel panel = new MyPanel();
        contentPane.add(BorderLayout.CENTER, panel);
        setSize(250, 200);
        setVisible(true);
    }
}

class MyPanel extends JPanel
{
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawRect(20,20, 300, 300);
    }
}