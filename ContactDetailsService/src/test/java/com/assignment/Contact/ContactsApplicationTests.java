package com.assignment.Contact;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.assignment.Contact.dto.ContactDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
class ContactsApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void addContactDewtails() throws Exception {
		Calendar dob = Calendar.getInstance();
		dob.set(1989, 9, 21);
		ContactDetailsDto contactDetailsDto = new ContactDetailsDto("Amol", "Wale", "8600009560", dob.getTime());
		mockMvc.perform(
				MockMvcRequestBuilders.post("/ContactDetailsService/contacts").content(asJsonString(contactDetailsDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1));
	}

	@Test
	public void updateContactDewtails() throws Exception {
		Calendar dob = Calendar.getInstance();
		dob.set(1989, 9, 21);
		ContactDetailsDto contactDetailsDto = new ContactDetailsDto("Amol updated", "Wale updated", "8600009561",
				dob.getTime());
		mockMvc.perform(MockMvcRequestBuilders.post("/ContactDetailsService/contacts/update")
				.content(asJsonString(contactDetailsDto)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1));
	}

	@Test
	public void deleteContactDetails() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/ContactDetailsService/contacts/{id}", 1))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1));
	}

	@Test	
	public void getAllContactDetails() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ContactDetailsService/contacts").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty());
	}

	@Test
	public void updateStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/ContactDetailsService/updatestatus/{contactId}/{status}", 1, 1)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
