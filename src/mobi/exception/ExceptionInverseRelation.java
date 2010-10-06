package mobi.exception;

import java.io.Serializable;

public class ExceptionInverseRelation extends Exception implements
		Serializable {

	private static final long serialVersionUID = -7885559854202123981L;

	public ExceptionInverseRelation(String exception) {
		super(exception);
	}
}
