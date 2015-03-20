package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class JavaCompositeServiceModel extends CompositeServiceModel {
	/**
	 * 
	 * 
	 */
	public void setSourceCodeUrl(String sourceCodeUrl) {
		this.sourceCodeUrl = sourceCodeUrl;
	}

	/**
	 * 
	 * 
	 */
	public String getSourceCodeUrl() {
		return sourceCodeUrl;
	}

	private String sourceCodeUrl;

	private static final long serialVersionUID = -5951853046840302109L;
}
