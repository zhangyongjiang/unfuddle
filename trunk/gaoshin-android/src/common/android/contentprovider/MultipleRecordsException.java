package common.android.contentprovider;

public class MultipleRecordsException extends RuntimeException {
	public MultipleRecordsException() {
	}
	
	public MultipleRecordsException(String msg) {
		super(msg);
	}
}
