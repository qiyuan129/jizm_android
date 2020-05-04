package dao;

import java.util.List;

import pojo.Bill;
import pojo.Category;

public interface CategoryDAO {

    List<Category> listCategory();

    void insertCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(int id);




}
