package imat.checkout.confirmedOrder;

import imat.BossController;
import imat.checkout.instruction.InstructionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;


import java.io.IOException;

public class ConfirmedPurchaseView extends AnchorPane{
    private BossController mainController;
    private IMatDataHandler handler = IMatDataHandler.getInstance();

    @FXML private Button tillbakaButton;
    @FXML private Button setTidigareButton;

    @FXML private Label totalPriceLabel, deliveryDayLabel, deliveryTimeLabel;


    public ConfirmedPurchaseView(BossController mainController){
        this.mainController = mainController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("confirmed.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        confirmedPurchaseInit();
    }

    private void confirmedPurchaseInit(){
        totalPriceLabel.setText(String.format("%.2f", handler.getShoppingCart().getTotal()) + " kr");
        handler.placeOrder(true);
        mainController.cartChange();
    }


    @FXML
    public void openHomeButton(ActionEvent event){
        mainController.nonEventHomeClick();
    }

    @FXML
    public void openTidigare(ActionEvent event){
        mainController.openPurchaseHistoryView();
    }

}
