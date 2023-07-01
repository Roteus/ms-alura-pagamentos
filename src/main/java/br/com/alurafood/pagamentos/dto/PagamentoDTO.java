package br.com.alurafood.pagamentos.dto;

import br.com.alurafood.pagamentos.model.ItemDoPedido;
import br.com.alurafood.pagamentos.model.Status;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoDTO {

  private Long id;
  private BigDecimal valor;
  private String nome;
  private String numero;
  private String expiracao;
  private String codigo;
  private Status status;
  private Long formaDePagamentoId;
  private Long pedidoId;
  private List<ItemDoPedido> itens;
}
