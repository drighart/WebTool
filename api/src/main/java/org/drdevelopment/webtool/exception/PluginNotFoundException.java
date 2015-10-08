package org.drdevelopment.webtool.exception;

public class PluginNotFoundException extends PluginException {

	public PluginNotFoundException() {
		super();
	}

    public PluginNotFoundException(String message) {
        super(message);
    }

    public PluginNotFoundException(Throwable throwable) {
		super(throwable);
	}

	public PluginNotFoundException(String message, Object... errorValues) {
		super(message, errorValues);
	}

}
