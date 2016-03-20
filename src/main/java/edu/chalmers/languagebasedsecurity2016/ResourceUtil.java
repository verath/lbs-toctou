package edu.chalmers.languagebasedsecurity2016;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class ResourceUtil {
    public static File getResourceFile(Class<?> clazz, String fileName) throws FileNotFoundException {
        URL resource = clazz.getResource(fileName);
        if(resource == null) {
            throw new FileNotFoundException(fileName + " does not exist");
        }
        return new File(resource.getFile());
    }
}
