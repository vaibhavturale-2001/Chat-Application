
import java.io.*;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class Server extends JFrame {
    
    ServerSocket server;
    Socket socket;

    BufferedReader br;              //for reading (It read Input Stream )
    PrintWriter out;                //for writing (It read Output Stream)

    // Declare Components
    private JLabel heading=new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", 20);


    // Constructor..
    public Server(){
        try{
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");

            socket=server.accept();                 //object of client inside the socket

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

             
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e){
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
                // System.out.println("Key released"+ e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("You have pressed enter  button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Server :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText(""); 
                    messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI(){
        // code of creating GUI
        this.setTitle("Server Messager[END]");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        // code for component
        heading.setFont(getFont());
        messageArea.setFont(getFont());
        messageInput.setFont(getFont());

        // heading.setIcon(new ImageIcon("new-chat.png"));
        // heading.setVerticalTextPosition(SwingConstants.CENTER);
        // heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // code for setting of frame layout
        this.setLayout(new BorderLayout());
        // code for adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
    }

    public void startReading(){
        // this thread will read and give the data

        Runnable r1 = () ->{
            System.out.println("Reader Started...");

            try{
            while(true){
                
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client : "+msg);            //for console
                    messageArea.append("Client : "+msg+"\n"); 
            }
             }catch(Exception e){
            // e.printStackTrace();
            System.out.println("connection closed");
        }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        // User take the data and send it to the client
        Runnable r2= () ->{
            System.out.println("Writer Started..");

            try{
            while(true && !socket.isClosed()){
               
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();

                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
            }
            // System.out.println("connection closed");
        }catch(Exception e){
            e.printStackTrace();
        }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println(" This is Server....Going to start server");
        new Server();
    }
}
