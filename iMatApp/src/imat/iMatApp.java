package imat;


import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import se.chalmers.cse.dat216.project.IMatDataHandler;



public class iMatApp extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("imat/resources/iMat");

        Parent root = FXMLLoader.load(getClass().getResource("imat_app.fxml"), bundle);
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.setTitle(bundle.getString("application.name"));
        primaryStage.setResizable(false);

        //Fullscreen config
        primaryStage.setFullScreenExitHint("You can never exit from here! Unless you press Q");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("q"));


        // Title and logo
        primaryStage.show();
        javafx.scene.image.Image applicationIcon = new Image("/imat/resources/logo.png");
        primaryStage.getIcons().add(applicationIcon);


    }


    public static void main(String[] args) {
        launch(args);

    }

    //sparar allt(varukorg, användarinställningar, favoriter osv.)
    public void stop(){
        IMatDataHandler.getInstance().shutDown();
    }
    
}
