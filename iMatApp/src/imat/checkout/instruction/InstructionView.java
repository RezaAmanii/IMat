package imat.checkout.instruction;

import imat.BossController;
import imat.checkout.CheckoutView;
import imat.checkout.confirmedOrder.ConfirmedPurchaseView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import java.io.IOException;
import java.util.*;

public class InstructionView extends AnchorPane{

    private BossController mainController;

    private static InstructionView instance = null;
    private final IMatDataHandler handler = IMatDataHandler.getInstance();

    private final static Image Checkmark = new Image("imat/resources/check-circle-solid-240.png");
    private final static Image Errormark = new Image("imat/resources/error.jpg");

    private String deliveryDay;
    private String deliveryTime;
    @FXML TextField firstNameField, lastNameField, addressField, postalCodeField,
            postAddressField, emailField, phoneNumberField, cardNumberField, cardOwnerField,
            monthField, yearField, cvcField;
    @FXML AnchorPane instruction2, instruction3, instruction4;
    @FXML Line line1, line2, line21, line3, line31,  line4;
    @FXML Circle circle1, circle2, circle3, circle4, circle5;
    @FXML Button monButton, tueButton, wedButton, thuButton, friButton, time1, time2, time3, time4, time5, time6,
    confirmButton;
    @FXML
    ImageView firstNameImage, lastNameImage, addressImage, postAddressImage, postalCodeImage,
            phoneNumberImage, emailImage, cardOwnerImage, cardNumberImage, cvcImage, yearImage, monthImage;
    @FXML
    Label firstNameError, lastNameError, localityError, emailError, phoneNumberError, postalCodeError,
            cardNumberError, cardOwnerError, cvcError, monthError, yearError;
    List<TextField> textFields = new ArrayList<>();

    public InstructionView(BossController mainController){
        this.mainController = mainController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("instruction.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        Collections.addAll(textFields, firstNameField, lastNameField, addressField, postalCodeField, postAddressField,
                emailField, phoneNumberField, cardNumberField, yearField, monthField, cardOwnerField, cvcField);
        instructionInit();
    }

