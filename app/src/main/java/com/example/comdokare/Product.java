package com.example.comdokare;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    public String name;
    public String barcodeId;
    public String imageResource;
    public String description;
    public List<Ingredients> ingredients;
}
