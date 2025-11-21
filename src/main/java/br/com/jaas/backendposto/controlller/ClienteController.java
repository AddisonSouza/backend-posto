package br.com.jaas.backendposto.controlller;

import br.com.jaas.backendposto.model.ApiResponse;
import br.com.jaas.backendposto.model.Cliente;
import br.com.jaas.backendposto.service.ClienteService;
import br.com.jaas.backendposto.util.ApiResponseFactory;
import br.com.jaas.backendposto.util.ServletHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/cliente/*")
public class ClienteController extends HttpServlet {

    private final ClienteService clienteService = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            List<Cliente> clientes = clienteService.findAll();
            ApiResponse<List<Cliente>> apiResponse = ApiResponseFactory.success(clientes);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } else {
            Cliente cliente = clienteService.findById(id);
            if (cliente == null) {
                ApiResponse<Cliente> apiResponse = ApiResponseFactory.notFound("Cliente");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
            } else {
                ApiResponse<Cliente> apiResponse = ApiResponseFactory.success(cliente);
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Cliente cliente = ServletHelper.parseRequestBody(request, Cliente.class);

            if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
                ApiResponse<Cliente> apiResponse = ApiResponseFactory.badRequest("Nome é obrigatório");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
                ApiResponse<Cliente> apiResponse = ApiResponseFactory.badRequest("CPF é obrigatório");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            Cliente saved = clienteService.save(cliente);
            ApiResponse<Cliente> apiResponse = ApiResponseFactory.created(saved);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_CREATED, apiResponse);
        } catch (Exception e) {
            ApiResponse<Cliente> apiResponse = ApiResponseFactory.error("Erro ao criar cliente: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            ApiResponse<Cliente> apiResponse = ApiResponseFactory.badRequest("ID é obrigatório");
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
            return;
        }

        try {
            Cliente cliente = ServletHelper.parseRequestBody(request, Cliente.class);
            cliente.setIdCliente(id);

            Cliente existing = clienteService.findById(id);
            if (existing == null) {
                ApiResponse<Cliente> apiResponse = ApiResponseFactory.notFound("Cliente");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            clienteService.update(cliente);
            Cliente updated = clienteService.findById(id);
            ApiResponse<Cliente> apiResponse = ApiResponseFactory.updated(updated);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Cliente> apiResponse = ApiResponseFactory.error("Erro ao atualizar cliente: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            ApiResponse<Void> apiResponse = ApiResponseFactory.badRequest("ID é obrigatório");
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
            return;
        }

        try {
            Cliente existing = clienteService.findById(id);
            if (existing == null) {
                ApiResponse<Void> apiResponse = ApiResponseFactory.notFound("Cliente");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            clienteService.deleteById(id);
            ApiResponse<Void> apiResponse = ApiResponseFactory.deleted();
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = ApiResponseFactory.error("Erro ao deletar cliente: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }
}
