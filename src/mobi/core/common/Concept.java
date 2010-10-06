package mobi.core.common;

import java.io.Serializable;

/**
 * This class represents an abstract idea. A concept is anything that needs an URI.
 * 
 * @author cajahyba
 *
 */
public class Concept implements Serializable {

	private static final long serialVersionUID = 7769304300353849479L;
	
	private Long id;
	
	private Boolean valid = true;
	
	private String uri;
	
	private String comment;
	
	public Concept() {
		
	}
	public Concept (String uri) {
		this.uri = uri;
		
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
    public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getValid() {
		return valid;
	}
	
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	public boolean equals(Object o) {
    	if (o == null) return false;
    	if (this == o) return true;
		if (getClass() != o.getClass()) return false;

        Concept oid = (Concept) o;
        if (this.getUri() == null || oid.getUri() == null)
            return super.equals(o);
        else
            return this.getUri().equals(oid.getUri());
    }
    
    public boolean equals(String o) {
    	if (o == null)          return false;
    	
    	if (this.equals(o))     return true;

        if (this.uri == null)   return false;
        
        if (this.uri.equals(o)) return true;
        
        return false;
    }

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getUri() == null) ? 0 : this.getUri().hashCode());
		return result;
	}
	
	public String toString() {
		return "Class Name: "+ this.getClass().getName() +" - URI:"+ this.uri;
	}

}
