package org.drdevelopment.webtool.exception;

public class TechnicalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TechnicalException() {
		super();
	}

	public TechnicalException(String message) {
		super(message);
	}

	public TechnicalException(String message, Object... errorValues) {
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
