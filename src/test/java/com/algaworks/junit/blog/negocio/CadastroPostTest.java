package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoPost;
import com.algaworks.junit.blog.modelo.Editor;
import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Nested
@DisplayName("CadastroPost Tests")
@ExtendWith(MockitoExtension.class)
//CLASSE QUE VOU TESTAR
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

    // MÉTODO QUE VOU TESTAR
    @Nested
    @DisplayName("Method: criar -> @param Post, @return Post")
    class criarMethodTests {
        @Nested
        @DisplayName("GIVEN a valid author and valid post") //CONDIÇÃO DO TESTE
        class givenNormalPost {
            Editor editor = new Editor(1L, "Filipe", "filipe@gmail.com",
                    BigDecimal.TEN, true);
            @Spy
            Post postSpy = new Post();

            //VALID POST
            @BeforeEach
            void validPost() {
                postSpy.setAutor(editor);
                postSpy.setTitulo("Meu Post");
                postSpy.setConteudo("Conteudo do Post");
                postSpy.setPago(true);
                postSpy.setPublicado(true);
            }

            @Nested
            @DisplayName("WHEN creating a post") //CASO ESPECIFICO
            class whenCreating {
                private Post postCreated;

                @BeforeEach
                void spyMethodInvocations() {
                    when(armazenamentoPost.salvar(any(Post.class)))
                            .thenAnswer(invocation -> {
                                Post post = invocation.getArgument(0, Post.class);
                                post.setId(1L);
                                return post;
                            });
                    when(calculadoraGanhos.calcular(postSpy))
                            .thenReturn(new Ganhos(BigDecimal.TEN,
                                    3,
                                    BigDecimal.valueOf(30)));

                    postCreated = cadastroPost.criar(postSpy);
                }

                @Test
                @DisplayName("SHOULD return a creation Id")
                    //RESULTADO
                void returnCreationId() {
                    assertEquals(1L, postCreated.getId());
                }

                @Test
                @DisplayName("SHOULD verify if the .salvar() was called")
                void verifySave() {
                    verify(armazenamentoPost,
                            times(1))
                            .salvar(any(Post.class));
                }

                @Test
                @DisplayName("SHOULD return a post with Ganhos")
                void returnWithGanhos() {
                    verify(postCreated,
                            times(1))
                            .setGanhos(any(Ganhos.class));
                }

                @Test
                @DisplayName("SHOULD return a post with Slug")
                void returnWithSlug() {
                    verify(postCreated,
                            times(1))
                            .setSlug(anyString());
                    assertNotNull(postCreated.getSlug());
                }

                @Test
                @DisplayName("SHOULD call .calculate before .salvar")
                void methodOrder() {
                    InOrder inOrder = inOrder(calculadoraGanhos, armazenamentoPost);
                    inOrder.verify(calculadoraGanhos,
                                    times(1))
                            .calcular(postCreated);
                    inOrder.verify(armazenamentoPost,
                                    times(1))
                            .salvar(postCreated);

                }
            }
        }

        @Nested
        @DisplayName("GIVEN a null post")
        class givenNullPost {
            @Test
            @DisplayName("IF recieved null THEN throw exception and don't save")
            void nullException() {
                assertThrows(NullPointerException.class, () -> cadastroPost.criar(null));
                verify(armazenamentoPost, never()).salvar(any(Post.class));
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

