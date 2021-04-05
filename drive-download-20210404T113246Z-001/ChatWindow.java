import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatWindow extends JFrame implements Runnable, ActionListener {
    String chatName;
    DatagramSocket chatSocket;
    int port;
    InetAddress address;
    JTextArea textField; //for actual chats
    JTextField typeBar; //for sending messages
    static JButton send;
    static JButton quit;

    private JPanel top;
    private JPanel centre;
    private JPanel bottom;
    private JPanel left;
    private JPanel right;

    private JScrollPane text;

    private JLabel groupName;
    private JLabel lspace;
    private JLabel rspace;
    private JLabel topSpace;

    String chatting = "";

    ArrayList<String> msgs = new ArrayList<String>();

    //public ChatWindow(String chatName, DatagramSocket chatSocket, int port, InetAddress address){
    public ChatWindow(String chatName, DatagramSocket chatSocket, int port){
        /////////////////////////////// HOME PAGE FRAME ///////////////////////////////

        super("Assignment 1 Chat Application");
        setSize(HomePage.WIDTH, HomePage.WIDTH);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(HomePage.LBLUE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        /////////////////////////////// INITIALISING VARIABLES ///////////////////////////////

        this.chatName = chatName;
        this.chatSocket = chatSocket;
        this.port = port;
        //this.address = address;

        /////////////////////////////// JPANELS ///////////////////////////////

        //Top
        top = new JPanel();
        top.setLayout(new BorderLayout());
        top.setBackground(HomePage.LBLUE);
        add(top, BorderLayout.NORTH);

        //Middle
        centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.PAGE_AXIS));
        centre.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));
        add(centre, BorderLayout.CENTER);

        //Bottom
        bottom = new JPanel();
        add(bottom, BorderLayout.SOUTH);
        bottom.setBackground(HomePage.LBLUE);

        //Left
        left = new JPanel();
        left.setBackground(HomePage.LBLUE);
        add(left, BorderLayout.WEST);

        //Right
        right = new JPanel();
        right.setBackground(HomePage.LBLUE);
        add(right, BorderLayout.EAST);

        /////////////////////////////// JLABELS ///////////////////////////////

        groupName = new JLabel(chatName);
        groupName.setFont(HomePage.tb20);
        groupName.setForeground(Color.BLACK);
        groupName.setHorizontalAlignment(JLabel.CENTER);
        groupName.setVerticalAlignment(JLabel.CENTER);
        top.add(groupName, BorderLayout.CENTER);

        lspace = new JLabel("                      ");
        rspace = new JLabel("                      ");
        topSpace = new JLabel(" ");
        left.add(lspace);
        right.add(rspace);
        top.add(topSpace, BorderLayout.WEST);

        /////////////////////////////// JTEXTFIELDS & JTEXTAREAS///////////////////////////////

        //for testing:
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");
        msgs.add("(03/04/2021 @ 11:55:00 GRNCLA009: Hello");
        msgs.add("(03/04/2021 @ 11:58:13) STVELI004: Hi");
        msgs.add("(03/04/2021 @ 12:01:05) JRDDOM002: Hey");

        for(String m : msgs){
            chatting += m + "\n\n";
        }

        textField = new JTextArea(chatting);

        typeBar = new JTextField(110);
        bottom.add(typeBar);

        /////////////////////////////// JSCROLLPANES ///////////////////////////////

        text = new JScrollPane(textField);
        centre.add(text);

        /////////////////////////////// JBUTTONS ///////////////////////////////

        send = new JButton("  SEND  ");
        send.setBackground(Color.RED);
        send.setForeground(Color.WHITE);
        send.setFont(HomePage.tp14);
        bottom.add(send);
        send.addActionListener(this);

        quit = new JButton(" QUIT ");
        quit.setBackground(Color.BLACK);
        quit.setForeground(Color.WHITE);
        quit.setFont(HomePage.tp14);
        top.add(quit, BorderLayout.EAST);
        quit.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();

        if(button.equals("  SEND  ")){
            //send packet through socket
            String message = typeBar.getText();
            if(message.length() > 0){ //to make sure that they actually typed something
                send.setBackground(Color.GRAY);
                //send here
            }
        }

        else if(button.equals(" QUIT ")){
            //quit group
            System.exit(0); //dispose when connected to other class and the main method is removed
        }

        revalidate();
        repaint();
    }

    public void run(){

    }

    public static void main(String[] args){ //remove after connecting it
        //String hostname = "What do I put here?";


        try{
            DatagramSocket socket = new DatagramSocket();
            //ChatWindow c = new ChatWindow("GROUP POEPHOL!", socket, 25, InetAddress.getByName(hostname));
            ChatWindow c = new ChatWindow("GROUP POEPHOL!", socket, 25);
            c.setVisible(true);
        }
        catch(SocketException e){
            System.out.println("Socket error.");
            System.exit(0);
        }
    }
}

//run method - receiving messages to add to arraylist msgs
//Action Listener for Sending a message, use send message function
//Leave
//Group members listed somewhere or nah?