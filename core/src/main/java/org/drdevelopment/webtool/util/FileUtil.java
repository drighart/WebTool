/*
 * This software source code is provided by the USEF Foundation. The copyright
 * and all other intellectual property rights relating to all software source
 * code provided by the USEF Foundation (and changes and modifications as well
 * as on new versions of this software source code) belong exclusively to the
 * USEF Foundation and/or its suppliers or licensors. Total or partial
 * transfer of such a right is not allowed. The user of the software source
 * code made available by USEF Foundation acknowledges these rights and will
 * refrain from any form of infringement of these rights.
 *
 * The USEF Foundation provides this software source code "as is". In no event
 * shall the USEF Foundation and/or its suppliers or licensors have any
 * liability for any incidental, special, indirect or consequential damages;
 * loss of profits, revenue or data; business interruption or cost of cover or
 * damages arising out of or in connection with the software source code or
 * accompanying documentation.
 *
 * For the full license agreement see http://www.usef.info/license.
 */

package org.drdevelopment.webtool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.drdevelopment.webtool.exception.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private FileUtil() {
        // private constructor
    }

    public static String getMD5(String source) {
    	try {
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		md.update(source.getBytes());
    		byte[] hash = md.digest();

    		StringBuilder hexString = new StringBuilder();
    		for (int i = 0; i < hash.length; i++) {
    			if ((0xff & hash[i]) < 0x10) {
    				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
    			} else {
    				hexString.append(Integer.toHexString(0xFF & hash[i]));
    			}
    		}

    		return hexString.toString();
    	} catch (NoSuchAlgorithmException e) {
    		LOGGER.error(e.getMessage(), e);
    	}
    	return null;
    }
    
    public static String getCurrentFolder() {
        return System.getProperty("user.dir");
    }

    /**
     * Creates all folders
     *
     * @param folderName
     */
    public static void createFolders(String folderName) {
        LOGGER.debug("Creating folder: " + folderName);
        File dir = new File(folderName);
        dir.mkdirs();
    }

    public static void removeFolders(String folderName) throws IOException {
        if (folderName == null || folderName.isEmpty() || folderName.trim().equals("\\") || folderName.trim().equals("/")) {
            LOGGER.warn("Prevent to delete folders recursively from the root location.");
        } else {
            Path directory = Paths.get(folderName);
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    LOGGER.debug("Removing folder " + dir);
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static void removeFile(String fileName, boolean failOnNotExists) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            if (failOnNotExists) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isFolderExists(String folderName) {
        File f = new File(folderName);
        return f.exists() && f.isDirectory();
    }

    public static boolean isFileExists(String fileName) {
        File f = new File(fileName);
        return f.exists() && !f.isDirectory();
    }

    public static void writeTextToFile(String filename, String data) throws IOException {
        FileUtils.writeStringToFile(new File(filename), data, DEFAULT_CHARSET);
    }

    public static void appendTextToFile(String filename, String data) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            out.println(data);
        }
    }

    public static void appendNonDelimitingTextToFile(String filename, String data) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            out.print(data);
        }
    }

    public static boolean isResourceExists(String filename) {
    	URL url = FileUtil.class.getResource(filename);
    	return (url != null);
    }

    public static byte[] readBytesFromResource(String filename) throws IOException {
    	byte[] fileContent;
    	try (InputStream input = FileUtil.class.getResourceAsStream(filename)) {
    		fileContent = IOUtils.toByteArray(input);
//    		fileContent = IOUtils.toCharArray(input);
            input.close();
        }
        return fileContent;
    }
    
    public static String readLinesFromResource(String filename) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        try (InputStream input = FileUtil.class.getResourceAsStream(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(input, DEFAULT_CHARSET))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            input.close();
        }
        return sb.toString();
    }

    public static String readLinesWithCarriageReturnFromResource(String filename) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        try (InputStream input = FileUtil.class.getResourceAsStream(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(input, DEFAULT_CHARSET))) {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            input.close();
        }
        return sb.toString();
    }

    public static byte[] readBytes(String filename) throws IOException {
    	byte fileContent[];
        try (FileInputStream input = new FileInputStream(filename); ) {
    		fileContent = IOUtils.toByteArray(input);
            input.close();
        }
        return fileContent;
    }
    
    public static List<String> readLines(String filename) throws IOException {
        String line;
        List<String> list = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(filename);
                BufferedReader br = new BufferedReader(new InputStreamReader(input, DEFAULT_CHARSET))) {
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static String readFile(String filename) throws IOException {
        File file = new File(filename);
        return org.apache.commons.io.FileUtils.readFileToString(file);
    }

    public static void copyFile(String fromFilename, String toFilename) throws IOException {
        if (!isFileExists(fromFilename)) {
            LOGGER.error("File {} can not be found and therefor can not be copied to {}.", fromFilename, toFilename);
            throw new RuntimeException("File " + fromFilename + " can not be found and therefor can not be copied to "
                    + toFilename + ".");
        }
//        LOGGER.info("Copy file from {} to {}.", fromFilename, toFilename);
        File fromFile = new File(fromFilename);
        File toFile = new File(toFilename);
        FileUtils.copyFile(fromFile, toFile);
    }

    public static void copyFolder(String fromFolder, String toFolder) throws IOException {
        if (!isFolderExists(fromFolder)) {
            LOGGER.error("Folder {} can not be found and therefor can not be copied to {}.", fromFolder, toFolder);
            throw new RuntimeException("Folder " + fromFolder + " can not be found and therefor can not be copied to "
                    + toFolder + ".");
        }

        LOGGER.info("Copy folder from {} to {}.", fromFolder, toFolder);
        File src = new File(fromFolder);
        File dest = new File(toFolder);
        FileUtils.copyDirectory(src, dest);
    }

    public static void renameFile(String fromFilename, String toFilename) throws IOException {
    	File fileFrom = new File(fromFilename);
        File fileTo = new File("newname");
        if (fileTo.exists()) {
        	throw new java.io.IOException("File " + toFilename + " already exists! Could not rename it.");
        }

        boolean success = fileFrom.renameTo(fileTo);
        if (!success) {
        	throw new java.io.IOException("Could not rename file " + toFilename + " to " + fromFilename + ".");
        }
    }
    
    public static Properties readProperties(String configFilename) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(configFilename);) {
            properties.load(input);
        } catch (IOException e) {
        	LOGGER.warn(e.getMessage(), e);
        }
        return properties;
    }

    public static Properties readPropertiesFromResource(String configFilename) {
//        LOGGER.debug("Reading properties file: {}", configFilename);
        Properties properties = new Properties();
        try ( InputStream input = FileUtil.class.getResourceAsStream(configFilename);
        	) {
            properties.load(input);
        } catch (IOException e) {
        	LOGGER.error(e.getMessage(), e);
		}
        return properties;
    }

    public static Properties writeProperties(String configFilename, String comment, Properties properties) throws IOException {
        LOGGER.info("Writing properties file: {}", configFilename);
        try (FileOutputStream output = new FileOutputStream(configFilename);) {
            properties.store(output, comment);
        }
        return properties;
    }

    public static File[] findJarFilesInFolder(String folderName){
    	File dir = new File(folderName);

    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".jar"); }
    	});
    }
    
}
