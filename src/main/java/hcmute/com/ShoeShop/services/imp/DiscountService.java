package hcmute.com.ShoeShop.services.imp;

import hcmute.com.ShoeShop.entity.Discount;
import hcmute.com.ShoeShop.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    public void saveDiscount(Discount discount) {
        discountRepository.save(discount);
    }

    public Discount findDiscountById(int id) {
        return discountRepository.findById(id);
    }

    public List<Discount> findAllDiscounts() {
        List<Discount> listDiscount = discountRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Discount discount : listDiscount) {
            if(!discount.getStatus().equals("EXPIRED")){
                // Kiểm tra nếu quantity của discount bằng 0 thì set status là "INACTIVE"
                if (discount.getQuantity() == 0) {
                    discount.setStatus("INACTIVE");
                }
                else if (discount.getStartDate() != null && discount.getEndDate() != null) {
                    if (!discount.getStartDate().isAfter(today) && !discount.getEndDate().isBefore(today)) {
                        if (discount.getMinOrderValue() == null) {
                            discount.setStatus("ACTIVE");  // Đặt trạng thái là ACTIVE (String)
                        }
                    }
                    // Kiểm tra nếu ngày kết thúc trước ngày hiện tại
                    else if (discount.getEndDate().isBefore(today)) {
                        discount.setStatus("EXPIRED");  // Đặt trạng thái là EXPIRED (String)
                    }
                    // Nếu ngày bắt đầu trong tương lai
                    else {
                        discount.setStatus("COMING");
                    }
                } else {
                    // Nếu ngày bắt đầu hoặc ngày kết thúc là null, có thể xem như là chưa xác định
                    discount.setStatus("COMING");
                }
            }
        }
        discountRepository.saveAll(listDiscount);        // Lưu lại các thay đổi vào cơ sở dữ liệu
        return listDiscount;
    }

    public Page<Discount> findAllDiscounts(PageRequest pageRequest) {
        return discountRepository.findAll(pageRequest);
    }

    public void usedDiscount (int id) {
        Discount discount = findDiscountById(id);
        if(discount.getStatus().equals("ACTIVE")){
            if(discount.getQuantity()==1){
                discount.setQuantity(0);
                discount.setStatus("INACTIVE");
            }
            else {
                discount.setQuantity(discount.getQuantity() - 1);
            }
        }
    }

    public List<Discount> findAllDiscountsCondition( double price) {
        List<Discount> listDiscount = discountRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Discount discount : listDiscount) {

            if(!discount.getStatus().equals("EXPIRED")){
                // Kiểm tra nếu quantity của discount bằng 0 thì set status là "INACTIVE"
                if (discount.getQuantity() == 0) {
                    discount.setStatus("INACTIVE");
                }
                else if (discount.getStartDate() != null && discount.getEndDate() != null) {
                    if (!discount.getStartDate().isAfter(today) && !discount.getEndDate().isBefore(today)) {
                        if (discount.getMinOrderValue() == null || discount.getMinOrderValue()<=price) {
                            discount.setStatus("ACTIVE");  // Đặt trạng thái là ACTIVE (String)
                        }
                        else if(discount.getMinOrderValue()>price){
                            discount.setStatus("INACTIVE");
                        }
                    }
                    // Kiểm tra nếu ngày kết thúc trước ngày hiện tại
                    else if (discount.getEndDate().isBefore(today)) {
                        discount.setStatus("EXPIRED");  // Đặt trạng thái là EXPIRED (String)
                    }
                    // Nếu ngày bắt đầu trong tương lai
                    else {
                        discount.setStatus("COMING");
                    }
                } else {
                    // Nếu ngày bắt đầu hoặc ngày kết thúc là null, có thể xem như là chưa xác định
                    discount.setStatus("COMING");
                }
            }
        }
        discountRepository.saveAll(listDiscount);        // Lưu lại các thay đổi vào cơ sở dữ liệu
        return listDiscount;
    }

}
