package edu.chalmers.languagebasedsecurity2016;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;


public class Pocket {
   /**
    * The RandomAccessFile of the pocket file
    */
   private RandomAccessFile file;

   /**
    * Creates a Pocket object
    * 
    * A Pocket object interfaces with the pocket RandomAccessFile.
    */
    public Pocket () throws Exception {
        this.file = new RandomAccessFile(new File("pocket.txt"), "rw");
    }

   /**
    * Adds a product to the pocket. 
    *
    * @param  product           product name to add to the pocket (e.g. "car")
    */
    public void addProduct(String product) throws Exception {
          this.file.seek(this.file.length());
          this.file.writeBytes(product+'\n'); 
    }
    
    /**
     * Safely add a product to the pocket.
     */
    public void safeAddProduct(String product) throws Exception {
        FileLock lock = null;
        try {
            // Block until we can obtain an exclusive lock on the file.
            lock = this.file.getChannel().lock();
            
            addProduct(product);
        } finally {
            if(lock != null) {
                lock.release();
            }
        }
    }

   /**
    * Closes the RandomAccessFile in this.file
    */
    public void close() throws Exception {
        this.file.close();
    }
}
