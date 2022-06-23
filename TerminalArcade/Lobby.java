package TerminalArcade;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class Lobby{

    //https://stackoverflow.com/questions/9776465/how-to-visualize-console-java-in-jframe-jpanel
    public class TextAreaOutputStream extends OutputStream {

        private final JTextArea textArea;
        private final StringBuilder sb = new StringBuilder();
        private String title;
     
        public TextAreaOutputStream(final JTextArea textArea, String title) {
           this.textArea = textArea;
           this.title = title;
           sb.append(title + "> ");
        }
     
        @Override
        public void flush() {
        }
     
        @Override
        public void close() {
        }
     
        @Override
        public void write(int b) throws IOException {
     
           if (b == '\r')
              return;
     
           if (b == '\n') {
              final String text = sb.toString() + "\n";
              SwingUtilities.invokeLater(new Runnable() {
                 public void run() {
                    textArea.append(text);
                 }
              });
              sb.setLength(0);
              sb.append(title + "> ");
              return;
           }
     
           sb.append((char) b);
        }
    }
    
    private JFrame frame;
    private JTextArea textArea = new JTextArea(15, 30);
    private TextAreaOutputStream taOutputStream = new TextAreaOutputStream(
         textArea, "");
    public Lobby(){
        makeFrame("Terminal Arcade", 500); 
        frame.add(clientPanel(), BorderLayout.SOUTH);
        frame.add(chatPanel(), BorderLayout.WEST);
        frame.pack();
        
    }

    private JPanel chatPanel() {
        textArea.setEditable(false);
        JPanel chatPanel = new JPanel();

        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        System.setOut(new PrintStream(taOutputStream));
        Chat.send();
        return chatPanel;
    }

    public void makeFrame(String title, int size){
        frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(size, size);
        frame.setVisible(true);
    }

    public JPanel clientPanel(){
        JPanel clientPanel = new JPanel();
        clientPanel.setBackground(Color.BLACK);
        clientPanel.setLayout(new FlowLayout());

        JButton startHost = new JButton("Host Server");
        startHost.setFocusable(false);

        JButton startClient = new JButton("Join Server");
        startClient.setFocusable(false);

        JTextField replyField = new JTextField();
        replyField.setVisible(false);
        replyField.setPreferredSize(new Dimension(300, 50));
        
        JButton sendButton = new JButton("Send");
        sendButton.setFocusable(false);
        sendButton.setVisible(false);

        startHost.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Server.makeServer();
                    frame.setTitle("Terminal Arcade (Hosting)");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                startHost.setEnabled(false);
                startHost.setVisible(false);
                startClient.setEnabled(false);
                startClient.setVisible(false);
                replyField.setVisible(true);
                sendButton.setVisible(true);
            }
        });

        startClient.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client.makeClient();
                    frame.setTitle("Terminal Arcade (Joined Server)");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                startClient.setEnabled(false);
                startClient.setVisible(false);
                startHost.setEnabled(false);
                startHost.setVisible(false);
                replyField.setVisible(true);
                sendButton.setVisible(true);
            }
        });

        sendButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String msg = replyField.getText();
                Server.sendMessage(msg);
                replyField.setText(null);
            }
        });

        clientPanel.add(replyField);
        clientPanel.add(startHost);
        clientPanel.add(startClient);
        clientPanel.add(sendButton);
        return clientPanel;
    }
    public static void main(String[] args) {
        new Lobby();
    }
}
