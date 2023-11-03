package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Cadastro Editor com Mock")
@ExtendWith(MockitoExtension.class)
public class CadastroEditorMockTest {

    Editor editor;

    @Captor
    ArgumentCaptor<Mensagem> mensagemArgumentCaptor;

    @Mock
    ArmazenamentoEditor armazenamentoEditor;
    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;

    @InjectMocks
    CadastroEditor cadastroEditor;

    @BeforeEach
    void beforeEach() {
        editor = new Editor(null,
                "Filipe",
                "filipe@gmail.com",
                BigDecimal.TEN,
                true);

        Mockito.when(armazenamentoEditor.salvar(Mockito.any(Editor.class)))
                .thenAnswer(invocation -> {
                    Editor editorPassado = invocation.getArgument(0, Editor.class);
                    editorPassado.setId(1L);
                    return editorPassado;
                });
    }

    @Test
    @DisplayName("Should return a creation id")
    void createId() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
    }

    @Test
    @DisplayName("Should call salvar in armazenamento")
    void callSalvar() {
        cadastroEditor.criar(editor);
        Mockito.verify(
                        armazenamentoEditor,
                        Mockito.times(1))
                .salvar(Mockito.eq(editor));
    }

    @Test
    @DisplayName("Dado um editor Quando criar lançar exception Entao não mandar email")
    void criarException() {
        Mockito.when(armazenamentoEditor.salvar(editor))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> cadastroEditor.criar(editor));
        Mockito.verify(gerenciadorEnvioEmail,
                Mockito.never()).enviarEmail(Mockito.any());
    }

    @Test
    @DisplayName("Argument Capture para métodos Void")
    void argumentCapture() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        Mockito.verify(gerenciadorEnvioEmail).enviarEmail(mensagemArgumentCaptor.capture());
        assertEquals(editorSalvo.getEmail(),
                mensagemArgumentCaptor.getValue().getDestinatario());
    }

}
