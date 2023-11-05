package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoPost;
import com.algaworks.junit.blog.modelo.Editor;
import com.algaworks.junit.blog.modelo.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Nested
@DisplayName("CadastroPost Tests")
@ExtendWith(MockitoExtension.class)
class CadastroPostTest {

    @Captor
    ArgumentCaptor<Mensagem> argumentCaptor;
    @Mock
    ArmazenamentoPost armazenamentoPost;
    @Mock
    CalculadoraGanhos calculadoraGanhos;
    @Mock
    GerenciadorNotificacao gerenciadorNotificacao;
    @InjectMocks
    CadastroPost cadastroPost;

    @Nested
    @DisplayName("Method: criar -> @param Post, @return Post")
    class criarMethodTests {
        @Nested
        @DisplayName("GIVEN a valid author and valid post")
        class givenNormalPost {
            Editor editor = new Editor(1L, "Filipe", "filipe@gmail.com",
                    BigDecimal.TEN, true);
            @Spy
            Post postSpy = new Post();

            @BeforeEach
            void validPost() {
                postSpy.setAutor(editor);
                postSpy.setTitulo("Meu Post");
                postSpy.setConteudo("Conteudo do Post");

                when(armazenamentoPost.salvar(any(Post.class)))
                        .thenAnswer(invocation -> {
                            Post post = invocation.getArgument(0, Post.class);
                            post.setId(1L);
                            return post;
                        });

//                when(calculadoraGanhos.calcular(any(Post.class)))
//                        .thenReturn(new Ganhos(postSpy.getAutor().getValorPagoPorPalavra(),
//                                3, new BigDecimal("30")));
            }

            @Nested
            @DisplayName("WHEN creating a post")
            class whenCreating {
                @BeforeEach
                void createPost() {
                    cadastroPost.criar(postSpy);
                }

                @Test
                @DisplayName("SHOULD return a creation Id")
                void returnCreationId() {
                    assertEquals(1L, postSpy.getId());
                }
            }
        }
    }

    @Nested
    @DisplayName("Method: editar -> @param Post, @return Post")
    class editarMethodTests {
    }

    @Nested
    @DisplayName("Method: remover -> @param Long, @return void")
    class removerMethodTests {
    }
}

