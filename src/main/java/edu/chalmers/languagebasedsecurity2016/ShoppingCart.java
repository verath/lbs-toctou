package edu.chalmers.languagebasedsecurity2016;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShoppingCart {

    private final Pocket pocket;
    private final Wallet wallet;
    private final BufferedReader reader;

    public ShoppingCart() throws Exception {
        wallet = new Wallet();
        pocket = new Pocket();
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws Exception {
        printBalance();
        printProductList();
        promptProductPurchase();
        printBalance();
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
            if (productName.isEmpty()) {
                System.exit(0);
            }
        } while (!Store.products.containsKey(productName));

        purchaseProduct(productName);
    }

    private void purchaseProduct(String productName) throws Exception {
        int cost = Store.products.get(productName);
        int balance = wallet.getBalance();
        // check if the amount of credits is enough, if not stop the execution.
        if (balance < cost) {
            System.exit(-1);
        }
        // otherwise, withdraw the price of the product from the wallet.
        wallet.setBalance(balance - cost);
        // add the name of the product to the pocket file.
        pocket.addProduct(productName);
    }


    public static void main(String[] args) {
        try {
            new ShoppingCart().run();
        } catch (Exception e) {
            System.err.println("Could not start application!");
            e.printStackTrace();
        }
    }
}
