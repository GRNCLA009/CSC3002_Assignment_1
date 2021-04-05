import javafx.scene.shape.Box;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class HomePage extends JFrame implements ActionListener{

    /////////////////////////////// VARIABLES ///////////////////////////////

    static final int WIDTH = 1365;
    static final int HEIGHT = 735;

    int num_in_group = 0;

    static final Font tb20 = new Font("Tahoma", Font.BOLD, 20);
    static final Font tp14 = new Font("Tahoma", Font.BOLD, 14);

    static final Color DBLUE = new Color(0, 0, 150);
    static final Color LBLUE = new Color(0, 150, 255);

    private JPanel topPanel; //NORTH PANEL
    private JPanel centrePanel; //CENTRE PANEL
    private JPanel bottomPanel; //SOUTH PANEL
    private JPanel leftPanel; //WEST PANEL
    private JPanel rightPanel; //EAST PANEL
    private JPanel loginPanel; //LOGIN CENTRE PANEL
    private JPanel listPanel;

    private JButton start_chatting;
    private JButton login_button;
    private JButton refresh;
    private JButton quit;

    private JLabel title;
    private JLabel lspace;
    private JLabel rspace;
    private JLabel loginspace;
    private JLabel loginspace2;
    private JLabel login;

    private JTextField username;

    private JScrollPane list;

    String stu_name;
      
    ArrayList<String> ChatMembers = new ArrayList<String>(30); // Dom: all the poepel being added to a chat
    
    //ArrayList<ChatServer> chats = new ArrayList<ChatServer>();

    DatagramSocket socket;

    /////////////////////////////// CONSTRUCTOR ///////////////////////////////

    public HomePage(){

        /////////////////////////////// HOME PAGE FRAME ///////////////////////////////

        super("Assignment 1 Chat Application");
        setSize(WIDTH, HEIGHT);
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(LBLUE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        /////////////////////////////// JPANELS ///////////////////////////////

        //Top
        topPanel = new JPanel();
        topPanel.setBackground(LBLUE);
        add(topPanel, BorderLayout.NORTH);

        //Middle
        centrePanel = new JPanel();
        //SpringLayout spring = new SpringLayout();
        //centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.PAGE_AXIS));
        centrePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));

        //Login
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        loginPanel.setBackground(LBLUE);
        add(loginPanel, BorderLayout.CENTER);

        //Bottom
        bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBackground(LBLUE);

        //Left
        leftPanel = new JPanel();
        leftPanel.setBackground(LBLUE);
        add(leftPanel, BorderLayout.WEST);

        //Right
        rightPanel = new JPanel();
        rightPanel.setBackground(LBLUE);
        add(rightPanel, BorderLayout.EAST);

        /////////////////////////////// JLABELS ///////////////////////////////

        //Title
        title = new JLabel();
        title.setFont(tb20);
        title.setForeground(Color.BLACK);

        //Login Instruction
        login = new JLabel("Please enter your username: ");
        login.setFont(tb20);
        login.setForeground(Color.WHITE);
        loginPanel.add(login);

        //Spaces
        lspace = new JLabel("                      ");
        rspace = new JLabel("                      ");
        loginspace = new JLabel("                      ");
        loginspace2 = new JLabel("                      ");
        leftPanel.add(lspace);
        rightPanel.add(rspace);
        loginPanel.add(loginspace);

        /////////////////////////////// JTEXTFIELDS ///////////////////////////////

        username = new JTextField(30);
        loginPanel.add(username);
        loginPanel.add(loginspace2);

        /////////////////////////////// JSCROLLPANES ///////////////////////////////

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
        listPanel.setPreferredSize(new Dimension(500, 600));
        list = new JScrollPane(listPanel);
        list.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        list.setPreferredSize(new Dimension(1180, 609));
        centrePanel.add(list);

        /////////////////////////////// JBUTTONS ///////////////////////////////

        //Login
        login_button = new JButton("  ENTER  ");
        login_button.setBackground(Color.RED);
        login_button.setForeground(Color.WHITE);
        login_button.setFont(tp14);
        loginPanel.add(login_button);
        login_button.addActionListener(this);

        //Start
        start_chatting = new JButton("  START CHATTING!  ");
        start_chatting.setBackground(Color.RED);
        start_chatting.setForeground(Color.WHITE);
        start_chatting.setFont(tp14);

        refresh = new JButton("  REFRESH  ");
        refresh.setBackground(Color.ORANGE);
        refresh.setForeground(Color.WHITE);
        refresh.setFont(tp14);

        quit = new JButton("  QUIT  ");
        quit.setBackground(Color.BLACK);
        quit.setForeground(Color.WHITE);
        quit.setFont(tp14);
        bottomPanel.add(quit);
        quit.addActionListener(this);
    }

    /////////////////////////////// ACTIONLISTENER ///////////////////////////////

    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();

        if(button.equals("  ENTER  ")){ //User has inserted username
            boolean dup = false;
            stu_name = username.getText();

            if(stu_name.length() > 0){
                //Duplicate method only tested when connected to ChatServer class
            /*for(ChatServer c : chats){ //checking for duplicates
                if(c.getName().equals(stu_name)){
                    dup = true;
                    username.setText("Username taken. Insert something original!");
                    break;
                }
            }*/

                if(!dup){ //continue normally
                    this.remove(loginPanel);
                    //join and ask for online users - MESSAGE1+2+3

                    int height = 0;
                    //int listLength = 0; - for later
                    String people = "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\na\nb\nc\nd\ne\nf\ng\nh\ni\nj\na\nb\nc\nd\ne\nf\ng\nh\ni\nj\na\nb\nc\nd\ne\nf\ng\nh\ni\nj";
                    String[] ppl = people.split("\n");
                    Arrays.sort(ppl);

                    for(String s : ppl){
                        
                        JButton j = new JButton(s);
                        j.setMinimumSize(new Dimension(10, 30));
                        j.setFont(tp14);
                        j.setForeground(Color.WHITE);
                        j.setBackground(DBLUE);
                        j.setHorizontalAlignment(SwingConstants.CENTER);
                        j.setVerticalAlignment(SwingConstants.CENTER);
                        j.setAlignmentX(Component.CENTER_ALIGNMENT);
                        listPanel.add(j);
                        JLabel space = new JLabel(" ");
                        space.setMinimumSize(new Dimension(1, 10));
                        space.setHorizontalAlignment(SwingConstants.CENTER);
                        space.setVerticalAlignment(SwingConstants.CENTER);
                        listPanel.add(space);
                        height += 40;
                    }

                    if(height > 600){
                        listPanel.setPreferredSize(new Dimension(500, height));
                    }

                    /*if(listLength == 0){
                        listPanel.add(new JLabel("No one is currently online."));
                    }*/

                    add(centrePanel, BorderLayout.CENTER);
                    title.setText("Welcome " + stu_name + "! Who would you like to chat with?");
                    topPanel.add(title);
                    //bottomPanel.add(start_chatting);
                    bottomPanel.remove(quit);
                    bottomPanel.add(refresh);
                    bottomPanel.add(quit);
                    bottomPanel.add(start_chatting) ;          //Dom: Adding the chat button
                    start_chatting.setVisible(false);         //Dom: Button will only become availible when first name is selected
                }
            }

        }

        else if(button.equals("  REFRESH  ")) {
            //MESSAGE 2+3
        }

        else if(button.equals("  QUIT  ")){
            //exit function
            if(stu_name != null){ //if not, no connection was set up
                //MESSAGE 8.1, 8.2, 9
            }

            System.exit(0);
        }

        this.revalidate();
        this.repaint();
    }

    /////////////////////////////// MAIN ///////////////////////////////

    public static void main(String[] args){
        HomePage h = new HomePage();
        h.setVisible(true);

        try{
            DatagramSocket socket0 = new DatagramSocket();
        }
        catch(SocketException e){
            System.out.println("Socket error.");
            System.exit(0);
        }
    }

    //start chatting only come up with add button is pressed at least once - how do you manage who was pressed?
        //Connect to ChatWindow
    //Send Message (start chat, quit, I’m online, who is online)
    //Exit function (tell all chats to end, go offline, close all windows)
    //Other ActionListeners if any

    //FIRST THING: else find button text and add that to an arraylist to send!! :D
}
