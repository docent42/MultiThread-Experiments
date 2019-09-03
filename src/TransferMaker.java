public class TransferMaker implements Runnable
{
    private long start;// время начала операции
    private int transferNum;// номер транзакции
    private int accountFrom;// счет отправителя
    private int accountTo;// счет получателя
    private long amount;// размер перевода

    TransferMaker(long start, int transferNum, int accountFrom, int accountTo, long amount) {
        this.start = start;
        this.transferNum = transferNum;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
    @Override
    public void run()
    {
        System.out.printf("Транзакция <%d> Старт. Перевод: %d руб. FromAccN: %d ToAccN: %d %n"
                ,transferNum,amount,accountFrom,accountTo);
        System.out.printf("    <Начальные остатки> from: %-10d  to: %d%n"
                ,Main.bank.getBalance(accountFrom),Main.bank.getBalance(accountTo));
        try {
            Main.bank.transfer(accountFrom,accountTo,amount,transferNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("     <Конечные остатки> from: %-10d  to: %d%n"
                ,Main.bank.getBalance(accountFrom),Main.bank.getBalance(accountTo));
        System.out.printf("Транзакция <%d> Финиш. Время операции: %.3f сек.%n"
                ,transferNum,(double)(System.currentTimeMillis() - start)/1000);
        System.out.println("---------------------------------------------------------------------------------");
    }
}
