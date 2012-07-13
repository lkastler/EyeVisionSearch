package eyevisionsearch.logic.events;

public class DollarEvent extends Event {

	protected DollarEvent(long time) {
		super(time);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "dollar";
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "dollar[t=" + getTime() + "]";
	}

}
