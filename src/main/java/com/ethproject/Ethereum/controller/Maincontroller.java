package com.ethproject.Ethereum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ethproject.Ethereum.services.BlockchainService;

@RestController
public class Maincontroller {
    @Autowired
    BlockchainService service;
 
    @PostMapping("/transaction")
    public BlockchainTransaction execute(@RequestBody BlockchainTransaction transaction) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        return service.process(transaction);
    }

}
