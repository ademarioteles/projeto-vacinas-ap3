package com.vacinas.ap3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vacinas.ap3.controller.RegistroDeVacinacaoController;
import com.vacinas.ap3.entity.RegistroDeVacinacao;
import com.vacinas.ap3.service.RegistroDeVacinacaoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistroDeVacinacaoController.class)
public class RegistroDeVacinacaoControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RegistroDeVacinacaoService registroDeVacinacaoService;

	@Test
	public void testCriarRegistroDeVacinacao() throws Exception {
		RegistroDeVacinacao registroDeVacinacao = new RegistroDeVacinacao();
		when(registroDeVacinacaoService.criarRegistroDeVacinacao(registroDeVacinacao)).thenReturn(true);

		mockMvc.perform(post("/cadastrar")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(registroDeVacinacao)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.mensagem").value("Registro cadastrado com sucesso!"));
	}

	@Test
	public void testEditarRegistroDeVacinacao() throws Exception {
		String id = "1";
		RegistroDeVacinacao registroDeVacinacao = new RegistroDeVacinacao();
		when(registroDeVacinacaoService.editarRegistroDeVacinacao(registroDeVacinacao, id)).thenReturn(true);

		mockMvc.perform(post("/editar/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(registroDeVacinacao)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.mensagem").value("Registro editado com sucesso!"));
	}

	@Test
	public void testEditarRegistroDeVacinacaoParcial() throws Exception {
		String id = "1";
		Map<String, Object> atualizacao = new HashMap<>();
		when(registroDeVacinacaoService.editarRegistroDeVacinacaoParcial(id, atualizacao)).thenReturn(true);

		mockMvc.perform(patch("/editar/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(atualizacao)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.mensagem").value("Registro editado com sucesso!"));
	}

	@Test
	public void testApagarRegistroDeVacinacaoPorId() throws Exception {
		String id = "1";
		when(registroDeVacinacaoService.apagarRegistro(id)).thenReturn(true);

		mockMvc.perform(get("/apagar/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().string("Resgistro apagado com sucesso"));
	}

	@Test
	public void testObterRegistroDeVacinacaoPorIdDoPaciente() throws Exception {
		String id = "1";
		List<RegistroDeVacinacao> registros = new ArrayList<>();  // Adicione registros à lista
		when(registroDeVacinacaoService.obterRegistroDeVacinacaoPorIdDoPaciente(id)).thenReturn(registros);

		mockMvc.perform(get("/paciente/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().json(asJsonString(registros)));
	}

	@Test
	public void testObterRegistrosDeVacinacaoPorIdDaVacina() throws Exception {
		String id = "1";
		List<RegistroDeVacinacao> registros = new ArrayList<>();  // Adicione registros à lista
		when(registroDeVacinacaoService.obterRegistrosDeVacinacaoPorIdDaVacina(id)).thenReturn(registros);

		mockMvc.perform(get("/vacina/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().json(asJsonString(registros)));
	}

	@Test
	public void testObterListaRegistroDeVacinacao() throws Exception {
		List<RegistroDeVacinacao> registros = new ArrayList<>();  // Adicione registros à lista
		when(registroDeVacinacaoService.listarTodosOsRegistrosDeVacinacao()).thenReturn(registros);

		mockMvc.perform(get("/lista"))
				.andExpect(status().isOk())
				.andExpect(content().json(asJsonString(registros)));
	}

	// Método para converter objetos Java em JSON
	private static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
