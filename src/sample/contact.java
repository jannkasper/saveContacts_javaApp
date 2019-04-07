package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

public class contact {
    private SimpleIntegerProperty cid;
    private SimpleStringProperty fname;
    private SimpleStringProperty lname;
    private SimpleStringProperty groupc;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private byte[] imageByte;
    private ImageView image;
    private SimpleIntegerProperty uid;

    public contact(Integer cid, String fname, String lname, String groupc, String phone, String email, String address, byte[] pic, int uid) {
        this.cid = new SimpleIntegerProperty(cid);
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.groupc = new SimpleStringProperty(groupc);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.imageByte = pic;

        if (pic != null) {
            Image image = null;
            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(pic));
                int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
                int width = img.getWidth();
                int height = img.getHeight();

                BufferedImage resized = resizeImage(img, type, 100, (int) ((float) height / ((float) width / 100)));
                image = SwingFXUtils.toFXImage(resized, null);
            } catch (IOException e) {
                e.getMessage();
            }
            this.image = new ImageView(image);
        }

        this.uid = new SimpleIntegerProperty(uid);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int img_width, int img_height){
        BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, img_width, img_height, null);
        g.dispose();

        return resizedImage;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public int getCid() {
        return cid.get();
    }

    public SimpleIntegerProperty cidProperty() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid.set(cid);
    }

    public String getFname() {
        return fname.get();
    }

    public SimpleStringProperty fnameProperty() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public String getLname() {
        return lname.get();
    }

    public SimpleStringProperty lnameProperty() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public String getGroupc() {
        return groupc.get();
    }

    public SimpleStringProperty groupcProperty() {
        return groupc;
    }

    public void setGroupc(String groupc) {
        this.groupc.set(groupc);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public int getUid() {
        return uid.get();
    }

    public SimpleIntegerProperty uidProperty() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid.set(uid);
    }
}
