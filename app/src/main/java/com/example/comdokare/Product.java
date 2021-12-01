package com.example.comdokare;

import java.io.Serializable;

public class Product implements Serializable {
    public String name;
    public String barcodeId;
    public Ingredients[] ingredients;
}
