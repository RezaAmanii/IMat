package imat.start;

import imat.BossController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;


import javax.tools.Tool;
import java.util.HashMap;
import java.util.Map;


public class StartPageController {

    //alla FXids för startsidan ska finnas här


    @FXML
    private AnchorPane homescreen;

    @FXML
    private AnchorPane pageSwitchPane;

    //HEADER BUTTONS + SEARCHBAR
    @FXML
    private TextField searchBar;
    @FXML
    private Button homeButton;
    @FXML
    private ImageView homeImageView;
    @FXML
    private Button favoriteButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button profileImageView;
    @FXML
    private Label logoiMat;

    //CATEGORIES
    @FXML
    private Button alltButton;
    @FXML
    private Button dryckButton;
    @FXML
    private Button fruktButton;
    @FXML
    private Button kottButton;
    @FXML
    private Button mejeriButton;
    @FXML
    private Button skafferiButton;

    //SHOPPINGCART
    @FXML
    private Label priceinshoppingcart;
    @FXML
    private Button gotoshopButton;



    private Map<String, Product> recipeListItemMap = new HashMap<String, Product>();

    @FXML
    public void closeRecipeView(){
        homescreen.toFront();
    }
    public void openDetailView(Product product){
        //.toFront();
       // populateRecipeDetailView(recipe);
    }

    public StartPageController(){


    }






}
