<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Clientes</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">Sistema de Gestão</a>
            <ul class="nav-menu">
                <li><a class="nav-link" href="categorias.jsp">Categorias</a></li>
                <li><a class="nav-link" href="produtos.jsp">Produtos</a></li>
                <li><a class="nav-link active" href="clientes.jsp">Clientes</a></li>
                <li><a class="nav-link" href="vendas.jsp">Vendas</a></li>
            </ul>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="page-header">
            <h2>Gerenciar Clientes</h2>
            <button class="btn btn-primary" onclick="novoCliente()">+ Novo Cliente</button>
        </div>

        <div id="alertContainer"></div>

        <div class="table-card">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>CPF</th>
                        <th>Telefone</th>
                        <th>Email</th>
                        <th>Endereço</th>
                        <th class="text-end">Ações</th>
                    </tr>
                </thead>
                <tbody id="clientesTable">
                    <tr>
                        <td colspan="7" class="text-center">
                            <div class="spinner"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal" id="clienteModal">
        <div class="modal-dialog">
            <div class="modal-header">
                <h5 class="modal-title" id="modalTitle">Novo Cliente</h5>
                <button class="modal-close" onclick="fecharModal()">×</button>
            </div>
            <div class="modal-body">
                <form id="clienteForm" onsubmit="event.preventDefault(); salvarCliente();">
                    <input type="hidden" id="idCliente">
                    <div class="row-2-cols">
                        <div class="form-group">
                            <label for="nome" class="form-label">Nome *</label>
                            <input type="text" class="form-control" id="nome" required>
                        </div>
                        <div class="form-group">
                            <label for="cpf" class="form-label">CPF *</label>
                            <input type="text" class="form-control" id="cpf" required maxlength="14" placeholder="000.000.000-00">
                        </div>
                    </div>
                    <div class="row-2-cols">
                        <div class="form-group">
                            <label for="telefone" class="form-label">Telefone</label>
                            <input type="text" class="form-control" id="telefone" placeholder="(00) 00000-0000">
                        </div>
                        <div class="form-group">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="endereco" class="form-label">Endereço</label>
                        <textarea class="form-control" id="endereco" rows="2"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="salvarCliente()">Salvar</button>
            </div>
        </div>
    </div>

    <script>
        const API_URL = '/api/cliente';
        const modal = document.getElementById('clienteModal');

        document.addEventListener('DOMContentLoaded', function() {
            carregarClientes();

            document.getElementById('cpf').addEventListener('input', function(e) {
                let value = e.target.value.replace(/\D/g, '');
                if (value.length <= 11) {
                    value = value.replace(/(\d{3})(\d)/, '$1.$2');
                    value = value.replace(/(\d{3})(\d)/, '$1.$2');
                    value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
                }
                e.target.value = value;
            });

            document.getElementById('telefone').addEventListener('input', function(e) {
                let value = e.target.value.replace(/\D/g, '');
                if (value.length <= 11) {
                    value = value.replace(/(\d{2})(\d)/, '($1) $2');
                    value = value.replace(/(\d{5})(\d)/, '$1-$2');
                }
                e.target.value = value;
            });
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

        async function carregarClientes() {
            try {
                const response = await fetch(API_URL);
                const result = await response.json();

                console.log('Resposta da API:', result);

                const tbody = document.getElementById('clientesTable');
                tbody.innerHTML = '';

                if (result.success && result.data && result.data.length > 0) {
                    result.data.forEach(cliente => {
                        const tr = document.createElement('tr');

                        const tdId = document.createElement('td');
                        tdId.textContent = cliente.idCliente;
                        tdId.style.color = '#000';
                        tdId.style.padding = '1rem';
                        tr.appendChild(tdId);

                        const tdNome = document.createElement('td');
                        tdNome.textContent = cliente.nome;
                        tdNome.style.color = '#000';
                        tdNome.style.padding = '1rem';
                        tr.appendChild(tdNome);

                        const tdCpf = document.createElement('td');
                        tdCpf.textContent = cliente.cpf || '-';
                        tdCpf.style.color = '#000';
                        tdCpf.style.padding = '1rem';
                        tr.appendChild(tdCpf);

                        const tdTel = document.createElement('td');
                        tdTel.textContent = cliente.telefone || '-';
                        tdTel.style.color = '#000';
                        tdTel.style.padding = '1rem';
                        tr.appendChild(tdTel);

                        const tdEmail = document.createElement('td');
                        tdEmail.textContent = cliente.email || '-';
                        tdEmail.style.color = '#000';
                        tdEmail.style.padding = '1rem';
                        tr.appendChild(tdEmail);

                        const tdEnd = document.createElement('td');
                        tdEnd.textContent = cliente.endereco || '-';
                        tdEnd.style.color = '#000';
                        tdEnd.style.padding = '1rem';
                        tr.appendChild(tdEnd);

                        const tdAcoes = document.createElement('td');
                        tdAcoes.className = 'text-end';
                        tdAcoes.style.padding = '1rem';

                        const divActions = document.createElement('div');
                        divActions.className = 'action-buttons';

                        const btnEdit = document.createElement('button');
                        btnEdit.className = 'btn btn-sm btn-primary';
                        btnEdit.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/></svg>';
                        btnEdit.onclick = () => editarCliente(cliente.idCliente);

                        const btnDelete = document.createElement('button');
                        btnDelete.className = 'btn btn-sm btn-danger';
                        btnDelete.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/><path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/></svg>';
                        btnDelete.onclick = () => deletarCliente(cliente.idCliente);

                        divActions.appendChild(btnEdit);
                        divActions.appendChild(btnDelete);
                        tdAcoes.appendChild(divActions);
                        tr.appendChild(tdAcoes);

                        tbody.appendChild(tr);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="7" class="text-center">Nenhum cliente encontrado</td></tr>';
                }
            } catch (error) {
                console.error('Erro ao carregar clientes:', error);
                const tbody = document.getElementById('clientesTable');
                tbody.innerHTML = '<tr><td colspan="7" class="text-center">Erro ao carregar clientes: ' + error.message + '</td></tr>';
                showAlert('Erro ao carregar clientes: ' + error.message, 'danger');
            }
        }

        function novoCliente() {
            document.getElementById('modalTitle').textContent = 'Novo Cliente';
            document.getElementById('clienteForm').reset();
            document.getElementById('idCliente').value = '';
            abrirModal();
        }

        async function editarCliente(id) {
            try {
                const url = API_URL + '/' + id;
                const response = await fetch(url);
                const result = await response.json();

                if (result.success && result.data) {
                    document.getElementById('modalTitle').textContent = 'Editar Cliente';
                    document.getElementById('idCliente').value = result.data.idCliente;
                    document.getElementById('nome').value = result.data.nome || '';
                    document.getElementById('cpf').value = result.data.cpf || '';
                    document.getElementById('telefone').value = result.data.telefone || '';
                    document.getElementById('email').value = result.data.email || '';
                    document.getElementById('endereco').value = result.data.endereco || '';
                    abrirModal();
                } else {
                    showAlert('Cliente não encontrado', 'danger');
                }
            } catch (error) {
                console.error('Erro ao buscar cliente:', error);
                showAlert('Erro ao buscar cliente', 'danger');
            }
        }

        async function salvarCliente() {
            const id = document.getElementById('idCliente').value;
            const nome = document.getElementById('nome').value.trim();
            const cpf = document.getElementById('cpf').value.trim();
            const telefone = document.getElementById('telefone').value.trim();
            const email = document.getElementById('email').value.trim();
            const endereco = document.getElementById('endereco').value.trim();

            if (!nome || !cpf) {
                showAlert('Nome e CPF são obrigatórios', 'warning');
                return;
            }

            const cliente = { nome, cpf, telefone, email, endereco };
            const url = id ? API_URL + '/' + id : API_URL;
            const method = id ? 'PUT' : 'POST';

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(cliente)
                });

                const result = await response.json();

                if (result.success) {
                    showAlert(id ? 'Cliente atualizado com sucesso' : 'Cliente criado com sucesso', 'success');
                    fecharModal();
                    carregarClientes();
                } else {
                    showAlert(result.message || 'Erro ao salvar cliente', 'danger');
                }
            } catch (error) {
                console.error('Erro ao salvar cliente:', error);
                showAlert('Erro ao salvar cliente', 'danger');
            }
        }

        async function deletarCliente(id) {
            if (!confirm('Deseja realmente excluir este cliente?')) {
                return;
            }

            try {
                const url = API_URL + '/' + id;
                const response = await fetch(url, {
                    method: 'DELETE'
                });

                const result = await response.json();

                if (result.success) {
                    showAlert('Cliente excluído com sucesso', 'success');
                    carregarClientes();
                } else {
                    showAlert(result.message || 'Erro ao excluir cliente', 'danger');
                }
            } catch (error) {
                console.error('Erro ao excluir cliente:', error);
                showAlert('Erro ao excluir cliente', 'danger');
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

