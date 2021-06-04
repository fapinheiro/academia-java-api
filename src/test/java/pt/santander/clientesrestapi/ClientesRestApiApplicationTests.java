package pt.santander.clientesrestapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.santander.clientesrestapi.dto.CustomerResponse;
import pt.santander.clientesrestapi.repository.CustomerRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ClientesRestApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ClientesRestApiApplicationTests {

	// If get error below
	// Error running 'ClientesRestApiApplicationTests.shouldReturnListOfCustomers': Failed to resolve org.junit.platform:junit-platform-launcher:1.7.2
	// File -> Settings -> Proxy settings
	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnListOfCustomers() throws Exception {

		// Make request
		MvcResult mvcResult = mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<CustomerResponse>>(){});
		assertThat(customers).isNotEmpty();

	}

	@Test
	void shouldReturnListOfCustomersByName() throws Exception {

		// Make request
		MvcResult mvcResult = mockMvc.perform(get("/customers").param("name", "filipe"))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<CustomerResponse>>(){});
		assertThat(customers).isNotEmpty();
		for(int i=0; i<customers.size(); i++){
			assertThat(customers.get(i).getName()).contains("filipe");
		}

	}

	@Test
	void shouldReturnListOfCustomersByNif() throws Exception {

		// Make request
		MvcResult mvcResult = mockMvc.perform(get("/customers").param("nif", "298973534"))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<CustomerResponse>>(){});
		assertThat(customers).isNotEmpty();
		assertThat(customers.get(0).getNif()).isEqualTo("298973534");

	}

	@Test
	void shouldReturnListOfCustomersByNameAndNif() throws Exception {

		// Make request
		MvcResult mvcResult = mockMvc.perform(get("/customers")
				.param("name", "filipe")
				.param("nif", "298973533"))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<CustomerResponse>>(){});
		assertThat(customers).isNotEmpty();
		assertThat(customers.get(0).getName()).contains("filipe");
		assertThat(customers.get(0).getNif()).isEqualTo("298973533");

	}

	@Test
	void shouldReturnNotFoundForListOfCustomers() throws Exception {

		// Make request
		mockMvc.perform(get("/customers")
				.param("name", "nuno"))
				.andExpect(status().isNotFound());

	}

	@Test
	void shouldCreateNewCustomer() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"jessica pinheiro\",\n" +
				"    \"nif\" : \"298973537\",\n" +
				"    \"email\" : \"jessica@gmail.com\"\n" +
				"}";

		// Make request
		MvcResult mvcResult = mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customerNew = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerNew).isNotNull();
		assertThat(customerNew.getId()).isPositive();
		assertThat(customerNew.getName()).isEqualTo("jessica pinheiro");
		assertThat(customerNew.getNif()).isEqualTo("298973537");
		assertThat(customerNew.getEmail()).isEqualTo("jessica@gmail.com");
	}

	@Test
	void shouldNotPermitCreateNewCustomerWithExistentNif() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"jessica pinheiro\",\n" +
				"    \"nif\" : \"298983545\",\n" +
				"    \"email\" : \"jessica@gmail.com\"\n" +
				"}";

		mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isOk());

		try {
			mockMvc.perform(post("/customers")
					.header("Content-Type", "application/json")
					.content(body))
				.andExpect(status().isInternalServerError());
		} catch (Exception e) {
			assertThat(e).hasCauseInstanceOf(DataIntegrityViolationException.class);
		}

	}

	@Test
	void shouldReturnBadRequestWhenCreatingNewCustomerInvalidName() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"\",\n" +
				"    \"nif\" : \"298973537\",\n" +
				"    \"email\" : \"jessica@gmail.com\"\n" +
				"}";

		mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnBadRequestWhenCreatingNewCustomerInvalidNif() throws Exception {
		final String body = "{\n" +
				"    \"name\" : \"jessica pinheiro\",\n" +
				"    \"nif\" : \"\",\n" +
				"    \"email\" : \"jessica@gmail.com\"\n" +
				"}";

		mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnBadRequestWhenCreatingNewCustomerInvalidEmail() throws Exception {
		final String body = "{\n" +
				"    \"name\" : \"jessica pinheiro\",\n" +
				"    \"nif\" : \"298973537\",\n" +
				"    \"email\" : \"jessica@\"\n" +
				"}";

		mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldUpdateCustomer() throws Exception {

		String body = "{\n" +
				"    \"name\" : \"nuno pinheiro\",\n" +
				"    \"nif\" : \"298973538\",\n" +
				"    \"email\" : \"nuno@gmail.com\"\n" +
				"}";

		// Make request
		MvcResult mvcResult = mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customerNew = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerNew).isNotNull();
		assertThat(customerNew.getId()).isPositive();
		assertThat(customerNew.getName()).isEqualTo("nuno pinheiro");
		assertThat(customerNew.getNif()).isEqualTo("298973538");
		assertThat(customerNew.getEmail()).isEqualTo("nuno@gmail.com");

		// Prepare update body
		body = "{\n" +
				"    \"name\" : \"nuno alves pinheiro\",\n" +
				"    \"nif\" : \"298973538\",\n" +
				"    \"email\" : \"nuno-pinheiro@gmail.com\"\n" +
				"}";

		// Make request
		mvcResult = mockMvc.perform(put("/customers/" + customerNew.getId())
				.header("Content-Type", "application/json").content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		mapper = new ObjectMapper();
		CustomerResponse customerUpdate = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerUpdate).isNotNull();
		assertThat(customerUpdate.getId()).isEqualTo(customerNew.getId());
		assertThat(customerUpdate.getName()).isEqualTo("nuno alves pinheiro");
		assertThat(customerUpdate.getNif()).isEqualTo("298973538");
		assertThat(customerUpdate.getEmail()).isEqualTo("nuno-pinheiro@gmail.com");

	}

	@Test
	void shouldReturnNotfoundCustomerWhenUpdating() throws Exception {

		String body = "{\n" +
				"    \"name\" : \"duarte pinheiro\",\n" +
				"    \"nif\" : \"298973539\",\n" +
				"    \"email\" : \"duarte@gmail.com\"\n" +
				"}";

		// Make request
		mockMvc.perform(put("/customers/20").header("Content-Type", "application/json").content(body))
				.andExpect(status().isNotFound());

	}

	@Test
	void shouldReturnBadRequestWhenUpdatingCustomerInvalidName() throws Exception {

		String body = "{\n" +
				"    \"name\" : \"duarte pinheiro\",\n" +
				"    \"nif\" : \"298973539\",\n" +
				"    \"email\" : \"duarte@gmail.com\"\n" +
				"}";

		// Make request
		MvcResult mvcResult = mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customerNew = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerNew).isNotNull();
		assertThat(customerNew.getId()).isPositive();
		assertThat(customerNew.getName()).isEqualTo("duarte pinheiro");
		assertThat(customerNew.getNif()).isEqualTo("298973539");
		assertThat(customerNew.getEmail()).isEqualTo("duarte@gmail.com");

		// Prepare update body
		body = "{\n" +
				"    \"name\" : \"\",\n" +
				"    \"nif\" : \"298973539\",\n" +
				"    \"email\" : \"duarte@gmail.com\"\n" +
				"}";

		// Make request
		mockMvc.perform(put("/customers/" + customerNew.getId()).header("Content-Type", "application/json").content(body))
				.andExpect(status().isBadRequest());

	}

	@Test
	void shouldReturnBadRequestWhenUpdatingCustomerInvalidNif() throws Exception {

		String body = "{\n" +
				"    \"name\" : \"matilde pinheiro\",\n" +
				"    \"nif\" : \"298973540\",\n" +
				"    \"email\" : \"matilde@gmail.com\"\n" +
				"}";

		// Make request
		MvcResult mvcResult = mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customerNew = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerNew).isNotNull();
		assertThat(customerNew.getId()).isPositive();
		assertThat(customerNew.getName()).isEqualTo("matilde pinheiro");
		assertThat(customerNew.getNif()).isEqualTo("298973540");
		assertThat(customerNew.getEmail()).isEqualTo("matilde@gmail.com");

		// Prepare update body
		body = "{\n" +
				"    \"name\" : \"matilde pinheiro\",\n" +
				"    \"nif\" : \"\",\n" +
				"    \"email\" : \"duarte@gmail.com\"\n" +
				"}";

		// Make request
		mockMvc.perform(put("/customers/" + customerNew.getId())
				.header("Content-Type", "application/json").content(body))
				.andExpect(status().isBadRequest());

	}

	@Test
	void shouldReturnBadRequestWhenUpdatingCustomerInvalidEmail() throws Exception {

		String body = "{\n" +
				"    \"name\" : \"caroline pinheiro\",\n" +
				"    \"nif\" : \"298973541\",\n" +
				"    \"email\" : \"caroline@gmail.com\"\n" +
				"}";

		// Make request
		MvcResult mvcResult = mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customerNew = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerNew).isNotNull();
		assertThat(customerNew.getId()).isPositive();
		assertThat(customerNew.getName()).isEqualTo("caroline pinheiro");
		assertThat(customerNew.getNif()).isEqualTo("298973541");
		assertThat(customerNew.getEmail()).isEqualTo("caroline@gmail.com");

		// Prepare update body
		body = "{\n" +
				"    \"name\" : \"matilde pinheiro\",\n" +
				"    \"nif\" : \"298973541\",\n" +
				"    \"email\" : \"duarte@\"\n" +
				"}";

		// Make request
		mockMvc.perform(put("/customers/" + customerNew.getId()).header("Content-Type", "application/json").content(body))
				.andExpect(status().isBadRequest());

	}

	@Test
	void shouldDeactivateCustomer() throws Exception {

		String body = "{\n" +
				"    \"name\" : \"thomaz pinheiro\",\n" +
				"    \"nif\" : \"298973542\",\n" +
				"    \"email\" : \"thomaz@gmail.com\"\n" +
				"}";

		// Make request
		MvcResult mvcResult = mockMvc.perform(post("/customers").header("Content-Type", "application/json").content(body))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customerNew = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customerNew).isNotNull();
		assertThat(customerNew.getId()).isPositive();
		assertThat(customerNew.getName()).isEqualTo("thomaz pinheiro");
		assertThat(customerNew.getNif()).isEqualTo("298973542");
		assertThat(customerNew.getEmail()).isEqualTo("thomaz@gmail.com");

		// Make request
		mockMvc.perform(delete("/customers/" + customerNew.getId()).header("Content-Type", "application/json"))
				.andExpect(status().isOk());

	}

	@Test
	void shouldReturnNotFoundWhenDeactivatingCustomer() throws Exception {

		// Make request
		mockMvc.perform(delete("/customers/99").header("Content-Type", "application/json"))
				.andExpect(status().isNotFound());

	}
}
