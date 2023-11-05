package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.exception.EditorNaoEncontradoException;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Cadastro Editor com Mock")
@ExtendWith(MockitoExtension.class)
public class CadastroEditorMockTest {
    @Captor
    ArgumentCaptor<Mensagem> mensagemArgumentCaptor;
    @Mock
    ArmazenamentoEditor armazenamentoEditor;
    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;
    @InjectMocks
    CadastroEditor cadastroEditor;

    @Nested
    @DisplayName("When editor not null")
    class CadastroComEditor {
        @Spy
        Editor editor = new Editor(null, "Filipe", "filipe@gmail.com",
                BigDecimal.TEN, true);

        @BeforeEach
        void beforeEach() {
            when(armazenamentoEditor.salvar(any(Editor.class)))
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
            verify(
                    armazenamentoEditor,
                    times(1))
                    .salvar(eq(editor));
        }

        @Test
        @DisplayName("Dado um editor Quando criar lançar exception Entao não mandar email")
        void criarException() {
            when(armazenamentoEditor.salvar(editor))
                    .thenThrow(new RuntimeException());
            assertThrows(RuntimeException.class, () -> cadastroEditor.criar(editor));
            verify(gerenciadorEnvioEmail,
                    never()).enviarEmail(any());
        }

        @Test
        @DisplayName("Argument Capture para métodos Void")
        void argumentCapture() {
            Editor editorSalvo = cadastroEditor.criar(editor);
            verify(gerenciadorEnvioEmail).enviarEmail(mensagemArgumentCaptor.capture());
            assertEquals(editorSalvo.getEmail(),
                    mensagemArgumentCaptor.getValue().getDestinatario());
        }

        @Test
        @DisplayName("Spy em verificação de email")
        void spyEmail() {
            cadastroEditor.criar(editor);
            verify(editor,
                    atLeast(1))
                    .getEmail();
        }

        @Test
        @DisplayName("Dado email existente lance exception")
        void emailExitente() {
            when(armazenamentoEditor.encontrarPorEmail("filipe@gmail.com"))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(editor));

            Editor editorComEmailExistente = new Editor(null, "Filipe", "filipe@gmail.com",
                    BigDecimal.TEN, true);

            cadastroEditor.criar(editor);

            assertThrows(RegraNegocioException.class,
                    () -> cadastroEditor.criar(editorComEmailExistente));
        }

        @Test
        @DisplayName("Deve enviar email após salvar")
        void enviarEmailAposSalvar() {
            cadastroEditor.criar(editor);

            InOrder inOrder = inOrder(armazenamentoEditor, gerenciadorEnvioEmail);
            inOrder.verify(armazenamentoEditor,
                    times(1)).salvar(editor);
            inOrder.verify(gerenciadorEnvioEmail, times(1))
                    .enviarEmail(any(Mensagem.class));
        }

    }

    @Nested
    @DisplayName("When editor is null")
    class CadastroNullEditor {
        @Test
        @DisplayName("With null editor")
        void editorNull() {
            assertThrows(NullPointerException.class, () -> cadastroEditor.criar(null));
        }
    }

    @Nested
    @DisplayName("When editing a valid editor")
    class EdicaoComEditorValido {
        @Spy
        Editor editor = new Editor(1L, "Filipe", "filipe@gmail.com",
                BigDecimal.TEN, true);

        @BeforeEach
        void beforeEach() {
            when(armazenamentoEditor
                    .salvar(editor))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0, Editor.class));
            when(armazenamentoEditor
                    .encontrarPorId(1L))
                    .thenReturn(Optional.of(editor));
        }

        @Test
        @DisplayName("Should alter a saved editor")
        void alterEditor() {
            Editor editorAtualizado = new Editor(
                    1L,
                    "Filipe Andrade",
                    "filipeandrade@gmail.com",
                    BigDecimal.ONE,
                    false);

            cadastroEditor.editar(editorAtualizado);
            verify(editor,
                    times(1))
                    .atualizarComDados(editorAtualizado);

            InOrder inOrder = inOrder(editor, armazenamentoEditor);
            inOrder.verify(editor).atualizarComDados(editorAtualizado);
            inOrder.verify(armazenamentoEditor).salvar(editor);
        }
    }

    @Nested
    @DisplayName("When editing a invalid editor")
    class EdicaoComEditorInexistente {
        Editor editor = new Editor(99L, "Filipe", "filipe@gmail.com",
                BigDecimal.TEN, true);

        @BeforeEach
        void init() {
            when(armazenamentoEditor.encontrarPorId(99L)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("SHOULD return exception")
        void editingEmptyEditor() {
            assertThrows(EditorNaoEncontradoException.class, () -> cadastroEditor.editar(editor));
            verify(armazenamentoEditor, never()).salvar(any(Editor.class));
        }
    }
}
