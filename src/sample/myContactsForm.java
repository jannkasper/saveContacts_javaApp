package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;

import javax.swing.*;

public class myContactsForm {

    private static int currentUserId;
    private static String userName;
    private static Image userImage;

public static void setUser (String name, int id, Image image) {
    currentUserId = id;
    userName = name;
    userImage = image;
}


    String imagePath = null;
    private static Stage stage;
    public static void setStage(Stage stage1) { stage = stage1;
    }

    @FXML
    protected void jButtonFirstAction () {
        contactTableView.getSelectionModel().selectFirst();
        tableSelectObject ();
    }
    @FXML
    protected void jButtonPreviousAction () {
        if (contactTableView.getSelectionModel().isEmpty()){
            contactTableView.getSelectionModel().selectFirst();
        }
        contactTableView.getSelectionModel().selectPrevious();
        tableSelectObject ();
    }
    @FXML
    protected void jButtonNextAction () {
        contactTableView.getSelectionModel().selectNext();
        tableSelectObject ();
    }
    @FXML
    protected void jButtonLastAction () {
        contactTableView.getSelectionModel().selectLast();
        tableSelectObject ();
    }



    @FXML
    private void initialize() {
        myUsernameLabel.setText(userName);
        circleImage.setStroke(Color.WHITESMOKE);
        circleImage.setFill(new ImagePattern(userImage));
        cEmailChoice.setItems(FXCollections.observableArrayList("Family", "Friends", "Work"));
        cEmailChoice.getSelectionModel().selectFirst();
        idColumn.setCellValueFactory(
                new PropertyValueFactory<contact, Integer>("cid"));
        fnameColumn.setCellValueFactory(
                new PropertyValueFactory<contact, String>("fname"));
        lnameColumn.setCellValueFactory(
                new PropertyValueFactory<contact, String>("lname"));
        groupColumn.setCellValueFactory(
                new PropertyValueFactory<contact, String>("groupc"));
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<contact, String>("phone"));
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<contact, String>("email"));
        addressColumn.setCellValueFactory(
                new PropertyValueFactory<contact, String>("address"));
        imageColumn.setCellValueFactory(
                new PropertyValueFactory<contact, ImageView>("image"));

        uptadeTable();
    }
    protected void uptadeTable () {
        contactQuery cq = new contactQuery();
        ObservableList<contact> data =  FXCollections.observableArrayList(cq.contactList(currentUserId));
        contactTableView.setItems(data);
    }

    @FXML
    private Circle circleImage;
    @FXML
    private Label myUsernameLabel;
    @FXML
    private TextField cFirstName;
    @FXML
    private TextField cLastName;
    @FXML
    private TextField cPhone;
    @FXML
    private TextField cEmail;
    @FXML
    private ChoiceBox<String> cEmailChoice;
    @FXML
    private ImageView cPhoto;
    @FXML
    private TextArea cAddress;
    private byte[] cImage = null;
    @FXML
    private TableView <contact> contactTableView;
    @FXML
    private TableColumn<contact, Integer> idColumn;
    @FXML
    private TableColumn<contact, String> fnameColumn;
    @FXML
    private TableColumn<contact, String> lnameColumn;
    @FXML
    private TableColumn<contact, String> groupColumn;
    @FXML
    private TableColumn<contact, String> phoneColumn;
    @FXML
    private TableColumn<contact, String> emailColumn;
    @FXML
    private TableColumn<contact, String> addressColumn;
    @FXML
    private TableColumn<contact, ImageView> imageColumn;

    private Integer contactId;

    @FXML
    protected void jButtonAddContact () {
        if (!verifyData()) {
            return;
        }
        contact c = createContact();
        contactQuery cq = new contactQuery();
        cq.insertContact(c);
        uptadeTable();
        cleanForumalar();
    }
    @FXML
    protected void jButtonUpdateContact () {
        if (!verifyData()) {
            return;
        }
        contact c = createContact();
        c.setCid(contactId);
        contactQuery cq = new contactQuery();
        if (imagePath != null) {
            cq.updateContact(c, true);
        } else {
            cq.updateContact(c, false);
        }
        uptadeTable();
        cleanForumalar();
    }

    @FXML
    protected void jButtonDeleteContact () {
        if (contactTableView.getSelectionModel().isEmpty()){
            errorDialog("No database selected");
            return;
        }
        contact c = createContact();
        c.setCid(contactId);
        contactQuery cq = new contactQuery();
        cq.deleteContact(c);
        uptadeTable();
        cleanForumalar();
    }

    protected contact createContact (){
        contact c;
        String fname = cFirstName.getText();
        String lname = cLastName.getText();
        String phone = cPhone.getText();
        String email = cEmail.getText();
        String address = cAddress.getText();
        String group = cEmailChoice.getSelectionModel().getSelectedItem();
        byte[] img = cImage;


        c = new contact(0, fname, lname, group, phone, email, address, img, currentUserId);

        return c;
    }

    protected void cleanForumalar () {
        imagePath = null;
        contactId = null;
        cFirstName.clear();
        cLastName.clear();
        cPhone.clear();
        cEmail.clear();
        cEmailChoice.getSelectionModel().selectFirst();
        cPhoto.setImage(null);
        cAddress.clear();
    }

    @FXML
    protected void tableSelectObject () {
        contact co = contactTableView.getSelectionModel().getSelectedItem();
        contactId = co.getCid();
        cFirstName.setText(co.getFname());
        cLastName.setText(co.getLname());
        cPhone.setText(co.getPhone());
        cEmail.setText(co.getEmail());
        cAddress.setText(co.getAddress());
        cEmailChoice.setValue(co.getGroupc());
        cPhoto.setImage(new Image(new ByteArrayInputStream(co.getImageByte())));
        cImage = co.getImageByte();
    }

    @FXML
    protected void browsePhoto (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resourse File");
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("IMAGE files (*.jpg)", "*.jpg","*.png", "*.image", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);





        File filePhoto = fileChooser.showOpenDialog(new Stage());
        if (filePhoto != null) {
            imagePath = filePhoto.toString();
            System.out.println("sciezka: " + imagePath);
            Image image = new Image(filePhoto.toURI().toString(), cPhoto.getFitHeight(), cPhoto.getFitWidth(), true, true);
            cPhoto.setImage(image);
            if (imagePath != null) {
                try {
                    Path pth = Paths.get(imagePath);
                    cImage = Files.readAllBytes(pth);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void errorDialog (String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    protected boolean verifyData() {
        if (cFirstName.getText().equals("") || cLastName.getText().equals("") || cPhone.getText().equals("") ||
                cEmail.getText().equals("") || cAddress.getText().equals("")) {
            errorDialog("One or more fields are empty");

            return false;
        }
        if (cImage == null) {
            errorDialog("No Image Selected");
            return false;
        }
        return true;
    }

    @FXML
    protected void minimalizeLogin() {
        stage.setIconified(true);

    }


    @FXML
    protected void closeLogin () {
        stage.close();
    }

    @FXML
    protected void logOut () {
        try {
            Stage stage = (Stage) cPhone.getScene().getWindow();
            stage.hide();

            stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Log In");
            Controller.setStage(stage);
            stage.show();
        } catch (Exception a) {
            System.out.println("Problem with new stage:" + a);
        }
    }
}
