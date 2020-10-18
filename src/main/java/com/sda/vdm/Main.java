package com.sda.vdm;

import com.sda.vdm.domain.*;
import com.sda.vdm.service.VendingMachineDAO;
import com.sda.vdm.service.VendingMachineDAOToFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        VendingMachineDAO vendingMachineDAO = new VendingMachineDAOToFile();

        VendingMachine vendingMachine1 = vendingMachineDAO.loadVendingMachine();
        System.out.println(vendingMachine1);


        System.out.println(vendingMachine1);

        vendingMachineDAO.saveVendingMachine(vendingMachine1);


        vendingMachine1.showProducts();
        int nextProduct = 0;
        do {
            System.out.println("Please choose your product: ");
            nextProduct = scanner.nextInt();

        } while (!vendingMachine1.isProductValid(nextProduct));

        System.out.println("Please choose what coin type do you want: ");

        Product productByCode = vendingMachine1.getProductByCode(nextProduct);
        showCoins();
        int currentAmount = 0;
        Map<CoinType, Integer> coinsEntered = new HashMap<>();
        while (currentAmount < productByCode.getPrice()) {
            System.out.println("Please select coin type to add: ");
            int coinChoice = scanner.nextInt();
            CoinType selectedCoin = CoinType.values()[coinChoice];
            if (coinsEntered.containsKey(selectedCoin)) {
                coinsEntered.put(selectedCoin, coinsEntered.get(selectedCoin) + 1);
            } else {
                coinsEntered.put(selectedCoin, 1);
            }
            currentAmount += selectedCoin.valueOfCoin;
        }
        System.out.println("Total amount entered " + currentAmount);
        try {
            Map<CoinType, Integer> finalExchange = vendingMachine1.buyProduct(productByCode, coinsEntered);
            System.out.println("Your exchange is: " + finalExchange);
            vendingMachineDAO.saveVendingMachine(vendingMachine1);
        } catch (InvalidProductException e) {
            e.printStackTrace();
        } catch (InvalidAmountException e) {
            e.printStackTrace();
        } catch (NoExchangePossibleException e) {
            e.printStackTrace();
        }
    }

    public static void showCoins() {
        Arrays.stream(CoinType.values()).forEach(c -> System.out.println(c.ordinal() + ": " + c.name()));
    }

}
