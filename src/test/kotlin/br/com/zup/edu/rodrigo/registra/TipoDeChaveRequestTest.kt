package br.com.zup.edu.rodrigo.registra

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class TipoChaveRequestTest {

    @Nested
    inner class ChaveAleatoriaTest {
        @Test
        fun `deve ser valido quando chave aleatoria for nula ou vazia`() {
            with(TipoDeChaveRequest.ALEATORIA) {
                assertTrue(valida(null))
                assertTrue(valida(""))
            }
        }

        @Test
        fun `nao deve ser valido quando chave aleatoria possuir um valor`() {
            with(TipoDeChaveRequest.ALEATORIA) {
                assertFalse(valida(UUID.randomUUID().toString()))
            }
        }
    }

    @Nested
    inner class CpfTest {
        @Test
        fun `deve ser valido quando cpf tiver numero valido`() {
            with(TipoDeChaveRequest.CPF) {
                assertTrue(valida("42353525806"))
            }
        }

        @Test
        fun `nao deve ser valido quando cpf tiver numero invalido`() {
            with(TipoDeChaveRequest.CPF) {
                assertFalse(valida("4235352580l"))
                assertFalse(valida("4235352580b"))
            }
        }

        @Test
        fun `nao deve ser valido quando cpf tiver numero nulo ou vazio`() {
            with(TipoDeChaveRequest.CPF) {
                assertFalse(valida(null))
                assertFalse(valida(""))
            }
        }
    }

    @Nested
    inner class CelularTest {

        @Test
        fun `deve ser valido quando celular for numero valido`() {
            with(TipoDeChaveRequest.CELULAR) {
                assertTrue(valida("+5517981664479"))
            }
        }

        @Test
        fun `nao deve ser valido quando celular for numero invalido`() {
            with(TipoDeChaveRequest.CELULAR) {
                assertFalse(valida("17984bb4479"))
                assertFalse(valida("17981bb4479"))
            }
        }

        @Test
        fun `nao deve ser valido quando celular tiver numero nulo ou vazio`() {
            with(TipoDeChaveRequest.CELULAR) {
                assertFalse(valida(null))
                assertFalse(valida(""))
            }
        }
    }

    @Nested
    inner class EmailTest {

        @Test
        fun `deve ser valido quando email for endereco valido`() {
            with(TipoDeChaveRequest.EMAIL) {
                assertTrue(valida("email@teste.com"))
            }
        }

        @Test
        fun `nao deve ser valido quando email for endereco invalido`() {
            with(TipoDeChaveRequest.EMAIL) {
                assertFalse(valida("emailteste.com"))
                assertFalse(valida("email@teste.com."))
            }
        }

        @Test
        fun `nao deve ser valido quando email for nulo ou vazio`() {
            with(TipoDeChaveRequest.EMAIL) {
                assertFalse(valida(null))
                assertFalse(valida(""))
            }
        }
    }
}