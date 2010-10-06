package mobi.exception;

import java.io.Serializable;

public class ExceptionSymmetricRelation extends Exception implements
		Serializable {

	private static final long serialVersionUID = 7600091455501846474L;

	public ExceptionSymmetricRelation(String exception) {
		super(exception);
	}
}
