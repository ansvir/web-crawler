package com.project.webcrawler;

import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.xpath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.project.webcrawler.websockets.WebSocketConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {WebConfig.class, WebSocketConfig.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(
                        containsString("Web-crawler")))
                .andExpect(MockMvcResultMatchers.xpath("//input[@id='inputSeed']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//button[@id='addTermButton']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//textarea[@id='output']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//button[@id='stopCrawlButton']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//select[@id='querySelect']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//button[@id='downloadAllStatReport']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//button[@id='downloadTopTenStatReport']").exists());
    }

}
