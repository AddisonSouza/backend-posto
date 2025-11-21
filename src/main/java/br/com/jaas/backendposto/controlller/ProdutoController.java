package br.com.jaas.backendposto.controlller;

import br.com.jaas.backendposto.model.ApiResponse;
import br.com.jaas.backendposto.model.Produto;
import br.com.jaas.backendposto.service.ProdutoService;
import br.com.jaas.backendposto.util.ApiResponseFactory;
import br.com.jaas.backendposto.util.ServletHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/produtos/*")
public class ProdutoController extends HttpServlet {

    private final ProdutoService service = new ProdutoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            List<Produto> produtos = service.findAll();
            ApiResponse<List<Produto>> apiResponse = ApiResponseFactory.success(produtos);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } else {
            Produto produto = service.findById(id);
            if (produto == null) {
                ApiResponse<Produto> apiResponse = ApiResponseFactory.notFound("Produto");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
            } else {
                ApiResponse<Produto> apiResponse = ApiResponseFactory.success(produto);
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Produto produto = ServletHelper.parseRequestBody(request, Produto.class);

            if (produto.getDescricao() == null || produto.getDescricao().trim().isEmpty()) {
                ApiResponse<Produto> apiResponse = ApiResponseFactory.badRequest("Descrição é obrigatória");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            if (produto.getCategoria() == null || produto.getCategoria().getIdCategoria() == null) {
                ApiResponse<Produto> apiResponse = ApiResponseFactory.badRequest("Categoria é obrigatória");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            if (produto.getPreco() <= 0) {
                ApiResponse<Produto> apiResponse = ApiResponseFactory.badRequest("Preço deve ser maior que zero");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            Produto saved = service.save(produto);
            ApiResponse<Produto> apiResponse = ApiResponseFactory.created(saved);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_CREATED, apiResponse);
        } catch (Exception e) {
            ApiResponse<Produto> apiResponse = ApiResponseFactory.error("Erro ao criar produto: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            ApiResponse<Produto> apiResponse = ApiResponseFactory.badRequest("ID é obrigatório");
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
            return;
        }

        try {
            Produto produto = ServletHelper.parseRequestBody(request, Produto.class);
            produto.setIdProduto(id);

            Produto existing = service.findById(id);
            if (existing == null) {
                ApiResponse<Produto> apiResponse = ApiResponseFactory.notFound("Produto");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            service.update(produto);
            Produto updated = service.findById(id);
            ApiResponse<Produto> apiResponse = ApiResponseFactory.updated(updated);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Produto> apiResponse = ApiResponseFactory.error("Erro ao atualizar produto: " + e.getMessage());
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
            Produto existing = service.findById(id);
            if (existing == null) {
                ApiResponse<Void> apiResponse = ApiResponseFactory.notFound("Produto");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            service.deleteById(id);
            ApiResponse<Void> apiResponse = ApiResponseFactory.deleted();
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = ApiResponseFactory.error("Erro ao deletar produto: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }
}
