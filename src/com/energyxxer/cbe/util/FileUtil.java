package com.energyxxer.cbe.util;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Provides utility methods for dealing with files.
 */
public class FileUtil {
	/**
	 * Deletes a folder and all its contents.
	 */
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	/**
	 * Returns whether or not the given string is a valid file name.
	 */
	public static boolean validateFilename(String str) {
		return str.indexOf("\\") + str.indexOf("/") + str.indexOf(":") + str.indexOf("*") + str.indexOf("?")
				+ str.indexOf("\"") + str.indexOf("<") + str.indexOf(">") + str.indexOf("|") == -9;
	}

	/**
	 * Takes one of my lazily written file paths and converts them to work with
	 * the user's operating system.
	 */
	public static String normalize(String path) {
		return path.replace("/", File.separator.replace("\\", "\\\\"));
	}

	public static String getRelativePath(File file, File root) {
		return file.getAbsolutePath().replaceFirst(Matcher.quoteReplacement(root.getAbsolutePath() + File.separator),"");
	}

	private FileUtil() {}
}
