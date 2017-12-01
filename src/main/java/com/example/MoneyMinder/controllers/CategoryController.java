package com.example.MoneyMinder.controllers;

import com.example.MoneyMinder.models.Category;
import com.example.MoneyMinder.models.Transaction;
import com.example.MoneyMinder.models.User;
import com.example.MoneyMinder.models.data.CategoryDao;
import com.example.MoneyMinder.models.data.TransactionDao;
import com.example.MoneyMinder.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionDao transactionDao;

    @RequestMapping(value = "")
    public String index(Model model, HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute("title", user.getUsername() + "'s Categories");

        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm(Model model,HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        List<Category> userCategories = categoryDao.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userCategories", userCategories);
        model.addAttribute(new Category());
        model.addAttribute("title", "Add Category");

        return "category/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddCategoryForm(Model model, @ModelAttribute
    @Valid Category category, Errors errors, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "category/add";
        }

        category.setUser(user);
        categoryDao.save(category);
        userDao.save(user);
        List<Category> userCategories = categoryDao.findByUserId(user.getId());
        model.addAttribute("userCategories", userCategories);

        return "redirect:";
    }

    @RequestMapping(value = "edit/{categoryId}", method = RequestMethod.GET)
    public String displayCategoryEditForm(Model model, @PathVariable int categoryId, HttpServletRequest request) {

        Category category = categoryDao.findOne(categoryId);
        User user = (User) request.getSession().getAttribute("user");

        boolean usedCategory = false;
        List<Transaction> transactions = transactionDao.findByUserId(user.getId());
        for ( Transaction transaction : transactions) { if (transaction.getCategory() == category) {
            usedCategory = true; }
        }

        model.addAttribute("category", category);
        model.addAttribute("usedCategory", usedCategory);
        model.addAttribute("title", "Edit Category " + category.getName());

        return "category/edit";
    }

    @RequestMapping(value = "edit", method = {RequestMethod.POST})
    public String processCategoryEditForm(Model model, @ModelAttribute @Valid Category category, Errors errors,
                                         @RequestParam int categoryId, String name) {


        if(errors.hasErrors()) {
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("title", "Errors " + category.getName());
            return "category/edit";
        }

        categoryDao.findOne(categoryId).setName(name);
        categoryDao.save(categoryDao.findOne(categoryId));

        return "redirect:";
    }

    @RequestMapping(value = "remove/{categoryId}", method = RequestMethod.GET)
    public String displayRemoveCategoryForm(Model model, @PathVariable int categoryId) {

        model.addAttribute("category", categoryDao.findOne(categoryId));
        model.addAttribute("title", "Delete Category");

        return "category/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCategoryForm(Model model, @RequestParam int categoryId) {

        Category category = categoryDao.findOne(categoryId);
        categoryDao.delete(category);

        return "redirect:";
    }

}
