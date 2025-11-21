package br.com.jaas.backendposto.util;

import br.com.jaas.backendposto.model.ApiResponse;

public class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operação realizada com sucesso", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, "Recurso criado com sucesso", data);
    }

    public static <T> ApiResponse<T> updated(T data) {
        return new ApiResponse<>(true, "Recurso atualizado com sucesso", data);
    }

    public static <T> ApiResponse<T> deleted() {
        return new ApiResponse<>(true, "Recurso deletado com sucesso", null);
    }

    public static <T> ApiResponse<T> notFound(String resource) {
        return new ApiResponse<>(false, resource + " não encontrado", null);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, "Requisição inválida: " + message, null);
    }
}
