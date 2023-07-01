package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.http.PedidoClient;
import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.model.Status;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

  @Autowired
  PedidoClient pedido;
  @Autowired
  private PagamentoRepository pagamentoRepository;
  @Autowired
  private ModelMapper modelMapper;

  public Page<PagamentoDTO> obterTodos(Pageable paginacao) {
    return pagamentoRepository.findAll(paginacao)
        .map(p -> modelMapper.map(p, PagamentoDTO.class));
  }

  public PagamentoDTO obterPorId(Long id) {
    Pagamento pagamento = pagamentoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException());
    PagamentoDTO pagamentoDTO = modelMapper.map(pagamento, PagamentoDTO.class);
    pagamentoDTO.setItens(pedido.obterItensDoPedido(pagamento.getPedidoId()).getItens());
    return modelMapper.map(pagamento, PagamentoDTO.class);
  }

  public PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO) {
    Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
    pagamento.setStatus(Status.CRIADO);
    pagamentoRepository.save(pagamento);
    return modelMapper.map(pagamento, PagamentoDTO.class);
  }

  public PagamentoDTO atualizarPagamento(Long id, PagamentoDTO pagamentoDTO) {
    Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
    pagamento.setId(id);
    pagamento = pagamentoRepository.save(pagamento);
    return modelMapper.map(pagamento, PagamentoDTO.class);
  }

  public void excluirPagamento(Long id) {
    pagamentoRepository.deleteById(id);
  }

  public void confirmarPagamento(Long id) {
    Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

    if (!pagamento.isPresent()) {
      throw new EntityNotFoundException();
    }
    pagamento.get().setStatus(Status.CONFIRMADO);
    pagamentoRepository.save(pagamento.get());
    pedido.atualizaPagamento(pagamento.get().getPedidoId());
  }

  public void alteraStatus(Long id) {
    Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

    if (!pagamento.isPresent()) {
      throw new EntityNotFoundException();
    }

    pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
    pagamentoRepository.save(pagamento.get());
  }
}
