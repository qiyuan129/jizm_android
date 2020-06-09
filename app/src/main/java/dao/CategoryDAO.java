package dao;

import java.util.Date;
import java.util.List;

import pojo.Bill;
import pojo.Category;

public interface CategoryDAO {

    List<Category> listCategory();

    void insertCategory(Category category);

    void insertCategoryById(Category category);

    void updateCategory(Category category);

    void deleteCategory(int id);

    void deleteAll();

    Category getCategoryById(int id);

    List<Category> getSyncCategory();

    Date getMaxAnchor();

    void setStateAndAnchor(int id, int state, Date anchor);

    void setState(int id,int state);
}
