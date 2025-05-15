package hcmute.com.ShoeShop.controller.admin;

import hcmute.com.ShoeShop.dto.InventoryDto;
import hcmute.com.ShoeShop.entity.Inventory;
import hcmute.com.ShoeShop.entity.Product;
import hcmute.com.ShoeShop.repository.InventoryRepository;
import hcmute.com.ShoeShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/inventory")
public class AdminInventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String category(Model model) {
        model.addAttribute("inventories", inventoryRepository.findAll());
        return "/admin/inventory/inventory-warehouse";
    }
    @GetMapping("/insertPage")
    public String insertProductPage(Model model) {
        InventoryDto inventory = new InventoryDto();
        model.addAttribute("inventory", inventory);
        model.addAttribute("products", productRepository.findAll());
        return "/admin/inventory/inventory-add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute(name = "inventory") InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();
        Product product = productRepository.findById(inventoryDto.getProductId()).get();
        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setProduct(product);
        inventory.setCreatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
        return "redirect:/admin/inventory";
    }

    @GetMapping("/update/{id}")
    public String getFormUpdateCategory(@PathVariable("id") Integer id, Model model){
        Inventory inventory = inventoryRepository.findById(id).get();
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setId(inventory.getId());
        inventoryDto.setTitle(inventory.getProduct().getTitle());
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setCreatedAt(inventory.getCreatedAt());
        inventoryDto.setProductId(inventory.getProduct().getId());
        System.out.println(inventoryDto);
        model.addAttribute("inventory", inventoryDto);
        model.addAttribute("id", inventory.getId());
        model.addAttribute("products", productRepository.findAll());
        return "/admin/inventory/inventory-edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute(name = "inventory") InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(inventoryDto.getId()).get();
        Product product = productRepository.findById(inventoryDto.getProductId()).get();
        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setProduct(product);
        inventory.setCreatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
        return "redirect:/admin/inventory";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        inventoryRepository.deleteById(id);
        return "redirect:/admin/inventory";
    }

}