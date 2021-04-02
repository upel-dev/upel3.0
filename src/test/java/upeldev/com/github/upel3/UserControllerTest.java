package upeldev.com.github.upel3;

import org.junit.Test;
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
    @WithMockUser(authorities = {"ADMIN"})
    public void findAllUsersAdminTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all")
                .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"STUDENT"})
    public void findAllUsersStudentTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all")
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"LECTURER"})
    public void findAllUsersLecturerTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all")
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

}
