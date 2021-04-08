package upeldev.com.github.upel3.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import upeldev.com.github.upel3.model.Role;
import upeldev.com.github.upel3.model.User;
import upeldev.com.github.upel3.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup(){
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should find users by their IDs")
    public void findByIdTest(){
        User user1 = new User("John", "Doe", "doe@gmail.com", "1234");
        User user2 = new User("John", "Smith", "smith@gmail.com", "4321");

        Long user1Id = user1.getId();
        Long user2Id = user2.getId();
        Long wrongId = 10L;

        Mockito.when(userRepository.findUserById(user1Id))
                .thenReturn(user1);
        Mockito.when(userRepository.findUserById(user2Id))
                .thenReturn(user2);
        Mockito.when(userRepository.findUserById(wrongId))
                .thenReturn(null);

        Assertions.assertEquals(userService.findById(user1Id), user1);
        Assertions.assertEquals(userService.findById(user2Id), user2);
        Assertions.assertNull(userService.findById(wrongId));
    }

    @Test
    @DisplayName("Should find users by their emails")
    public void findByEmailTest(){
        User user1 = new User("John", "Doe", "doe@gmail.com", "1234");
        User user2 = new User("John", "Smith", "smith@gmail.com", "4321");

        String user1Email = user1.getEmail();
        String user2Email = user2.getEmail();
        String wrongEmail = "pitt@gmail.com";

        Mockito.when(userRepository.findUserByEmail(user1Email))
                .thenReturn(user1);
        Mockito.when(userRepository.findUserByEmail(user2Email))
                .thenReturn(user2);
        Mockito.when(userRepository.findUserByEmail(wrongEmail))
                .thenReturn(null);

        Assertions.assertEquals(userService.findByEmail(user1Email), user1);
        Assertions.assertEquals(userService.findByEmail(user2Email), user2);
        Assertions.assertNull(userService.findByEmail(wrongEmail));
    }

    @Test
    @DisplayName("Should check if a user is admin or not")
    public void isAdminTest(){
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.getRoles().add(Role.ADMIN);

        user2.getRoles().add(Role.STUDENT);
        user2.getRoles().add(Role.LECTURER);

        user3.getRoles().add(Role.LECTURER);
        user3.getRoles().add(Role.ADMIN);

        Assertions.assertTrue(userService.isAdmin(user1));
        Assertions.assertFalse(userService.isAdmin(user2));
        Assertions.assertTrue(userService.isAdmin(user3));
    }

    @Test
    @DisplayName("Should register user with valid data")
    public void registerNewUserWithValidDataTest(){
        User newUser1 = new User("Brad", "Pitt", "pitt@gmail.com", "8888");
        newUser1.getRoles().add(Role.STUDENT);
        newUser1.setIndexNumber("123456");
        String goodRegistrationEmail1 = newUser1.getEmail();

        User newUser2 = new User("Angelina", "Jolie", "jolie@gmail.com", "8888");
        newUser2.getRoles().add(Role.LECTURER);
        String goodRegistrationEmail2 = newUser2.getEmail();

        User newUser3 = new User("Leonardo", "DiCaprio", "dicaprio@gmail.com", "8888");
        newUser2.getRoles().add(Role.ADMIN);
        String goodRegistrationEmail3 = newUser3.getEmail();

        Mockito.when(userRepository.findUserByEmail(goodRegistrationEmail1))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByEmail(goodRegistrationEmail2))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByEmail(goodRegistrationEmail3))
                .thenReturn(null);

        // Successful registration
        Assertions.assertDoesNotThrow(() -> userService.registerNewUser(newUser1));
        Assertions.assertDoesNotThrow(() -> userService.registerNewUser(newUser2));
        Assertions.assertDoesNotThrow(() -> userService.registerNewUser(newUser3));
    }

    @Test
    @DisplayName("Should not register student without index number")
    public void registerNewUserStudentWithoutIndexNumberTest(){
        User newUser = new User("Brad", "Pitt", "pitt@gmail.com", "8888");
        newUser.getRoles().add(Role.STUDENT);
        String goodRegistrationEmail = newUser.getEmail();

        Mockito.when(userRepository.findUserByEmail(goodRegistrationEmail))
                .thenReturn(null);

        // Student has to have an index number
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(newUser));
    }

    @Test
    @DisplayName("Should not register users with invalid emails")
    public void registerNewUserWithInvalidEmailTest(){
        String wrongEmail1 = "pittgmail.com";
        String wrongEmail2 = "pitt@gmail.";
        String wrongEmail3 = "..pitt@gmail.com";

        User wrongUser1 = new User();
        wrongUser1.setEmail(wrongEmail1);
        User wrongUser2 = new User();
        wrongUser2.setEmail(wrongEmail2);
        User wrongUser3 = new User();
        wrongUser3.setEmail(wrongEmail3);

        Mockito.when(userRepository.findUserByEmail(wrongEmail1))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByEmail(wrongEmail2))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByEmail(wrongEmail3))
                .thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(wrongUser1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(wrongUser2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(wrongUser3));
    }

    @Test
    @DisplayName("Should not register users with emails present in database")
    public void registerNewUserExistingUserTest(){
        String user1Email = "doe@gmail.com";
        String user2Email = "smith@gmail.com";

        User user1 = new User();
        user1.setEmail(user1Email);
        User user2 = new User();
        user2.setEmail(user2Email);

        Mockito.when(userRepository.findUserByEmail(user1Email))
                .thenReturn(user1);
        Mockito.when(userRepository.findUserByEmail(user2Email))
                .thenReturn(user2);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(user1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(user2));
    }
}
