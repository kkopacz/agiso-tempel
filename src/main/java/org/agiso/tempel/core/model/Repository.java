/* org.agiso.tempel.core.model.Repository (03-11-2012)
 * 
 * Repository.java
 * 
 * Copyright 2012 PPW 'ARAJ' Sp. z o. o.
 */
package org.agiso.tempel.core.model;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@araj.pl">Karol Kopacz</a>
 */
public interface Repository extends Cloneable {
	public String getValue();

//	--------------------------------------------------------------------------
	public Repository clone();
}
