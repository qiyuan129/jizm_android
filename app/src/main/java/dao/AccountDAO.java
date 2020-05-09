package dao;

import java.util.Date;
import java.util.List;

import pojo.Account;

public interface AccountDAO {

    List<Account> listAccount();

    void insertAccount(Account account);

    Account getAccountById(int id);

    void updateAccount(Account account);

    void deleteAccount(int id);

    List<Account> getSyncAccount();

    Date getMaxAnchor();

    void setStateAndAnchor(int id, int state, Date anchor);


}
