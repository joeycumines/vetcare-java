package joeycumines.vetcare;

//required for ucanaccess
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.lang.*;
import org.json.JSONObject;


/**
	This class wraps ucanaccess, and provides methods that can be accessed
	by our node.js front end, in order to perform database related operations.
*/
class Vetcare {
	/**
		Persistant connection to the database.
	*/
	private Connection conn = null;
	
	/**
		Constructs this object, which simply consists of initializing the db.
		parameters:
			- _path: path to the driver.
	*/
	public Vetcare(String _path) throws ClassNotFoundException, SQLException {
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		conn=DriverManager.getConnection("jdbc:ucanaccess://"+_path); 
	}
	
	/**
		Get a client row as a JSON string, given a client ID.
	*/
	public String getClient(long _clientId) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = 
				st.executeQuery("SELECT * FROM [Clients] WHERE [Client Id] =" +
				_clientId + ";");
		ResultSetMetaData rsmd = rs.getMetaData();
		while (rs.next()){
			JSONObject result = new JSONObject();
			for (int x = 1; x <= rsmd.getColumnCount(); x++) {
				result.put(rsmd.getColumnName(x), rs.getObject(x));
			}
			return result.toString();
		}
		
		return null;
	}
	
	/**
		Get a patient row as a JSON string, given a patient ID.
	*/
	public String getPatient(long _patientId) {
		return null;
	}
	
	/**
		Returns all of the appointments between _start and _end (inclusive),
		as a string encoded json.
		
		The format taken is the flat db table structure, extended with objects
		for the client and patient.
	*/
	public String getAppointments(LocalDateTime _start, LocalDateTime _end) {
		return null;
	}
	
	/**
		Returns all of the patient reminders between _start and _end (inclusive)
		as a string encoded json.
		
		The format is as the flat db table structure, extended with objects
		for the client and patient.
	*/
	public String getPatientReminders(LocalDateTime _start, 
			LocalDateTime _end) {
		return null;
	}
	
	/**
		Returns all the different types of patient reminders, from the relevant
		table in the database. The result is returned as a string encoded
		JSON array.
	*/
	public String getPatientReminderTypes() {
		return null;
	}
	
	private static final LocalDateTime AT_ZERO_VISIT_DATE = 
		LocalDate.parse("2016-08-10").atStartOfDay().minusDays(42592);
	
	/**
		Conversion between a visit date (float) and localdatetime.
	*/
	public static LocalDateTime convert_visitDateToLocalDateTime(double _input) {
		LocalDateTime result = AT_ZERO_VISIT_DATE;
		//separate day for temporal reliability
		long days = (long) Math.floor(_input);
		result = result.plusDays(days);
		//add the rest as seconds
		long seconds = (long) ((_input - Math.floor(_input)) * 86400);
		result = result.plusSeconds(seconds);
		return result;
	}
	
	/**
		Conversion between a LocalDateTime and a visit date (float)
	*/
	public static double convert_localDateTimeToVisitDate(LocalDateTime _input) {
		return DateHelper.calculateDaysBetweenLocalDateTime(AT_ZERO_VISIT_DATE, _input);
	}
	
	/**
		Returns all of the visits between _start and _end (inclusive) as a
		string encoded json.
		
		The format is as the flat db table structure, extended with objects
		for the client and the patient.
		
		The date field is parsed using the private conversion functions above.
	*/
	public String getVisits(LocalDateTime _start, LocalDateTime _end) {
		return null;
	}
}
