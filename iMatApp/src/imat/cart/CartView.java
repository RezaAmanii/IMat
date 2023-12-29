package imat.cart;

import imat.DataController;
import imat.checkout.VarukorgView;
import imat.detailView.IDetailViewListener;
import imat.detailView.detailViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static imat.checkout.VarukorgView.*;

public class CartView extends AnchorPane implements IDetailViewListener {

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    DataController dataController = DataController.getInstance();

    public static final List<ICartViewListener> listeners = new ArrayList<>();

    //private static CartView self;

    @FXML private Label productNameLabel;
    //@FXML private Label productPriceLabel;
    @FXML private Label productUnitLabel;
    @FXML private Label productSumLabel;
    @FXML private TextField productAmount;
    @FXML private ImageView plusImageView;
    @FXML private ImageView minusImageView;

    private final ShoppingItem shoppingItem;

    public CartView(ShoppingItem item) {
        this.shoppingItem = item;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cartview.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        productNameLabel.setText(shoppingItem.getProduct().getName());
        //productPriceLabel.setText(String.format("%.2f",shoppingItem.getProduct().getPrice()) + shoppingItem.getProduct().getUnit());
        //productUnitLabel.setText(shoppingItem.getProduct().getUnit());
        productSumLabel.setText(String.format("%.2f",shoppingItem.getTotal()) + " kr");
        productAmount.setText(String.valueOf((int)shoppingItem.getAmount()));
        //shoppingItem.getProduct().getUnitSuffix();

        productAmount.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
        productAmount.addEventHandler(ActionEvent.ACTION, event-> productAmountChange(shoppingItem));
        productAmount.setText(String.valueOf((int)shoppingItem.getAmount()));

        plusImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event-> handlePlusClick());
        minusImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event-> handleMinusClick());
        productAmount.addEventHandler(MouseEvent.MOUSE_EXITED, event-> productAmountChange(item));
        //detailViewController.addListener(this);
    }

    private void productAmountChange(ShoppingItem shoppingItem) {
        if(Integer.parseInt(productAmount.getText()) == shoppingItem.getAmount()){ //om någon ändrar tillbaka till det som stod från början/det antal som finns i varukorgen
            return;
        }
        if (Integer.parseInt(productAmount.getText()) < 0) {
            productAmount.setText(String.valueOf((int)shoppingItem.getAmount()));
            return;
        }
        shoppingItem.setAmount(Double.parseDouble(productAmount.getText()));
        if(shoppingItem.getAmount() > 0) {
            if (!iMatDataHandler.getShoppingCart().getItems().contains(shoppingItem)) {
                iMatDataHandler.getShoppingCart().addItem(shoppingItem);
            }
            if(Integer.parseInt(productAmount.getText())== 0) {

                if (!(iMatDataHandler.getShoppingCart().getItems().contains(shoppingItem))) {
                    //System.out.println("ye");
                    removeItem(shoppingItem);
                    return;
                }
            }
        }
        //VarukorgView.varukorgInit();
        informShoppingCartChange();
    }

    private void handlePlusClick(){
        if(!iMatDataHandler.getShoppingCart().getItems().contains(shoppingItem))
            iMatDataHandler.getShoppingCart().addItem(shoppingItem);

        shoppingItem.setAmount(shoppingItem.getAmount() + 1);
        informShoppingCartChange();
    }

    private void handleMinusClick(){
        double currentItemAmount = shoppingItem.getAmount();
        double nextItemAmount = currentItemAmount - 1;
        if((shoppingItem.getAmount()-1) < 0) {
            return;
        }
        //System.out.println("ji");
        shoppingItem.setAmount(shoppingItem.getAmount() - 1);
        if (!(currentItemAmount>1)){
            dataController.subtractFromShoppingCart(shoppingItem.getProduct());
        }
        informShoppingCartChange();
    }

    private void removeItem(ShoppingItem shoppingItem) {
        dataController.subtractFromShoppingCart(shoppingItem.getProduct());
        informShoppingCartChange();
    }

//    private void cartChanged(){
//        //dataController.cartChange();
//    }

    public void informShoppingCartChange(){
        for(ICartViewListener l: listeners)
            l.updateCart(this);
    }


    public static void addListener(ICartViewListener listener){
        listeners.add(listener);
    }

    @Override
    //public void informChange(detailViewController detailview, ShoppingItem cartItem) {
    public void informChange(detailViewController detailview) {
        //System.out.println("made it");
        informShoppingCartChange();
    }

    //public static void addListener(ICartItemListener listener){
    //   listeners.add(listener);
    //}

}
