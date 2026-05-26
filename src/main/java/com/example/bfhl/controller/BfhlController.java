package com.example.bfhl.controller;

import com.example.bfhl.model.RequestDto;
import com.example.bfhl.model.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class BfhlController {

    private static final String USER_ID     = "ankita_goswami_19062005";
    private static final String EMAIL       = "ankitagoswani230569@acropolis.in";
    private static final String ROLL_NUMBER = "0827CY231013";

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/bfhl")
    public ResponseEntity<ResponseDto> process(@RequestBody RequestDto request) {

        List<String> data = request.getData() != null ? request.getData() : new ArrayList<>();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long totalSum                 = 0;

        for (String item : data) {
            if (item == null || item.isEmpty()) continue;

            if (item.matches("\\d+")) {
                // Pure number
                long num = Long.parseLong(item);
                totalSum += num;
                if (num % 2 == 0) evenNumbers.add(item);
                else oddNumbers.add(item);

            } else if (item.matches("[A-Za-z]+")) {
                // Pure alphabet string — uppercase
                alphabets.add(item.toUpperCase());

            } else {
                // Special / mixed — add as-is
                specialChars.add(item);
            }
        }

        // concat_string logic:
        // 1. Take all alphabet entries (already uppercased)
        // 2. Reverse the overall order of the list
        // 3. Concatenate all characters
        // 4. Apply alternating caps: index 0 = uppercase, index 1 = lowercase, ...
        //
        // Example: ["a","R"] → alphabets = ["A","R"] → reversed = ["R","A"]
        //          concat = "RA" → alternating = R(upper) a(lower) = "Ra"
        //
        // Example: ["A","ABCD","DOE"] → alphabets = ["A","ABCD","DOE"]
        //          reversed list = ["DOE","ABCD","A"]
        //          concat chars  = D,O,E,A,B,C,D,A
        //          alternating   = D(up) o(lo) E(up) a(lo) B(up) c(lo) D(up) a(lo)
        //                        = "DoEaBcDa"  — but expected is "EoDdCbAa"
        //
        // Re-reading the expected: ["A","ABCD","DOE"] → "EoDdCbAa"
        // reversed list = ["DOE","ABCD","A"]
        // chars in order: D O E A B C D A
        // expected chars: E o D d C b A a
        // That is the chars reversed: A D C B A E O D → alternating caps
        // A(up) d(lo) C(up) b(lo) A(up) e(lo) O(up) d(lo) = "AdCbAeOd" — still not matching
        //
        // Try: join all chars first = "ADOECDBA" (reversed list joined)
        // Wait — reversed list = ["DOE","ABCD","A"], joined = "DOEABCDA"
        // Reverse that string = "ADCBAOED"
        // Alternating: A(up) d(lo) C(up) b(lo) A(up) o(lo) E(up) d(lo) = "AdCbAoEd" — no
        //
        // Try original list order joined = "A"+"ABCD"+"DOE" = "AABCDDOE"
        // Reverse string = "EODDCBAA"
        // Alternating: E(up) o(lo) D(up) d(lo) C(up) b(lo) A(up) a(lo) = "EoDdCbAa" ✓ MATCHES
        //
        // So the correct algorithm:
        // 1. Join all alphabet strings in ORIGINAL order (uppercased)
        // 2. Reverse the resulting string
        // 3. Apply alternating caps (even index = upper, odd index = lower)

        StringBuilder joined = new StringBuilder();
        for (String alpha : alphabets) {
            joined.append(alpha); // already uppercased
        }

        String reversed = joined.reverse().toString();

        StringBuilder concat = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) concat.append(Character.toUpperCase(c));
            else             concat.append(Character.toLowerCase(c));
        }

        ResponseDto response = new ResponseDto();
        response.setSuccess(true);
        response.setUserId(USER_ID);
        response.setEmail(EMAIL);
        response.setRollNumber(ROLL_NUMBER);
        response.setOddNumbers(oddNumbers);
        response.setEvenNumbers(evenNumbers);
        response.setAlphabets(alphabets);
        response.setSpecialCharacters(specialChars);
        response.setSum(String.valueOf(totalSum));
        response.setConcatString(concat.toString());

        return ResponseEntity.ok(response);
    }
}
