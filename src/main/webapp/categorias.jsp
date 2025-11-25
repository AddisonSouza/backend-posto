<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Categorias</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">Sistema de Gestão</a>
            <ul class="nav-menu">
                <li><a class="nav-link active" href="categorias.jsp">Categorias</a></li>
                <li><a class="nav-link" href="produtos.jsp">Produtos</a></li>
                <li><a class="nav-link" href="clientes.jsp">Clientes</a></li>
                <li><a class="nav-link" href="vendas.jsp">Vendas</a></li>
            </ul>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="page-header">
            <h2>Gerenciar Categorias</h2>
            <button class="btn btn-primary" onclick="novaCategoria()">+ Nova Categoria</button>
        </div>

        <div id="alertContainer"></div>

        <div class="table-card">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th class="text-end">Ações</th>
                    </tr>
                </thead>
                <tbody id="categoriasTable">
                    <tr>
                        <td colspan="3" class="text-center">
                            <div class="spinner"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal" id="categoriaModal">
        <div class="modal-dialog">
            <div class="modal-header">
                <h5 class="modal-title" id="modalTitle">Nova Categoria</h5>
                <button class="modal-close" onclick="fecharModal()">×</button>
            </div>
            <div class="modal-body">
                <form id="categoriaForm" onsubmit="event.preventDefault(); salvarCategoria();">
                    <input type="hidden" id="idCategoria">
                    <div class="form-group">
                        <label for="nomeCategoria" class="form-label">Nome da Categoria *</label>
                        <input type="text" class="form-control" id="nomeCategoria" required>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="salvarCategoria()">Salvar</button>
            </div>
        </div>
    </div>

    <script>
        const API_URL = '/api/categoria';
        const modal = document.getElementById('categoriaModal');

        document.addEventListener('DOMContentLoaded', () => carregarCategorias());

        function showAlert(message, type = 'success') {
            document.getElementById('alertContainer').innerHTML = `
                <div class="alert alert-${type}">
                    ${message}
                    <button class="alert-close" onclick="this.parentElement.remove()">×</button>
                </div>
            `;
            setTimeout(() => document.querySelector('.alert')?.remove(), 5000);
        }

        async function carregarCategorias() {
            try {
                const response = await fetch(API_URL);
                const result = await response.json();
                const tbody = document.getElementById('categoriasTable');
                tbody.innerHTML = '';

                if (result.success && result.data?.length > 0) {
                    result.data.forEach(cat => {
                        const tr = document.createElement('tr');

                        const tdId = document.createElement('td');
                        tdId.textContent = cat.idCategoria;
                        tr.appendChild(tdId);

                        const tdNome = document.createElement('td');
                        tdNome.textContent = cat.nomeCategoria;
                        tr.appendChild(tdNome);

                        const tdAcoes = document.createElement('td');
                        tdAcoes.className = 'text-end';

                        const divActions = document.createElement('div');
                        divActions.className = 'action-buttons';

                        const btnEdit = document.createElement('button');
                        btnEdit.className = 'btn btn-sm btn-primary';
                        btnEdit.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/></svg>';
                        btnEdit.onclick = () => editarCategoria(cat.idCategoria);

                        const btnDelete = document.createElement('button');
                        btnDelete.className = 'btn btn-sm btn-danger';
                        btnDelete.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/><path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/></svg>';
                        btnDelete.onclick = () => deletarCategoria(cat.idCategoria);

                        divActions.appendChild(btnEdit);
                        divActions.appendChild(btnDelete);
                        tdAcoes.appendChild(divActions);
                        tr.appendChild(tdAcoes);
                        tbody.appendChild(tr);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="3" class="text-center">Nenhuma categoria encontrada</td></tr>';
                }
            } catch (error) {
                document.getElementById('categoriasTable').innerHTML =
                    '<tr><td colspan="3" class="text-center">Erro ao carregar categorias</td></tr>';
                showAlert('Erro ao carregar categorias', 'danger');
            }
        }

        function novaCategoria() {
            document.getElementById('modalTitle').textContent = 'Nova Categoria';
            document.getElementById('categoriaForm').reset();
            document.getElementById('idCategoria').value = '';
            abrirModal();
        }

        async function editarCategoria(id) {
            try {
                const response = await fetch(API_URL + '/' + id);
                const result = await response.json();

                if (result.success && result.data) {
                    document.getElementById('modalTitle').textContent = 'Editar Categoria';
                    document.getElementById('idCategoria').value = result.data.idCategoria;
                    document.getElementById('nomeCategoria').value = result.data.nomeCategoria;
                    abrirModal();
                } else {
                    showAlert('Categoria não encontrada', 'danger');
                }
            } catch (error) {
                showAlert('Erro ao buscar categoria', 'danger');
            }
        }

        async function salvarCategoria() {
            const id = document.getElementById('idCategoria').value;
            const nomeCategoria = document.getElementById('nomeCategoria').value.trim();

            if (!nomeCategoria) {
                showAlert('Nome da categoria é obrigatório', 'warning');
                return;
            }

            try {
                const response = await fetch(id ? API_URL + '/' + id : API_URL, {
                    method: id ? 'PUT' : 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ nomeCategoria })
                });

                const result = await response.json();

                if (result.success) {
                    showAlert(id ? 'Categoria atualizada com sucesso' : 'Categoria criada com sucesso');
                    fecharModal();
                    carregarCategorias();
                } else {
                    showAlert(result.message || 'Erro ao salvar categoria', 'danger');
                }
            } catch (error) {
                showAlert('Erro ao salvar categoria', 'danger');
            }
        }

        async function deletarCategoria(id) {
            if (!confirm('Deseja realmente excluir esta categoria?')) return;

            try {
                const response = await fetch(API_URL + '/' + id, { method: 'DELETE' });
                const result = await response.json();

                if (result.success) {
                    showAlert('Categoria excluída com sucesso');
                    carregarCategorias();
                } else {
                    showAlert(result.message || 'Erro ao excluir categoria', 'danger');
                }
            } catch (error) {
                showAlert('Erro ao excluir categoria', 'danger');
            }
        }

        function abrirModal() {
            modal.classList.add('show');
        }

        function fecharModal() {
            modal.classList.remove('show');
        }

        modal.addEventListener('click', (e) => {
            if (e.target === modal) fecharModal();
        });
    </script>
</body>
</html>

