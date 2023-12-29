package imat.checkout;

import imat.BossController;
import imat.cart.CartView;
import imat.checkout.confirmedOrder.ConfirmedPurchaseView;
import imat.checkout.instruction.InstructionView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class CheckoutView extends AnchorPane {
    @FXML
    private FlowPane instructionPane, checkoutCartPane, fullCartPane;

    private BossController mainController;

    private IMatDataHandler handler = IMatDataHandler.getInstance();

    public CheckoutView(BossController mainController) {
        this.mainController = mainController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("checkout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        checkoutInit();
    }

    private void checkoutInit(){
        instructionPane.getChildren().clear();
        instructionPane.getChildren().add(new InstructionView(mainController));
        fullCartPane.getChildren().clear();
        fullCartPane.getChildren().add(new VarukorgView(this.mainController));
    }

//    @FXML
//    public void handleHomeClick(ActionEvent event){
//        //System.out.println("hello");
//        mainController.openStartPage();
//    }
}
