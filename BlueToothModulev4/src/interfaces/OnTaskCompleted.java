package interfaces;

public interface OnTaskCompleted {

	void onTaskCompleted(String result);
	
	void onProtocolCodeReceive(String result);
	
	void onSent(boolean ok);
	
}
