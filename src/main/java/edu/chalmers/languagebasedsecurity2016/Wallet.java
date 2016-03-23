package edu.chalmers.languagebasedsecurity2016;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class Wallet {
   /**
    * The RandomAccessFile of the wallet file
    */  
   private RandomAccessFile file;

   /**
    * Creates a Wallet object
    *
    * A Wallet object interfaces with the wallet RandomAccessFile
    */
    public Wallet () throws Exception {
	this.file = new RandomAccessFile(new File("wallet.txt"), "rw");
    }

   /**
    * Gets the wallet balance. 
    *
    * @return                   The content of the wallet file as an integer
    */
    public int getBalance() throws IOException {
        // To be strict, we should probably acquire a shared lock with
        // lock(0, Long.MAX_VALUE, true), so that the setBalance method
        // does not interfere with us when reading (theoretically).
	this.file.seek(0);
	return Integer.parseInt(this.file.readLine());
    }

   /**
    * Sets a new balance in the wallet
    *
    * @param  newBalance          new balance to write in the wallet
    */
    public void setBalance(int newBalance) throws Exception {
	this.file.setLength(0);
	String str = new Integer(newBalance).toString()+'\n'; 
	this.file.writeBytes(str); 
    }
    
    /**
     * Safely withdraw a value from the wallet (race-free).
     */
    public void safeWithdraw(int valueToWithdraw) throws Exception {
        FileLock lock = null;
        try {
            // Block until we can obtain an exclusive lock on the file.
            lock = this.file.getChannel().lock();
            
            int newBalance = this.getBalance() - valueToWithdraw;
            
            if(newBalance < 0) {
                throw new Exception("Out of money exception!");
            } else {
                Thread.sleep(10*1000); // Sleep to show it works
                this.setBalance(newBalance);
            }
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
