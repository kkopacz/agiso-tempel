/* org.agiso.tempel.core.ITempelFileProcessor (16-12-2012)
 * 
 * ITempelFileProcessor.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.api.internal;

import java.io.File;
import java.io.InputStream;


/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public interface ITempelFileProcessor {
	/**
	 * @param xmlString
	 * @param entryProcessor
	 * @throws Exception
	 */
	public void process(String xmlString, ITempelEntryProcessor entryProcessor) throws Exception;

	/**
	 * @param xmlFile
	 * @param entryProcessor
	 * @throws Exception
	 */
	public void process(File xmlFile, ITempelEntryProcessor entryProcessor) throws Exception;

	/**
	 * @param xmlStream
	 * @param entryProcessor
	 * @throws Exception
	 */
	public void process(InputStream xmlStream, ITempelEntryProcessor entryProcessor) throws Exception;
}
