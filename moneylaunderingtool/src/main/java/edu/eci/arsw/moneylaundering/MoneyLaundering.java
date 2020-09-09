package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{

    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    public final int nt=5;
    public static List<ThreadControlador> tl;
    public boolean pausa;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        tl = new CopyOnWriteArrayList<>();
        pausa=true;
    }

    public void processTransactionData()
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        int min=0;
        int saltos=amountOfFilesTotal%nt;
        int max=saltos;

        for(int i=0; i<nt; i++){
            ThreadControlador t = new ThreadControlador(min, max,transactionFiles,transactionReader,transactionAnalyzer,amountOfFilesProcessed);
            tl.add(t);
            t.start();
            min=max;
            max=min+saltos;
        }

    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        moneyLaundering.processTransactionData();
        //Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        //processingThread.start();

        while(true)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            if(!moneyLaundering.pausa) {
                System.out.println("Pausado");
                moneyLaundering.pausa=true;
                for (ThreadControlador t : tl) {
                    t.pausa(false);
                }
            }else{
                System.out.println("Reanudado");
                moneyLaundering.pausa=false;
                for (ThreadControlador t : tl) {
                    t.reanudar(true);
                }
                String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
                List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
                String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
                message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
                System.out.println(message);
            }
        }

    }


}
