package nwmissouri.edu.staticclasses;

/**
 * Created by S521670 on 6/29/2015.
 */

import java.io.File;

/**
 * This class is used to when there is any modifications to be done in case of existing directories.
 */
public class DirectoryEditings {

    /**
     * This method is used to deletes a directory with the files existing in it or even a single file.
     * @param path - Path of the directory or a file.
     * @return
     */
    public static boolean deleteDirectory(File path) {
        // TODO Auto-generated method stub
        if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

}

