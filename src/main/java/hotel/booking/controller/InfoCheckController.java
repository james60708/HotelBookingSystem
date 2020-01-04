package hotel.booking.controller;

import java.util.List;

import javax.annotation.Resource;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hotel.booking.Global;
import hotel.booking.container.Account;
import hotel.booking.container.Hotel;
import hotel.booking.container.LoginInfo;
import hotel.booking.container.Order;
import hotel.booking.container.Room;

@Controller
public class InfoCheckController {     
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	Order order;
	
	@RequestMapping(value="/confirmation", method=RequestMethod.POST)
    public String infoCheck(@RequestParam int numofSingle, @RequestParam int numofDouble, @RequestParam int numofQuad, @RequestParam int totalprice, Model model) {
		System.out.println("Confirmation: " + loginInfo.search_datein+" "+loginInfo.search_dateout+" "+loginInfo.search_location+" "+loginInfo.search_person);
		System.out.println("Select hotel id:" + loginInfo.select_hotel_id);
		Hotel selecthotel = Global.db.getHotel(loginInfo.select_hotel_id); 
		
		order = bookCheck(selecthotel, loginInfo.search_datein, loginInfo.search_dateout, numofSingle, numofDouble, numofQuad);
		if( order == null)
			return "index?msg=6"; //     alert("Selected hotel is unavailable now, please try again!");
		else {
			model.addAttribute("selecthotel", selecthotel);
			model.addAttribute("numofSingle", numofSingle);
			model.addAttribute("numofDouble", numofDouble);
			model.addAttribute("numofQuad", numofQuad);
			model.addAttribute("totalprice", totalprice);
	    	model.addAttribute("loginInfo", loginInfo);
	        return "confirmation";    
		}
		                    
    }
    
    public Order bookCheck(Hotel hotel, String dateIn, String dateOut, int numofSingle, int numofDouble, int numofQuad) {
    	// Given a hotel and dateIn~dateOut, and the number of room the user want to book, 
        // this function will check if the desired room are available, and create an order for you.
        // (If not all rooms are available, it will return null.)
        boolean available = true;
    	List<Room> roomlist = Global.db.getRoomsOfHotel(hotel);
    	if (Global.db.roomLeft(roomlist.get(0), dateIn, dateOut) >= numofSingle) {   //not finished yet
    		available = false;
    	}
    	if (Global.db.roomLeft(roomlist.get(1), dateIn, dateOut) >= numofDouble) {
    		available = false;
    	}
    	if (Global.db.roomLeft(roomlist.get(2), dateIn, dateOut) >= numofQuad) {
    		available = false;
    	}
    	if(available = true) {
    		List<Room> orderRoomlist = new ArrayList<>();
    		Room single_room = new Room(roomlist.get(0).id, roomlist.get(0).type, roomlist.get(0).price, numofSingle);
    		Room double_room = new Room(roomlist.get(1).id, roomlist.get(1).type, roomlist.get(1).price, numofDouble);
    		Room quad_room = new Room(roomlist.get(2).id, roomlist.get(2).type, roomlist.get(2).price, numofQuad);
    		orderRoomlist.add(single_room);
    		orderRoomlist.add(double_room);
    		orderRoomlist.add(quad_room);
            Order order = new Order(dateIn, dateOut, orderRoomlist);
            return order;
    	}else{
            return null;
        }
    }
    @RequestMapping(value="/bookcomplete")
    public String confirmOrder() {
    	int msg;
    	if(bookComplete(loginInfo.account, this.order)) {
    		msg = 7;
    		System.out.println("Book Success");
    	}

    	else {
    		msg = 8;
    		System.out.println("Book Fail");
    	}
    	String newurl = "redirect:/";
		newurl += ("?msg=" + msg);
    	return newurl;
    }
    public boolean bookComplete(Account account, Order order) {
        // Place an order. Return true if success, false if failed. 
    	return Global.db.addCustomerOrder(account, order); 
    }

}
