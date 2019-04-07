package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class contactQuery {
    public boolean insertContact (contact cont) {

        boolean contactIsCreated = true;
            Connection con = myConnection.getConnection();
            PreparedStatement ps;

            try {
                ps = con.prepareStatement("INSERT INTO `mycontact`(`fname`, `lname`, `groupc`, `phone`, `email`, `address`, `pic`, `userid`) VALUES (?,?,?,?,?,?,?,?)");
                ps.setString(1,cont.getFname());
                ps.setString(2, cont.getLname());
                ps.setString(3, cont.getGroupc());
                ps.setString(4, cont.getPhone());
                ps.setString(5,cont.getEmail());
                ps.setString(6,cont.getAddress());
                ps.setBytes(7, cont.getImageByte());
                ps.setInt(8,cont.getUid());

                if (ps.executeUpdate() != 0) {
                    System.out.println("New contact Added");
                } else {
                    contactIsCreated = false;
                    System.out.println("Something wrong with contact add");
                }
            } catch (SQLException e) {
                System.out.println("Error 1: " + e.getMessage());
            }


        return contactIsCreated;
    }
    public boolean updateContact (contact cont, boolean withImage) {

        boolean contactIsCreated = true;
        Connection con = myConnection.getConnection();
        PreparedStatement ps;
        String updateQuery = "";

        if (withImage == true) {
            updateQuery = "UPDATE `mycontact` SET `fname`=?,`lname`=?,`groupc`=?,`phone`=?,`email`=?,`address`=?,`pic`=?  WHERE `id` = ?";
            try {
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, cont.getFname());
                ps.setString(2, cont.getLname());
                ps.setString(3, cont.getGroupc());
                ps.setString(4, cont.getPhone());
                ps.setString(5, cont.getEmail());
                ps.setString(6, cont.getAddress());
                ps.setBytes(7, cont.getImageByte());
                ps.setInt(8, cont.getCid());

                if (ps.executeUpdate() != 0) {
                    System.out.println("Contact Update with Image");
                } else {
                    contactIsCreated = false;
                    System.out.println("Something wrong with contact Update");
                }
            } catch (SQLException e) {
                System.out.println("Error 1: " + e.getMessage());
            }
        } else {
            try {
            updateQuery = "UPDATE `mycontact` SET `fname`=?,`lname`=?,`groupc`=?,`phone`=?,`email`=?,`address`=?  WHERE `id` = ?";
            ps = con.prepareStatement(updateQuery);
            ps.setString(1, cont.getFname());
            ps.setString(2, cont.getLname());
            ps.setString(3, cont.getGroupc());
            ps.setString(4, cont.getPhone());
            ps.setString(5, cont.getEmail());
            ps.setString(6, cont.getAddress());
            ps.setInt(7, cont.getCid());

            if (ps.executeUpdate() != 0) {
                System.out.println("Contact update without Image");
            } else {
                contactIsCreated = false;
                System.out.println("Something wrong with contact Update");
            }
            } catch(SQLException e){
            System.out.println("Error 1: " + e.getMessage());
        }
    }
        return contactIsCreated;
    }

    public void deleteContact (contact cont) {

        Connection con = myConnection.getConnection();
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("DELETE FROM `mycontact` WHERE `id`= ?");
            ps.setInt(1,cont.getCid());

            if (ps.executeUpdate() != 0) {
                System.out.println("Contact deleted");
            } else {

                System.out.println("Something wrong with contact delete");
            }
        } catch (SQLException e) {
            System.out.println("Error 1: " + e.getMessage());
        }



    }

    public ArrayList<contact> contactList (int currentUserId) {
        ArrayList<contact> clist = new ArrayList<>();
        Connection con = myConnection.getConnection();
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT `id`, `fname`, `lname`, `groupc`, `phone`, `email`, `address`, `pic` FROM `mycontact` WHERE `userid` = " + currentUserId);

            contact ct;
            while (rs.next()) {
                ct = new contact(rs.getInt("id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("groupc"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getBytes("pic"),
                        currentUserId);

                clist.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clist;
    }
}
