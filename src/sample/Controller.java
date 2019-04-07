package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
public class Controller {

    private static Stage stage;
    public static void setStage(Stage stage1) { stage = stage1;
    }

    @FXML
    private void initialize() {
        pass_hidden.setVisible(true);
        pass_text.setVisible(false);
    }

    @FXML
    private PasswordField pass_hidden;
    @FXML
    private TextField pass_text;
    @FXML
    private CheckBox pass_toggle;
    @FXML
    private TextField loginField;

    @FXML
    public void togglevisiblePassword(ActionEvent event) {
        if (pass_toggle.isSelected()) {
            pass_text.setText(pass_hidden.getText());
            pass_text.setVisible(true);
            pass_hidden.setVisible(false);
            return;
        }
        pass_hidden.setText(pass_text.getText());
        pass_hidden.setVisible(true);
        pass_text.setVisible(false);
    }

    public void buttonLoginAction () throws SQLException {
        Connection con = myConnection.getConnection();
        PreparedStatement ps;
        ResultSet rs;

        ps = con.prepareStatement("SELECT * FROM `user` WHERE `username` = ? AND `pass` = ?");
        ps.setString(1, loginField.getText());
        ps.setString(2, pass_hidden.getText());
        rs = ps.executeQuery();

        Image imagex = null;
        if (rs.next()) {
            System.out.println("Loged");
            
            InputStream is = rs.getBinaryStream("pic");
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File("photo.jpg"));
                byte[]content = new byte[1024];
                int size = 0;
                while((size=is.read(content))!= -1)
                {
                    os.write(content,0,size);
                }
                os.close();
                is.close();
                imagex = new Image("file:photo.jpg",250,250,true,true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            myContactsForm.setUser(rs.getString("username"),rs.getInt("id"), imagex);


            try {
                Stage stage = (Stage) pass_text.getScene().getWindow();
                stage.hide();

                stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("MyContactsForm.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("My Panel");
                myContactsForm.setStage(stage);
                stage.show();
            } catch (Exception a) {
                System.out.println("Problem with new stage:" + a);
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            ps = con.prepareStatement("SELECT * FROM `user` WHERE `username` = ?");
            ps.setString(1, loginField.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                alert.setContentText("Wrong Password");
            } else {
                alert.setContentText("User does not exist");
            }
            alert.showAndWait();
        }

    }

    @FXML
    public void createAccount () {
        try {
            Stage stage = (Stage) pass_text.getScene().getWindow();
            stage.hide();

            stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("registrationPanel.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registration");
            registrationPanel.setStage(stage);
            stage.show();
        } catch (Exception a) {
            System.out.println("Problem with new stage:" + a);
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