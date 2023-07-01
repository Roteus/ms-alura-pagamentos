package br.com.alurafood.pagamentos.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pedido {

  private List<ItemDoPedido> itens;
}
