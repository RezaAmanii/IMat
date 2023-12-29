package imat.categories;

import imat.BossController;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.LinkedList;
import java.util.List;

public class subCategories extends AnchorPane {

    IMatDataHandler backEnd = IMatDataHandler.getInstance();
    ProductCategory[] productCategories;

    public static List<subCategories.subCategory> subCategoriesList = new LinkedList<>();

    public subCategories(){
    }

//    static{
//        subCategoriesList.add(new subCategories.subCategory("Dryck", ProductCategory.HOT_DRINKS, ProductCategory.HOT_DRINKS));
//        subCategoriesList.add(new subCategories.subCategory("Frukt&Grönt",ProductCategory.BERRY, ProductCategory.CABBAGE, ProductCategory.CITRUS_FRUIT, ProductCategory.FRUIT, ProductCategory.EXOTIC_FRUIT, ProductCategory.HERB, ProductCategory.MELONS, ProductCategory.ROOT_VEGETABLE, ProductCategory.VEGETABLE_FRUIT));
//        subCategoriesList.add(new subCategories.subCategory("Kött&Fisk", ProductCategory.FISH, ProductCategory.MEAT));
//        subCategoriesList.add(new subCategories.subCategory("Mejeri,Ost&Ägg", ProductCategory.DAIRIES));
//        subCategoriesList.add(new subCategories.subCategory("Torrvaror", ProductCategory.BREAD, ProductCategory.FLOUR_SUGAR_SALT, ProductCategory.NUTS_AND_SEEDS, ProductCategory.PASTA, ProductCategory.POTATO_RICE, ProductCategory.POD, ProductCategory.SWEET));
//    }

    public static class subCategory {
        private String nameOfSubCategory;
        public ProductCategory[] categories;
        private BossController parentController;
        public SubCategoriesEnum[] subCategories;


        public subCategory(String nameOfSubCategory, ProductCategory...categories){
            this.categories = categories;
            this.nameOfSubCategory = nameOfSubCategory;
        }
    }

    public enum SubCategoriesEnum {
        //Kött & Fisk
        FLÄSK,
        NÖTKÖTT,
        FÅGEL,
        FISK_SKALDJUR,
        //Frukt & Grönt
        FRUKT_BÄR,
        GRÖNSAKER_ROTFRUKTER,
        NÖTTER_FRÖN,
        ÖVRIGT,
        //Mejeri
        MJÖLK_YOGHURT,
        OST,
        ÄGG,
        LAKTOSFRITT,
        //Torrvaror
        BRÖD,
        PASTA_RIS_POTATIS,
        MJÖL_SOCKER_SALT,
        SÖTSAKER,
        //Dryck
        VARMA_DRYCKER,
        LÄSK,
        FRUKTDRYCK,
        VATTEN;

        private SubCategoriesEnum(){
        }
    }



}
