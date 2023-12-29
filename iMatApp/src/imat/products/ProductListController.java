package imat.products;
import imat.BossController;
import imat.DataController;
import imat.cart.CartView;
import imat.cart.ICartViewListener;
import imat.detailView.IDetailViewListener;
import imat.detailView.detailViewController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.converter.IntegerStringConverter;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProductListController extends AnchorPane implements ICartViewListener  {

    private boolean isFavorite;
    @FXML private Label unitLabel;

    //@FXML private Label quantityLabel;
    @FXML private TextField quantityTextField;
    @FXML private Label productName;
    @FXML private ImageView setFavorite;
    @FXML private Label priceLabel;
    @FXML private ImageView ecoSymbol;
    @FXML private ImageView decrement;
    @FXML private ImageView increment;
    @FXML private AnchorPane myItemPane;
    @FXML private FlowPane itemFlowPane;

    @FXML private ImageView productPicture;

    private Product product;
    private ShoppingItem cartItem;
    private IMatDataHandler backEnd = IMatDataHandler.getInstance();
    private DataController dataController = DataController.getInstance();
    private BossController parentController = new BossController();

    public static final List<IProductListener> listeners = new ArrayList<>(); //om saker inte funkar kolla lists import

    private static final Image incrementButton = new Image("/imat/resources/incrementButton.png");
    private static final Image decrementButton = new Image("/imat/resources/decrementButton.png");
    public static final Image favorited = new Image("/imat/resources/FilledHeart.png");
    public static final Image notFavorited = new Image("/imat/resources/EmptyHeart.png");
    public static final Image Eco = new Image("/imat/resources/Eco.jpg");




    public ProductListController(ShoppingItem cartItem)  {
    //public ProductListController(Product product){

        this.cartItem = cartItem;
        product = cartItem.getProduct();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //this.product = product;
        //this.cartItem = cartItem;
        productName.setText(product.getName());
        priceLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());

        productPicture.setImage(backEnd.getFXImage(product));
        increment.setImage(incrementButton);
        decrement.setImage(decrementButton);
        if(product.isEcological()){
            ecoSymbol.setImage(Eco);
        }

        if(backEnd.isFavorite( product)){
            setFavorite.setImage(favorited);
            backEnd.addFavorite(product);
        }else{
            setFavorite.setImage(notFavorited);
            backEnd.removeFavorite(product);
        }
        quantityTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
        quantityTextField.addEventHandler(ActionEvent.ACTION, event-> quantityFieldChange(cartItem)); //kan vara anledningen till bugget
        quantityTextField.setText(String.valueOf((int)cartItem.getAmount()));
        productPicture.addEventHandler(MouseEvent.MOUSE_CLICKED, event-> informDetailViewClick());


        quantityTextField.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setupQuantityFieldChange(cartItem));
        increment.setOnMouseClicked(event-> incrementMethod(quantityTextField, dataController, product, cartItem));
        decrement.setOnMouseClicked(event -> decrementMethod(quantityTextField, dataController, cartItem));
        setFavorite.setOnMouseClicked(event ->setToFavorite(this.product, setFavorite));

        CartView.addListener(this);


        // Tooltips for products card
        Tooltip incrementTip = new Tooltip("Lägg till");
        Tooltip decrementTip = new Tooltip("Ta bort");
        Tooltip favoriterTip = new Tooltip("Lägg till favoriter");

        incrementTip.setStyle("-fx-font-size: 15pt;");
        decrementTip.setStyle("-fx-font-size: 15pt;");
        setFavorite.setStyle("-fx-font-size: 15pt;");

        parentController.getToolTipsForImageView(increment, incrementTip);
        parentController.getToolTipsForImageView(decrement, decrementTip);
        parentController.getToolTipsForImageView(setFavorite, favoriterTip);



    }

    private void setupQuantityFieldChange(ShoppingItem cartItem) {
        EventHandler<InputEvent> quantityFieldHandler = new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent event) {
                //quantityTextField.addEventHandler(MouseEvent.MOUSE_EXITED, quantityFieldHandler);
                quantityFieldChange(cartItem);
                event.consume();
            }
        };
        quantityTextField.addEventHandler(MouseEvent.MOUSE_EXITED, quantityFieldHandler);
    }

    private void informDetailViewClick() {
        for(IProductListener l: listeners)
            l.detailedViewShow(this);
    }

