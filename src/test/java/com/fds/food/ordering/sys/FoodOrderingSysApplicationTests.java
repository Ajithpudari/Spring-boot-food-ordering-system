package com.fds.food.ordering.sys;

import com.fds.food.ordering.sys.models.CartItem;
import com.fds.food.ordering.sys.models.FoodProduct;
import com.fds.food.ordering.sys.models.Role;
import com.fds.food.ordering.sys.models.User;
import com.fds.food.ordering.sys.repositories.FoodProductRepository;
import com.fds.food.ordering.sys.repositories.UserRepository;
import com.fds.food.ordering.sys.services.FoodProductServiceImpl;
import com.fds.food.ordering.sys.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class FoodOrderingSysApplicationTests {
	//noob tests
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private FoodProductServiceImpl foodProductService;

	@MockBean
	private FoodProductRepository foodProductRepository;

	@MockBean
	private UserRepository userRepository;

	@Test
	public void getUsersTest() {
		when(userRepository.findAll()).thenReturn(Stream.of(
				new User("customer3","pws",true,new HashSet<Role>(Arrays.asList(new Role("USER")))),
				new User("vendor2","pws",true, new HashSet<Role>(Arrays.asList(new Role("VENDOR")))),
				new User("customer3","pws",true, new HashSet<Role>(Arrays.asList(new Role("USER"))))
		).collect(Collectors.toList()));
		assertEquals(3, userDetailsService.findAll().size());
	}

	@Test
	public void saveUser(){
		User user = new User("vendor5", "pass", true,
				new HashSet<Role>(Arrays.asList(new Role("VENDOR"))));
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(user, userDetailsService.save(user));
	}

	@Test
	public void deleteUser() {
		User user = new User("vendor6", "pass", true,
				new HashSet<Role>(Arrays.asList(new Role("VENDOR"))));
		userDetailsService.deleteUser(user);
		verify(userRepository,times(1)).delete(user);
	}


}
