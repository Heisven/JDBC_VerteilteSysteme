import java.sql.*;
import java.text.*;

public class db {

	public static void main(String[] args) {
		Connection con;
		Statement stmt = null;
		ResultSet ut, dt;
		int id = 1;

		boolean passwort = false; // wird aus dem front end �bergeben
		String username = "test"; // wird aus dem front end �bergeben

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.print("Fehler: ");
		}

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "");

			try {
				stmt = con.createStatement();
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			ut = stmt.executeQuery("select * from usertabelle where nutzername='" + username + "';"); // tabellezeile
																										// des users
																										// wird geladen
			while (ut.next()) {
				String pw = ut.getString("passwort"); // passwort des users wird �bergeben
				id = ut.getInt("id"); // id des users wird �bergeben
				String mail = ut.getString("email"); // e mail des users wird �bergeben
				if (pw == "Passwort Front end") { // passwortabfrage
					passwort = true; // frontend eingabe des pw wird mit db abgeglichen
				}
			}

			dt = stmt.executeQuery("select * from dokumententabelle where id='" + id + "';"); // alle notizen mit
																								// zugeh�riger ID (id
																								// des angemeldeten
																								// nutzers) werden
																								// geladen
			String[][] notizen = new String[1000][3]; // array zum speichern der notizen, 3 spalten breit (titel,
														// inhalt, datum), bis zu 1000 eintr�ge
			int i = 0;
			while (dt.next()) { // wird solange wiederholt bis die Tabelle leer ist
				notizen[i][0] = dt.getString("title"); // Titel aus der Dokumententabelle
				notizen[i][1] = dt.getString("inhalt"); // Inhalt aus der Dokumententabelle

				Date d = dt.getDate("datum");
				DateFormat df = new SimpleDateFormat();
				String dateString = df.format(d); // Datum in einen String umwandeln, wegen Strng array
				notizen[i][2] = dateString;

				i++;
			}

			// neue notizen anlegen
			String title = ""; // eingabe aus dem Front end
			String inhalt = "";// eingabe aus dem front end
			stmt.addBatch("insert into dokumententabelle (title, datum, inhalt, eigentuemerid)  values('" + title
					+ "','2018-01-01' ,'" + inhalt + "'," + id + ");");
			stmt.executeBatch();

			// neue User anlegen
			String nutzername = ""; // eingabe aus dem frontend
			String passwort1 = ""; // eingabe aus dem frontend
			String email = ""; // eingabe aus dem frontend
			stmt.addBatch("INSERT INTO usertabelle (nutzername, passwort, email) VALUES ('" + nutzername + "','"
					+ passwort1 + "','" + email + "');");
			stmt.executeBatch();
			
			String id3="1"; //notizen id
			stmt.addBatch("delete from dokumententabelle where id=" + id3);
			stmt.executeBatch();
			
			String inhalt1 ="testtest";
			String titel="blabla";
			String dokumentenid="5";			
			stmt.addBatch("update dokumententabelle set title='"+titel+"',inhalt='"+inhalt1+"'where id="+dokumentenid);
			stmt.executeBatch();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}