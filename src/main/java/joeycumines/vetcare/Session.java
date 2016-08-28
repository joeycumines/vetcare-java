package joeycumines.vetcare;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author Joey
 */
public class Session implements Serializable {
	/**
	 * v1
	 */
	private static final long serialVersionUID = 4701976089208280670L;
	
	private String sessionId;
	/**
	 * The last moment when this is valid for.
	 */
	private LocalDateTime expiresAfter;
	
	/**
	 * 
	 * @param _sessionId
	 * @param _liveForSeconds
	 * -1 or less means it will never expire.
	 */
	public Session(String _sessionId, long _liveForSeconds) {
		if (_liveForSeconds >= 0) {
			//will expire
			expiresAfter = LocalDateTime.now().plusSeconds(_liveForSeconds);
		} else {
			//will not expire
			expiresAfter = null;
		}
		sessionId = _sessionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public LocalDateTime getExpiresAfter() {
		return expiresAfter;
	}
}
