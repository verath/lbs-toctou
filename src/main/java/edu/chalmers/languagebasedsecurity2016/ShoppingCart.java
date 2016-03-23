package edu.chalmers.languagebasedsecurity2016;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShoppingCart {

    private Pocket pocket = null;
    private Wallet wallet = null;

    private final BufferedReader reader;

    /**
     * True iff we run in race-free mode
     */
    private boolean isSafe = false;

    public ShoppingCart(boolean isSafe) throws Exception {
        this.isSafe = isSafe;
        System.out.println("Safe mode: " + isSafe + "\n");

        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws Exception {
        try {
            wallet = new Wallet();
            pocket = new Pocket();

            printBalance();
            printProductList();
            promptProductPurchase();
            printBalance();
        } finally {
            if (pocket != null) {
                pocket.close();
            }
            if (wallet != null) {
                wallet.close();
            }
        }
    }

    private void printBalance() throws IOException {
        System.out.printf("Your balance: %d credits\n", wallet.getBalance());
    }

    private void printProductList() {
        System.out.println(Store.asString());
    }

    private void promptProductPurchase() throws Exception {
        String productName;
        do {
            System.out.print("What you want to buy?: ");
            productName = reader.readLine();
        } while (!Store.products.containsKey(productName));

        purchaseProduct(productName);
    }

    private void purchaseProduct(String productName) throws Exception {
        int cost = Store.products.get(productName);

        // (is run with command line argument safe?)
        if (isSafe) {
            wallet.safeWithdraw(cost); // throws exception if fail => exits app
            pocket.safeAddProduct(productName);
        } else {
            int balance = wallet.getBalance();
            if (balance < cost) {
                throw new Exception("No money left :("); // => exits app
            }

            Thread.sleep(10 * 1000); // Sleep to make it easier to exploit the race

            wallet.setBalance(balance - cost);
            pocket.addProduct(productName);
        }
    }


    public static void main(String[] args) {
        try {
            new ShoppingCart(args.length > 0 && args[0].equals("safe")).run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
