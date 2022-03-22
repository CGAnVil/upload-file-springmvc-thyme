package com.codegym.service;

import com.codegym.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductService implements IProductService{

    private static List<Product> products = new ArrayList<>();
    @Override
    public List<Product> findAll() {
        return products;
    }

    public int findIndexById(int id){
        int index = -1;
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public Product findById(int id) {
        int index = findIndexById(id);
        if(index != -1){
            return products.get(index);
        }
        return null;
    }

    @Override
    public void create(Product product) {
        products.add(product);
    }

    @Override
    public void updateById(int id, Product product) {
        int index = findIndexById(id);
        products.set(index,product);

    }

    @Override
    public void remove(int id) {
       int index = findIndexById(id);
       products.remove(index);
    }

    @Override
    public List<Product> findByName(String name){
        List<Product> allProductsByName = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getName().contains(name)){
                allProductsByName.add(products.get(i));
            }
        }
        return allProductsByName;
    }
}
