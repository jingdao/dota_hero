package mechanics;


public class Status {
	public enum StatusType {
		STUN,SLOW,SLEEP,SILENCE,DISARM,POISON
	}	

	public StatusType type;
	public int duration;

	public Status(StatusType type,int duration) {
		this.type = type;
		this.duration = duration;
	}
	
}