    private void instructionInit(){
        class textFieldListener implements ChangeListener<String>{
            private final TextField textField;

            textFieldListener(TextField textField) {
                this.textField = textField;
            }

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                checkErrors();
                saveFields();
                nextStep();
            }
        }
        confirmButton.setDisable(true);
        EventHandler<ActionEvent> dayButtonHandler = new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                List<Button> buttons = Arrays.asList(monButton, tueButton, wedButton, thuButton, friButton);
                for(Button button : buttons){
                    if(button == event.getTarget()){
                        if(!(button.getStyleClass().contains("deliveryButtonPressed"))){
                            button.getStyleClass().remove("deliveryButtons");
                            button.getStyleClass().add("deliveryButtonPressed");
                        }
                    }
                    else{
                        button.getStyleClass().remove("deliveryButtonPressed");
                        button.getStyleClass().add("deliveryButtons");
                    }
                }
                nextStep();
                event.consume();
            }
        };
        EventHandler<ActionEvent> timeButtonHandler = new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                List<Button> buttons = Arrays.asList(time1, time2, time3, time4, time5, time6);
                for(Button button : buttons){
                    if(button == event.getTarget()){
                        if(!(button.getStyleClass().contains("deliveryButtonPressed"))){
                            button.getStyleClass().remove("deliveryButtons");
                            button.getStyleClass().add("deliveryButtonPressed");
                        }
                    }
                    else{
                        button.getStyleClass().remove("deliveryButtonPressed");
                        button.getStyleClass().add("deliveryButtons");
                    }
                }
                nextStep();
                event.consume();
            }
        };

        monButton.setOnAction(dayButtonHandler);
        tueButton.setOnAction(dayButtonHandler);
        wedButton.setOnAction(dayButtonHandler);
        thuButton.setOnAction(dayButtonHandler);
        friButton.setOnAction(dayButtonHandler);
        time1.setOnAction(timeButtonHandler);
        time2.setOnAction(timeButtonHandler);
        time3.setOnAction(timeButtonHandler);
        time4.setOnAction(timeButtonHandler);
        time5.setOnAction(timeButtonHandler);
        time6.setOnAction(timeButtonHandler);

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
        nextStep();
        checkErrors();
    }

    private void setDeliveryDay(){
        List<Button> instruction2Buttons = Arrays.asList(monButton, tueButton, wedButton, thuButton, friButton);
        for(Button button : instruction2Buttons){
            if(button.getStyleClass().contains("deliveryButtonPressed")) deliveryDay = button.getText();
        }
    }

    private void setDeliveryTime(){
        List<Button> instruction3Buttons = Arrays.asList(time1, time2, time3, time4, time5, time6);
        for(Button button : instruction3Buttons){
            if(button.getStyleClass().contains("deliveryButtonPressed")) deliveryTime = button.getText();
        }
    }

    public String getDeliveryDay(){
        return this.deliveryDay;
    }

    public String getDeliveryTime(){
        return this.deliveryTime;
    }

    @FXML
    private void onConfirmPurchaseClick(){
        mainController.cartChange();
        mainController.changeToConfirmedPane();
    }


    private void saveFields(){
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
            yearError.setVisible(false);
            yearImage.setVisible(false);
            removeFieldStyling(yearField);
            if(yearField.getText().matches("[0-9 ]+")){
                yearImage.setVisible(true);
                yearImage.setImage(Checkmark);
                yearField.getStyleClass().add("completedField");
            }
        }
        else{
            yearError.setText("Vänligen fyll i endast siffror");
            yearError.setVisible(true);
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
        if(cvcField.getText().matches("[0-9 ]+") || cvcField.getText().isEmpty()){
            cvcError.setVisible(false);
            cvcImage.setVisible(false);
            removeFieldStyling(cvcField);
            if(cvcField.getText().matches("[0-9 ]+")){
                cvcImage.setVisible(true);
                cvcImage.setImage(Checkmark);
                cvcField.getStyleClass().add("completedField");
            }
        }
        else{
            cvcError.setText("Vänligen fyll i endast siffror");
            cvcError.setVisible(true);
            cvcImage.setVisible(true);
            cvcImage.setImage(Errormark);
            cvcField.getStyleClass().add("errorField");
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

    private void nextStep(){
        List<Line> lines = Arrays.asList(line1, line2, line21, line3, line31, line4);
        List<Circle> circles = Arrays.asList(circle1, circle2, circle3, circle4, circle5);
        List<AnchorPane> instructions = Arrays.asList(instruction2, instruction3, instruction4);
        List<Button> buttons = Arrays.asList(monButton, tueButton, wedButton, thuButton, friButton,
                time1, time2, time3, time4, time5, time6, confirmButton);
        List<TextField> cardFields = Arrays.asList(cardNumberField, cardOwnerField, monthField, yearField, cvcField);
        for(Line line : lines) changeStyleClass(line, "progressBar");
        for(Circle circle : circles) changeStyleClass(circle, "progressBar");
        for(AnchorPane instruction : instructions) addGreyStyle(instruction);
        for(Button button : buttons) button.setDisable(true);
        for(TextField cardField: cardFields) cardField.setDisable(true);
        confirmButton.setDisable(true);
        if(isInstruction1Done()){
            instruction2.getStyleClass().remove("greyInstructionBox");
            changeStyleClass(line1, "finishedProgressBar");
            changeStyleClass(circle1, "finishedProgressBar");
            monButton.setDisable(false);
            tueButton.setDisable(false);
            wedButton.setDisable(false);
            thuButton.setDisable(false);
            friButton.setDisable(false);
        }
        if(isInstruction1Done() && isInstruction2Done()){
            setDeliveryDay();
            instruction3.getStyleClass().remove("greyInstructionBox");
            changeStyleClass(line2, "finishedProgressBar");
            changeStyleClass(line21, "finishedProgressBar");
            changeStyleClass(circle2, "finishedProgressBar");
            time1.setDisable(false);
            time2.setDisable(false);
            time3.setDisable(false);
            time4.setDisable(false);
            time5.setDisable(false);
            time6.setDisable(false);
        }
        if(isInstruction1Done() && isInstruction2Done() && isInstruction3Done()){
            setDeliveryTime();
            instruction4.getStyleClass().remove("greyInstructionBox");
            changeStyleClass(line3, "finishedProgressBar");
            changeStyleClass(line31, "finishedProgressBar");
            changeStyleClass(circle3, "finishedProgressBar");
            cardNumberField.setDisable(false);
            cardOwnerField.setDisable(false);
            monthField.setDisable(false);
            yearField.setDisable(false);
            cvcField.setDisable(false);
        }
        if(isInstruction1Done() && isInstruction2Done() && isInstruction3Done() && isInstruction4Done()){
            changeStyleClass(line4, "finishedProgressBar");
            changeStyleClass(circle4, "finishedProgressBar");
            changeStyleClass(circle5, "finishedProgressBar");
            confirmButton.getStyleClass().remove("greyButton");
            confirmButton.getStyleClass().add("confirmButton");
            confirmButton.setDisable(false);
            confirmButton.setCursor(Cursor.HAND);
        }
    }
    private boolean isInstruction1Done(){
        return areAllFieldsFilled();
    }

    private boolean areAllFieldsFilled(){
        if(!(firstNameField.getText().matches("[a-zA-ZåäöÅÄÖ ]+"))){
            return false;
        }
        if(!(lastNameField.getText().matches("[a-zA-ZåäöÅÄÖ ]+"))){
            return false;
        }
        if(!(postAddressField.getText().matches("[a-zA-ZåäöÅÄÖ ]+"))){
            return false;
        }
        if(!(phoneNumberField.getText().matches("[0-9 ]+"))){
            return false;
        }
        if(!(postalCodeField.getText().matches("[0-9 ]+"))){
            return false;
        }
        if(addressField.getText().isEmpty()){
            return false;
        }
        return true;
    }

    private boolean isInstruction2Done(){
            List<Button> instruction2Buttons = Arrays.asList(monButton, tueButton, wedButton, thuButton, friButton);
            for(Button button : instruction2Buttons){
                if(button.getStyleClass().contains("deliveryButtonPressed")) return true;
            }
            return false;
    }

    private boolean isInstruction3Done(){
        List<Button> instruction3Buttons = Arrays.asList(time1, time2, time3, time4, time5, time6);
        for(Button button : instruction3Buttons){
            if(button.getStyleClass().contains("deliveryButtonPressed")) return true;
        }
        return false;
    }

    private boolean isInstruction4Done(){
        return areAllFieldsFilled2();
    }

    private boolean areAllFieldsFilled2(){
        if(!(cardOwnerField.getText().matches("[a-zA-ZåäöÅÄÖ ]+"))){
            return false;
        }
        if(!(cardNumberField.getText().matches("[0-9 ]+"))){
            return false;
        }
        if(!(yearField.getText().matches("[0-9 ]+"))){
            return false;
        }
        if(!(monthField.getText().matches("[0-9 ]+"))){
            return false;
        }
        if(!(cvcField.getText().matches("[0-9 ]+"))){
            return false;
        }
        return true;
    }

    private static void addGreyStyle(Node obj){
        obj.getStyleClass().clear();
        obj.getStyleClass().add("progressInstructionBoxes");
        obj.getStyleClass().add("greyInstructionBox");
    }
    private static void changeStyleClass(Node obj, String style){
        obj.getStyleClass().clear();
        obj.getStyleClass().add(style);
    }
    private static void changeStyleClass(Shape obj, String style){
        obj.getStyleClass().clear();
        obj.getStyleClass().add(style);
    }
}
