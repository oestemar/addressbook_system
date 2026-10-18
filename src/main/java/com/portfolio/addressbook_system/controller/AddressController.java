package com.portfolio.addressbook_system.controller;

import com.portfolio.addressbook_system.entity.Address;
import com.portfolio.addressbook_system.repository.AddressRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddressController {
    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Address> addresses;

        if (keyword != null && !keyword.isEmpty()) {
            addresses = addressRepository.findByNameContaining(keyword);
        } else {
            addresses = addressRepository.findAll(
                    Sort.by("id")
            );
        }

        model.addAttribute("addresses", addresses);

        return "list";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Long id,
            Model model) {

        Address address = addressRepository.findById(id).orElseThrow();

        model.addAttribute ("address", address);

        return "detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        Address address = addressRepository.findById(id).orElseThrow();

        model.addAttribute ("address", address);

        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String postalCode,
            @RequestParam String address
        ) {

        Address record = addressRepository.findById(id).orElseThrow();

        record.setName(name);
        record.setEmail(email);
        record.setPhone(phone);
        record.setPostalCode(postalCode);
        record.setAddress(address);

        addressRepository.save(record);

        return "redirect:/list";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String postalCode,
            @RequestParam String address) {

        try {

            Address record = new Address();

            record.setName(name);
            record.setEmail(email);
            record.setPhone(phone);
            record.setPostalCode(postalCode);
            record.setAddress(address);

            System.out.println("SAVE前");

            addressRepository.save(record);

            System.out.println("SAVE後");

        } catch (Exception e) {

            e.printStackTrace();

        }
        return "redirect:/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(
            @PathVariable Long id,
            Model model) {

        Address address = addressRepository.findById(id).orElseThrow();

        model.addAttribute ("address", address);

        return "delete";
    }

    @PostMapping("delete/{id}")
    public String delete(
            @PathVariable Long id) {

        Address address = addressRepository.findById(id).orElseThrow();
        addressRepository.delete(address);

        return "redirect:/list";
    }

    @GetMapping("/import")
    public String importForm() {

        return "import";

    }

    @PostMapping("/import")
    public String importCsv(
            @RequestParam("file")
            MultipartFile file) throws IOException {
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                file.getInputStream()
                        )
                );
        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null){
            String[] data = line.split(",");
            if (data.length < 5){
                continue;
            }

            Address address = new Address();

            address.setName(data[0].replace("\"", ""));
            address.setEmail(data[1].replace("\"", ""));
            address.setPhone(data[2].replace("\"", ""));
            address.setPostalCode(data[3].replace("\"", ""));
            address.setAddress(data[4].replace("\"", ""));

            addressRepository.save(address);
        }
        return "redirect:/list";

    }

}
