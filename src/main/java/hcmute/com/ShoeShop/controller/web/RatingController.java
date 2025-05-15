package hcmute.com.ShoeShop.controller.web;

import hcmute.com.ShoeShop.entity.Rating;
import hcmute.com.ShoeShop.entity.Users;
import hcmute.com.ShoeShop.services.imp.RatingService;
import hcmute.com.ShoeShop.services.imp.UserService;
import hcmute.com.ShoeShop.utlis.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public String addComment(@RequestParam String email, @RequestParam String comment,
                             @RequestParam(name = "image", required = false) MultipartFile image,
                             @RequestParam int star, @RequestParam long productId, HttpSession session) {

        // kiem tra khach hang co dang nhap khong
        Users user = (Users) session.getAttribute(Constant.SESSION_USER);
        if(user == null) {
            return "redirect:/login";
        }

        ratingService.addRating(email, comment, star, image, productId);

        return "redirect:/product/details/" + productId;
    }

    @GetMapping("")
    public String viewAllRating(@RequestParam long productId, ModelMap model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "6") int size) {

        Page<Rating> ratingsPage = ratingService.getAllRatingsWithPaginationByProductId(productId, PageRequest.of(page, size));
        List<Rating> ratings = ratingService.getAllRatingsByProductId(productId);

        int totalRating = ratingService.countRatingsByProductId(productId);
        model.addAttribute("totalRating", totalRating);

        // Tính trung bình số sao
        double averageStar = ratings.stream().mapToInt(Rating::getStar).average().orElse(0.0);
        averageStar = Math.round(averageStar * 10) / 10.0;
        model.addAttribute("avgrating", averageStar);
        model.addAttribute("ratings", ratingsPage);
        model.addAttribute("currentPage", ratingsPage.getNumber());
        model.addAttribute("totalPages", ratingsPage.getTotalPages());
        return "user/review-content";
    }
    
}
