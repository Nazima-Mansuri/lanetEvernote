package com.example.lcom67.demoapp.Beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lcom67 on 1/7/16.
 */

public class Contacts {
    long id;
    String name;
    String email;
    String passwd;
    String Mobile;
    String Address;

    String Notes_Title;
    String Notes_Description;

    String mImagePath;
    String cameraImage;

    Date reminder_date = null;


    public Contacts() {
    }

    public Contacts(long id, String name, String passwd, String email, String mobile, String address, String notes_Title, String notes_Description) {
        this.id = id;
        this.name = name;
        this.passwd = passwd;
        this.email = email;
        Mobile = mobile;
        Address = address;
        Notes_Title = notes_Title;
        Notes_Description = notes_Description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getNotes_Title() {
        return Notes_Title;
    }

    public void setNotes_Title(String notes_Title) {
        Notes_Title = notes_Title;
    }

    public String getNotes_Description() {
        return Notes_Description;
    }

    public void setNotes_Description(String notes_Description) {
        Notes_Description = notes_Description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getCameraImage() {
        return cameraImage;
    }

    public void setCameraImage(String cameraImage) {
        this.cameraImage = cameraImage;
    }

    public String getReminder_date()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        if (reminder_date != null)
            return dateFormat.format(reminder_date);
        else
            return null;
    }

    public void setReminder_date(Date reminder_date) {
        this.reminder_date = reminder_date;
    }

}
