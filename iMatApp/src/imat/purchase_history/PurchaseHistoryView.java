package imat.purchase_history;
import imat.BossController;
import imat.purchase_history.purchasedListItem.PurchasedListItem;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PurchaseHistoryView extends AnchorPane {
    private BossController mainController;
    private IMatDataHandler handler = IMatDataHandler.getInstance(); //TODO ta bort om den inte gör något

    private static PurchaseHistoryView self;
    @FXML
    private FlowPane previousOrderList;
    @FXML
    private ScrollPane purchaseHistoryScrollpane;
    @FXML
    private Button purchasehistcheckoutbutton;
    @FXML
    private Label emptyShoppingCartLabel;

    public PurchaseHistoryView(BossController mainController){
        this.mainController = mainController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchasehistory.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        purchaseHistoryScrollpane.setFitToWidth(true);
        purchaseHistoryInit();
    }

    private void purchaseHistoryInit(){
        if(handler.getShoppingCart().getItems().isEmpty()){
            purchasehistcheckoutbutton.setDisable(true);
            emptyShoppingCartLabel.setText("Varukorgen är tom!");
        }
        previousOrderList.getChildren().clear();
        List<Order> orders = handler.getOrders();
        Collections.sort(orders, Comparator.comparing(Order::getDate).reversed());
        for(Order order: orders)
            previousOrderList.getChildren().add(new PurchasedListItem(order));
    }
    @FXML
    public void handleHomeClick(ActionEvent event){
        mainController.nonEventHomeClick();
    }

    @FXML
    private void handleCheckoutClick(ActionEvent event){
        mainController.openCheckoutView();
    }
}