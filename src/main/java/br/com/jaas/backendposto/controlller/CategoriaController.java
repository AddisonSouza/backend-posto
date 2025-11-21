package br.com.jaas.backendposto.controlller;

import br.com.jaas.backendposto.model.ApiResponse;
import br.com.jaas.backendposto.model.Categoria;
import br.com.jaas.backendposto.service.CategoriaService;
import br.com.jaas.backendposto.util.ApiResponseFactory;
import br.com.jaas.backendposto.util.ServletHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/categoria/*")
public class CategoriaController extends HttpServlet {

    private final CategoriaService categoriaService = new CategoriaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            List<Categoria> categorias = categoriaService.findAll();
            ApiResponse<List<Categoria>> apiResponse = ApiResponseFactory.success(categorias);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } else {
            Categoria categoria = categoriaService.findById(id);
            if (categoria == null) {
                ApiResponse<Categoria> apiResponse = ApiResponseFactory.notFound("Categoria");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
            } else {
                ApiResponse<Categoria> apiResponse = ApiResponseFactory.success(categoria);
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Categoria categoria = ServletHelper.parseRequestBody(request, Categoria.class);

            if (categoria.getNomeCategoria() == null || categoria.getNomeCategoria().trim().isEmpty()) {
                ApiResponse<Categoria> apiResponse = ApiResponseFactory.badRequest("Nome da categoria é obrigatório");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
                return;
            }

            Categoria saved = categoriaService.save(categoria);
            ApiResponse<Categoria> apiResponse = ApiResponseFactory.created(saved);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_CREATED, apiResponse);
        } catch (Exception e) {
            ApiResponse<Categoria> apiResponse = ApiResponseFactory.error("Erro ao criar categoria: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = ServletHelper.getIdFromPath(request);

        if (id == null) {
            ApiResponse<Categoria> apiResponse = ApiResponseFactory.badRequest("ID é obrigatório");
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, apiResponse);
            return;
        }

        try {
            Categoria categoria = ServletHelper.parseRequestBody(request, Categoria.class);
            categoria.setIdCategoria(id);

            Categoria existing = categoriaService.findById(id);
            if (existing == null) {
                ApiResponse<Categoria> apiResponse = ApiResponseFactory.notFound("Categoria");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            categoriaService.update(categoria);
            Categoria updated = categoriaService.findById(id);
            ApiResponse<Categoria> apiResponse = ApiResponseFactory.updated(updated);
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Categoria> apiResponse = ApiResponseFactory.error("Erro ao atualizar categoria: " + e.getMessage());
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
            Categoria existing = categoriaService.findById(id);
            if (existing == null) {
                ApiResponse<Void> apiResponse = ApiResponseFactory.notFound("Categoria");
                ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResponse);
                return;
            }

            categoriaService.deleteById(id);
            ApiResponse<Void> apiResponse = ApiResponseFactory.deleted();
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_OK, apiResponse);
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = ApiResponseFactory.error("Erro ao deletar categoria: " + e.getMessage());
            ServletHelper.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, apiResponse);
        }
    }
}
