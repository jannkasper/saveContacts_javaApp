package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert.AlertType;


public class registrationPanel {


    String imagePath = null;
    private static Stage stage;
    public static void setStage(Stage stage1) { stage = stage1;
    }

    @FXML
    private TextField rFirstName;
    @FXML
    private TextField rLastName;
    @FXML
    private TextField rUserName;
    @FXML
    private PasswordField rPassword;
    @FXML
    private PasswordField rPassword2;
    @FXML
    private ImageView rPhoto;
    @FXML
    private Label rErrorLabel;

    protected void errorDialog (String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    protected boolean verifyData() {
        if (rFirstName.getText().equals("") || rLastName.getText().equals("") || rUserName.getText().equals("") ||
                rPassword.getText().equals("") || rPassword2.getText().equals("")) {
            errorDialog("One or more fields are empty");

            return false;
        }
        if ( isUsernameExist(rUserName.getText())) {
            errorDialog("Username Already Exist");
            return false;
        }
        if (!rPassword.getText().equals(rPassword2.getText())) {
            errorDialog("Incorrect second password");
            return false;
        }
        if (imagePath == null) {
            errorDialog("No Image Selected");
            return false;
        }
        return true;
    }
    @FXML
    protected void logIn () {
        try {
            Stage stage = (Stage) rFirstName.getScene().getWindow();
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
            Image image = new Image(filePhoto.toURI().toString(), rPhoto.getFitHeight(), rPhoto.getFitWidth(), true, true);
            rPhoto.setImage(image);
        }
    }
    protected boolean isUsernameExist (String un) {
        Connection con = myConnection.getConnection();
        PreparedStatement ps;
        ResultSet rs;

        boolean uExist = false;
        try {
            ps = con.prepareStatement("SELECT * FROM `user` WHERE `username` = ?");

        ps.setString(1, un);
        rs = ps.executeQuery();

        if (rs.next()) {
            uExist = true;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uExist;

    }
    @FXML
    protected void buttonCreate () {

        if (verifyData()) {
            Connection con = myConnection.getConnection();
            PreparedStatement ps;

            try {
                ps = con.prepareStatement("INSERT INTO `user`(`fname`, `lname`, `username`, `pass`, `pic`) VALUES (?,?,?,?,?)");
                ps.setString(1, rFirstName.getText());
                ps.setString(2, rLastName.getText());
                ps.setString(3, rUserName.getText());
                ps.setString(4, rPassword.getText());

                InputStream img = new FileInputStream(new File(imagePath));

                ps.setBlob(5, img);

                if (ps.executeUpdate() != 0) {
                    System.out.println("Account Created");
                    logIn();
                } else {
                    System.out.println("Something wrong with account created ");
                }
            } catch (SQLException e) {
                System.out.println("Error 1: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.out.println("Error 2: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void minimalizeLogin() {
        stage.setIconified(true);

    }


    @FXML
    protected void closeLogin () {
        stage.close();
    }



}
