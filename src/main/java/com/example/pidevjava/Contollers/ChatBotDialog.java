package com.example.pidevjava.Contollers;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.Color;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Random;

public class ChatBotDialog extends JFrame implements KeyListener {

    JPanel p = new JPanel();
    JTextArea dialog = new JTextArea(20, 50);
    JTextArea input = new JTextArea(1, 50);
    JScrollPane scroll = new JScrollPane(
            dialog,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    HashMap<String, String[]> chatBot = new HashMap<>();

    public static void main(String[] args) {
        new ChatBotDialog();
    }

    public ChatBotDialog() {
        super("Chat Bot");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dialog.setEditable(false);
        input.addKeyListener(this);

        p.add(scroll);
        p.add(input);
        p.setBackground(new Color(30, 112, 154));
        add(p);

        // Initialize the chatBot HashMap
        initializeChatBot();

        setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            input.setEditable(false);
            // -----grab quote-----------
            String quote = input.getText();
            input.setText("");
            addText("-->You:\t" + quote);
            quote = quote.toLowerCase().trim();
            byte response = 0;
            /*
             * 0: we're searching through chatBot for matches 1: we didn't find anything 2: we did find something
             */
            // -----check for matches----
            for (String keyword : chatBot.keySet()) {
                if (quote.contains(keyword)) {
                    response = 2;
                    String[] availableResponses = chatBot.get(keyword);
                    int r = new Random().nextInt(availableResponses.length);
                    addText("\n-->Chat\t" + availableResponses[r]);
                    break;
                }
            }

            // -----default--------------
            if (response == 0) {
                String[] defaultResponses = chatBot.get("default");
                int r = new Random().nextInt(defaultResponses.length);
                addText("\n-->Chat\t" + defaultResponses[r]);
            }
            addText("\n");
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            input.setEditable(true);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void addText(String str) {
        dialog.setText(dialog.getText() + str);
    }

    public void initializeChatBot() {
        chatBot.put("hi", new String[]{"Hi there!", "Hello!", "Hola!", "Hey!", "Howdy!"});
        chatBot.put("cv ?", new String[]{"I'm doing well, thank you!", "I'm great!"});
        chatBot.put("who are you ?", new String[]{"I'm chatbot how can i help you ?", "I am ChatBot, an AI language model developed by Eya"});
        chatBot.put("merci", new String[]{"No problem!", "Alright then.", "Sure."});
        chatBot.put("default", new String[]{"Repeter s'il te plait je ne comprends pas"});
        chatBot.put("shut up", new String[]{"on tolere pas ce genre de langage"});
        chatBot.put("noob", new String[]{"on tolere pas ce genre de langage"});
        chatBot.put("je demande la liste des programmes", new String[]{"Vous pouvez acceder a notre page d'acceuil pour découvrir nos programmes diponibles"});
        chatBot.put("est ce que l'hotel est inclus dans le voyage", new String[]{"Vous pouvez vérifier la disponibilité des hotels dans le champ hotel."});
        chatBot.put("comment reserver un voyage", new String[]{"Vous pouvez réserver sur l'application ou bien nous contacter sur notre numéro 97033612."});
    }
}