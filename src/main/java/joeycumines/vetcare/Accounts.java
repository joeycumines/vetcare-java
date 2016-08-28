package joeycumines.vetcare;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a serializable class that encapsulates accessing and managing
 * user information. These accounts are used for logging purposes, and login
 * purposes. The application itself is global, except for account management,
 * that requires admin powers.
 * 
 * @author Joey
 */
public class Accounts implements Serializable {

	/**
	 * v1
	 */
	private static final long serialVersionUID = -2844958467393352664L;

	/**
	 * All of the account details, stored in a hashmap for usage.
	 */
	private HashMap<String, Account> accounts;

	/**
	 * Constructor, we initialize everything.
	 */
	public Accounts() {
		accounts = new HashMap<String, Account>();
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

	public boolean createAccount(String _userName, String _password, String _fullName, boolean _isAdmin) {
		// validate the info
		if (_userName == null || _userName.isEmpty() || this.getAccountExists(_userName) || _password == null
				|| _password.isEmpty() || _fullName == null || _fullName.isEmpty())
			return false;
		
		//create account
		this.accounts.put(_userName, new Account(_userName, _fullName, _isAdmin));
		
		//set the password of the account
		this.getAccount(_userName).setPassword(_password);
		
		return true;
	}
	
	public void deleteAccount(String _userName) {
		this.accounts.remove(_userName);
	}
}
