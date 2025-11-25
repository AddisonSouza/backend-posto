<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestão - Posto</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">Sistema de Gestão - Posto</a>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="text-center mb-4">
            <h1 class="display-4">Bem-vindo ao Sistema de Gestão</h1>
            <p class="lead">Gerencie categorias, produtos, clientes e vendas</p>
        </div>

        <div class="row">
            <div class="col-3">
                <div class="card">
                    <h5 class="card-title">Categorias</h5>
                    <p class="card-text">Gerencie as categorias de produtos</p>
                    <a href="categorias.jsp" class="btn btn-primary">Acessar</a>
                </div>
            </div>

            <div class="col-3">
                <div class="card">
                    <h5 class="card-title">Produtos</h5>
                    <p class="card-text">Controle seu estoque de produtos</p>
                    <a href="produtos.jsp" class="btn btn-primary">Acessar</a>
                </div>
            </div>

            <div class="col-3">
                <div class="card">
                    <h5 class="card-title">Clientes</h5>
                    <p class="card-text">Cadastre e gerencie clientes</p>
                    <a href="clientes.jsp" class="btn btn-primary">Acessar</a>
                </div>
            </div>

            <div class="col-3">
                <div class="card">
                    <h5 class="card-title">Vendas</h5>
                    <p class="card-text">Registre e consulte vendas</p>
                    <a href="vendas.jsp" class="btn btn-primary">Acessar</a>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <div class="container">
            <p>&copy; 2025 Sistema de Gestão - Posto. Todos os direitos reservados.</p>
        </div>
    </footer>
</body>
</html>

