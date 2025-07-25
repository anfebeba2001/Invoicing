package proyectoSeminario.Invoicing.ServiceImpl;

import proyectoSeminario.Invoicing.dto.SaleDTORequest;
import proyectoSeminario.Invoicing.dto.SaleDTOResponse;
import proyectoSeminario.Invoicing.mapper.SaleMapper;
import proyectoSeminario.Invoicing.repository.SaleRepository;
import proyectoSeminario.Invoicing.service.SaleServiceCreating;
import proyectoSeminario.Invoicing.service.SaleServiceReading;

import java.util.List;
import java.util.Optional;

public class SaleServiceImpl implements SaleServiceReading, SaleServiceCreating {

    private final SaleRepository saleRepository;

    public SaleServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<SaleDTOResponse> findAll() {
        return saleRepository
                .findAll()
                .stream()
                .map(SaleMapper::entityToResponse)
                .toList();
    }

    @Override
    public Optional<SaleDTOResponse> findById(Long id) {
        return saleRepository
                .findById(id)
                .map(SaleMapper::entityToResponse);
    }

    @Override
    public SaleDTOResponse create(SaleDTORequest request) {
        return null;
    }
}
