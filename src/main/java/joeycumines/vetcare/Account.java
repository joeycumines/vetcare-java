package joeycumines.vetcare;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A user account.
 * 
 * @author Joey
 *
 */
public class Account implements Serializable {

	/**
	 * v1
	 */
	private static final long serialVersionUID = 8815099369493845545L;

	private String fullName;
	private String passHash;
	private String passSalt;
	private String userName;
	private boolean isAdmin;
	private ConcurrentHashMap<String, Session> sessions;

	public Account(String _userName, String _fullName, boolean _isAdmin) {
		userName = _userName;
		fullName = _fullName;
		passHash = null;
		passSalt = null;
		isAdmin = _isAdmin;
		sessions = new ConcurrentHashMap<String, Session>();
	}

	public void setFullName(String _fullName) {
		fullName = _fullName;
	}

	public boolean setPassword(String _password) {
		passSalt = Crypto.nextSessionId();
		passHash = Crypto.get_SHA_512_SecurePassword(_password, passSalt);
		return passHash != null;
	}

	public String getFullName() {
		return fullName;
	}

	public boolean getPasswordMatches(String _password) {
		if (passHash == null || passSalt == null)
			return false;
		return passHash.equals(Crypto.get_SHA_512_SecurePassword(_password, passSalt));
	}

	public String getUserName() {
		return userName;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean _isAdmin) {
		isAdmin = _isAdmin;
	}

	/**
	 * Removes all expired keys from the sessions
	 */
	public void cleanSessions() {
		Vector<String> toRemove = new Vector<String>();
		LocalDateTime now = LocalDateTime.now();
		for (Map.Entry<String, Session> entry : sessions.entrySet()) {
			if (entry.getValue().getExpiresAfter().isBefore(now))
				toRemove.add(entry.getKey());
		}
		for (String s : toRemove) {
			sessions.remove(s);
		}
	}

	/**
	 * Returns null if the key is not valid or expired.
	 * 
	 * @param _apiKey
	 * @return
	 */
	public Session getSession(String _apiKey) {
		this.cleanSessions();
		// now we just need to return the key
		return this.sessions.get(_apiKey);
	}
	
	
	/**
	 * Create a new session for this user. Temporary session will be useable as a password for
	 * API authentication. _liveForSeconds must be positive, though this is not checked.
	 * @param _liveForSeconds
	 * @return
	 */
	public Session addSession(long _liveForSeconds) {
		this.cleanSessions();
		Session sess = new Session(Crypto.nextSessionId(), _liveForSeconds);
		sessions.put(sess.getSessionId(), sess);
		return sess;
	}
	
	/**
	 * Removes a session from the object.
	 * @param _apiKey
	 */
	public void removeSession(String _apiKey) {
		this.sessions.remove(_apiKey);
	}

	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		aInputStream.defaultReadObject();
		//clear all non valid sessions
		this.cleanSessions();
	}

	private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
		//clear all non valid sessions
		this.cleanSessions();
		aOutputStream.defaultWriteObject();
	}
}
