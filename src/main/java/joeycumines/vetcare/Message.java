package joeycumines.vetcare;

import java.time.LocalDateTime;

import org.json.JSONObject;

/**
 * Encapsulates a message that has been extracted or otherwise queued, and is pending to send.
 * @author Joey
 *
 */
public class Message {
	//the message object fields
	/**
	 * A title used to give a brief overview of the message.
	 */
	public String title;
	/**
	 * Flag that is required to be true, to send the message.
	 */
	public boolean send;
	/**
	 * The LocalDateTime that is the first moment this message is valid from.
	 */
	public LocalDateTime validFrom;
	/**
	 * The localDateTime that is the last moment this message is valid to.
	 */
	public LocalDateTime validTo;
	/**
	 * A string key that represents what method will be used to send this message.
	 * 
	 * Valid values:
	 * 	- email
	 * 	- sms
	 */
	public String vector;
	
	/**
	 * A string key, this should be the email address or the phone number.
	 * 
	 * The phone number only supports 0412345678 formatted numbers.
	 * While the field is public, we provide a setter for consistency.
	 */
	public String address;
	
	/**
	 * A string key representing the category for this message. Used for superficial purposes, eg sorting.
	 */
	public String category;
	
	/**
	 * Row information, from the database. Display to give the user a understanding of the reasons for sending.
	 */
	public JSONObject info;
	
	/**
	 * Construct a message.
	 */
	public Message(String _title, LocalDateTime _validFrom, LocalDateTime _validTo, String _vector, String _address, String _category, JSONObject _info) {
		title = _title;
		send = false;
		validFrom = _validFrom;
		validTo = _validTo;
		vector = _vector;
		address = _address;
		category = _category;
		info = _info;
	}
	
	/**
	 * Use to set the address to a mobile number, performs validation and returns true if invalid.
	 * @param _number
	 */
	public boolean setMobileNumber(String _number) {
		if (_number.length() != 10)
			return false;
		for (int x = 0; x < _number.length(); x++) {
			if (x == 0 && _number.charAt(x) != '0')
				return false;
			else if (x == 1 && _number.charAt(x) != '4')
				return false;
			else if (_number.charAt(x) < '0' || _number.charAt(x) > '9')
				return false;
		}
		//we get here then is valid format, set
		this.address = _number;
		return true;
	}
}
