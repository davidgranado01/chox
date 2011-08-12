/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.uploadclient;

import java.io.File;
import java.io.FilenameFilter;

public class MyFilter implements FilenameFilter {
    String ext, start;

    public MyFilter(String start, String ext) {
        this.ext = "." + ext;
        this.start = start;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(ext) && name.startsWith(start);
    }
}
