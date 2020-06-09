package dao;

import java.util.Date;
import java.util.List;

import pojo.Account;

public interface AccountDAO {

    List<Account> listAccount();

    void insertAccount(Account account);

    void insertAccountById(Account account);

    Account getAccountById(int id);

    void updateAccount(Account account);

    void deleteAccount(int id);

    void deleteAll();

    List<Account> getSyncAccount();

    Date getMaxAnchor();

    void setStateAndAnchor(int id, int state, Date anchor);

    void setState(int id,int state);


}
