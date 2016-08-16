package joeycumines.vetcare;

//import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

//import com.healthmarketscience.jackcess.*;

public class Main {
	public static void main(String args[]) throws IOException, SQLException, ClassNotFoundException {
		System.out.println("Main Thread");
	/*	DatabaseBuilder builder = new DatabaseBuilder();
		builder.setReadOnly(true);
		builder.setFile(new File("VetCare.mdb"));
		Database db = builder.open();
		Table table = db.getTable("Breed");
		for(Row row : table) {
			  System.out.println(row.toString());
		}*/
		Vetcare vetcare = new Vetcare("VetCare.mdb");
		
		System.out.println("starting");
		//System.out.println(vetcare.getClient(356289));
		//System.out.println(vetcare.getPatient(45257));
		LocalDateTime start = LocalDateTime.parse("2015-01-01T00:00:00");
		LocalDateTime end = LocalDateTime.parse("2015-02-01T00:00:00");
		//System.out.println(vetcare.getAppointments(start, end));
		//System.out.println(vetcare.getPatientReminderTypes());
		System.out.println("done");
	}
}
