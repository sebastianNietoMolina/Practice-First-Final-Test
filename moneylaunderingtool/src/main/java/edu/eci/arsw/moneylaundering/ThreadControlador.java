/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author JUAN NIETO
 */
public class ThreadControlador extends Thread {

    int min,max;
    List<File> archivo;
    TransactionReader transactionReader;
    TransactionAnalyzer transactionAnalyzer;
    AtomicInteger amountOfFilesProcessed;
    public boolean pausa;

    public ThreadControlador(int min, int max, List<File> transactionFiles, TransactionReader transactionReader,TransactionAnalyzer transactionAnalyzer, AtomicInteger amountOfFilesProcessed) {
        this.min=min;
        this.max=max;
        this.archivo=transactionFiles;
        this.transactionReader=transactionReader;
        this.transactionAnalyzer=transactionAnalyzer;
        this.amountOfFilesProcessed = amountOfFilesProcessed;
        pausa=true;

    }

    public void pausa(boolean pausa) {
        this.pausa = pausa;
    }

    public synchronized void reanudar(boolean pausa){
        this.pausa=pausa;
        notifyAll();
    }

    @Override
    public void run(){

        if(pausa){
            for(int i=min; i<max; i++)
            {
                File pedazoFile = archivo.get(i);
                List<Transaction> transactions = transactionReader.readTransactionsFromFile(pedazoFile);
                for(Transaction transaction : transactions)
                {
                    transactionAnalyzer.addTransaction(transaction);
                }
                amountOfFilesProcessed.incrementAndGet();
            }
        }else{
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
