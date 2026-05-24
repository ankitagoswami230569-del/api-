package com.example.bfhl.service;

import com.example.bfhl.model.RequestDto;
import com.example.bfhl.model.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BfhlService {

    // Regex: pure digits only
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");

    // Regex: pure alphabets only (upper or lower case)
    private static final Pattern ALPHABET_PATTERN = Pattern.compile("^[A-Za-z]+$");

    @Value("${bfhl.user.id}")
    private String userId;

    @Value("${bfhl.user.email}")
    private String email;

    @Value("${bfhl.user.roll-number}")
    private String rollNumber;

    /**
     * Processes the input data list and separates numbers from alphabets.
     * Mixed values (e.g. "A1", "12B") and special characters are ignored.
     *
     * @param requestDto the incoming request containing the data array
     * @return a populated ResponseDto with separated numbers and alphabets
     */
    public ResponseDto processData(RequestDto requestDto) {
        List<String> numbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();

        List<String> data = requestDto.getData();

        if (data != null) {
            for (String item : data) {
                if (item == null || item.isBlank()) {
                    continue;
                }
                if (NUMBER_PATTERN.matcher(item).matches()) {
                    numbers.add(item);
                } else if (ALPHABET_PATTERN.matcher(item).matches()) {
                    alphabets.add(item);
                }
                // Mixed values and special characters are silently ignored
            }
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .numbers(numbers)
                .alphabets(alphabets)
                .build();
    }
}
