package database.facility;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanReservation extends BD_Bean implements Serializable {

    public BeanReservation(String string, String user, String pwd) throws SQLException
    {
        super(string,user,pwd);
    }

    @Override
    public synchronized ResultSet Login() throws SQLException{
        //on récupère tous les utilisateurs, utilise la methode de BD_Bean
        return super.Login();
    }

    @Override
    public synchronized ResultSet RequestLROOMS(String date) throws SQLException
    {
        /*
        * Permet de recuperer toutes les chambres réservés dont la date est egale ou superieur a la date d'aujourd'hui
        */
        String colonnes = "*";
        String query = "Select <columns> from ReserActCha ";

        System.out.println("date = " + date);

        //A MODIFIER

        query = query + "where <cond>";

        setCondition("DATE(dateDeb) >= " + date + " AND " + "typeCha IS NOT NULL"); //"AND type = 'Chambre'" pourrait marcher aussi

        query = query + ";";

        String SQL = query.replaceAll("<columns>", colonnes).replaceAll("<cond>",getCondition());
        System.out.println("SQL = " + SQL);
        PreparedStatement pStmt = this.getConnection().prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Requête : "+SQL);

        return pStmt.executeQuery();
    }

    @Override
    public synchronized ResultSet RequestBROOM(String categorie, String typeChambre, String dateDeb, String dateFin) throws SQLException
    {
        /*
         * Permet de recuperer toutes les chambres libre pour la tranche de date que l'utilisateur a demander
         */
        /*
        REQUETE testee qui marche dans mysql, a readapter au code

        select * from chambre
          where categorie = 'Motel' and typeChambre = 'Simple' and numeroChambre
          NOT IN (select numChambre from reseractcha
			where type ='Chambre' and date(datedeb) between '2023-02-11' and '2023-02-12');
        */

        String colonnes = "*";
        String query = "Select * from chambre ";

        query = query + "where ";

        if(!categorie.equals("")) {
            query = query + "categorie = '<categorie>' ";
        }
        if(!typeChambre.equals("")) {
            if(!categorie.equals(""))
                query = query + "AND ";
            query = query + "typeChambre = '<typeChambre>' ";
        }
        if(!categorie.equals("") || !typeChambre.equals(""))
            query = query + "AND ";

        query = query + "numeroChambre NOT IN (select numChambre from reseractcha where type ='Chambre' and date(datedeb) between '<dateDeb>' and '<dateFin>');";

        String SQL = query.replaceAll("<columns>", colonnes).replaceAll("<categorie>",categorie)
                .replaceAll("<typeChambre>",typeChambre).replaceAll("<dateDeb>",dateDeb).replaceAll("<dateFin>",dateFin);
        System.out.println("SQL = " + SQL);
        PreparedStatement pStmt = this.getConnection().prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Requête : "+SQL);

        return pStmt.executeQuery();
    }

    @Override
    public synchronized int Insert() throws SQLException
    {
        /*
         * Permet d'ajouter ce que le client a demander comme chambre, utilise la methode de BD_Bean
         */
        return super.Insert();
    }
}
