package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Cadastro Editor com Mock")
@ExtendWith(MockitoExtension.class)
public class CadastroEditorMockTest {

    Editor editor;

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

        Mockito.when(armazenamentoEditor.salvar(editor))
                .thenReturn(new Editor(1L, "Filipe", "filipe@gmail.com",
                        BigDecimal.TEN, true));
    }

    @Test
    @DisplayName("Should return a creation id")
    void createId() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
    }

}
