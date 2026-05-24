package com.example.bfhl.controller;

import com.example.bfhl.model.RequestDto;
import com.example.bfhl.model.ResponseDto;
import com.example.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * GET / — Health check endpoint.
     *
     * @return 200 OK with a simple status message
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("message", "BFHL API Running"));
    }

    /**
     * POST /bfhl — Main qualifier endpoint.
     * Accepts a JSON body with a "data" array and returns separated numbers and alphabets.
     *
     * @param requestDto validated request body
     * @return 200 OK with the processed response
     */
    @PostMapping("/bfhl")
    public ResponseEntity<ResponseDto> processData(@Valid @RequestBody RequestDto requestDto) {
        ResponseDto response = bfhlService.processData(requestDto);
        return ResponseEntity.ok(response);
    }
}
