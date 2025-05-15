package hcmute.com.ShoeShop.controller.web;

import hcmute.com.ShoeShop.entity.Order;
import hcmute.com.ShoeShop.entity.Users;
import hcmute.com.ShoeShop.services.imp.OrderServiceImpl;
import hcmute.com.ShoeShop.utlis.PayOption;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderServiceImpl orderService;


    @GetMapping()
    public String order(@RequestParam(value = "page-size", defaultValue = "5")int pagesize,
                        @RequestParam(name = "page-num", defaultValue = "0") int pageNum,
                        Model model,
                        HttpSession session) {
        Pageable pageable = PageRequest.of(pageNum, pagesize, Sort.by("status"));
        Users user = (Users) session.getAttribute("user");
        int userId = user.getId();
        Page<Order> orderPage = orderService.findOrderByUserId(userId, pageable);

        model.addAttribute("orderPage", orderPage);
//        orderService.orderCart(2, 150, PayOption.COD);
        return "user/order";
    }
}
