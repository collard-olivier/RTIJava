package Windows;

import Classe.Chambre;
import Classe.ReserActCha;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class App_BROOM extends JDialog{

    private JPanel panelBROOM;
    private JComboBox comboBoxCategorie;
    private JComboBox comboBoxTypeChambre;
    private JTextField textFieldDateArrivee;
    private JTextField textFieldNbNuits;
    private JTextField textFieldNomClient;
    private JButton buttonLancerRecherche;
    private JTable tableAffichage;
    private JButton buttonValiderResa;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();
    DefaultComboBoxModel comboBoxModel_Categorie = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBoxModel_TypeChambre = new DefaultComboBoxModel();
    int verificationBoutonRech;

    public App_BROOM(Socket s, ObjectOutputStream oos, ObjectInputStream ois){

        verificationBoutonRech =0;
        initializeComboBox();
        buttonLancerRecherche.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //REQUETE BROOM
                if (verificationBoutonRech == 0) {
                    JTable_Affichage.setRowCount(0);
                    JTable_Affichage.setColumnCount(2);
                    Vector V = new Vector<>();
                    V.add("Numero Chambre");
                    V.add("Prix de la chambre");
                    JTable_Affichage.addRow(V);

                    try {
                        oos.writeObject("BROOM");
                        //DEMANDE DES INFOS AU CLIENT
                        ReserActCha reservationChambre = new ReserActCha();
                        reservationChambre.set_categorie(comboBoxCategorie.getSelectedItem().toString());
                        reservationChambre.set_typeCha(comboBoxTypeChambre.getSelectedItem().toString());
                        reservationChambre.set_nbNuits(Integer.parseInt(textFieldNbNuits.getText()));
                        reservationChambre.set_date(textFieldDateArrivee.getText());
                        reservationChambre.set_persRef(textFieldNomClient.getText());
                        //ENVOI DES INFOS AU SERVEUR SOUS FORME DE STRING
                        oos.writeObject(reservationChambre);

                        //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                        Chambre chambre;
                        int compteur = 0;//sert a savoir si on a au moins un resultat
                        while (true) {
                            chambre = (Chambre) ois.readObject();
                            if (chambre == null)
                                break;
                            else {
                                Vector v = new Vector();
                                v.add(chambre.get_numeroChambre());
                                v.add(chambre.get_prixHTVA());
                                JTable_Affichage.addRow(v);
                                compteur++;
                            }
                        }
                        if(compteur == 0) {
                            oos.writeObject("Aucune");
                        }
                        else {
                            oos.writeObject("OK");
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    tableAffichage.setModel(JTable_Affichage);
                    verificationBoutonRech = 1;
                }
            }
        });

        buttonValiderResa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //DEMANDE AU CLIENT LA CHAMBRE QU'IL SOUHAITE ET AUSSI LE PRIX CAR PAS ENREGISTRE TANT QUE PAS D'INTERFACE
                try {
                    if (tableAffichage.getSelectedRow() != -1) {
                        Chambre chambre = new Chambre();
                        chambre.set_numeroChambre((Integer) tableAffichage.getValueAt(tableAffichage.getSelectedRow(), 0));
                        chambre.set_prixHTVA((Float) tableAffichage.getValueAt(tableAffichage.getSelectedRow(), 1));
                        oos.writeObject(chambre);
                        String confirmation = (String) ois.readObject();

                        if (confirmation.equals("NOK")) {
                            JOptionPane.showMessageDialog(null, "Erreur R??servation", "Alert", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "R??servation accept??e", "Alert", JOptionPane.WARNING_MESSAGE);
                            ReserActCha reservation = (ReserActCha) ois.readObject();
                            System.out.println(reservation);
                        }
                        verificationBoutonRech = 0;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                App_BROOM.super.dispose();
            }
        });


        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelBROOM);
        this.pack();
    }

    public void initializeComboBox(){

        comboBoxModel_Categorie.addElement("Motel");
        comboBoxModel_Categorie.addElement("Village");
        this.comboBoxCategorie.setModel(comboBoxModel_Categorie);

        comboBoxModel_TypeChambre.addElement("Simple");
        comboBoxModel_TypeChambre.addElement("Double");
        comboBoxModel_TypeChambre.addElement("Familiale");
        this.comboBoxTypeChambre.setModel(comboBoxModel_TypeChambre);
    }
}
