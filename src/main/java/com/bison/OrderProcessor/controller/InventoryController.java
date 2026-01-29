package com.bison.OrderProcessor.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bison.OrderProcessor.entity.Inventory;
import com.bison.OrderProcessor.service.InventoryService;


@RestController
@RequestMapping("/inventory")
public class InventoryController {

    
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @RequestParam Long productId,
            @RequestParam int stock) {

        Inventory inventory =
            inventoryService.createInventory(productId, stock);

        return ResponseEntity.ok(inventory);
    }


}
