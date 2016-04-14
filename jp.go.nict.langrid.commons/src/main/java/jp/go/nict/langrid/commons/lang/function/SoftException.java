package jp.go.nict.langrid.commons.lang.function;

/**
 * Original idea of softening exception is from AspectJ.
 * @author nakaguchi
 *
 */
public class SoftException extends RuntimeException{
	public SoftException(Throwable t) {
		super(t);
	}

	private static final long serialVersionUID = -396334685436630389L;
}
