package com.sda.vdm.domain;

import lombok.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class VendingMachine {

    private Map<Product, Integer> productMenu = new HashMap<>();
    private Map<CoinType, Integer> coinsMap = new HashMap<>();
    private Currency currency;

    public void showProducts() {
        System.out.println("The products of the Vending machine are: ");
//        for (Map.Entry<Product, Integer> productEntry : productMenu.entrySet()) {
//
//            if (productEntry.getValue() > 0) {
//                printProduct(productEntry.getKey());
//            }
//        }
        productMenu.entrySet().stream().filter(p -> p.getValue() > 0).forEach(p -> printProduct(p.getKey()));
    }

    public boolean isProductValid(int productCode) {
        return productMenu.entrySet().stream()
                .anyMatch(p -> p.getKey().getCode() == productCode && p.getValue() > 0);
    }

    public Product getProductByCode(int productCode) {
        return productMenu.entrySet().stream()
                .filter(p -> p.getKey().getCode() == productCode)
                .findFirst().get().getKey();
    }

    public Map<CoinType, Integer> buyProduct(Product product, Map<CoinType, Integer> enteredAmount) throws InvalidProductException, InvalidAmountException, NoExchangePossibleException {

        Map<CoinType, Integer> exchangeCoins = new HashMap<>();
        int amountPayed = validatePurchase(product, enteredAmount);
        int amountToPayBack = amountPayed - product.getPrice();
        Map<CoinType, Integer> tempCoinsMap = new HashMap<>(coinsMap);
        while (amountToPayBack > 0) {
            final int currentAmountToBePayedBack = amountToPayBack;
            Optional<Map.Entry<CoinType, Integer>> coinTypeToBeReturned = tempCoinsMap.entrySet().stream()
                    .filter(c -> c.getValue() > 0)
//                    .map(c -> {
//                        System.out.println("Coin Type F1: " + c);
//                        return c;
//                    })
                    .filter(c -> c.getKey().valueOfCoin <= currentAmountToBePayedBack)
//                    .map(c -> {
//                        System.out.println("Coin Type F2: " + c);
//                        return c;
//                    })
                    .sorted(new Comparator<Map.Entry<CoinType, Integer>>() {
                        @Override
                        public int compare(Map.Entry<CoinType, Integer> c1, Map.Entry<CoinType, Integer> c2) {
                            return c2.getKey().valueOfCoin - c1.getKey().valueOfCoin;
                        }
                    })
                    .findFirst();
            if (!coinTypeToBeReturned.isPresent()) {
                throw new NoExchangePossibleException("This machine is out of money!");
            }
            CoinType coinType = coinTypeToBeReturned.get().getKey();
            amountToPayBack -= coinType.valueOfCoin;
            tempCoinsMap.put(coinType, tempCoinsMap.get(coinType) - 1);
            if (exchangeCoins.containsKey(coinType)) {
                exchangeCoins.put(coinType, exchangeCoins.get(coinType) + 1);
            } else {
                exchangeCoins.put(coinType, 1);
            }

        }
        coinsMap = tempCoinsMap;
        productMenu.put(product, productMenu.get(product) - 1);
        addMoneyToVendingMachine(enteredAmount);

        return exchangeCoins;


    }

    private void addMoneyToVendingMachine(Map<CoinType, Integer> enteredAmount) {
        enteredAmount.entrySet().stream().forEach(c -> coinsMap.put(c.getKey(), coinsMap.get(c.getKey()) + c.getValue()));
    }

    private int validatePurchase(Product product, Map<CoinType, Integer> enteredAmount) throws InvalidProductException, InvalidAmountException {
        if (!productMenu.containsKey(product) || productMenu.get(product) < 1) {
            throw new InvalidProductException("No such Product available");
        }
        AmountCollector amountCollector = new AmountCollector();
        enteredAmount.entrySet().stream()
                .forEach(c -> amountCollector.incrementAmount(c.getKey().valueOfCoin * c.getValue()));
        if (product.getPrice() > amountCollector.amount) {
            throw new InvalidAmountException("This machine needs more money!");
        }
        return amountCollector.amount;
    }


    private void printProduct(Product product) {
        System.out.println("Product code: " + product.getCode() +
                "\t\t Product name: " + product.getName() +
                "\t\t Price: " + product.getPrice());
    }

    private class AmountCollector {
        int amount;

        public void incrementAmount(int incrementedValue) {
            amount += incrementedValue;
        }
    }


}

