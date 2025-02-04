package com.fastbin.app.service;
import com.fastbin.app.entity.Paste;
import com.fastbin.app.entity.User;
import com.fastbin.app.repository.PasteRepo;
import com.fastbin.app.security.CustomUserDetailsService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PasteService {
    
    // @Autowired
    // private FileStorageService fileStorageService;

    @Autowired
    private PasteRepo pasteRepo;

    @Autowired
    private CustomUserDetailsService userService;

    // @Autowired
    private final SecureRandom random = new SecureRandom();
    // Constructor injection (assuming you're using it)

    public Paste shortenUrl(String title,String expiry,String content) {
        // 1. Generate a unique short code
        // if(isUrlValid(originalUrl) == true){
            String shortCode = generateUniqueShortCode();
            while(true){
                if(pasteRepo.findByShortCode(shortCode) != null){
                    shortCode = generateUniqueShortCode();
                }
                else{
                    break;
                }
            }

            // String filePath = fileStorageService.storeFile(content);
            // 2. Create a ShortUrl object
            Paste paste = new Paste();
            paste.setShortCode(shortCode);
            paste.setTitle(title);
            paste.setContent(content);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            if (authentication != null && !authentication.getName().equals("anonymousUser")) {
                System.out.println("User is authenticated");
                String username = authentication.getName();
                User user = userService.findByUsername(username);
                paste.setUserId(user.getId());
                // System.out.println("Expiry time: "+expiry);
                if(!expiry.equals("")){
                    // System.out.println("Expiry is not blank.");
                    paste.setExpirationDate(LocalDateTime.parse(expiry, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
                }
                else{
                    // System.out.println("Expiry is black string");
                    paste.setExpirationDate(LocalDateTime.now().plusMonths(2));
                }
                // 3. Save to the database
            }
            else{
                paste.setUserId(-1L);
                paste.setExpirationDate(LocalDateTime.now().plusMinutes(10));
            }
            pasteRepo.save(paste);
            // 4. Return the shortened URL
            return paste;
        // }
        // else{
        //     return "Some Error Occured";
        // }
    }

    public void updatePaste(String shortCode, String content){
        Paste paste = getLinkedPaste(shortCode);
        paste.setContent(content);
        pasteRepo.save(paste);
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void deleteExpiredPastes() {
        LocalDateTime now = LocalDateTime.now();
        List<Paste> expiredPastes = pasteRepo.findByExpirationDateBefore(now);
        pasteRepo.deleteAll(expiredPastes);
        System.out.println("Deleted expired pastes: " + expiredPastes.size());
    }

    public String deletePaste(String shortCode){
        pasteRepo.deleteByShortCode(shortCode);
        return "Successfully deleted";
    }

    public Paste getLinkedPaste(String shortCode) {
        Paste urlMapping = pasteRepo.findByShortCode(shortCode);
        return urlMapping;
    }

    private String generateUniqueShortCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());

            code.append(CHARACTERS.charAt(randomIndex));
        }
        return code.toString();
    }

    public List<Paste> getAllShortUrl() {
        List<Paste> shortUrlList = new ArrayList<Paste>();
        shortUrlList = pasteRepo.findAll();
        return shortUrlList;
    }

    public List<Paste> getAllPastesByUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<Paste> shortUrlList = new ArrayList<Paste>();
        shortUrlList = pasteRepo.findByUserId(user.getId());
        return shortUrlList;
    }

    // public boolean isUrlValid(String urlString) {
    //     try {
    //         URL url = new URL();
    //         HTTPConnection connection = (HTTPConnection) url.openConnection();
    //         connection.setRequestMethod("HEAD"); // Efficient for just checking validity
    //         int responseCode = connection.getResponseCode();
    //         return (responseCode >= 200 && responseCode < 400);
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }
}