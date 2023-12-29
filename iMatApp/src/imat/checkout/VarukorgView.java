package imat.checkout;

import imat.BossController;
import imat.cart.CartView;
import imat.cart.ICartViewListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class VarukorgView extends AnchorPane implements ICartViewListener {

    private IMatDataHandler handler = IMatDataHandler.getInstance();

    @FXML
    private FlowPane checkoutCartPane;

    @FXML
    private ImageView trashImageView;

    @FXML
    private Label shoppingCartSum;

    private BossController mainController;

    public VarukorgView(BossController mainController) {
        this.mainController = mainController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("varukorg.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        trashImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event->  emptyShoppingCart());
        shoppingCartSum.setText(String.format("%.2f", handler.getShoppingCart().getTotal()) + " kr");
        varukorgInit();
        CartView.addListener(this);
    }

    public void varukorgInit(){
        for(ShoppingItem item: handler.getShoppingCart().getItems()){
            checkoutCartPane.getChildren().add(new CartView(item));
        }
    }

    private void emptyShoppingCart(){
        handler.getShoppingCart().clear();
        checkoutCartPane.getChildren().clear();
        for(ShoppingItem item: handler.getShoppingCart().getItems()){
            checkoutCartPane.getChildren().add(new CartView(item));
        }
        mainController.cartChange();
        shoppingCartSum.setText(String.format("%.2f", handler.getShoppingCart().getTotal()) + " kr");
    }

    private void incrementMethod(){

    }

    @Override
    public void updateCart(CartView item) {
        checkoutCartPane.getChildren().clear();
        shoppingCartSum.setText(String.format("%.2f", handler.getShoppingCart().getTotal()) + " kr");
        varukorgInit();
    }
}
