

import java.io.Serializable;

public abstract class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8208308082738524066L;
	
	protected Integer userID = null;
	protected Integer clientID = null;
}
