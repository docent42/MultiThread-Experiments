import java.util.concurrent.atomic.AtomicBoolean;

class Account
{
    private long money;// не стал менять и так все хорошо работает:)
    private int accNumber;
    private AtomicBoolean status = new AtomicBoolean(true);// на всякий пожарный

    Account(long money, int accNumber) {
        this.money = money;
        this.accNumber = accNumber;
        assert false;
    }

    long getMoney() {
        return money;
    }

    void setMoney(long money)
    {
        this.money = money;
    }

    int getAccNumber() {
        return accNumber;
    }

    boolean isStatus() {
        return status.get();
    }

    void blockAccount() {
        status.set(false);
    }
}
