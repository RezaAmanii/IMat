
package imat;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

import imat.cart.CartView;
import imat.cart.ICartViewListener;
import imat.categories.Categories;
import imat.checkout.CheckoutView;
import imat.checkout.confirmedOrder.ConfirmedPurchaseView;
import imat.detailView.detailViewController;
import imat.products.IProductListener;
import imat.profile.ProfileView;
import imat.purchase_history.PurchaseHistoryView;
import imat.purchase_history.purchasedListItem.IPurchasedListItemListener;
import imat.purchase_history.purchasedListItem.PurchasedListItem;
import imat.start.StartPageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;
import imat.products.ProductListController;



import static imat.categories.Categories.categoriesList;

public class BossController implements Initializable, ShoppingCartListener, IProductListener, ICartViewListener, IPurchasedListItemListener {


    @FXML private AnchorPane pageSwitchPane;
    @FXML private ScrollPane scrollPane;
    @FXML private FlowPane itemFlowPane;
    @FXML private AnchorPane myItemPane;


    //subkategoriknappar under
    @FXML private AnchorPane subCategoryBar;
    @FXML private Button firstButton;
    @FXML private Button secondButton;
    @FXML private Button thirdButton;
    @FXML private Button fourthButton;


// Buttons and ImageViews for Categories
    @FXML private Button alltButton;
    @FXML private Button dryckButton;
    @FXML private Button fruktButton;
    @FXML private Button kottButton;
    @FXML private Button mejeriButton;
    @FXML private Button skafferiButton;
    @FXML private ImageView dryckImageView;
    @FXML private ImageView fruktImageView;
    @FXML private ImageView kottImageView;
    @FXML private ImageView mejeriImageView;
    @FXML private ImageView torrvarorImageView;
    @FXML private ImageView alltImageView;

    //ShoppingCart
    @FXML private ScrollPane cartScrollPane;
    @FXML private FlowPane cartFlowPane;
    @FXML private Label shoppingCartSum;
    @FXML private ImageView trashImageView;
    @FXML private Button gotoshopButton;

    private static final Image disposeShoppingItems = new Image("/imat/resources/disposal.png");
    private static final Image disposeShoppingItemsTransparent = new Image("/imat/resources/trashTransparent.png");



    //Search components
    @FXML private TextField searchBar;
    @FXML private AnchorPane switchToDetailPane;
    @FXML private Button favoriteButton;
    @FXML private ImageView favoriteImageView;


    // Profil - Tidigare köp - favoriter
    @FXML private Button homeButton;
    @FXML private Button purchaseHistoryButton;
    @FXML private Button profileButton;
    @FXML private Button searchButton;

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    detailViewController detailView;
    Product product;
    StartPageController startPageController = new StartPageController();
    public Map<Product,ProductListController> ListItemsAndProducts;

    //public List<Product> productList = getOrderedProducts();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String iMatDirectory = iMatDataHandler.imatDirectory();
        itemFlowPane.setHgap(30);
        itemFlowPane.setVgap(30);

        ProductListController.addListener(this);
        CartView.addListener(this);
        PurchasedListItem.addListener(this);

        productsInit();
        categoryInit();
        shoppingCartInit();

        cartScrollPane.setFitToWidth(true); //verkar inte göra något i detta fallet
        //scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //scrollPane.setHvalue(0); //set heightvalue?

        shoppingCartSum.setText(String.format("%.2f", iMatDataHandler.getShoppingCart().getTotal()) + " kr");

        searchBar.setOnKeyPressed(event->{

            if(event.getCode() == KeyCode.ENTER){
                searchFunction();
                searchBar.clear();

            }
        });


        // Alert
        trashImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, events ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bekräftelse");
            alert.setContentText("Är du säker på att du vill ta bort alla produkter från varukorgen?");

            alert.getButtonTypes().setAll(
                    new ButtonType("Ja"),
                    new ButtonType("Nej")
            );

