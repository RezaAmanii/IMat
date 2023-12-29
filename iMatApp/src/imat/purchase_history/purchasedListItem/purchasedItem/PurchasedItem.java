package imat.purchase_history.purchasedListItem.purchasedItem;

import javafx.fxml.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class PurchasedItem extends AnchorPane{
    @FXML
    Label productNameLabel, productPriceLabel, productAmountLabel;

    //IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();

    private Product product;

    public PurchasedItem(int amount, Product product, double totalprice){
        this.product = product;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchaseditem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        //System.out.println(amount);
        productAmountLabel.setText(String.valueOf(amount) + " " + String.valueOf(product.getUnitSuffix()));
        productNameLabel.setText(product.getName());
        productPriceLabel.setText(String.format("%.2f", totalprice) + " kr");





    }
}
