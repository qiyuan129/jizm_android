package dao;

import java.util.Date;
import java.util.List;

import pojo.Bill;
import pojo.Category;
import pojo.Periodic;

public interface PeriodicDAO {

    List<Periodic> listPeriodic();

    void insertPeriodic(Periodic  periodic);

    void insertPeriodicById(Periodic periodic);

    void updatePeriodic(Periodic  periodic);

    void deletePeriodic(int id);

    Periodic getPeriodicById(int id);

    List<Periodic> getSyncPeriodic();

    Date getMaxAnchor();

    void setStateAndAnchor(int id, int state, Date anchor);

    void setState(int id,int state);

}