            alert.showAndWait().ifPresent(response ->{
                if(response.getText().equals("Ja")){
                    trashImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event->  emptyShoppingCart());
                }else if(response.getText().equals("Nej")){
                    alert.close();
                }
            });
        });


        // Tooltips
        Tooltip trashTip = new Tooltip("Töm varukorgen");
        trashTip.setStyle("-fx-font-size: 15pt;");
        getToolTipsForImageView(trashImageView, trashTip);

        Tooltip homeTip = new Tooltip("Till huvudmenyn");
        homeTip.setStyle("-fx-font-size: 15pt;");
        getToolTipsForButtons(homeButton, homeTip);

        Tooltip purchaseTip = new Tooltip("Till tidigare inköpslista");
        purchaseTip.setStyle("-fx-font-size: 15pt;");
        getToolTipsForButtons(purchaseHistoryButton, purchaseTip);

        Tooltip profileTip = new Tooltip("Till profilinställningar");
        profileTip.setStyle("-fx-font-size: 15pt;");
        getToolTipsForButtons(profileButton, profileTip);

        Tooltip favoriterTip = new Tooltip("Sparade favoriter");
        favoriterTip.setStyle("-fx-font-size: 15pt;");
        getToolTipsForButtons(favoriteButton, favoriterTip);

        Tooltip searchTip = new Tooltip("Sök");
        searchTip.setStyle("-fx-font-size: 15pt;");
        getToolTipsForButtons(searchButton, searchTip);

    }

    public void amountItem(){
        System.out.println(iMatDataHandler.getShoppingCart().getItems().size());
    }


    private void alltCategorySetup(ActionEvent event){
        List<Button> buttons = Arrays.asList(alltButton, dryckButton, fruktButton,
                kottButton, skafferiButton, mejeriButton);
        productsInit();
        updateButtonStyleOnClick(event, buttons, "categoryButtons", "categoryButtonPressed");
    }

    private void setAlltButtonPressedStyle(){
        alltButton.getStyleClass().remove("categoryButtons");
        alltButton.getStyleClass().add("categoryButtonPressed");
    }

    private void categoryInit() {
        setAlltButtonPressedStyle();
        alltButton.setContentDisplay(ContentDisplay.TOP);
        alltButton.setOnAction(event -> alltCategorySetup(event));//TODO byt till något som inte är productsinit, vi kör vissa saker som vi inte behöver kö


        List<Product> drycksList = getProductsByCategory(ProductCategory.HOT_DRINKS, ProductCategory.COLD_DRINKS);
        dryckButton.setContentDisplay(ContentDisplay.TOP);
        dryckButton.setOnAction(event -> categorySetup(drycksList, "dryck", event));


        List<Product> fruktOchGrontList = getProductsByCategory(ProductCategory.BERRY, ProductCategory.CABBAGE,
                ProductCategory.CITRUS_FRUIT, ProductCategory.FRUIT, ProductCategory.EXOTIC_FRUIT,
                ProductCategory.HERB, ProductCategory.MELONS, ProductCategory.ROOT_VEGETABLE,
                ProductCategory.VEGETABLE_FRUIT);
        fruktButton.setContentDisplay(ContentDisplay.TOP);
        fruktButton.setOnAction(event -> categorySetup(fruktOchGrontList, "fruktOchGront", event));

        List<Product> kottOchFisk = getProductsByCategory(ProductCategory.FISH, ProductCategory.MEAT);
        kottButton.setContentDisplay(ContentDisplay.TOP);
        kottButton.setOnAction(event -> categorySetup(kottOchFisk, "kottOchFisk", event));


        List<Product> torrvarorsList = getProductsByCategory(ProductCategory.BREAD,
                ProductCategory.FLOUR_SUGAR_SALT, ProductCategory.NUTS_AND_SEEDS, ProductCategory.PASTA,
                ProductCategory.POTATO_RICE, ProductCategory.POD, ProductCategory.SWEET);
        skafferiButton.setContentDisplay(ContentDisplay.TOP);
        skafferiButton.setOnAction(event -> categorySetup(torrvarorsList, "torrvaror", event));

        List<Product> mejeriList = getProductsByCategory(ProductCategory.DAIRIES);
        mejeriButton.setContentDisplay(ContentDisplay.TOP);
        mejeriButton.setOnAction(event -> categorySetup(mejeriList, "mejeri", event));

        List<Product> favoriteList = iMatDataHandler.favorites();
        favoriteButton.setOnAction(event->favoriteSetup(favoriteList, event));
        //favoriteImageView.setOnMouseClicked(event->favoriteSetup(favoriteList, event));
    }

    private void favoriteSetup(List<Product> favList, ActionEvent event){
        openStartPage();
        categorySetup(favList, "favorite", event);
    }

    public List<Product> getProductsByCategory(ProductCategory...categories){
        List<Product> productList = new LinkedList<>();

        for(Categories.Category cat: categoriesList){
            for(ProductCategory category : categories){
                if(containsCategory(cat.categories, category)){
                    //System.out.println(iMatDataHandler.getProducts(category) + "yo");
                    productList.addAll(iMatDataHandler.getProducts(category));
                }
            }
        }
        return productList;
    }

    public List<Product> getSpecificProducts(List<String> sList){
        //itemFlowPane.getChildren().clear();
        List<Product> foundProductsList = new LinkedList<>();
        //System.out.println(sList);
        for(String s: sList){
            foundProductsList.addAll(iMatDataHandler.findProducts(s.trim().toLowerCase()));
        }
        return foundProductsList;

    }

