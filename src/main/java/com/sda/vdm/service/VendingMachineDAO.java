package com.sda.vdm.service;


import com.sda.vdm.domain.VendingMachine;

public interface VendingMachineDAO {

    public void saveVendingMachine (VendingMachine vendingMachine);
    public VendingMachine loadVendingMachine ();
}
