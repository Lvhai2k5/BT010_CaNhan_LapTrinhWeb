package vn.iotstar.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/home")
    public String adminHome() {
        return "admin/admin-home";
    }

    // Category CRUD
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("category", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            return "admin/categories";
        }
        categoryRepo.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
        return "redirect:/admin/categories";
    }

    // Product CRUD
    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/products";
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productRepo.findAll());
            model.addAttribute("categories", categoryRepo.findAll());
            return "admin/products";
        }
        productRepo.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/admin/products";
    }
}
