package com.sda.vdm.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sda.vdm.domain.VendingMachine;


import java.io.*;

public class VendingMachineDAOToFile implements VendingMachineDAO {

    private static final String SAVED_VENDING_MACHINE_FILE = "vendingmachine.json";

    private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

    @Override
    public void saveVendingMachine(VendingMachine vendingMachine) {

        try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(SAVED_VENDING_MACHINE_FILE))) {
            gson.toJson(vendingMachine, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VendingMachine loadVendingMachine() {
        VendingMachine vm = null;
        try {
            vm = gson.fromJson(new FileReader(SAVED_VENDING_MACHINE_FILE), VendingMachine.class);
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading the vending machine." + e);
        }
        return vm;
    }
}
