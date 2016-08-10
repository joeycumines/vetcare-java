package joeycumines.vetcare;

//import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import com.healthmarketscience.jackcess.*;

public class Main {
	public static void main(String args[]) throws IOException, SQLException, ClassNotFoundException {
		System.out.println("Main Thread");
	/*	DatabaseBuilder builder = new DatabaseBuilder();
		builder.setReadOnly(true);
		builder.setFile(new File("Vetcare.mdb"));
		Database db = builder.open();
		Table table = db.getTable("Breed");
		for(Row row : table) {
			  System.out.println(row.toString());
		}*/
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		Connection conn=DriverManager.getConnection("jdbc:ucanaccess://Vetcare.mdb"); 
	}
}
