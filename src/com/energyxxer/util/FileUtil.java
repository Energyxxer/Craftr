package com.energyxxer.util;

import java.io.File;

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
	 * the user's operating system's.
	 */
	public static String normalize(String path) {

		return path.replaceAll("//", File.separator);
	}
}
