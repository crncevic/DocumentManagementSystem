package rs.netset.training.exception;

public class NetSetException extends RuntimeException {

	ErrorCode code;

	public NetSetException(String message, Throwable throwable, ErrorCode code) {
		super(message, throwable);
		this.code = code;
	}

}
