package imat.detailView;

import imat.BossController;
import imat.DataController;
//import imat.products.IProductListener;
import imat.cart.ICartViewListener;
import imat.products.ProductListController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static imat.products.ProductListController.*;

public class detailViewController extends AnchorPane {

    private BossController parentController;
    private Product product;
    private ShoppingItem cartItem;

    public static final List<IDetailViewListener> listeners = new ArrayList<>();

    private IMatDataHandler backEnd = IMatDataHandler.getInstance();
    //public ProductListController productsController = new ProductListController();
    public ProductListController productsController;
    private boolean isFavorite;
    private DataController dataController = DataController.getInstance();


    @FXML private ImageView detailViewExitImage;
    @FXML private ImageView detailViewProductImage;
    @FXML private ImageView detailViewEco;
    @FXML private ImageView detailViewFavorite;
    @FXML private ImageView detailViewIncrementButton;
    @FXML private ImageView detailViewDecrementButton;
    @FXML private TextField detailViewPriceTextField;
    @FXML private Label detailViewProductName;
    @FXML private Label detailViewPriceLabel;
    @FXML private Label breadCrumb;


    Image exitButtonEmpty = new Image("/imat/resources/exitEmpty.png");
    Image exitButtonFilled = new Image("/imat/resources/exitFilled.png");
    Image incrementButton = new Image("/imat/resources/incrementButton.png");
    Image decrementButton = new Image("/imat/resources/decrementButton.png");


    public detailViewController(BossController parentController, ShoppingItem cartItem, ProductListController productsController){
        //System.out.println(cartItem);
        this.productsController = productsController;
        this.cartItem = cartItem;
        this.product = cartItem.getProduct();
        this.parentController = parentController;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detailView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        populateDetailView(product);

        //productsController = new ProductListController(cartItem);
    }


    public void populateDetailView(Product product) {
        this.isFavorite = backEnd.isFavorite(this.product);
        detailViewPriceLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        detailViewProductName.setText(product.getName());
        detailViewProductImage.setImage(backEnd.getFXImage(product));
        detailViewExitImage.setImage(exitButtonEmpty);
        detailViewIncrementButton.setImage(incrementButton);
        detailViewPriceTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
        detailViewPriceTextField.setText(String.valueOf((int)cartItem.getAmount()));
        detailViewIncrementButton.setOnMouseClicked(event-> productsController.incrementMethod(detailViewPriceTextField, dataController, product, cartItem));
        detailViewFavorite.setOnMouseClicked(event -> productsController.setToFavorite(this.product, detailViewFavorite));
        detailViewFavorite.setOnMouseClicked(event -> favoriteChange(this.product, detailViewFavorite));
        detailViewDecrementButton.setImage(decrementButton);
        detailViewDecrementButton.setOnMouseClicked(event -> productsController.decrementMethod(detailViewPriceTextField, dataController, cartItem));
        detailViewExitImage.setOnMouseEntered(event -> detailViewExitImage.setImage(exitButtonFilled));
        detailViewExitImage.setOnMouseExited(event -> detailViewExitImage.setImage(exitButtonEmpty));

        Tooltip incrementTip = new Tooltip("Lägg till");
        incrementTip.setStyle("-fx-font-size: 15pt;");
        parentController.getToolTipsForImageView(detailViewIncrementButton, incrementTip);

        Tooltip decrementTip = new Tooltip("Ta bort");
        decrementTip.setStyle("-fx-font-size: 15pt;");
        parentController.getToolTipsForImageView(detailViewDecrementButton, decrementTip);

        Tooltip favoriterTip = new Tooltip("Lägg till favoriter");
        favoriterTip.setStyle("-fx-font-size: 15pt;");
        parentController.getToolTipsForImageView(detailViewFavorite, favoriterTip);

        Tooltip closeTip = new Tooltip("Stäng");
        closeTip.setStyle("-fx-font-size: 15pt;");
        parentController.getToolTipsForImageView(detailViewExitImage, closeTip);


        String breadCrumbsPath = "HuvudMeny > " + product.getCategory().toString() + " > " + product.getName();
        breadCrumb.setText(breadCrumbsPath);


        //detailViewFavorite.setOnMouseClicked(event -> productsController.setToFavorite());


        if (product.isEcological()) {
            detailViewEco.setImage(Eco);
        }

        if (isFavorite) {
            detailViewFavorite.setImage(favorited);
            //backEnd.addFavorite(product);
        } else {
            detailViewFavorite.setImage(notFavorited);
            //backEnd.removeFavorite(product);
        }
    }

    private void favoriteChange(Product prod, ImageView iView){
        productsController.setToFavorite(prod, iView);

    }

//    private void setToFavorite(){
//        isFavorite = !isFavorite;
//        if(isFavorite){
//            detailViewFavorite.setImage(favorited);
//            backEnd.addFavorite(product);
//        }else {
//            detailViewFavorite.setImage(notFavorited);
//            backEnd.removeFavorite(product);
//        }
//    }



    @FXML
    private void quantityFieldChange(ShoppingItem cartItem){
        if(Integer.parseInt(detailViewPriceTextField.getText()) == cartItem.getAmount()){ //om någon ändrar tillbaka till det som stod från början/det antal som finns i varukorgen
            return;
        }
        if (Integer.parseInt(detailViewPriceTextField.getText()) < 0) {
            detailViewPriceTextField.setText(String.valueOf((int)cartItem.getAmount()));
            return;
        }
        cartItem.setAmount(Double.parseDouble(detailViewPriceTextField.getText()));
        if(cartItem.getAmount() > 0) {
            if (!backEnd.getShoppingCart().getItems().contains(cartItem)) {
                backEnd.getShoppingCart().addItem(cartItem);
            }
        }
        //informProductChange();
    }

    void exitMethod(){

    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }

    @FXML
    public void closeDetailView(){
        //informProductChange();
        productsController.detailViewUpdates(cartItem);
        parentController.closeDetailViews();

    }

//    public void informProductChange(){
//        //System.out.println("ye");
//
//        for(IDetailViewListener l: listeners)
//            //l.informChange(this, this.cartItem);
//            l.informChange(this);
//            System.out.println(listeners);
//    }
//
//    public static void addListener(IDetailViewListener listener){
//        listeners.add(listener);
//    }


//    public ShoppingItem getCartItem(){
//        return this.cartItem;
//    }

}
