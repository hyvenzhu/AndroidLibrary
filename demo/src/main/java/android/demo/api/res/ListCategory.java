package android.demo.api.res;

import java.io.Serializable;
import java.util.List;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public class ListCategory implements Serializable {
    List<Category> categoryList;
    
    public static class Category {
        String categoryId;
        String categoryName;
    
        @Override
        public String toString() {
            return "Category{" +
                    "categoryId='" + categoryId + '\'' +
                    ", categoryName='" + categoryName + '\'' +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "ListCategory{" +
                "categoryList=" + categoryList +
                '}';
    }
}
