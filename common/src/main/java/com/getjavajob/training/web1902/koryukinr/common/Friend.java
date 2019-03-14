package com.getjavajob.training.web1902.koryukinr.common;

public class Friend {
    private Account accountFrom;
    private Account accountTo;
    private Status status;

    public Friend(Account accountFrom, Account accountTo, Status status) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.status = status;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
