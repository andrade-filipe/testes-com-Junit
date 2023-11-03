package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("CadastroEditor com Stub")
class CadastroEditorStubTest {

    private CadastroEditor cadastroEditor;
    private ArmazenamentoEditorStub armazenamentoEditor;
    private Editor editor;

    @BeforeEach
    void beforeEach() {
        editor = new Editor(null,
                "Filipe",
                "filipe@gmail.com",
                BigDecimal.TEN,
                true);

        armazenamentoEditor = new ArmazenamentoEditorStub();

        cadastroEditor = new CadastroEditor(
                armazenamentoEditor,
                new GerenciadorEnvioEmail() {
                    @Override
                    void enviarEmail(Mensagem mensagem) {
                        System.out.println("Enviando email");
                    }
                });
    }

    @Test
    @DisplayName("SHOULD return an editor with id")
    public void cadastrar() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
        assertTrue(armazenamentoEditor.chamouSalvar);
    }

    @Test
    @DisplayName("IF given an null editor")
    public void nullPointer() {
        assertThrows(NullPointerException.class, () -> cadastroEditor.criar(null));
        assertFalse(armazenamentoEditor.chamouSalvar);
    }

    @Test
    @DisplayName("IF email exists THEN throw exception")
    void emailExists() {
        editor.setEmail("filipe.existe@gmail.com");
        assertThrows(RegraNegocioException.class, () -> cadastroEditor.criar(editor));
        assertFalse(armazenamentoEditor.chamouSalvar);
    }
}