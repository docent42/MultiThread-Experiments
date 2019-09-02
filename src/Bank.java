import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Bank
{
    private CopyOnWriteArrayList<Account> accounts;
    private final Random random = new Random();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    static Bank getInstance()
    {
        Bank bank = new Bank();
        bank.init();
        return bank;
    }

    private void init()// формирование счетов в банке
    {
        List<Account> tmp = new ArrayList<>();
        for (int i = 0;i < 1000;i++)
            tmp.add(new Account(Math.round((Math.random() * 200000) + 300000),i));
        accounts = new CopyOnWriteArrayList<>(tmp);
    }

    private synchronized boolean isFraud()
        throws InterruptedException
    {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    void transfer(int fromAccountNum, int toAccountNum, long transferAmt) throws InterruptedException
    {
        boolean fromAccStatus = accounts.get(fromAccountNum).isStatus();
        boolean toAccStatus = accounts.get(toAccountNum).isStatus();
        long fromMoneyAmount = accounts.get(fromAccountNum).getMoney();
        long toMoneyAmount = accounts.get(toAccountNum).getMoney();

        if (fromAccStatus && toAccStatus) // проверка доступа к счетам до транзакции
        {
            accounts.get(fromAccountNum).setMoney(fromMoneyAmount - transferAmt);
            accounts.get(toAccountNum).setMoney(toMoneyAmount + transferAmt);
            if (transferAmt > 50000) // уходим на проверку
                if (isFraud())
                {
                    System.out.println(ANSI_YELLOW + "[ ПОДОЗРИТЕЛЬНАЯ ОПЕРАЦИЯ ! СЧЕТА ЗАБЛОКИРОВАНЫ ! ]" + ANSI_RESET);
                    accounts.get(fromAccountNum).blockAccount();
                    accounts.get(toAccountNum).blockAccount();
                }
        }
        else {
            System.out.println(ANSI_RED + "[ Операция невозможна из-за блокировки счетов транзакции ! ]");
            System.out.println("from: " + fromAccStatus + " to: " + toAccStatus + ANSI_RESET);// причина блокировки
        }
    }
    public void accountsList() // на всякий пожарный
    {
        accounts.forEach(account -> System.out.println("N " + account.getAccNumber() + " - " + account.getMoney() + " Status: " + account.isStatus()));
    }

    long getBalance(int accountNum)
    {
        return accounts.get(accountNum).getMoney();
    }
}
