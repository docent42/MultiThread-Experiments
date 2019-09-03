import java.util.concurrent.atomic.AtomicBoolean;

class Account
{
    private long money;// не стал менять и так все хорошо работает:)
    private int accNumber;
    // поле для ограничения доступа к счету на время проверки в СБ
    private AtomicBoolean access = new AtomicBoolean(true);
    private AtomicBoolean status = new AtomicBoolean(true); // рабочий счет или нет

    Account(long money, int accNumber) {
        this.money = money;
        this.accNumber = accNumber;
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

    boolean isAccess() { return access.get(); }

    void blockAccount() {
        status.set(false);
    }

    void closeAccess() { access.set(false); }

    void openAccess() { access.set(true); }
}