//    public List<Product> getSpecificProducts(String s){
//        //itemFlowPane.getChildren().clear();
//
//        List <Product> foundProductsList = iMatDataHandler.findProducts(s.trim().toLowerCase()) ;
//        System.out.println(foundProductsList);
//
//
//        return foundProductsList;
//
//    }


    public void searchFunction(){
        itemFlowPane.getChildren().clear();

        String searchText = searchBar.getText();
        List <Product> foundProductsList = iMatDataHandler.findProducts(searchText.trim().toLowerCase()) ;
        for (Product products: foundProductsList) {
            itemFlowPane.getChildren().add(ListItemsAndProducts.get(products));
            searchBar.clear();
        }

    }


    private boolean containsCategory(ProductCategory[] categories, ProductCategory category) {
        for (ProductCategory cat : categories) {
            if (cat == category) {
                return true;
            }
        }
        return false;
    }


/*    @FXML
    private void handleDetailViewClick(ActionEvent event){
        openDetailView();

    }*/

    public void openDetailView(ShoppingItem cartItem, ProductListController prodItem){
        switchToDetailPane.getChildren().clear();
        switchToDetailPane.getChildren().add(new detailViewController(this, cartItem, prodItem));
        switchToDetailPane.toFront();
    }



    public void closeDetailViews(){
        switchToDetailPane.toBack();
    }

    @FXML
    private void handleProfileClick(ActionEvent event) {
        openProfileView();
    }

    @FXML
    private void handleHomeClick(ActionEvent event) {
        nonEventHomeClick();
    }

    public void nonEventHomeClick(){
        clearCategoryButtonStyling();
        setAlltButtonPressedStyle();
        homeProductSetup();
    }
    //använder inte denna funktionen på hem knappen,
    // blir problem & tror att det kan vara för att den har samma namn som en metod i ProfileView


    @FXML
    private void handlePurchaseHistoryClick(ActionEvent event){
        openPurchaseHistoryView();
    }


    public void openStartPage() {
        pageSwitchPane.toBack();
    }

    public void homeProductSetup(){
        openStartPage();
        productsInit();

    }

    private void clearCategoryButtonStyling(){
        List<Button> categoryButtons = Arrays.asList(alltButton, dryckButton, fruktButton, kottButton,
                skafferiButton, mejeriButton);
        for(Button button : categoryButtons){
            button.getStyleClass().clear();
            button.getStyleClass().add("categoryButtons");
        }
    }

    public void changeToConfirmedPane(){
        pageSwitchPane.getChildren().clear();
        pageSwitchPane.getChildren().add(new ConfirmedPurchaseView(this));
    }

    public void openProfileView(){
        clearCategoryButtonStyling();
        pageSwitchPane.getChildren().clear();
        pageSwitchPane.getChildren().add(new ProfileView(this));
        pageSwitchPane.toFront();}

    public void openCheckoutView(){
        clearCategoryButtonStyling();
        pageSwitchPane.getChildren().clear();
        pageSwitchPane.getChildren().add(new CheckoutView(this));
        pageSwitchPane.toFront();}

    public void openPurchaseHistoryView(){
        clearCategoryButtonStyling();
        pageSwitchPane.getChildren().clear();
        pageSwitchPane.getChildren().add(new PurchaseHistoryView(this));
        pageSwitchPane.toFront();
    }

    private void productsInit(){
         ListItemsAndProducts = new HashMap<>();
         ListItemsAndProducts = getHashMap();

        List<Product> productsList = getOrderedProducts(iMatDataHandler.getProducts());
        itemFlowPane.getChildren().clear();
        for(Product product: productsList){

            itemFlowPane.getChildren().add(ListItemsAndProducts.get(product));
        }
        scrollPane.setFitToWidth(true); //tar bort horisontell scrollbar
        subCategoryBar.toBack();
    }

    public Map<Product, ProductListController> getHashMap(){
        ListItemsAndProducts = new HashMap<>();

        for(ShoppingItem i: iMatDataHandler.getShoppingCart().getItems()) {
            ListItemsAndProducts.put(i.getProduct(), new ProductListController(i));
        }
        for(Product p: iMatDataHandler.getProducts()) {
            if(!ListItemsAndProducts.containsKey(p))
                ListItemsAndProducts.put(p,new ProductListController(new ShoppingItem(p,0)));
        }
        return ListItemsAndProducts;
    }

    public List<Product> getOrderedProducts(List <Product> unsortedList) {

        //List <Product> unsortedList = iMatDataHandler.getProducts();

        Collections.sort(unsortedList, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });


        return unsortedList;
    }

    private void updateProductList(List<Product> productList, ActionEvent event){
        //getSubCategoryBar(productList);
        List<Button> buttons = Arrays.asList(firstButton, secondButton, thirdButton, fourthButton);
        productList  = getOrderedProducts(productList);
        itemFlowPane.getChildren().clear();
        if((event.getTarget() == firstButton) || (event.getTarget() == secondButton) || (event.getTarget() == thirdButton)
        || (event.getTarget() == fourthButton)){
            updateButtonStyleOnClick(event, buttons, "subCategoryButtons",
                    "subcategoryButtonPressed");
        }
//        for(Product products: productList){
//           itemFlowPane.getChildren().add(new ProductListController(products));
//        }
        for(Product products: productList) {
//            if(!ListItemsAndProducts.containsKey(products))
//                ListItemsAndProducts.put(products,new ProductListController(new ShoppingItem(products,0)));
            //System.out.println(ListItemsAndProducts.get(product) + "yo");
            itemFlowPane.getChildren().add(ListItemsAndProducts.get(products));
        }
    }

    public void updateItemList(){
        cartFlowPane.getChildren().clear();
        for(ShoppingItem item: iMatDataHandler.getShoppingCart().getItems()){
            cartFlowPane.getChildren().add(new CartView(item));
        }
        cartScrollPane.toFront();
        cartScrollPane.setFitToWidth(true); //verkar inte göra något i detta fallet
        if(iMatDataHandler.getShoppingCart().getTotal() > 0){
            gotoshopButton.setDisable(false);
        }
    }

    private void shoppingCartInit(){
        List<ShoppingItem> shoppingCartList = iMatDataHandler.getShoppingCart().getItems();
        cartFlowPane.getChildren().clear();
        updateItemList();
        cartScrollPane.setFitToWidth(true);
        if (iMatDataHandler.getShoppingCart().getItems().isEmpty()){
            gotoshopButton.setDisable(true);
        }
        if(iMatDataHandler.getShoppingCart().getTotal() > 0){
            gotoshopButton.setDisable(false);
        }
        //cartFlowPane.getChildren().add(CartView.getInstance());
        //CartView.getInstance().
    }

    private void categorySetup(List<Product> productList, String category, ActionEvent event){
        //System.out.println(productList);
        List<Button> buttons = Arrays.asList(alltButton, dryckButton, fruktButton,
                kottButton, skafferiButton, mejeriButton);
        updateProductList(productList, event);
        getSubCategoryBar(category);
        clearSubcategoryButtonStyle();
        updateButtonStyleOnClick(event, buttons, "categoryButtons", "categoryButtonPressed");
    }

    private void updateButtonStyleOnClick(ActionEvent event, List<Button> buttons,
                                          String notPressedStyle, String pressedStyle){
        for(Button button : buttons){
            if(button == event.getTarget()){
                if(!(button.getStyleClass().contains(pressedStyle))){
                    button.getStyleClass().remove(notPressedStyle);
                    button.getStyleClass().add(pressedStyle);
                }
            }
            else{
                button.getStyleClass().remove(pressedStyle);
                button.getStyleClass().add(notPressedStyle);
            }
        }
    }

    private void clearSubcategoryButtonStyle(){
        List<Button> buttons = Arrays.asList(firstButton, secondButton, thirdButton, fourthButton);
        for(Button button : buttons){
            button.getStyleClass().remove("subcategoryButtonPressed");
            button.getStyleClass().add("subCategoryButtons");
        }
    }

    private void getSubCategoryBar(String category)
    {
        //System.out.println(category);
        if(category=="fruktOchGront"){
            firstButton.setText("Frukt & Bär");
            secondButton.setText("Grönsaker & Rotfrukter");
            thirdButton.setText("Melon");
            fourthButton.setText("Örter");

            List<Product> fruktOchBarList = getProductsByCategory(ProductCategory.BERRY, ProductCategory.CITRUS_FRUIT, ProductCategory.FRUIT, ProductCategory.EXOTIC_FRUIT);
            firstButton.setOnAction(event -> updateProductList(fruktOchBarList, event));
            List<Product> gronsakerOchRotfrukter = getProductsByCategory(ProductCategory.CABBAGE, ProductCategory.VEGETABLE_FRUIT, ProductCategory.ROOT_VEGETABLE);
            secondButton.setOnAction(event -> updateProductList(gronsakerOchRotfrukter, event));
            List<Product> melonList = getProductsByCategory(ProductCategory.MELONS);
            thirdButton.setOnAction(event -> updateProductList(melonList, event));
            List<Product> ovrigtList = getProductsByCategory(ProductCategory.HERB);
            fourthButton.setOnAction(event -> updateProductList(ovrigtList, event));
        } else if (category=="torrvaror") {
            firstButton.setText("Mjöl, Socker & Salt");
            secondButton.setText("Pasta, Ris & Potatis");
            thirdButton.setText("Sötsaker");
            fourthButton.setText("Övrigt");

            List<Product> mjolList= getProductsByCategory(ProductCategory.FLOUR_SUGAR_SALT);
            firstButton.setOnAction(event -> updateProductList(mjolList, event));
            List<Product> pastaList= getProductsByCategory(ProductCategory.PASTA, ProductCategory.POTATO_RICE);
            secondButton.setOnAction(event -> updateProductList(pastaList, event));
            List<Product> sweetsList= getProductsByCategory(ProductCategory.SWEET);
            thirdButton.setOnAction(event -> updateProductList(sweetsList, event));
            List<Product> brodList= getProductsByCategory(ProductCategory.BREAD, ProductCategory.NUTS_AND_SEEDS, ProductCategory.POD);
            fourthButton.setOnAction(event -> updateProductList(brodList, event));
        } else if (category=="kottOchFisk") {
            firstButton.setText("Fläsk");
            secondButton.setText("Nötkött");
            thirdButton.setText("Fågel");
            fourthButton.setText("Fisk & Skaldjur");

            List<Product> pigList= getSpecificProducts(Arrays.asList("bland"));
            firstButton.setOnAction(event -> updateProductList(pigList, event));
            List<Product> cowList= getSpecificProducts(Arrays.asList("bland", "grytbitar", "tfärs", "högrev"));
            secondButton.setOnAction(event -> updateProductList(cowList, event));
            List<Product> birdList= getSpecificProducts(Arrays.asList("kyckling"));
            thirdButton.setOnAction(event -> updateProductList(birdList, event));
            List<Product> fishList= getProductsByCategory(ProductCategory.FISH);
            fourthButton.setOnAction(event -> updateProductList(fishList, event));
        } else if (category=="mejeri") {
            firstButton.setText("Mjölk & Yoghurt");
            secondButton.setText("Ost");
            thirdButton.setText("Ägg");
            fourthButton.setText("Laktosfritt");

            List<Product> mjolkList= getSpecificProducts(Arrays.asList("mjölk", "yoghurt"));
            firstButton.setOnAction(event -> updateProductList(mjolkList, event));
            List<Product> ostList= getSpecificProducts(Arrays.asList("Brie", "ost", "mozzarella", "västerbotten"));
            secondButton.setOnAction(event -> updateProductList(ostList, event));
            List<Product> eggList= getSpecificProducts(Arrays.asList("ägg"));
            thirdButton.setOnAction(event -> updateProductList(eggList, event));
            List<Product> laktosList= getSpecificProducts(Arrays.asList("laktosfri"));
            fourthButton.setOnAction(event -> updateProductList(laktosList, event));
        } else if (category=="dryck") {
            firstButton.setText("Varma Drycker");
            secondButton.setText("Läsk");
            thirdButton.setText("Fruktdryck");
            fourthButton.setText("Vatten");

            List<Product> hotDrycksList= getProductsByCategory(ProductCategory.HOT_DRINKS);
            firstButton.setOnAction(event -> updateProductList(hotDrycksList, event));
            List<Product> sodaList= getSpecificProducts(Arrays.asList("flaska","burk"));
            secondButton.setOnAction(event -> updateProductList(sodaList, event));
            List<Product> juiceList= getSpecificProducts(Arrays.asList("juice","fruktsoppa"));
            thirdButton.setOnAction(event -> updateProductList(juiceList, event));
            List<Product> waterList= getSpecificProducts(Arrays.asList("vatten"));
            waterList.removeIf(product -> product.getName().equals("Vattenmelon"));
            fourthButton.setOnAction(event -> updateProductList(waterList, event));
        } else if(category == "favorite"){
            subCategoryBar.toBack();
            return;
        }
        updateSubcategoryActiveStyling();
        subCategoryBar.toFront();
    }

    private void updateSubcategoryActiveStyling(){
        subCategoryBar.getStyleClass().clear();
        subCategoryBar.getStyleClass().add("subcategoryBar");
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        cartFlowPane.getChildren().clear();
        for(ShoppingItem item: iMatDataHandler.getShoppingCart().getItems()){
            cartFlowPane.getChildren().add(new CartView(item));
        }
    }

    public void cartChange() {
        cartFlowPane.getChildren().clear();
        for(ShoppingItem item: iMatDataHandler.getShoppingCart().getItems()){
            cartFlowPane.getChildren().add(new CartView(item));
        }
        shoppingCartSum.setText(String.format("%.2f", iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        if(iMatDataHandler.getShoppingCart().getTotal() > 0){
            gotoshopButton.setDisable(false);
        } else gotoshopButton.setDisable(true);
    }

    @Override
    public void cartChange(ProductListController prodItem) {
        cartFlowPane.getChildren().clear();
        for(ShoppingItem item: iMatDataHandler.getShoppingCart().getItems()){
            cartFlowPane.getChildren().add(new CartView(item));
        }
        shoppingCartSum.setText(String.format("%.2f", iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        if(iMatDataHandler.getShoppingCart().getTotal() > 0){
            gotoshopButton.setDisable(false);
        } else gotoshopButton.setDisable(true);
    }

    public void clearFlowPane(){
        cartFlowPane.getChildren().clear();
    }

    @Override
    public void detailedViewShow(ProductListController item) {
        ShoppingItem shopItem = item.getShoppingItem();
        openDetailView(shopItem, item);
    }

    @Override
    public void updateCart(CartView item) {
        cartChange();
    }

    private void emptyShoppingCart(){

        if(iMatDataHandler.getShoppingCart().getItems().size() == 0){
            System.out.println("Its empty");
            trashImageView.setImage(disposeShoppingItemsTransparent);
        }else {
            trashImageView.setImage(disposeShoppingItems);
            iMatDataHandler.getShoppingCart().clear();

        }
        cartChange();

    }

    public void getToolTipsForImageView(ImageView imageView, Tooltip tooltip){
        Tooltip.install(imageView, tooltip);

    }

    public void getToolTipsForButtons(Button buttons, Tooltip tooltip) {
        Tooltip.install(buttons, tooltip);
    }

    @Override
    public void copiedOrder(PurchasedListItem listItem) {
        cartChange();
    }

//    @Override
//    public void changeCart(ProductListController item) {
//        cartChange();
//    }

}

