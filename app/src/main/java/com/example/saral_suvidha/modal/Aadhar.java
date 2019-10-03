package com.example.saral_suvidha.modal;

public class Aadhar {
    private String Title;
    private String Category;
    private String Description;
    private int Thumbmail;

    public Aadhar() {

    }

    public Aadhar(String title, String category, String description, int thumbmail) {
        Title = title;
        Category = category;
        Description = description;
        Thumbmail = thumbmail;
    }

    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public int getThumbmail() {
        return Thumbmail;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setThumbmail(int thumbmail) {
        Thumbmail = thumbmail;
    }
}
