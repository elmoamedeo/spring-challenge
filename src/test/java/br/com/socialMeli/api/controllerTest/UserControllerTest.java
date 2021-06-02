package br.com.socialMeli.api.controllerTest;

import br.com.socialMeli.api.controller.UserController;
import br.com.socialMeli.api.model.User;
import br.com.socialMeli.api.repository.UserRepository;
import br.com.socialMeli.api.service.FollowersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private static final int HTTP_STATUS_CODE_OK = 200;
    private static final int HTTP_STATUS_CODE_BAD_REQUEST = 400;

    private static final Long USER_SELLER_ID = 1L;
    private static final Long USER_NOT_SELLER_ID = 2L;

    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowersService followersService;

    @Mock
    private User userMock;

    @Mock
    private User userMock1;

    @Before
    public void setup() {
        userController = new UserController(userRepository, followersService);
    }

    @Before
    public void init() {
        List<User> userList = new ArrayList<>();

        userMock = new User(
                1L,
                "Joe",
                "joe@gmail.com",
                new Date(1999 - 8 - 3),
                "12345678910",
                true,
                new ArrayList<>(),
                new ArrayList<>(),
                new Date(),
                new Date()
        );

        userMock1 = new User(
                2L,
                "Brandon",
                "brandon@gmail.com",
                new Date(2001 - 8 - 3),
                "12345678910",
                false,
                new ArrayList<>(),
                new ArrayList<>(),
                new Date(),
                new Date()
        );

        userList.add(userMock);
        userList.add(userMock);

        when(userRepository.findById(userMock.getId())).thenReturn(Optional.of(userMock));
        when(userRepository.findById(userMock1.getId())).thenReturn(Optional.of(userMock1));
    }

    @Test
    public void shouldSaveToFollowersListIfValidAndReturnOk() {
        logger.info("TEST - POST - Social Meli - (followUser) - shouldSaveToFollowersListIfValidAndReturnOk()");

        ResponseEntity<?> responseEntity = userController.followUser(USER_NOT_SELLER_ID, USER_SELLER_ID);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HTTP_STATUS_CODE_OK);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldNotSaveToFollowersListIfUsersDontExistAndReturnBadRequest() {
        logger.info("TEST - POST - Social Meli - (followUser) - shouldNotSaveToFollowersListIfUsersDontExistAndReturnBadRequest()");

        ResponseEntity<?> responseEntity = userController.followUser(10L, 20L);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HTTP_STATUS_CODE_BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldNotSaveToFollowersListIfSameUserAndReturnBadRequest() {
        logger.info("TEST - POST - Social Meli - (followUser) - shouldNotSaveToFollowersListIfSameUserAndReturnBadRequest()");

        ResponseEntity<?> responseEntity = userController.followUser(USER_SELLER_ID, USER_SELLER_ID);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HTTP_STATUS_CODE_BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldNotSaveToFollowersListIfUserIsNotSellerAndReturnBadRequest() {
        logger.info("TEST - POST - Social Meli - (followUser) - shouldNotSaveToFollowersListIfUserIsNotSellerAndReturnBadRequest()");

        ResponseEntity<?> responseEntity = userController.followUser(USER_SELLER_ID, USER_NOT_SELLER_ID);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HTTP_STATUS_CODE_BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldShowFollowersCountAndReturnOk() {
        logger.info("TEST - GET - Social Meli - (getFollowerCountForUser) - shouldShowFollowersCountAndReturnOk()");

        ResponseEntity<?> responseEntity = userController.getFollowerCountForUser(USER_SELLER_ID);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HTTP_STATUS_CODE_OK);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldNotShowFollowersCountIfUserNotSellerAndReturnBadRequest() {
        logger.info("TEST - GET - Social Meli - (getFollowerCountForUser) - shouldNotShowFollowersCountIfUserNotSellerAndReturnBadRequest()");

        ResponseEntity<?> responseEntity = userController.getFollowerCountForUser(USER_NOT_SELLER_ID);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HTTP_STATUS_CODE_BAD_REQUEST);
        assertNotNull(responseEntity.getBody());
    }

}
