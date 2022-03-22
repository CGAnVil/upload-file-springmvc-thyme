package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.IProductService;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller

public class ProductController {

    @Autowired
    private IProductService productService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping("/products/list")
    public ModelAndView showListProduct(@RequestParam (name = "q",required = false) String q) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        List<Product> products = productService.findAll();
        if(q!=null){
            products = productService.findByName(q);
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("product", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/products/create")
    public ModelAndView createProduct(@ModelAttribute ProductForm productForm) {
        String fileName = productForm.getImage().getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getDescription(), fileName);
        productService.create(product);
        return new ModelAndView("redirect:/products/list");

    }

    @GetMapping("/products/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        Product product = productService.findById(id);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/products/edit/{id}")
    public ModelAndView editProduct(@PathVariable Integer id, @ModelAttribute ProductForm productForm) {

        MultipartFile multipartFile = productForm.getImage();
        if (multipartFile.getSize() == 0) {
            Product product = productService.findById(id);
            productService.updateById(id, product);
        } else {

            String fileName = productForm.getImage().getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + fileName;
            try {
                FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getDescription(), fileName);
            productService.updateById(id, product);
        }
            return new ModelAndView("redirect:/products/list");

    }

    @GetMapping("/products/{id}")
    public ModelAndView showProductDetail(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return new ModelAndView("/product/view", "product", product);
    }

    @GetMapping("/products/delete/{id}")
    public ModelAndView showDeleteProduct(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        Product product = productService.findById(id);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/products/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable Integer id){
        Product product = productService.findById(id);
        File file = new File(uploadPath + product.getImage());
        if (file.exists()){
            file.delete();
        }
        productService.remove(id);
        return new ModelAndView("redirect:/products/list");
    }

}
