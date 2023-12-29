package imat.categories;

import imat.BossController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Categories extends AnchorPane {


    @FXML private Label subCategoryLabel;
    @FXML private FlowPane subCategoryFlowPane;
    @FXML private FlowPane itemFlowPane;



    IMatDataHandler backEnd = IMatDataHandler.getInstance();
    ProductCategory[] productCategories;


    public static  List<Categories.Category> categoriesList = new LinkedList<>();
    //public static  List<Categories.Category> subCategoriesList = new LinkedList<>();

     public Categories(){
             FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SubCategories.fxml"));
             fxmlLoader.setRoot(this);
             fxmlLoader.setController(this);

             try {
                 fxmlLoader.load();
             } catch (IOException exception) {
                 throw new RuntimeException(exception);
             }


         }

        static{
            categoriesList.add(new Categories.Category("Dryck",ProductCategory.HOT_DRINKS, ProductCategory.COLD_DRINKS));
            categoriesList.add(new Categories.Category("Frukt&Grönt",ProductCategory.BERRY, ProductCategory.CABBAGE, ProductCategory.CITRUS_FRUIT, ProductCategory.FRUIT, ProductCategory.EXOTIC_FRUIT, ProductCategory.HERB, ProductCategory.MELONS, ProductCategory.ROOT_VEGETABLE, ProductCategory.VEGETABLE_FRUIT));
            categoriesList.add(new Categories.Category("Kött&Fisk", ProductCategory.FISH, ProductCategory.MEAT));
            categoriesList.add(new Categories.Category("Mejeri,Ost&Ägg", ProductCategory.DAIRIES));
            categoriesList.add(new Categories.Category("Torrvaror", ProductCategory.BREAD, ProductCategory.FLOUR_SUGAR_SALT, ProductCategory.NUTS_AND_SEEDS, ProductCategory.PASTA, ProductCategory.POTATO_RICE, ProductCategory.POD, ProductCategory.SWEET));
        }


    public static class Category {
        private String nameOfCategory;
        public ProductCategory[] categories;
        private BossController parentController;


        public Category(String nameOfCategory, ProductCategory...categories){
            this.categories = categories;
            this.nameOfCategory = nameOfCategory;
        }
    }



}
