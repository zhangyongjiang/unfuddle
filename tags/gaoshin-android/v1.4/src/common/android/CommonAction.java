package common.android;

public enum CommonAction {
	SmsMessage,
	LocationChanged, 
	C2DM,
	Test,
	;
	
	public String getAction() {
		return this.getDeclaringClass().getName() + "." + this.name();
	}
}
