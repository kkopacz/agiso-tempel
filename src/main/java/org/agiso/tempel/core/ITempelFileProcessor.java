/* org.agiso.tempel.core.ITempelFileProcessor (16-12-2012)
 * 
 * ITempelFileProcessor.java
 * 
 * Copyright 2012 PPW 'ARAJ' Sp. z o. o.
 */
package org.agiso.tempel.core;

import java.io.File;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@araj.pl">Karol Kopacz</a>
 */
public interface ITempelFileProcessor {
	/**
	 * @param file
	 * @param entryProcessor
	 * @throws Exception
	 */
	public void process(File file, ITempelEntryProcessor entryProcessor) throws Exception;
}
