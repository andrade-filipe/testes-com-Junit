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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Nested
@DisplayName("CadastroPost Tests") //CLASSE QUE VOU TESTAR
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
    @Spy
    Editor editor = new Editor(1L, "Filipe", "filipe@gmail.com",
            BigDecimal.TEN, true);


    @Nested
    @DisplayName("Method: criar -> @param Post, @return Post") // MÉTODO QUE VOU TESTAR
    class criarMethodTests {
        @Nested
        @DisplayName("GIVEN a valid post") //CONDIÇÃO DO TESTE
        class givenNormalPost {
            @Spy
            Post validPost = new Post();

            //VALID POST
            @BeforeEach
            void validPost() {
                validPost.setAutor(editor);
                validPost.setTitulo("Meu Post");
                validPost.setConteudo("Conteudo do Post");
                validPost.setPago(true);
                validPost.setPublicado(true);
            }

            @Nested
            @DisplayName("WHEN creating a post") //CASO ESPECIFICO
            class whenCreating {
                private Post postCreated;

                @BeforeEach
                void mockInvocations() {
                    when(armazenamentoPost.salvar(any(Post.class)))
                            .thenAnswer(invocation -> {
                                Post post = invocation.getArgument(0, Post.class);
                                post.setId(1L);
                                return post;
                            });
                    when(calculadoraGanhos.calcular(validPost))
                            .thenReturn(new Ganhos(BigDecimal.TEN,
                                    3,
                                    BigDecimal.valueOf(30)));

                    postCreated = cadastroPost.criar(validPost);
                }

                @Nested
                @DisplayName("TO ASSERT information is being registered")
                class assertInformation {
                    @Test
                    @DisplayName("return a creation Id")
                        //RESULTADO
                    void returnCreationId() {
                        assertEquals(1L, postCreated.getId());
                    }

                    @Test
                    @DisplayName("return a post with Ganhos")
                    void returnWithGanhos() {
                        verify(postCreated,
                                times(1))
                                .setGanhos(any(Ganhos.class));
                    }

                    @Test
                    @DisplayName("return a post with Slug")
                    void returnWithSlug() {
                        verify(postCreated,
                                times(1))
                                .setSlug(anyString());
                        assertNotNull(postCreated.getSlug());
                    }
                }

                @Nested
                @DisplayName("TO ASSERT all actions are being performed")
                class assertMethodsIntegrity {
                    @Test
                    @DisplayName("verify if the .salvar() was called")
                    void verifySave() {
                        verify(armazenamentoPost,
                                times(1))
                                .salvar(any(Post.class));
                    }

                    @Test
                    @DisplayName("call .calculate before .salvar")
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
        @Nested
        @DisplayName("GIVEN a valid post")
        class givenNormalPost {
            @Spy
            Post validPost = new Post();

            @BeforeEach
            void validPost() {
                validPost.setId(1L);
                validPost.setTitulo("Meu Post");
                validPost.setConteudo("Conteudo do Post");
                validPost.setAutor(editor);
                validPost.setSlug("meu-post-123");
                validPost.setGanhos(new Ganhos(BigDecimal.TEN, 3, BigDecimal.valueOf(30)));
                validPost.setPago(true);
                validPost.setPublicado(true);
            }

            @Nested
            @DisplayName("WHEN editing an existing post")
            class whenEditing {
                @BeforeEach
                void init() {
                    when(armazenamentoPost.salvar(any(Post.class)))
                            .then(invocation -> invocation.getArgument(0, Post.class));
                    when(armazenamentoPost.encontrarPorId(1L))
                            .thenReturn(Optional.ofNullable(validPost));
                }

                @Nested
                @DisplayName("TO ASSERT information is being registered")
                class assertInformation {

                    @BeforeEach
                    void editValidPost() {
                        validPost.setConteudo("Conteúdo editado");
                        cadastroPost.editar(validPost);
                    }

                    @Test
                    @DisplayName("call .salvar() method")
                    void callSalvar() {
                        verify(armazenamentoPost,
                                times(1))
                                .salvar(any(Post.class));
                    }

                    @Test
                    @DisplayName("return same id")
                    void sameId() {
                        assertEquals(1L, validPost.getId());
                    }

                    @Test
                    @DisplayName("alter the content")
                    void alterContent() {
                        assertEquals("Conteúdo editado", validPost.getConteudo());
                    }
                }
            }
        }

        @Nested
        @DisplayName("GIVEN a null post")
        class givenNullPost {

        }
    }

    @Nested
    @DisplayName("Method: remover -> @param Long, @return void")
    class removerMethodTests {
    }
}

