/* org.agiso.tempel.Temp (14-09-2012)
 * 
 * Temp.java
 * 
 * Copyright 2012 agiso.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Klasa tymczasowa (do usunięcia). Zawiera statyczne metody narzędziowe do
 * przeniesienia do właściwych dla nich bibliotek i klas.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Deprecated
public class Temp {

//	--------------------------------------------------------------------------
//	StringUtils
//	--------------------------------------------------------------------------
	/**
	 * @param string
	 * @return
	 */
	public static final boolean StringUtils_isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final boolean StringUtils_isBlank(String string) {
		return string == null || string.trim().length() == 0;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_nullIfEmpty(String string) {
		return StringUtils_isEmpty(string)? null : string;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_nullIfBlank(String string) {
		return StringUtils_isBlank(string)? null : string;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_emptyIfNull(String string) {
		return string == null? "" : string;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_emptyIfBlank(String string) {
		return StringUtils_isBlank(string)? "" : string;
	}

//	--------------------------------------------------------------------------
//	FileUtils
//	--------------------------------------------------------------------------
	/**
	 * @param srcFile
	 * @param dstFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final void FileUtils_copyFile(String srcFile, String dstFile) throws IOException {
		FileUtils_copyFile(new File(srcFile), new File(dstFile));
	}

	/**
	 * @param srcFile
	 * @param dstFile
	 * @throws IOException
	 */
	public static final void FileUtils_copyFile(File srcFile, File dstFile) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(srcFile);

			FileUtils_copyFile(is, dstFile);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * @param is
	 * @param dstFile
	 * @throws IOException
	 */
	public static final void FileUtils_copyFile(InputStream is, File dstFile) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(dstFile);

			int len;
			byte[] buf = new byte[1024];
			while((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
		} finally {
			if(os != null) {
				try {
					os.flush();
					os.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

//	--------------------------------------------------------------------------
//	DigestUtils
//	--------------------------------------------------------------------------
	/**
	 * @param algorithm
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static final String DigestUtils_countDigest(String algorithm, InputStream is) throws Exception {
		MessageDigest digest = MessageDigest.getInstance(algorithm);

		DigestUtils_updateDigest(digest, is);

		return HexUtils_toHexString(digest.digest());
	}

	/**
	 * @param md
	 * @param is
	 * @throws Exception
	 */
	public static final void DigestUtils_updateDigest(MessageDigest md, InputStream is) throws Exception {
		DigestInputStream dis = new DigestInputStream(new BufferedInputStream(is), md);

		while(dis.read() != -1);
	}

	/**
	 * @param algorithm
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static final String DigestUtils_countDigest(String algorithm, File file) throws Exception {
		if(!file.exists()) {
			throw new FileNotFoundException(file.getPath());
		}

		MessageDigest digest = MessageDigest.getInstance(algorithm);

		String basePath = file.getCanonicalPath();
		if(file.isDirectory()) {
			File[] content = file.listFiles();
			Arrays.sort(content, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});

			for(File subFile : content) {
				DigestUtils_updateDirecotryDigest(basePath, subFile, digest);
			}
		} else {
			DigestUtils_updateDigest(digest, new FileInputStream(file));
		}

		return HexUtils_toHexString(digest.digest());
	}

//	--------------------------------------------------------------------------
	private static void DigestUtils_updateDirecotryDigest(String path, File file, MessageDigest md) throws Exception {
		String relativePath = file.getCanonicalPath().substring(path.length());
		relativePath = relativePath.replace('\\', '/');		// normalizacja ścieżki
		if(file.isDirectory()) {
			md.update(relativePath.getBytes(Charset.forName("UTF8")));

			File[] content = file.listFiles();
			Arrays.sort(content, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});

			for(File subFile : content) {
				DigestUtils_updateDirecotryDigest(path, subFile, md);
			}
		} else {
			md.update(relativePath.getBytes(Charset.forName("UTF8")));

			DigestUtils_updateDigest(md, new FileInputStream(file));
		}
	}

//	--------------------------------------------------------------------------
//	HextUtils
//	--------------------------------------------------------------------------
	/**
	 * @param array
	 * @return
	 */
	public static final String HexUtils_toHexString(byte[] array) {
		int length = array.length;
		StringBuilder hexString = new StringBuilder();
		for(int i = 0; i < length; i++) {
			hexString.append(HexUtils_hexDigit(array[i]));
		}

		return hexString.toString();
	}

	/**
	 * @param b
	 * @return
	 */
	public static final String HexUtils_hexDigit(byte b) {
		StringBuilder sb = new StringBuilder();
		char c;
		// First nibble
		c = (char)((b >> 4) & 0xf);
		if(c > 9) {
			c = (char)((c - 10) + 'a');
		} else {
			c = (char)(c + '0');
		}
		sb.append(c);
		// Second nibble
		c = (char)(b & 0xf);
		if(c > 9) {
			c = (char)((c - 10) + 'a');
		} else {
			c = (char)(c + '0');
		}
		sb.append(c);
		return sb.toString();
	}

//	--------------------------------------------------------------------------
//	ConvertUtils
//	--------------------------------------------------------------------------
	public static final String ConvertUtils_convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
