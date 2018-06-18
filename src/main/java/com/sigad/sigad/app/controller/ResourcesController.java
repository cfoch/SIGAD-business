/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 *
 * @author jorgeespinoza
 */
public class ResourcesController {
    public ResourcesController(){
        
    }
    
    public byte[] extractBytes (String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }
    
    public byte[] getFileInBytes(String filename) {
        File file  = new File(filename);
        int length = (int)file.length();
        byte[] bytes = new byte[length];
        try {
            BufferedInputStream reader = new BufferedInputStream(new 
            FileInputStream(file));
            reader.read(bytes, 0, length);
            System.out.println(reader);
            // setFile(bytes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;
    }
    
    public byte[] getFileInBytes1(String filename) throws IOException{
        Path pdfPath = Paths.get("/path/to/file.pdf");
        byte[] pdf = Files.readAllBytes(pdfPath);
        return pdf;
    }
}
