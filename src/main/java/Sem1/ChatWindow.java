package Sem1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


/*
Создать окно клиента чата. Окно должно содержать JtextField для ввода логина, пароля, IP-адреса сервера,
порта подключения
к серверу, область ввода сообщений, JTextArea область просмотра сообщений чата и JButton подключения к серверу
и отправки сообщения в чат. Желательно сразу сгруппировать компоненты, относящиеся
к серверу сгруппировать на JPanel сверху экрана, а компоненты, относящиеся к отправке сообщения – на JPanel снизу
 */

public class ChatWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_POSX = 500;
    private static final int WINDOW_POSY = 100;
    JPanel panel = new JPanel(new GridLayout(1, 2));
    JLabel jlb1 = new JLabel("Введите логин: ");
    JTextField jtf1 = new JTextField();

    JPanel panel2 = new JPanel(new GridLayout(1, 2));
    JLabel jlb2 = new JLabel("Введите пароль: ");
    JTextField jtf2 = new JTextField();

    JPanel panel3 = new JPanel(new GridLayout(1, 2));
    JLabel jlb3 = new JLabel("Введите IP адрес сервера: ");
    JTextField jtf3 = new JTextField();

    JPanel panel4 = new JPanel(new GridLayout(1, 2));
    JLabel jlb4 = new JLabel("Введите номер порта: ");
    JTextField jtf4 = new JTextField();

    JButton btnLogin = new JButton("Подключиться");
    JTextArea textChat = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textChat);
    JPanel panelMain = new JPanel(new GridLayout(10, 1));

    JLabel jlb6 = new JLabel("Введите ваше сообщение: ");
    JTextArea chatMessage = new JTextArea();
    JButton pushMsg = new JButton("Отправить сообщение");
//    String logChat = "";
//    char[] bufferLog;

    public ChatWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("GB_chat");
        setResizable(true);
        setVisible(true);

        panel.setLayout(new GridLayout(1, 1));
        panel.add(jlb1);
        panel.add(jtf1);
        panel2.add(jlb2);
        panel2.add(jtf2);
        panel3.add(jlb3);
        panel3.add(jtf3);
        panel4.add(jlb4);
        panel4.add(jtf4);
        panelMain.add(panel);
        panelMain.add(panel2);
        panelMain.add(panel3);
        panelMain.add(panel4);
        btnLogin.addActionListener(e -> readChatHistoryFromFile());
        panelMain.add(btnLogin);
        textChat.setEditable(false);
        panelMain.add(scrollPane);

        panelMain.add(jlb6);
        panelMain.add(chatMessage);
        panelMain.add(pushMsg);
        add(panelMain);
        pushMsg.addActionListener(e -> sendMessage());
    }
/**
 * Читает историю чата из файла "chat.txt" и отображает ее в текстовом поле.
 */
    private void readChatHistoryFromFile() {
        try (FileReader fr = new FileReader("chat.txt"); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                textChat.append(line + "\n");
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
/**
 * Отправляет сообщение, отображает его в текстовом поле и сохраняет в файл "chat.txt".
 */
    private void sendMessage(){
        String message = chatMessage.getText();
        textChat.append("Вы: " + message + "\n");
        chatMessage.setText("");
        try (FileWriter writer = new FileWriter("chat.txt", true)) {
            writer.write("Вы: " + message + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
