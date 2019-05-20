package fr.diginamic.demo_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConnexionJdbc {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConnexionJdbc.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ResourceBundle dbProperties = ResourceBundle.getBundle("db");
		String driverName = dbProperties.getString("database.driver");
		String url = dbProperties.getString("database.url");
		String user = dbProperties.getString("database.user");
		String password = dbProperties.getString("database.password");

		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);

			// insertion des données
			Statement statement = connection.createStatement();
			int nb = statement.executeUpdate("INSERT into article (ID, DESIGNATION, FOURNISSEUR, PRIX) "
					+ "values (1, 'premierArticle', 'Monsanto', 10.00)," + " (2,'deuxiemeArticle', 'Bayer', 0.50),"
					+ "(3, 'troisiemeArticle','Intermarché',25.00)," + "(4, 'quatriemeArticle','BASF',15.00);");

			statement.close();

			// augmentation de 15% du prix des articles
			Statement statement2 = connection.createStatement();
			int nb2 = statement2.executeUpdate("update article set PRIX = PRIX* 1.25 where prix > 10;");
			statement2.close();

			// affichage de tous les articles
			Statement statement3 = connection.createStatement();
			ResultSet curseur = statement3.executeQuery("select * from article;");
			while (curseur.next()) {
				int id = curseur.getInt("ID");
				String designation = curseur.getString("DESIGNATION");
				String fournisseur = curseur.getString("FOURNISSEUR");
				Float prix = curseur.getFloat("PRIX");
				System.out.println(id + " - " + designation + " -Fournisseur : " + fournisseur + ", prix : " + prix);

			}

			curseur.close();
			statement3.close();

			// affichage de la moyenne des prix des articles
			Statement statement4 = connection.createStatement();
			ResultSet curseur2 = statement4.executeQuery("SELECT AVG(PRIX) AS MOYENNE FROM ARTICLE;");
			if (curseur2.next()) {
				float moyenne = curseur2.getFloat("MOYENNE");
				System.out.println("Moyenne des prix de la table : " + moyenne);
			}

			curseur2.close();
			statement4.close();

			// suppression de tous les articles
			Statement statement5 = connection.createStatement();
			int nb3 = statement5.executeUpdate("delete from article;");

			statement5.close();

		} catch (SQLException e) {
			LOGGER.error("Erreur SQL");
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
