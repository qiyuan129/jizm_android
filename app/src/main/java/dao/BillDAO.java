package dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pojo.Bill;

public interface BillDAO {
    void insertBill(Bill bill);

    List<Bill> listBill();

    void updateBill(Bill bill);

    void deleteBill(int id);

    Bill getBillById(int id);

    List<Double> monthlyIncome(int year);

    List<Double> monthlyOutcome(int year);

    List<Bill> top10(Date begin,Date end,int type);

    Map<Integer, Double> getCategoryChartData(Date begin,Date end,int type);

    double getAllmoney(Date begin,Date end,int type);

    List<Bill> getSyncBill();

    Date getMaxAnchor();

    void setStateAndAnchor(int id, int state, Date anchor);

    void setState(int id,int state);

}
