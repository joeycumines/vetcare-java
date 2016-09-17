package joeycumines.vetcare;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

/**
 * This class is a serializable class that encapsulates accessing and managing
 * the application.
 * 
 * @author Joey
 */
public class MessageAPI implements Serializable {
	
	private boolean stop;

	private JSONObject lastError;

	/**
	 * v1
	 */
	private static final long serialVersionUID = -2844958467393352664L;

	/**
	 * All of the account details, stored in a hashmap for usage.
	 */
	private ConcurrentHashMap<String, Account> accounts;

	/**
	 * Constructor, we initialize everything.
	 */
	public MessageAPI() {
		accounts = new ConcurrentHashMap<String, Account>();
		lastError = null;
		stop = false;
	}
	
	public void stopWorker() {
		stop = true;
	}
	public boolean shouldStop() {
		return stop;
	}

	public Account getAccount(String _userName) {
		return accounts.get(_userName);
	}

	public String[] getAccountNames() {
		String[] result = new String[accounts.size()];
		int x = 0;
		for (Map.Entry<String, Account> entry : accounts.entrySet()) {
			result[x] = entry.getKey();
			x++;
		}
		return result;
	}

	public boolean getAccountExists(String _userName) {
		return _userName != null && !_userName.isEmpty() && this.getAccount(_userName) != null;
	}

	/**
	 * Uses lastError.
	 * 
	 * @param _userName
	 * @param _password
	 * @param _fullName
	 * @param _isAdmin
	 * @return
	 */
	public boolean createAccount(String _userName, String _password, String _fullName, boolean _isAdmin) {
		// validate the info
		if (_userName == null || _userName.isEmpty() || this.getAccountExists(_userName) || _password == null
				|| _password.isEmpty() || _fullName == null || _fullName.isEmpty()) {
			this.setLastError(400, "Bad input.");
			return false;
		}

		// check for a valid format of username
		String pattern = "^[\\p{IsAlphabetic}\\p{IsDigit}]+$";
		if (!_userName.matches(pattern)) {
			this.setLastError(400, "Username may only be alphanumerical, no spaces etc.");
			return false;
		}

		// create account
		this.accounts.put(_userName, new Account(_userName, _fullName, _isAdmin));

		// set the password of the account
		this.getAccount(_userName).setPassword(_password);

		return true;
	}

	public void deleteAccount(String _userName) {
		this.accounts.remove(_userName);
	}

	/**
	 * Returns an account with a api key. If it returns null, the exact problem
	 * can be retrieved with getLastError().
	 * 
	 * @param _apiKey
	 * @return
	 */
	public Account getAccountAPI(String _apiKey) {
		// first we decode
		String decoded = Crypto.fromBase64(_apiKey);
		// now, split at the :
		int del = decoded.indexOf(":");
		if (del < 0) {
			this.setLastError(400, "Malformed API Key.");
			return null;
		}
		String userName = decoded.substring(0, del);
		String apiKey = decoded.substring(del + 1);
		if (userName.isEmpty() || apiKey.isEmpty() || !this.getAccountExists(userName)) {
			this.setLastError(400, "Username does not exist.");
			return null;
		}
		// get the account we are working on
		Account acc = this.getAccount(userName);
		// we check to see if we can authenticate using a session token
		if (acc.getSession(apiKey) != null)
			return acc;
		// we check to see if we can authenticate using username and password
		if (acc.getPasswordMatches(apiKey))
			return acc;
		this.setLastError(401, "Bad or expired API Key.");
		return null;
	}

	private void setLastError(int _code, Object _content) {
		JSONObject temp = new JSONObject();
		temp.put("code", _code);
		temp.put("content", _content);
		lastError = temp;
	}

	public JSONObject getLastError() {
		return lastError;
	}
}
