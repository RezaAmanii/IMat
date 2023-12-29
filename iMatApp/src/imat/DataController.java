package imat;

import imat.cart.CartView;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

public class DataController {
    private static DataController instance = null;
    private IMatDataHandler iMatDataHandler;

    //ShoppingCart
    @FXML private ScrollPane cartScrollPane;
    @FXML private FlowPane cartFlowPane;

    protected DataController(){}

    public static DataController getInstance() {
        if (instance == null) {
            instance = new DataController();
            instance.init();
        }
        return instance;
    }

    private void init() {
        iMatDataHandler = IMatDataHandler.getInstance();
    }

    public void addToShoppingCart(Product p) {
        ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();
//        ShoppingItem item = new ShoppingItem(p);
//        DataController.getInstance().getShoppingCart().addItem(item);
        shoppingCart.addProduct(p);
    }
    public void subtractFromShoppingCart(Product p) {
        ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();

        shoppingCart.removeProduct(p);
    }

    public void getProductAmount(Product p){

    }

    public ShoppingCart getShoppingCart() {
        return iMatDataHandler.getShoppingCart();
    }

    public void clearShoppingCart() { //Får se om vi implementerar denna.
        iMatDataHandler.getShoppingCart().clear();
    }

    public void shutDown(){ IMatDataHandler.getInstance().shutDown(); }

    public void cartChange() {
        cartFlowPane.getChildren().clear();
        for(ShoppingItem item: iMatDataHandler.getShoppingCart().getItems()){
            cartFlowPane.getChildren().add(new CartView(item));
        }
    }

}
