package imat.profile;


import imat.BossController;
import javafx.fxml.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.IMatDataHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileView extends AnchorPane {
    private BossController mainController;
    private final IMatDataHandler handler = IMatDataHandler.getInstance(); //TODO ta bort om den inte gör något

    private final static Image Checkmark = new Image("imat/resources/check-circle-solid-240.png");
    private final static Image Errormark = new Image("imat/resources/error.jpg");
    @FXML
    TextField firstNameField, lastNameField, addressField, postalCodeField, emailField, phoneNumberField,
            postAddressField, cardNumberField, yearField, monthField, cardOwnerField;

    @FXML
    Button checkoutButton;

    @FXML
    Label emptyShoppingCartLabel, firstNameError, lastNameError, postalCodeError, localityError,
            emailError, phoneNumberError, cardOwnerError, cardNumberError, monthError;

    @FXML
    ImageView firstNameImage, lastNameImage, addressImage, postalCodeImage,
            postAddressImage, emailImage, phoneNumberImage, cardOwnerImage, cardNumberImage, monthImage, yearImage;

    List<TextField> textFields = new ArrayList<>();

    public ProfileView(BossController mainController) {
        this.mainController = mainController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try{
            fxmlLoader.load();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        Collections.addAll(textFields, firstNameField, lastNameField, addressField, postalCodeField, postAddressField,
                emailField, phoneNumberField, cardNumberField, yearField, monthField, cardOwnerField);
        profileInit();
    }

    public void onSaveButtonClick(){
        Customer customer = handler.getCustomer();
        CreditCard creditCard = handler.getCreditCard();
        customer.setFirstName(firstNameField.getText());
        customer.setLastName(lastNameField.getText());
        customer.setAddress(addressField.getText());
        customer.setPostCode(postalCodeField.getText());
        customer.setPostAddress(postAddressField.getText());
        customer.setEmail(emailField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
        creditCard.setCardNumber(cardNumberField.getText());
        creditCard.setValidMonth(Integer.parseInt(monthField.getText()));
        creditCard.setValidYear(Integer.parseInt(yearField.getText()));
        creditCard.setHoldersName(cardOwnerField.getText());
    }

    public void saveFields(){
        Customer customer = handler.getCustomer();
        CreditCard creditCard = handler.getCreditCard();
        customer.setFirstName(firstNameField.getText());
        customer.setLastName(lastNameField.getText());
        customer.setAddress(addressField.getText());
        customer.setPostCode(postalCodeField.getText());
        customer.setPostAddress(postAddressField.getText());
        customer.setEmail(emailField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
        creditCard.setCardNumber(cardNumberField.getText());
        creditCard.setValidMonth(Integer.parseInt(monthField.getText()));
        creditCard.setValidYear(Integer.parseInt(yearField.getText()));
        creditCard.setHoldersName(cardOwnerField.getText());
    }

    public void profileInit(){
        class textFieldListener implements ChangeListener<String>{
            private final TextField textField;

            textFieldListener(TextField textField) {
                this.textField = textField;
            }

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkErrors();
                saveFields();
            }
        }

        if(handler.getShoppingCart().getItems().isEmpty()){
            checkoutButton.setDisable(true);
            emptyShoppingCartLabel.setText("Varukorgen är tom!");
        }

        Customer customer = handler.getCustomer();
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostCode());
        postAddressField.setText(customer.getPostAddress());
        emailField.setText(customer.getEmail());
        phoneNumberField.setText(customer.getPhoneNumber());

        CreditCard creditCard = handler.getCreditCard();
        cardNumberField.setText(creditCard.getCardNumber());
        monthField.setText(String.valueOf(creditCard.getValidMonth()));
        yearField.setText(String.valueOf(creditCard.getValidYear()));
        cardOwnerField.setText(creditCard.getHoldersName());

        for(TextField textField : textFields){
            textField.textProperty().addListener(new textFieldListener(textField));
        }
        checkErrors();
    }

    private void removeFieldStyling(TextField field){
        field.getStyleClass().remove("completedField");
        field.getStyleClass().remove("errorField");
    }

    private void checkErrors(){
        if(firstNameField.getText().matches("[a-zA-ZåäöÅÄÖ ]+") || firstNameField.getText().isEmpty()){
            firstNameError.setVisible(false);
            firstNameImage.setVisible(false);
            removeFieldStyling(firstNameField);
            if(firstNameField.getText().matches("[a-zA-ZåäöÅÄÖ ]+")){
                firstNameImage.setVisible(true);
                firstNameImage.setImage(Checkmark);
                firstNameField.getStyleClass().add("completedField");
            }
        }
        else{
            firstNameError.setText("Vänligen fyll i endast bokstäver");
            firstNameError.setVisible(true);
            firstNameImage.setVisible(true);
            firstNameImage.setImage(Errormark);
            firstNameField.getStyleClass().add("errorField");
        }
        if(lastNameField.getText().matches("[a-zA-ZåäöÅÄÖ ]+") || lastNameField.getText().isEmpty()){
            lastNameError.setVisible(false);
            lastNameImage.setVisible(false);
            removeFieldStyling(lastNameField);
            if(lastNameField.getText().matches("[a-zA-ZåäöÅÄÖ ]+")){
                lastNameImage.setVisible(true);
                lastNameImage.setImage(Checkmark);
                lastNameField.getStyleClass().add("completedField");
            }
        }
        else{
            lastNameError.setText("Vänligen fyll i endast bokstäver");
            lastNameError.setVisible(true);
            lastNameImage.setVisible(true);
            lastNameImage.setImage(Errormark);
            lastNameField.getStyleClass().add("errorField");
        }
        if(postAddressField.getText().matches("[a-zA-ZåäöÅÄÖ ]+") || postAddressField.getText().isEmpty()){
            localityError.setVisible(false);
            postAddressImage.setVisible(false);
            removeFieldStyling(postAddressField);
            if(postAddressField.getText().matches("[a-zA-ZåäöÅÄÖ ]+")){
                postAddressImage.setVisible(true);
                postAddressImage.setImage(Checkmark);
                postAddressField.getStyleClass().add("completedField");
            }
        }
        else{
            localityError.setText("Vänligen fyll i endast bokstäver");
            localityError.setVisible(true);
            postAddressImage.setVisible(true);
            postAddressImage.setImage(Errormark);
            postAddressField.getStyleClass().add("errorField");
        }
        if(emailField.getText().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
                || emailField.getText().isEmpty()) {
            emailError.setVisible(false);
            emailImage.setVisible(false);
            removeFieldStyling(emailField);
            if(emailField.getText().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
                emailImage.setVisible(true);
                emailImage.setImage(Checkmark);
                emailField.getStyleClass().add("completedField");
            }
        }
        else{
            emailError.setText("Vänligen fyll i med korrekt format: xxx@xxx.xxx");
            emailError.setVisible(true);
            emailImage.setVisible(true);
            emailImage.setImage(Errormark);
            emailField.getStyleClass().add("errorField");
        }
        if(phoneNumberField.getText().matches("[0-9 ]+") || phoneNumberField.getText().isEmpty()){
            phoneNumberError.setVisible(false);
            phoneNumberImage.setVisible(false);
            removeFieldStyling(phoneNumberField);
            if(phoneNumberField.getText().matches("[0-9 ]+")){
                phoneNumberImage.setVisible(true);
                phoneNumberImage.setImage(Checkmark);
                phoneNumberField.getStyleClass().add("completedField");
            }
        }
        else{
            phoneNumberError.setText("Vänligen fyll i endast siffror");
            phoneNumberError.setVisible(true);
            phoneNumberImage.setVisible(true);
            phoneNumberImage.setImage(Errormark);
            phoneNumberField.getStyleClass().add("errorField");
        }
        if(postalCodeField.getText().matches("[0-9 ]+") || postalCodeField.getText().isEmpty()){
            postalCodeError.setVisible(false);
            postalCodeImage.setVisible(false);
            removeFieldStyling(postalCodeField);
            if(phoneNumberField.getText().matches("[0-9 ]+")){
                postalCodeImage.setVisible(true);
                postalCodeImage.setImage(Checkmark);
                postalCodeField.getStyleClass().add("completedField");
            }
        }
        else{
            postalCodeError.setText("Vänligen fyll i endast siffror");
            postalCodeError.setVisible(true);
            postalCodeImage.setVisible(true);
            postalCodeImage.setImage(Errormark);
            postalCodeField.getStyleClass().add("errorField");
        }
        if(!(addressField.getText().isEmpty())){
            addressImage.setVisible(true);
            addressImage.setImage(Checkmark);
            addressField.getStyleClass().add("completedField");
        }
        else{
            addressImage.setVisible(false);
            removeFieldStyling(addressField);
        }
        if(cardNumberField.getText().matches("[0-9 ]+") || cardNumberField.getText().isEmpty()){
            cardNumberError.setVisible(false);
            cardNumberImage.setVisible(false);
            removeFieldStyling(cardNumberField);
            if(cardNumberField.getText().matches("[0-9 ]+")){
                cardNumberImage.setVisible(true);
                cardNumberImage.setImage(Checkmark);
                cardNumberField.getStyleClass().add("completedField");
            }
        }
        else{
            cardNumberError.setText("Vänligen fyll i endast siffror");
            cardNumberError.setVisible(true);
            cardNumberImage.setVisible(true);
            cardNumberImage.setImage(Errormark);
            cardNumberField.getStyleClass().add("errorField");
        }
        if(yearField.getText().matches("[0-9 ]+") || yearField.getText().isEmpty()){
            monthError.setVisible(false);
            yearImage.setVisible(false);
            removeFieldStyling(yearField);
            if(yearField.getText().matches("[0-9 ]+")){
                yearImage.setVisible(true);
                yearImage.setImage(Checkmark);
                yearField.getStyleClass().add("completedField");
            }
        }
        else{
            monthError.setText("Vänligen fyll i endast siffror");
            monthError.setVisible(true);
            yearImage.setVisible(true);
            yearImage.setImage(Errormark);
            yearField.getStyleClass().add("errorField");
        }
        if(monthField.getText().matches("[0-9 ]+") || monthField.getText().isEmpty()){
            monthError.setVisible(false);
            monthImage.setVisible(false);
            removeFieldStyling(monthField);
            if(monthField.getText().matches("[0-9 ]+")){
                monthImage.setVisible(true);
                monthImage.setImage(Checkmark);
                monthField.getStyleClass().add("completedField");
            }
        }
        else{
            monthError.setText("Vänligen fyll i endast siffror");
            monthError.setVisible(true);
            monthImage.setVisible(true);
            monthImage.setImage(Errormark);
            monthField.getStyleClass().add("errorField");
        }
        if(cardOwnerField.getText().matches("[a-zA-ZåäöÅÄÖ ]+") || cardOwnerField.getText().isEmpty()){
            cardOwnerError.setVisible(false);
            cardOwnerImage.setVisible(false);
            removeFieldStyling(cardOwnerField);
            if(cardOwnerField.getText().matches("[a-zA-ZåäöÅÄÖ ]+")){
                cardOwnerImage.setVisible(true);
                cardOwnerImage.setImage(Checkmark);
                cardOwnerField.getStyleClass().add("completedField");
            }
        }
        else{
            cardOwnerError.setText("Vänligen fyll i endast bokstäver");
            cardOwnerError.setVisible(true);
            cardOwnerImage.setVisible(true);
            cardOwnerImage.setImage(Errormark);
            cardOwnerField.getStyleClass().add("errorField");
        }
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
