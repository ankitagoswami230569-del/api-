package com.example.bfhl;

import com.example.bfhl.controller.BfhlController;
import com.example.bfhl.model.RequestDto;
import com.example.bfhl.model.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BfhlApplicationTests {

    private final BfhlController controller = new BfhlController();

    @Test
    void contextLoads() {}

    @Test
    void testBasicSeparation() {
        RequestDto req = new RequestDto();
        req.setData(Arrays.asList("a", "1", "334", "4", "R", "$"));

        ResponseEntity<ResponseDto> resp = controller.process(req);
        ResponseDto body = resp.getBody();

        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals(List.of("1"), body.getOddNumbers());
        assertEquals(List.of("334", "4"), body.getEvenNumbers());
        assertEquals(List.of("A", "R"), body.getAlphabets());
        assertEquals(List.of("$"), body.getSpecialCharacters());
        assertEquals("339", body.getSum());
    }

    @Test
    void testConcatString() {
        // ["a","R"] → alphabets=["A","R"] → joined="AR" → reversed="RA"
        // alternating: R(up) a(lo) = "Ra"
        RequestDto req = new RequestDto();
        req.setData(Arrays.asList("a", "R"));

        ResponseDto body = controller.process(req).getBody();
        assertNotNull(body);
        assertEquals("Ra", body.getConcatString());
    }

    @Test
    void testConcatStringMultiChar() {
        // ["A","ABCD","DOE"] → joined="AABCDDOE" → reversed="EODDCBAA"
        // alternating: E o D d C b A a = "EoDdCbAa"
        RequestDto req = new RequestDto();
        req.setData(Arrays.asList("A", "ABCD", "DOE"));

        ResponseDto body = controller.process(req).getBody();
        assertNotNull(body);
        assertEquals("EoDdCbAa", body.getConcatString());
    }

    @Test
    void testEmptyData() {
        RequestDto req = new RequestDto();
        req.setData(List.of());

        ResponseDto body = controller.process(req).getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("0", body.getSum());
        assertEquals("", body.getConcatString());
    }
}
