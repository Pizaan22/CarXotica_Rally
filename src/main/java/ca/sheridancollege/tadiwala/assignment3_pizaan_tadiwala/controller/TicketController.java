package ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean.Ticket;
import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.bean.User;
import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.database.SecurityRepository;
import ca.sheridancollege.tadiwala.assignment3_pizaan_tadiwala.database.TicketRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TicketController {
    @Autowired
    private TicketRepository repo;

    @Autowired
    private SecurityRepository secRepo;

    @GetMapping(value = { "/", "Home" })
    public String navHome() {
        return "Home.html";
    }

    @GetMapping("/Add")
    public String navAdd(Model model) {
        model.addAttribute("person", new Ticket());
        ArrayList<String> names = new ArrayList<>();
        for (Ticket t : repo.getTickets()) {
            names.add(t.getPersonName());
        }

        model.addAttribute("names", names);
        return "Add.html";
    }

    @PostMapping("/Add")
    public String processAdd(Model model, @ModelAttribute Ticket person) {
        repo.addTicket(person);
        return "redirect:/Add";
    }

    @GetMapping("/View")
    public String navView(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getAuthorities().toString().equals("[ROLE_GUEST]")) {
            model.addAttribute("tickets", repo.getTicketsByName(userDetails.getUsername()));

            DecimalFormat df = new DecimalFormat("#.00");
            double subtotal = 0;

            for (Ticket t : repo.getTicketsByName(userDetails.getUsername())) {
                subtotal += t.getPrice();
            }

            double taxes = 0.13 * subtotal; 
            double total = subtotal + taxes;

            model.addAttribute("subtotal", Double.parseDouble(df.format(subtotal)));
            model.addAttribute("taxes", Double.parseDouble(df.format(taxes)));
            model.addAttribute("total", Double.parseDouble(df.format(total)));

        } else {
            model.addAttribute("tickets", repo.getTickets());
        }
        return "View.html";
    }

    @GetMapping("/Delete/{id}")
    public String deleteTicket(@PathVariable int id) {
        repo.deleteTicketById(id);
        return "redirect:/View";
    }

    @GetMapping("/Edit/{id}")
    public String editTicket(@PathVariable int id, Model model) {
        model.addAttribute("ticket", repo.getTicketById(id));
        ArrayList<String> names = new ArrayList<>();
        for (Ticket t : repo.getTickets()) {
            names.add(t.getPersonName());
        }

        model.addAttribute("names", names);
        return "Edit.html";
    }

    @PostMapping("/Edit")
    public String processEdit(@ModelAttribute Ticket ticket) {
        repo.editTicket(ticket);
        return "redirect:/View";
    }

    @GetMapping("/Statistics")
    public String navStats(Model model) {
        int avgAge = (int) repo.avgAge() / repo.getTickets().size();
        model.addAttribute("avgAge", avgAge);
        model.addAttribute("sales", repo.totalSales());
        return "Statistics.html";
    }

    @GetMapping("/AgePlot")
    public String agePlot(Model model) {
        model.addAttribute("ages", repo.agePlot());
        return "AgePlot.html";
    }

    @GetMapping("/GenderRatio")
    public String gRatio(Model model) {
        double mPercent = (double) repo.mNum() / repo.getTickets().size();
        double fPercent = (double) repo.fNum() / repo.getTickets().size();
        double oPercent = (double) repo.oNum() / repo.getTickets().size();

        model.addAttribute("male", mPercent);
        model.addAttribute("female", fPercent);
        model.addAttribute("other", oPercent);
        return "gRatio.html";
    }

    @GetMapping("/PriceRange")
    public String priceRange(Model model) {
        LinkedHashMap<String, Double> prices = new LinkedHashMap<>();
        prices.put("Zone A", 89.99);
        prices.put("Zone B", 49.99);
        prices.put("Zone C", 29.99);
        model.addAttribute("prices", prices);
        return "PriceRange.html";
    }

    @GetMapping("/Login")
    public String navLogin() {
        return "Login.html";
    }

    @GetMapping("/SignUp")
    public String navSignup(Model model) {
        model.addAttribute("user", new User());
        return "SignUp.html";
    }

    @PostMapping("/SignUp")
    public String processSignup(@ModelAttribute User user) {
        secRepo.addUser(user);
        secRepo.addRole(secRepo.getUserId(user.getUserName()));
        return "redirect:/Login";
    }
}
