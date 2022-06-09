package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {
	Log LOGGER = LogFactory.getLog(FundamentosApplication.class);
	private MyBean myBean;
	private ComponentDependency componentDependency;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run (String... args) {
//		testingScriptHere();
		saveUsersInDataBase();
		getInformationJpqFromUser();
		saveWithErrorTransactional();
	}

	private void saveUsersInDataBase(){
		User user1=new User("Johnny", "john@domain.com", LocalDate.of(2021,03,20));
		User user2=new User("Manie", "manie@domain.com", LocalDate.of(2021,01,21));
		User user3=new User("tom", "tom@domain.com", LocalDate.of(2021,9,21));
		User user4=new User("willie", "wilie@domain.com", LocalDate.of(2021,03,10));
		User user5=new User("josue", "josue@domain.com", LocalDate.of(2021,07,11));
		User user6=new User("lef", "lef@domain.com", LocalDate.of(2021,04,25));
		User user7=new User("imma", "imma@domain.com", LocalDate.of(2021,02,14));
		List<User> list = Arrays.asList(user1,user2,user3,user4,user5,user6,user7);
		list.stream().forEach(userRepository::save);
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@domain.com",  LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional1@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@domain.com", LocalDate.now());
		List<User> users = Arrays.asList(test1, test2, test3, test4);

		try {
			userService.saveTransactional(users);
		} catch(Exception e){
			LOGGER.error("Esta es una excepcion en el metodo transaccional " + e);
		}

		userService.getAllUsers()
				.forEach(user -> LOGGER.info("este es el usuario dentro del metodo transaccional " + user));
	}

	private void getInformationJpqFromUser(){
		LOGGER.info("User with findByUserEmail method: " + userRepository.findByUserEmail("wilie@domain.com").orElseThrow(()-> new RuntimeException("Error: User not found!")));

		userRepository.findAndSort("M", Sort.by("id").descending())
				.stream()
				.forEach(user->LOGGER.info("USER with method  sort: " + user));
		userRepository.findByName("willie")
		.stream()
				.forEach(user -> LOGGER.info("User with Query method: " + user));
		LOGGER.info(userRepository.findByEmailAndName("tom@domain.com","tom").orElseThrow(()-> new RuntimeException("user not found!!")));
		userRepository.findByNameLike("%i%").stream().forEach(user->LOGGER.info("Usuario findByNameLike " + user));

		LOGGER.info(userRepository.findByEmailAndName("tom@domain.com","tom").orElseThrow(()-> new RuntimeException("user not found!!")));

		userRepository.findByNameOrEmail("josue", null).stream().forEach(user->LOGGER.info("Usuario findByNameOrEmail: " + user));

		userRepository.findByBirthdayBetween(LocalDate.of(2021,3,1),LocalDate.of(2022,4,2)).stream().forEach(user->LOGGER.info("Usuario findByBirthdayBetween: " + user));

		userRepository.findByNameLikeOrderByIdDesc("%e%").stream().forEach(user->LOGGER.info("User found with like and order: " + user));

		userRepository.findByNameContainingOrderByIdDesc("imma").stream().forEach(user->LOGGER.info("User found with contain: " + user));

		userRepository.findByEmailContainingOrderById("a").stream().forEach(user->LOGGER.info("User found with findByEmailContainingOrderById: " + user));

		LOGGER.info("El usuario es x named parameter = " + userRepository.getAllByBirthdayAndEmail(LocalDate.of(2021,01,21),"manie@domain.com").orElseThrow(()-> new RuntimeException("No se encontro el usuario a partir del named parameter")));
	}

	private void testingScriptHere(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getAge() + "-" + userPojo.getPassword());
		try {
			//code
			int value = 20/0;
			LOGGER.debug(value);

		} catch (Exception e) {
			LOGGER.error("esto es un error");
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getStackTrace());
		}
	}
}
