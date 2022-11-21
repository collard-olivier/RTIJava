package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class App_CROOM extends JDialog {

    private JPanel panelCROOM;
    private JTextField textFieldID;
    private JButton buttonSupprimer;
    private JLabel labelExplication;

    /*public App_CROOM(Socket s, DataOutputStream dos, DataInputStream dis) {

        buttonSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!textFieldID.equals("")) {
                        dos.writeUTF("CROOM");
                        //DEMANDE AU CLIENT L'ID POUR SUPPRIMER LA RESEVATION CORRESPONDANTE
                        System.out.println("id de la reservation : ");
                        String id = textFieldID.getText();
                        dos.writeUTF(id);
                        JOptionPane.showMessageDialog(null, dis.readUTF(), "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        labelExplication.setText("Veuillez entrer un id");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                App_CROOM.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelCROOM);
        this.pack();
    }*/

    public App_CROOM(Socket s, ObjectOutputStream dos, ObjectInputStream dis) {

        buttonSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!textFieldID.getText().equals("")) {
                        dos.writeObject("CROOM");
                        //DEMANDE AU CLIENT L'ID POUR SUPPRIMER LA RESEVATION CORRESPONDANTE
                        System.out.println("id de la reservation : ");
                        String id = textFieldID.getText();
                        dos.writeObject(id);
                        JOptionPane.showMessageDialog(null, dis.readObject(), "Alert", JOptionPane.WARNING_MESSAGE);
                        App_CROOM.super.dispose();
                    } else {
                        labelExplication.setText("Veuillez entrer un id");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelCROOM);
        this.pack();
    }
}