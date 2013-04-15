package net.worldoftomorrow.noitem.features;

public abstract class CheckableNIFeature extends NIFeature {

	public CheckableNIFeature(String name, String message, boolean notify) {
		super(name, message, notify);
	}
	
	/**
	 * This method is used to check every player for compliance to this feature.
	 */
	public abstract void check();

}
