package com.fastbin.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import com.fastbin.app.entity.Paste;
import com.fastbin.app.entity.Role;
import com.fastbin.app.entity.User;
import com.fastbin.app.security.CustomUserDetailsService;
import com.fastbin.app.service.PasteService;


@RestController
@RequestMapping("/api/paste")
@CrossOrigin
public class PasteController {

    @Autowired
    private PasteService pasteService;

    @Autowired
    private CustomUserDetailsService userService;

    // Constructor injection
    
    @GetMapping("/all")
    public List<Paste> getAllShortUrl(){
        // Long id = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        // System.out.println(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<com.fastbin.app.entity.Role> roles = user.getRoles();
        for(int i=0;i<roles.size();i++){
            if(roles.get(i).getName().equals("ADMIN")){
                List<Paste> arr = pasteService.getAllShortUrl();
                return arr;
            }
        }
        List<Paste> arr = pasteService.getAllPastesByUser();
        return arr;
    }

    @PostMapping("/shorten")
    public Paste shortenUrl(@RequestParam(value = "title", defaultValue = "Arbitrary Title") String title,@RequestParam(value = "expiry", defaultValue = "") String expiry,@RequestBody String content) {
        return pasteService.shortenUrl(title,expiry,content);
    }

    @GetMapping("/{shortCode}")
    @PreAuthorize("permitAll()")
    public Paste openLinkedPaste(@PathVariable("shortCode") String shortCode) {
        System.out.println(shortCode);
        Paste model = pasteService.getLinkedPaste(shortCode);

        // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // User user = (User) userService.loadUserByUsername(username);
        // model.setUserId(user.getId());
        return model;
    }

    @PostMapping("/update/{shortCode}")
    public ResponseEntity<Object> changeTextOfPaste(@PathVariable("shortCode") String shortCode, @RequestBody String content) {
        System.out.println(shortCode);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Paste model = pasteService.getLinkedPaste(shortCode);
        for(int i=0;i<user.getRoles().size();i++){
            System.out.println(user.getRoles().get(i).getName());
        }
        Role admin = user.getRoles().stream()
                   .filter(person -> person.getName().equals("ADMIN"))
                   .findFirst()
                   .orElse(null);
        if(model != null && (model.getUserId() == user.getId() || admin != null)){
            model.setContent(content);
            pasteService.updatePaste(shortCode, content);
            return new ResponseEntity<>(model,HttpStatus.OK) ;
        }
        return new ResponseEntity<>("Not Authorized to perform this action.",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/delete/{shortCode}")
    public ResponseEntity<Object> postMethodName(@PathVariable("shortCode") String shortCode) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Paste model = pasteService.getLinkedPaste(shortCode);
        for(int i=0;i<user.getRoles().size();i++){
            System.out.println(user.getRoles().get(i).getName());
        }
        Role admin = user.getRoles().stream()
                   .filter(person -> person.getName().equals("ADMIN"))
                   .findFirst()
                   .orElse(null);
        if(model != null && (model.getUserId() == user.getId() || admin != null)){
            pasteService.deletePaste(shortCode);
            return new ResponseEntity<>("Paste Deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Authorized to perform this action.",HttpStatus.BAD_REQUEST);
    }
    
}