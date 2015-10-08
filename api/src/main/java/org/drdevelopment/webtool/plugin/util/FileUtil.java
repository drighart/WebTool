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

package org.drdevelopment.webtool.plugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private FileUtil() {
        // private constructor
    }

    public static String getPluginWebRootFolder(String nameOfPlugin) {
		return FileUtil.getCurrentFolder() + File.separator + "plugins/" + nameOfPlugin + "/classes/web";
    }
    
    public static String getCurrentFolder() {
        return System.getProperty("user.dir");
    }

    public static boolean isFolderExists(String folderName) {
        File f = new File(folderName);
        return f.exists() && f.isDirectory();
    }

    public static boolean isFileExists(String fileName) {
        File f = new File(fileName);
        return f.exists() && !f.isDirectory();
    }

    public static boolean isResourceExists(String filename) {
    	URL url = FileUtil.class.getResource(filename);
    	return (url != null);
    }

    public static byte[] readBytesFromResource(String filename) throws IOException {
    	byte fileContent[];
    	try (InputStream input = FileUtil.class.getResourceAsStream(filename)) {
    		fileContent = IOUtils.toByteArray(input);
            input.close();
        }
        return fileContent;
    }

    public static byte[] readBytes(String filename) throws IOException {
    	byte fileContent[];
        try (FileInputStream input = new FileInputStream(filename); ) {
    		fileContent = IOUtils.toByteArray(input);
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

    public static String getPluginClassesFolder(String pluginId) {
        return System.getProperty("user.dir") + File.separator + "plugins" + File.separator + pluginId + File.separator + "classes";
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

}
