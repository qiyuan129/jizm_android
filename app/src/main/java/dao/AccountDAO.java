package dao;

import java.util.List;

import pojo.Account;

public interface AccountDAO {

    List<Account> listAccount();

    void insertAccount(Account account);

    List<Account> getAyncAccount();

    Long getMaxAnchor();


}
