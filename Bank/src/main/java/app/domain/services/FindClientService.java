package app.domain.services;

import app.domain.Exceptions.ResourceNotFoundException;
import app.domain.models.Client;
import app.domain.ports.in.FindClientUseCase;
import app.domain.ports.out.ClientRepositoryPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Application service — read-only queries for the {@link Client} aggregate.
 *
 * <p>Queries are not audited.
 */
@Service
public class FindClientService implements FindClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public FindClientService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public Client findById(UUID clientId) {
        return clientRepositoryPort.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with ID: " + clientId));
    }

    @Override
    public Client findByIdentificationId(String identificationId) {
        return clientRepositoryPort.findByIdentificationId(identificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with identification ID: " + identificationId));
    }
}