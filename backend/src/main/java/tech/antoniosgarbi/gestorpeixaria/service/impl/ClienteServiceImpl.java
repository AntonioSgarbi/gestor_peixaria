package tech.antoniosgarbi.gestorpeixaria.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.gestorpeixaria.dto.model.ClienteDTO;
import tech.antoniosgarbi.gestorpeixaria.dto.specification.SpecBodyCliente;
import tech.antoniosgarbi.gestorpeixaria.exception.PessoaException;
import tech.antoniosgarbi.gestorpeixaria.model.Cliente;
import tech.antoniosgarbi.gestorpeixaria.repository.ClienteRepository;
import tech.antoniosgarbi.gestorpeixaria.service.Util;
import tech.antoniosgarbi.gestorpeixaria.service.contract.ClienteService;
import tech.antoniosgarbi.gestorpeixaria.specification.ClienteSpecification;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public long cadastrar(ClienteDTO clienteDTO) {
        Optional<Cliente> optional = this.clienteRepository.findClienteByDocumento(clienteDTO.getDocumento());
        if (optional.isPresent()) throw new PessoaException("O documento informado já possui cadastro no sistema!");
        Cliente modelo = new Cliente(clienteDTO);
        modelo = this.clienteRepository.save(modelo);
        return modelo.getId();
    }

    @Override
    public ClienteDTO atualizarCadastro(ClienteDTO clienteDTO) {
        Cliente modelo = this.encontrarModelo(clienteDTO.getId());
        Util.myCopyProperties(clienteDTO, modelo);
        this.clienteRepository.save(modelo);
        return clienteDTO;
    }

    @Override
    public ClienteDTO encontrarCadastro(long id) {
        return new ClienteDTO(this.encontrarModelo(id));
    }

    @Override
    public Page<ClienteDTO> encontrarTodos(Pageable pageable) {
        return this.clienteRepository.findAll(pageable).map(ClienteDTO::new);
    }

    @Override
    public Page<ClienteDTO> encontrarTodos(SpecBodyCliente specBody, Pageable pageable) {
        Specification<Cliente> specification = new ClienteSpecification(specBody);
        return this.clienteRepository.findAll(specification, pageable).map(ClienteDTO::new);
    }

    @Override
    public void apagarCadastro(long id) {
        ClienteDTO dto = this.encontrarCadastro(id);
        Cliente modelo = new Cliente(dto);
        modelo.setExcluido(true);
        this.clienteRepository.save(modelo);
    }

    public Cliente encontrarModelo(long id) {
        Optional<Cliente> optional = this.clienteRepository.findById(id);
        if (optional.isEmpty()) throw new PessoaException("Cadastro não encontrado");
        return optional.get();
    }

}