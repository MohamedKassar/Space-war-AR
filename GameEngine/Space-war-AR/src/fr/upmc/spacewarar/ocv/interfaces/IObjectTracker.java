package fr.upmc.spacewarar.ocv.interfaces;

public interface IObjectTracker {
	
	/**
	 * 
	 * @return the real position of the robot between 0 and 100 or negative value if error
	 */
	default int getPosition() {
		return -1;
	};
}