//    public static ShoppingItem getCartItem(){
//        return cartItem;
//    }


    public void incrementMethod(TextField textfield, DataController dataController, Product product, ShoppingItem cartItem){
        if(!backEnd.getShoppingCart().getItems().contains(cartItem))
            backEnd.getShoppingCart().addItem(cartItem);

        cartItem.setAmount(cartItem.getAmount() + 1);
        //quantityTextField.setText(String.valueOf((int)Product.getAmount()));
        //BossController.updateItemList();
        updateQuantityField(cartItem, textfield);
        informCartChange();
    }


    public void decrementMethod(TextField textfield, DataController dataController, ShoppingItem cartItem){
        double currentItemAmount = cartItem.getAmount();
        double nextItemAmount = currentItemAmount - 1;
        if((cartItem.getAmount()-1) < 0) {
            return;
        }
        cartItem.setAmount(cartItem.getAmount() - 1);
        if (!(currentItemAmount>1)){
            dataController.subtractFromShoppingCart(cartItem.getProduct());
            //return;
            //textfield.setText(Double.toString(cartItem.getAmount()));
        }

        updateQuantityField(cartItem, textfield);

        informCartChange();
    }

    private void updateQuantityField(ShoppingItem cartItem, TextField textField) {
        textField.setText(String.valueOf((int)cartItem.getAmount()));
    }


    public void setToFavorite(Product prod, ImageView setFavorite){
        isFavorite = backEnd.isFavorite(prod);
        isFavorite = !isFavorite;
        if(isFavorite){
            setFavorite.setImage(favorited);
            backEnd.addFavorite(prod);
        }else {
            setFavorite.setImage(notFavorited);
            backEnd.removeFavorite(prod);
        }
    }

    @FXML
    private void quantityFieldChange(ShoppingItem cartItem){
        if(Integer.parseInt(quantityTextField.getText()) == cartItem.getAmount()){ //om någon ändrar tillbaka till det som stod från början/det antal som finns i varukorgen
            return;
        }

        if (Integer.parseInt(quantityTextField.getText()) < 0) {
            quantityTextField.setText(String.valueOf((int)cartItem.getAmount()));
            return;
        }
        cartItem.setAmount(Double.parseDouble(quantityTextField.getText()));
        if(cartItem.getAmount() > 0) {
            if (!backEnd.getShoppingCart().getItems().contains(cartItem)) {
                backEnd.getShoppingCart().addItem(cartItem);
            }
        }
        if(Integer.parseInt(quantityTextField.getText())== 0){
            if ((backEnd.getShoppingCart().getItems().contains(cartItem))){
                dataController.subtractFromShoppingCart(cartItem.getProduct());
            }
        }
        informCartChange();
    }

    public void informCartChange(){
        for(IProductListener l: listeners)
            l.cartChange(this);
    }

    public static void addListener(IProductListener listener){
        listeners.add(listener);
    }

//    public Product getProduct(){return product;}
//
//    public cartItem getcartItem(){return cartItem;} //kanske ska användas
    public ShoppingItem getShoppingItem() {return this.cartItem;}

    public Product getProduct() {
        return this.product;
    }
    public ProductListController(){

    }
    @Override
    public void updateCart(CartView item) {
        quantityTextField.setText(String.valueOf((int)cartItem.getAmount()));
    }


    public void detailViewUpdates(ShoppingItem item) {
        //System.out.println("hi");
        product = item.getProduct();
        if(backEnd.isFavorite( product)){
            setFavorite.setImage(favorited);
            backEnd.addFavorite(product);
        }else{
            setFavorite.setImage(notFavorited);
            backEnd.removeFavorite(product);
        }
        quantityTextField.setText(String.valueOf((int)cartItem.getAmount()));
    }


}
