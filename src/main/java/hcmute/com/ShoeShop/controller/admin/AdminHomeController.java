package hcmute.com.ShoeShop.controller.admin;

import hcmute.com.ShoeShop.dto.DiscountDTO;
import hcmute.com.ShoeShop.entity.Discount;
import hcmute.com.ShoeShop.entity.Product;
import hcmute.com.ShoeShop.entity.Users;
import hcmute.com.ShoeShop.services.imp.DiscountService;
import hcmute.com.ShoeShop.utlis.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    DiscountService discountService;

    @GetMapping
    public String adminHome(RedirectAttributes redirectAttributes, HttpSession session) {
        Users u = (Users) session.getAttribute(Constant.SESSION_USER);
        if(u == null) {
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("user", u);
        return "admin/admin_home";
    }

    @GetMapping("/discount-list")
    public String discountList(HttpSession session, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "4") int size) {
        Users u = (Users) session.getAttribute(Constant.SESSION_USER);

        if(u == null) {
            return "redirect:/login";
        }
        List<Discount> li = discountService.findAllDiscounts();

        Page<Discount> discountPage = discountService.findAllDiscounts(PageRequest.of(page, size));
        model.addAttribute("disco", discountPage.getContent());  // Các bản ghi của trang hiện tại
        model.addAttribute("currentPage", page);  // Trang hiện tại
        model.addAttribute("totalPages", discountPage.getTotalPages());  // Tổng số trang
        model.addAttribute("totalElements", discountPage.getTotalElements());  // Tổng số bản ghi
        model.addAttribute("size", size);  // Kích thước của mỗi trang

        model.addAttribute("user", u);
        return "admin/discount/discount_list";
    }

    @GetMapping("/discount-add")
    public String discountAdd(RedirectAttributes redirectAttributes, HttpSession session) {

        Users u = (Users) session.getAttribute(Constant.SESSION_USER);
        if(u == null) {
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("user", u);

        return "admin/discount/discount_add";
    }

    @PostMapping("/discount/add")
    public String addDiscount(@ModelAttribute DiscountDTO discountDTO, HttpSession session) {
        Users u = (Users) session.getAttribute(Constant.SESSION_USER);
        if(u == null) {
            return "redirect:/login";
        }
        if(discountDTO.getStatus()==null){
            return "redirect:/discount-list";
        }
        else{
            System.out.println(discountDTO.getStatus());
        }
        // Chuyển đổi ngày tháng từ String thành LocalDate
        LocalDate start = LocalDate.parse(discountDTO.getStartDate());
        LocalDate end = LocalDate.parse(discountDTO.getEndDate());

        Discount discount = new Discount();
        discount.setName(discountDTO.getName());
        discount.setQuantity(discountDTO.getQuantity());
        discount.setPercent(discountDTO.getPercent()/100.0);
        discount.setMinOrderValue(discountDTO.getMinOrderValue());
        discount.setStatus(discountDTO.getStatus());
        discount.setStartDate(start);
        discount.setEndDate(end);
        discountService.saveDiscount(discount);
        return "redirect:/admin/discount-list";
    }

    @GetMapping("/discount-delete/{id}")
    public String deleteDiscount(@PathVariable("id") int id, HttpSession session) {

        Users u = (Users) session.getAttribute(Constant.SESSION_USER);
        if(u == null) {
            return "redirect:/login";
        }

        Discount d = discountService.findDiscountById(id);
        d.setStatus("EXPIRED");
        discountService.saveDiscount(d);
        return "redirect:/admin/discount-list";
    }

    @GetMapping("/discount-edit/{id}")
    public String editDiscount(@PathVariable("id") int id, Model model) {
        Discount d = discountService.findDiscountById(id);
        model.addAttribute("discount", d);
        return "admin/discount/discount_edit";
    }

    @PostMapping("/discount/edit")
    public String editDiscount(@ModelAttribute DiscountDTO discountDTO, HttpSession session, @RequestParam("id") int id) {

        Users u = (Users) session.getAttribute(Constant.SESSION_USER);
        if(u == null) {
            return "redirect:/login";
        }

        // Chuyển đổi ngày tháng từ String thành LocalDate
        LocalDate start = LocalDate.parse(discountDTO.getStartDate());
        LocalDate end = LocalDate.parse(discountDTO.getEndDate());

        Discount discount = new Discount();
        discount.setId(id);
        discount.setName(discountDTO.getName());
        discount.setQuantity(discountDTO.getQuantity());
        discount.setPercent(discountDTO.getPercent()/100.0);
        discount.setMinOrderValue(discountDTO.getMinOrderValue());
        discount.setStatus(discountDTO.getStatus());
        discount.setStartDate(start);
        discount.setEndDate(end);
        discountService.saveDiscount(discount);
        return "redirect:/admin/discount-list";
    }
}
