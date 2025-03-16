package com.rudemoc.convert.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageConverter {
    
    // Method to convert JPG to PNG
    public void convertJPGtoPNG(String inputPath, String outputPath) {
        try {
            // Read the input image
            BufferedImage image = ImageIO.read(new File(inputPath));
            
            // Write the image as PNG
            ImageIO.write(image, "png", new File(outputPath));
            
            System.out.println("Conversion completed successfully!");
        } catch (IOException e) {
            System.out.println("Error during conversion: " + e.getMessage());
        }
    }
}
