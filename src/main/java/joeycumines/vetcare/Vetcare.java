package joeycumines.vetcare;

//required for ucanaccess
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


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
}
