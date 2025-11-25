<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Produtos</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">Sistema de Gestão</a>
            <ul class="nav-menu">
                <li><a class="nav-link" href="categorias.jsp">Categorias</a></li>
                <li><a class="nav-link active" href="produtos.jsp">Produtos</a></li>
                <li><a class="nav-link" href="clientes.jsp">Clientes</a></li>
                <li><a class="nav-link" href="vendas.jsp">Vendas</a></li>
            </ul>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="page-header">
            <h2>Gerenciar Produtos</h2>
            <button class="btn btn-primary" onclick="novoProduto()">+ Novo Produto</button>
        </div>

        <div id="alertContainer"></div>

        <div class="table-card">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Descrição</th>
                        <th>Categoria</th>
                        <th>Quantidade</th>
                        <th>Preço</th>
                        <th class="text-end">Ações</th>
                    </tr>
                </thead>
                <tbody id="produtosTable">
                    <tr>
                        <td colspan="6" class="text-center">
                            <div class="spinner"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal" id="produtoModal">
        <div class="modal-dialog">
            <div class="modal-header">
                <h5 class="modal-title" id="modalTitle">Novo Produto</h5>
                <button class="modal-close" onclick="fecharModal()">×</button>
            </div>
            <div class="modal-body">
                <form id="produtoForm" onsubmit="event.preventDefault(); salvarProduto();">
                    <input type="hidden" id="idProduto">
                    <div class="form-group">
                        <label for="descricao" class="form-label">Descrição *</label>
                        <input type="text" class="form-control" id="descricao" required>
                    </div>
                    <div class="row-2-cols">
                        <div class="form-group">
                            <label for="idCategoria" class="form-label">Categoria *</label>
                            <select class="form-select" id="idCategoria" required>
                                <option value="">Selecione...</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="quantidade" class="form-label">Quantidade *</label>
                            <input type="number" class="form-control" id="quantidade" required min="0">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="preco" class="form-label">Preço (R$) *</label>
                        <input type="number" class="form-control" id="preco" required min="0" step="0.01">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="salvarProduto()">Salvar</button>
            </div>
        </div>
    </div>

    <script>
        const API_URL = '/api/produto';
        const CATEGORIA_API_URL = '/api/categoria';
        const modal = document.getElementById('produtoModal');
        let categorias = [];

        document.addEventListener('DOMContentLoaded', function() {
            carregarCategorias();
            carregarProdutos();
        });

        function showAlert(message, type = 'success') {
            const alertHtml = `
                <div class="alert alert-${type}">
                    ${message}
                    <button class="alert-close" onclick="this.parentElement.remove()">×</button>
                </div>
            `;
            document.getElementById('alertContainer').innerHTML = alertHtml;
            setTimeout(() => {
                const alert = document.querySelector('.alert');
                if (alert) alert.remove();
            }, 5000);
        }

        async function carregarCategorias() {
            try {
                const response = await fetch(CATEGORIA_API_URL);
                const result = await response.json();

                if (result.success && result.data) {
                    categorias = result.data;
                    const select = document.getElementById('idCategoria');
                    select.innerHTML = '<option value="">Selecione...</option>';

                    categorias.forEach(cat => {
                        const option = document.createElement('option');
                        option.value = cat.idCategoria;
                        option.textContent = cat.nomeCategoria;
                        select.appendChild(option);
                    });
                }
            } catch (error) {
                showAlert('Erro ao carregar categorias', 'danger');
            }
        }

        async function carregarProdutos() {
            try {
                const response = await fetch(API_URL);
                const result = await response.json();

                console.log('Resposta da API:', result);

                const tbody = document.getElementById('produtosTable');
                tbody.innerHTML = '';
                if (result.success && result.data && result.data.length > 0) {
                    result.data.forEach(produto => {
                        console.log('Produto:', produto);

                        const tr = document.createElement('tr');

                        const tdId = document.createElement('td');
                        tdId.textContent = produto.idProduto;
                        tdId.style.color = '#000';
                        tdId.style.padding = '1rem';
                        tr.appendChild(tdId);

                        const tdDesc = document.createElement('td');
                        tdDesc.textContent = produto.descricao;
                        tdDesc.style.color = '#000';
                        tdDesc.style.padding = '1rem';
                        tr.appendChild(tdDesc);

                        const tdCat = document.createElement('td');
                        tdCat.textContent = produto.categoria ? produto.categoria.nomeCategoria : '-';
                        tdCat.style.color = '#000';
                        tdCat.style.padding = '1rem';
                        tr.appendChild(tdCat);

                        const tdQtd = document.createElement('td');
                        tdQtd.textContent = produto.quantidade;
                        tdQtd.style.color = '#000';
                        tdQtd.style.padding = '1rem';
                        tr.appendChild(tdQtd);

                        const tdPreco = document.createElement('td');
                        tdPreco.textContent = 'R$ ' + produto.preco.toFixed(2);
                        tdPreco.style.color = '#000';
                        tdPreco.style.padding = '1rem';
                        tr.appendChild(tdPreco);

                        const tdAcoes = document.createElement('td');
                        tdAcoes.className = 'text-end';
                        tdAcoes.style.padding = '1rem';

                        const divActions = document.createElement('div');
                        divActions.className = 'action-buttons';

                        const btnEdit = document.createElement('button');
                        btnEdit.className = 'btn btn-sm btn-primary';
                        btnEdit.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/></svg>';
                        btnEdit.onclick = () => editarProduto(produto.idProduto);

                        const btnDelete = document.createElement('button');
                        btnDelete.className = 'btn btn-sm btn-danger';
                        btnDelete.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/><path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/></svg>';
                        btnDelete.onclick = () => deletarProduto(produto.idProduto);

                        divActions.appendChild(btnEdit);
                        divActions.appendChild(btnDelete);
                        tdAcoes.appendChild(divActions);
                        tr.appendChild(tdAcoes);

                        tbody.appendChild(tr);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="6" class="text-center">Nenhum produto encontrado</td></tr>';
                }
            } catch (error) {
                console.error('Erro ao carregar produtos:', error);
                const tbody = document.getElementById('produtosTable');
                tbody.innerHTML = '<tr><td colspan="6" class="text-center">Erro ao carregar produtos: ' + error.message + '</td></tr>';
                showAlert('Erro ao carregar produtos: ' + error.message, 'danger');
            }
        }

        function novoProduto() {
            document.getElementById('modalTitle').textContent = 'Novo Produto';
            document.getElementById('produtoForm').reset();
            document.getElementById('idProduto').value = '';
            abrirModal();
        }

        async function editarProduto(id) {
            try {
                const url = API_URL + '/' + id;
                const response = await fetch(url);
                const result = await response.json();

                if (result.success && result.data) {
                    document.getElementById('modalTitle').textContent = 'Editar Produto';
                    document.getElementById('idProduto').value = result.data.idProduto;
                    document.getElementById('descricao').value = result.data.descricao || '';
                    document.getElementById('quantidade').value = result.data.quantidade || 0;
                    document.getElementById('preco').value = result.data.preco || 0;
                    document.getElementById('idCategoria').value = result.data.categoria ? result.data.categoria.idCategoria : '';
                    abrirModal();
                } else {
                    showAlert('Produto não encontrado', 'danger');
                }
            } catch (error) {
                console.error('Erro ao buscar produto:', error);
                showAlert('Erro ao buscar produto', 'danger');
            }
        }

        async function salvarProduto() {
            const id = document.getElementById('idProduto').value;
            const descricao = document.getElementById('descricao').value.trim();
            const quantidade = parseInt(document.getElementById('quantidade').value);
            const preco = parseFloat(document.getElementById('preco').value);
            const categoriaId = document.getElementById('idCategoria').value;

            if (!descricao || !categoriaId || quantidade < 0 || preco < 0) {
                showAlert('Preencha todos os campos obrigatórios corretamente', 'warning');
                return;
            }

            const produto = {
                descricao,
                quantidade,
                preco,
                categoria: { idCategoria: parseInt(categoriaId) }
            };

            const url = id ? API_URL + '/' + id : API_URL;
            const method = id ? 'PUT' : 'POST';

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(produto)
                });

                const result = await response.json();

                if (result.success) {
                    showAlert(id ? 'Produto atualizado com sucesso' : 'Produto criado com sucesso', 'success');
                    fecharModal();
                    carregarProdutos();
                } else {
                    showAlert(result.message || 'Erro ao salvar produto', 'danger');
                }
            } catch (error) {
                console.error('Erro ao salvar produto:', error);
                showAlert('Erro ao salvar produto', 'danger');
            }
        }

        async function deletarProduto(id) {
            if (!confirm('Deseja realmente excluir este produto?')) {
                return;
            }

            try {
                const url = API_URL + '/' + id;
                const response = await fetch(url, {
                    method: 'DELETE'
                });

                const result = await response.json();

                if (result.success) {
                    showAlert('Produto excluído com sucesso', 'success');
                    carregarProdutos();
                } else {
                    showAlert(result.message || 'Erro ao excluir produto', 'danger');
                }
            } catch (error) {
                console.error('Erro ao excluir produto:', error);
                showAlert('Erro ao excluir produto', 'danger');
            }
        }

        function abrirModal() {
            modal.classList.add('show');
        }

        function fecharModal() {
            modal.classList.remove('show');
        }

        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                fecharModal();
            }
        });
    </script>
</body>
</html>

