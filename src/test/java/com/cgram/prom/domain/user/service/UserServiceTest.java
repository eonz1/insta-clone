package com.cgram.prom.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.service.MailSender;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    MailSender mailSender;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    ProfileRepository profileRepository;

    MailRequest mailRequest;

    private User user;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository, mailSender, passwordEncoder,
            profileRepository);
        user = User.builder()
            .email("a@gmail.com")
            .password("test1234!")
            .build();
    }

    @Test
    @DisplayName("이메일이 중복되면 회원가입 할 수 없다.")
    public void registerDuplicatedEmail() throws Exception {
        // given
        Optional<User> returnUser = Optional.of(new User());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(returnUser);

        // expected
        UserException exception = assertThrows(UserException.class, () -> {
            userService.register(user);
        });
        assertEquals(exception.getExceptionType().getStatus(), HttpStatus.CONFLICT);
        verify(userRepository, never()).save(user);
        verify(profileRepository, never()).save(any(Profile.class));
        verify(mailSender, never()).send(mailRequest);
    }

    @Test
    @DisplayName("이메일이 중복되지 않으면 회원가입에 성공한다.")
    public void registerEmail() throws Exception {
        // given
        Optional<User> returnUser = Optional.empty();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(returnUser);
//        doNothing().when(mailSender).send(any(MailRequest.class));

        // when
        userService.register(user);

        // then
        verify(userRepository, times(1)).save(user);
        verify(profileRepository, times(1)).save(any(Profile.class));
//        verify(mailSender, times(1)).send(any(MailRequest.class));
    }

    @Test
    @DisplayName("비밀번호 확인")
    public void checkPasswordMatching() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // given
        String password = "tests123123!!";
        String encodedPassword = encoder.encode(password);

        // when
        boolean matches = encoder.matches(password, encodedPassword);

        // then
        System.out.println(password + ", " + encodedPassword);
        assertTrue(matches);
    }

    @Test
    @DisplayName("회원 탈퇴테스트")
    public void withdrawUser() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        User user = User.builder()
            .id(id)
            .isPresent(true)
            .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        userService.withdrawUser(id.toString());

        // then
        Assertions.assertThat(user.isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("탈퇴한 회원 조회 시 예외 발생")
    public void findUserByIdAndPresent() throws Exception {
        // given
        when(userRepository.findByIdAndIsPresent(any(UUID.class), eq(true))).thenReturn(
            Optional.empty());

        // expected
        assertThrows(UserException.class, () -> {
            userService.getUserByIdAndIsPresent(
                UUID.randomUUID().toString());
        });
    }
}