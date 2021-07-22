package br.com.zup.edu.rodrigo.registra

import br.com.zup.edu.rodrigo.RegistraChavePixRequest
import br.com.caelum.stella.validation.CPFValidator
import br.com.zup.edu.rodrigo.TipoChave
import br.com.zup.edu.rodrigo.TipoConta
import br.com.zup.edu.rodrigo.shared.validacao.ValidPixKey
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NovaChavePixRequest(@field:NotNull val tipoDeConta: TipoDeContaRequest?,
                          @field:Size(max = 77) val chave: String?,
                          @field:NotNull val tipoDeChave: TipoDeChaveRequest?) {

    fun toModelGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoDeConta(tipoDeConta?.atributoGrpc ?: TipoConta.UNKNOW_TIPO_CONTA)
            .setTipoDeChave(tipoDeChave?.atributoGrpc ?: TipoChave.UNKNOW_TIPO_CHAVE)
            .setChave(chave ?: "")
            .build()
    }
}

enum class TipoDeChaveRequest(val atributoGrpc: TipoChave) {

    CPF(TipoChave.CPF) {

        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator(false)
                .invalidMessagesFor(chave)
                .isEmpty()
        }

    },

    CELULAR(TipoChave.CELULAR) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoChave.EMAIL) {

        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }

        }
    },

    ALEATORIA(TipoChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoDeContaRequest(val atributoGrpc: TipoConta) {

    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}
