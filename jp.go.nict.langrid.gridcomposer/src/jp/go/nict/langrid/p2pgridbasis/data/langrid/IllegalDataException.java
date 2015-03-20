package jp.go.nict.langrid.p2pgridbasis.data.langrid;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class IllegalDataException extends Exception {
	private static final long serialVersionUID = 2751012069299324533L;

	public IllegalDataException(Exception e) {
		super(e);
	}
}
