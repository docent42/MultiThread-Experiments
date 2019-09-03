import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main
{
    static Bank bank;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    //
    public static void main(String[] args) throws InterruptedException
    {
        // ====================================== Подготовка ======================================================

        bank = Bank.getInstance();
        long amount;// создаем банк: 1000 счетов
        ExecutorService executor = Executors.newFixedThreadPool(100);// Создаем пул с лимитом 100 потоков
        long now = System.currentTimeMillis();
        int interval = 2000;// стартовый темп выполнения транзакций

        //==================================== Начало стресс теста ================================================

        for (int i = 0; i < 5000;i++)
        {
            if ((System.currentTimeMillis() - now) >= 5000)
            {
                interval /= 2;
                System.out.printf(ANSI_CYAN + "***********************   < %.3f операция в секунду >   **************************%n" + ANSI_RESET,(1000 / (double)interval));
                now = System.currentTimeMillis();
            }
            TimeUnit.MILLISECONDS.sleep(interval);// имитация темпа транзакций
            long start = System.currentTimeMillis();
            int from = 0,to = 0;
            while (from == to)
            {
                from = random(1000);
                to = random(1000);
            }
            amount = (Math.random() > 0.05) ? Math.round(Math.random() * 10000 + 100) : 55000;
            executor.submit(new TransferMaker(start,i,from,to,amount));
        }
        //============= Конец стресстеста, тормозим адскую машинку, время рстановки 120 сек. ====================
        try {
            System.out.println("<<<<<<< Подготовка к остановке Executor >>>>>>>>>");
            executor.shutdown();
            executor.awaitTermination(120, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        //========================== прерываем незавершившиеся процессы за 120 сек ===================================
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("<<<<<<< Executor остановлен >>>>>>>");
            System.out.printf("%nКоличество блокировок по транзакциям - %d%n",Bank.getBlockCount());
        }
    }

    private static int random(int Amount)
    {
        int rnd = (int) (Math.random() * (Amount) + 1);
        if (rnd > Amount) rnd = Amount;
        return rnd;
    }
}
