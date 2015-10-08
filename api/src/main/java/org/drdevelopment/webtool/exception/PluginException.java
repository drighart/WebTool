package org.drdevelopment.webtool.exception;

public class PluginException extends Exception {
	private static final long serialVersionUID = 1L;

	public PluginException() {
		super();
	}

    public PluginException(String message) {
        super(message);
    }

    public PluginException(Throwable throwable) {
		super(throwable);
	}

	public PluginException(String message, Object... errorValues) {
		this(parse(message, errorValues));
	}

    /**
     * Parses everything between {} in the error messages.
     *
     * @param errorValues a list of objects which represents the parameters to put in the error message.
     */
    private static String parse(String message, Object... errorValues) {
        if (message != null) {
        	for (Object errorValue : errorValues) {
        		message = message.replaceFirst("\\{\\}", errorValue.toString());
        	}
        }
        return message;
    }

}
