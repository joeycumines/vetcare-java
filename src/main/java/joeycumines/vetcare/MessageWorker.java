package joeycumines.vetcare;

import java.util.logging.Logger;

public class MessageWorker extends SimpleProcess {
	private MessageAPI api;

	public MessageWorker(String _procName, Logger _logger, MessageAPI _api) {
		super(_procName, _logger);
		api = _api;
	}

	@Override
	public void abRun() throws Exception {
		while (!api.shouldStop()) {
			
			//do some sleeping
			this.sleep();
		}
	}

}
