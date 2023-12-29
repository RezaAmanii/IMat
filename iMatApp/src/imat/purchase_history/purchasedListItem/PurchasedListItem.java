package imat.purchase_history.purchasedListItem;

import imat.BossController;
import imat.cart.CartView;
import imat.cart.ICartViewListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.text.SimpleDateFormat;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import imat.purchase_history.purchasedListItem.purchasedItem.PurchasedItem;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PurchasedListItem extends AnchorPane{

    private Order order;
    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    BossController parentController = new BossController();
    public static final List<IPurchasedListItemListener> listeners = new ArrayList<>();
    private boolean isExtended;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @FXML
    Label orderDateLabel, orderPriceLabel;
    @FXML
    FlowPane articleList;

    @FXML
    Button showItemsButton;
    @FXML private ImageView arrow;
    @FXML private Button kopieraTillVarukorgenButton;
    ShoppingCart shoppingCart;


    Image arrowUp = new Image("/imat/resources/Up.png");
    Image arrowDown = new Image("/imat/resources/Down.png");



    public PurchasedListItem(Order order){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchasedlistitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.order = order;
        orderDateLabel.setText(dateFormat.format(order.getDate()));
        //orderNumberLabel.setText(String.valueOf(order.getOrderNumber()));
        orderPriceLabel.setText(String.format("%.2f", getPrice(order.getItems())) + " kr");
        arrow.setImage(arrowDown);

        kopieraTillVarukorgenButton.setOnAction(event -> addToCart());


    }

    private double getPrice(List<ShoppingItem> itemList){
        double price = 0;
        for(ShoppingItem item : itemList){
            price += item.getProduct().getPrice() * item.getAmount();
        }
        return price;
    }

    @FXML
    private void showOrderItems(){
        if(!isExtended){
            for(ShoppingItem shoppingItem: order.getItems())
                articleList.getChildren().add(new PurchasedItem((int) shoppingItem.getAmount(),
                        shoppingItem.getProduct(), shoppingItem.getTotal()));
            arrow.setImage(arrowUp);

            articleList.getStyleClass().add("extendedList");
            showItemsButton.setText("Dölj Artiklar");

        }
        else{
            articleList.getChildren().clear();
            articleList.getStyleClass().clear();
            showItemsButton.setText("Visa Artiklar");
            arrow.setImage(arrowDown);
        }
        isExtended = !isExtended;
    }


    public void addToCart(){
        for(ShoppingItem shoppingItem : order.getItems()){
            iMatDataHandler.getShoppingCart().addItem(shoppingItem);
            System.out.println(shoppingItem.getProduct().getName() +" added to the cart");
            informShoppingCartCopy();
        }
    }

    public void informShoppingCartCopy(){
        for(IPurchasedListItemListener l: listeners)
            l.copiedOrder(this);
    }


    public static void addListener(IPurchasedListItemListener listener){
        listeners.add(listener);
    }






}
