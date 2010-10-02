/*
 * j2seDictFactory.java
 *
 * Created on September 28, 2006, 5:50 PM
Copyright (C) 2006,2007  Yong Li. All rights reserved.
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.
This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.teesoft.jfile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wind
 */
public class j2seURLFactory implements IFileFactory {

    private static IFileFactory instance = null;

    public static IFileFactory getInstance() {

        if (instance == null) {

            instance = new j2seURLFactory();
        }

        return instance;
    }
    


    /**
     * Creates a new instance of j2seDictFactory
     */
    private j2seURLFactory() {
    }

    public FileAccessBase newFileAccess(String filename) throws IOException {

        try {

            URL url = new URL(filename);
            return new j2seURLAccess(filename);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public String getSeparator() {

        return File.separator;
    }

    public Vector listRoots() throws IOException {

        return j2seURLAccess.listRoots();
    }

    public boolean isSupportedFile(String filename) {
        try {

            URL url = new URL(filename);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }

    }
}
