package br.com.jaas.backendposto.controlller;

import br.com.jaas.backendposto.model.ApiResponse;
import br.com.jaas.backendposto.model.Venda;
import br.com.jaas.backendposto.service.VendaService;
import br.com.jaas.backendposto.util.ApiResponseFactory;
import br.com.jaas.backendposto.util.ServletHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/venda/")
public class VendaController extends HttpServlet {

    private final VendaService vendaService = new VendaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            List<Venda> vendas = vendaService.findAll();
            ApiResponse<List<Venda>> apiResponse = ApiResponseFactory.success(vendas);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } else {
            Venda venda = vendaService.findById(id);
            if (venda == null) {
                ApiResponse<Venda> apiResponse = ApiResponseFactory.notFound("Venda");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
            } else {
                ApiResponse<Venda> apiResponse = ApiResponseFactory.success(venda);
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Venda venda = ServletHelper.parseRequestBody(request, Venda.class);

            if (venda.getCliente() == null || venda.getCliente().getIdCliente() == null) {
                ApiResponse<Venda> apiResponse = ApiResponseFactory.badRequest("Cliente é obrigatório");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            if (venda.getValorTotal() <= 0) {
                ApiResponse<Venda> apiResponse = ApiResponseFactory.badRequest("Valor total deve ser maior que zero");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            if (venda.getDataVenda() == null) {
                ApiResponse<Venda> apiResponse = ApiResponseFactory.badRequest("Data da venda é obrigatória");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            Venda saved = vendaService.save(venda);
            ApiResponse<Venda> apiResponse = ApiResponseFactory.created(saved);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_CREATED, apiResponse);
        } catch (Exception e) {
            ApiResponse<Venda> apiResponse = ApiResponseFactory.error("Erro ao criar venda: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            ApiResponse<Venda> apiResponse = ApiResponseFactory.badRequest("ID é obrigatório");
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
            return;
        }

        try {
            Venda venda = ServletHelper.parseRequestBody(request, Venda.class);
            venda.setIdVenda(id);

            Venda existing = vendaService.findById(id);
            if (existing == null) {
                ApiResponse<Venda> apiResponse = ApiResponseFactory.notFound("Venda");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            vendaService.update(venda);
            Venda updated = vendaService.findById(id);
            ApiResponse<Venda> apiResponse = ApiResponseFactory.updated(updated);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Venda> apiResponse = ApiResponseFactory.error("Erro ao atualizar venda: " + e.getMessage());
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
            Venda existing = vendaService.findById(id);
            if (existing == null) {
                ApiResponse<Void> apiResponse = ApiResponseFactory.notFound("Venda");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            vendaService.deleteById(id);
            ApiResponse<Void> apiResponse = ApiResponseFactory.deleted();
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = ApiResponseFactory.error("Erro ao deletar venda: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }
}
