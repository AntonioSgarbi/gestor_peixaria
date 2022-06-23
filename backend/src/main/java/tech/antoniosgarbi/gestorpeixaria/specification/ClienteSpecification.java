package tech.antoniosgarbi.gestorpeixaria.specification;

import org.springframework.data.jpa.domain.Specification;
import tech.antoniosgarbi.gestorpeixaria.dto.specification.SpecBodyCliente;
import tech.antoniosgarbi.gestorpeixaria.model.Cliente;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class ClienteSpecification implements Specification<Cliente> {

    @Transient
    private final SpecBodyCliente specBodyCliente;

    public ClienteSpecification(SpecBodyCliente specBody) {
        this.specBodyCliente = specBody;
    }
    @Override
    public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        if (specBodyCliente.getNome() != null) {
            predicates.add(
                    builder.like(
                            builder.upper(root.get("nome")),
                            String.format("%%%s%%", specBodyCliente.getNome().toUpperCase())
                    ));
        }
        if (specBodyCliente.getDocumento() != null) {
            predicates.add(
                    builder.like(
                            builder.upper(root.get("documento")),
                            String.format("%%%s%%", specBodyCliente.getDocumento().toUpperCase())
                    ));
        }
        if (specBodyCliente.getPessoaTipo() != null) {
            predicates.add(
                    builder.equal(
                            root.get("pessoaTipo"),
                            specBodyCliente.getPessoaTipo()
                    )
            );
        }
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
