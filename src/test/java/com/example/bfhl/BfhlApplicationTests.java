package com.example.bfhl;

import com.example.bfhl.model.RequestDto;
import com.example.bfhl.model.ResponseDto;
import com.example.bfhl.service.BfhlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BfhlApplicationTests {

    @Autowired
    private BfhlService bfhlService;

    @Test
    void contextLoads() {
        // Verifies the Spring context starts without errors
    }

    @Test
    void shouldSeparateNumbersAndAlphabets() {
        RequestDto request = new RequestDto(Arrays.asList("A", "1", "334", "4", "R"));
        ResponseDto response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getNumbers()).containsExactly("1", "334", "4");
        assertThat(response.getAlphabets()).containsExactly("A", "R");
    }

    @Test
    void shouldIgnoreMixedAndSpecialValues() {
        RequestDto request = new RequestDto(Arrays.asList("A1", "12B", "@123", "!", "Z", "99"));
        ResponseDto response = bfhlService.processData(request);

        assertThat(response.getNumbers()).containsExactly("99");
        assertThat(response.getAlphabets()).containsExactly("Z");
    }

    @Test
    void shouldHandleEmptyList() {
        RequestDto request = new RequestDto(List.of());
        ResponseDto response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getNumbers()).isEmpty();
        assertThat(response.getAlphabets()).isEmpty();
    }
}
