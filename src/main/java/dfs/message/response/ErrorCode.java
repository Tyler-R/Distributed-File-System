package main.java.dfs.message.response;

public enum ErrorCode {
	ACK ("0"),
	ASK_RESEND ("0"),
	INVALID_TRANSACTION_ID ("201"),
	INVALID_OPERATION ("202"),
	FILE_IO_ERROR ("205"),
	FILE_NOT_FOUND ("206");
	
	private final String code;
	
	ErrorCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
}
