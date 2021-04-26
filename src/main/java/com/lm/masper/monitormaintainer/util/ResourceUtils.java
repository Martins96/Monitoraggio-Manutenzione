package com.lm.masper.monitormaintainer.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {
	
	public static InputStream getAsClasspathResource(String location) {
		InputStream is = ResourceUtils.class.getResourceAsStream(location);
		if (is == null) {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
		}
		return is;
	}

	public static InputStream getAsFileSystemResource(String fileLocation) throws IOException {
		try {
			return new FileInputStream(fileLocation);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
