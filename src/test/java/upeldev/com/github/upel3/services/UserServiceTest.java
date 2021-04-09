package upeldev.com.github.upel3.services;

import org.assertj.core.api.Assertions;
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

        Assertions.assertThat(userService.findById(user1Id))
                .isEqualTo(user1);
        Assertions.assertThat(userService.findById(user2Id))
                .isEqualTo(user2);
        Assertions.assertThat(userService.findById(wrongId))
                .isNull();
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

        Assertions.assertThat(userService.findByEmail(user1Email))
                .isEqualTo(user1);
        Assertions.assertThat(userService.findByEmail(user2Email))
                .isEqualTo(user2);
        Assertions.assertThat(userService.findByEmail(wrongEmail))
                .isNull();
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

        Assertions.assertThat(userService.isAdmin(user1))
                .isTrue();
        Assertions.assertThat(userService.isAdmin(user2))
                .isFalse();
        Assertions.assertThat(userService.isAdmin(user3))
                .isTrue();
    }

    @Test
    @DisplayName("Should register user with valid data")
    public void registerNewUserWithValidDataTest(){
        User newUser1 = new User("Brad", "Pitt", "pitt@gmail.com", "8888");
        newUser1.getRoles().add(Role.STUDENT);
        newUser1.setIndexNumber("123456");
        String newUser1Email = newUser1.getEmail();

        User newUser2 = new User("Angelina", "Jolie", "jolie@gmail.com", "8888");
        newUser2.getRoles().add(Role.LECTURER);
        String newUser2Email = newUser2.getEmail();

        User newUser3 = new User("Leonardo", "DiCaprio", "dicaprio@gmail.com", "8888");
        newUser2.getRoles().add(Role.ADMIN);
        String newUser3Email = newUser3.getEmail();

        Mockito.when(userRepository.findUserByEmail(newUser1Email))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByEmail(newUser2Email))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByEmail(newUser3Email))
                .thenReturn(null);

        // Successful registration
        Assertions.assertThatCode(() -> userService.registerNewUser(newUser1))
                .doesNotThrowAnyException();
        Assertions.assertThatCode(() -> userService.registerNewUser(newUser2))
                .doesNotThrowAnyException();
        Assertions.assertThatCode(() -> userService.registerNewUser(newUser3))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should not register student without index number")
    public void registerNewUserStudentWithoutIndexNumberTest(){
        User newUser = new User("Brad", "Pitt", "pitt@gmail.com", "8888");
        newUser.getRoles().add(Role.STUDENT);
        String goodRegistrationEmail = newUser.getEmail();

        Mockito.when(userRepository.findUserByEmail(goodRegistrationEmail))
                .thenReturn(null);

        Assertions.assertThatThrownBy(() -> userService.registerNewUser(newUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student has to have an index number");
    }

    @Test
    @DisplayName("Should not register users with invalid email")
    public void registerNewUserWithInvalidEmailTest(){
        String wrongEmail = "pittgmail.com";

        User wrongUser = new User();
        wrongUser.setEmail(wrongEmail);
        wrongUser.setPassword("1234");

        Mockito.when(userRepository.findUserByEmail(wrongEmail))
                .thenReturn(null);

        Assertions.assertThatThrownBy(() -> userService.registerNewUser(wrongUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("Email %s is invalid", wrongEmail));
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

        Assertions.assertThatThrownBy(() -> userService.registerNewUser(user1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("User with email %s already exists", user1Email));
        Assertions.assertThatThrownBy(() -> userService.registerNewUser(user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("User with email %s already exists", user2Email));
    }
}
