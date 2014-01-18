/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.webdemo.data.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author matfed
 */
public class MatchingRequestTest {

    /**
     * Test of fromJson method, of class Request.
     */
    @Test
    public void testFromJson() {
        System.out.println("fromJson");
        String json = "{\n" +
"  \"citations\":[\n" +
"    {\n" +
"      \"citation-text\":\"De revolutionibus\"\n" +
"    },\n" +
"    {\n" +
"      \"citation-text\":\n" +
"        \"Principia mathematica\"\n" +
"    }\n" +
"  ]\n" +
"}";
        MatchingRequest result = MatchingRequest.fromJson(json);
        assertEquals(2, result.getCitations().length);
        assertEquals("De revolutionibus", result.getCitations()[0].getCitationText());
        assertEquals("Principia mathematica", result.getCitations()[1].getCitationText());
    }
    
}
