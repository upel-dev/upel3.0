package upeldev.com.github.upel3;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Upel3Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Should allow an admin to see all users")
    @WithMockUser(authorities = {"ADMIN"})
    public void findAllUsersByAdminTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all")
                .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not allow a student to see all users")
    @WithMockUser(authorities = {"STUDENT"})
    public void findAllUsersByStudentTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all")
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should not allow a lecturer to see all users")
    @WithMockUser(authorities = {"LECTURER"})
    public void findAllUsersByLecturerTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all")
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

}
