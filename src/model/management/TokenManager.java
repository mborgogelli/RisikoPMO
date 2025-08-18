package model.management;

public abstract class TokenManager implements IManager{
	
	private Boolean isReady;
	
	protected TokenManager() {
		this.isReady = false;
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	};
	
	
	
	

}
