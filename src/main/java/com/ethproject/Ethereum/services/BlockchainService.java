package com.ethproject.Ethereum.services;

import java.io.IOException;
import java.math.BigInteger;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;





public class BlockchainService {
	  private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(BlockchainService.class);
	  
	    @Autowired
	    Web3j web3j;
	 
	    public BlockchainTransaction process(BlockchainTransaction trx) throws IOException {
	        EthAccounts accounts = web3j.ethAccounts().send();
	        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(accounts.getAccounts().get(trx.getFromId()), DefaultBlockParameterName.LATEST).send();
	        Transaction transaction = Transaction.createEtherTransaction(accounts.getAccounts().get(trx.getFromId()), transactionCount.getTransactionCount(), BigInteger.valueOf(trx.getValue()), BigInteger.valueOf(21_000), accounts.getAccounts().get(trx.getToId()),BigInteger.valueOf(trx.getValue()));
	        EthSendTransaction response = web3j.ethSendTransaction(transaction).send();
	        if (response.getError() != null) {
	            trx.setAccepted(false);
	            return trx;
	        }
	        trx.setAccepted(true);
	        String txHash = response.getTransactionHash();
	        LOGGER.info("Tx hash: {}", txHash);
	        trx.setId(txHash);
	        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();
	        if (receipt.getTransactionReceipt().isPresent()) {
	            LOGGER.info("Tx receipt: {}", receipt.getTransactionReceipt().get().getCumulativeGasUsed().intValue());
	        }
	        return trx;
	    }

}
