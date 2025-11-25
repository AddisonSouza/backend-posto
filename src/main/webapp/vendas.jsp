<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Vendas</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">Sistema de Gestão</a>
            <ul class="nav-menu">
                <li><a class="nav-link" href="categorias.jsp">Categorias</a></li>
                <li><a class="nav-link" href="produtos.jsp">Produtos</a></li>
                <li><a class="nav-link" href="clientes.jsp">Clientes</a></li>
                <li><a class="nav-link active" href="vendas.jsp">Vendas</a></li>
            </ul>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="page-header">
            <h2> Gerenciar Vendas</h2>
            <button class="btn btn-primary" onclick="novaVenda()">+ Nova Venda</button>
        </div>

        <div id="alertContainer"></div>

        <div class="table-card">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Cliente</th>
                        <th>Produto</th>
                        <th>Quantidade</th>
                        <th>Preço Unit.</th>
                        <th>Total</th>
                        <th>Data</th>
                        <th class="text-end">Ações</th>
                    </tr>
                </thead>
                <tbody id="vendasTable">
                    <tr>
                        <td colspan="8" class="text-center">
                            <div class="spinner"></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal" id="vendaModal">
        <div class="modal-dialog">
            <div class="modal-header">
                <h5 class="modal-title" id="modalTitle">Nova Venda</h5>
                <button class="modal-close" onclick="fecharModal()">×</button>
            </div>
            <div class="modal-body">
                <form id="vendaForm" onsubmit="event.preventDefault(); salvarVenda();">
                    <input type="hidden" id="idVenda">
                    <div class="row-2-cols">
                        <div class="form-group">
                            <label for="idCliente" class="form-label">Cliente *</label>
                            <select class="form-select" id="idCliente" required>
                                <option value="">Selecione...</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="idProduto" class="form-label">Produto *</label>
                            <select class="form-select" id="idProduto" required onchange="atualizarPreco()">
                                <option value="">Selecione...</option>
                            </select>
                        </div>
                    </div>
                    <div class="row-3-cols">
                        <div class="form-group">
                            <label for="quantidade" class="form-label">Quantidade *</label>
                            <input type="number" class="form-control" id="quantidade" required min="1" onchange="calcularTotal()">
                        </div>
                        <div class="form-group">
                            <label for="precoUnitario" class="form-label">Preço Unit. (R$) *</label>
                            <input type="number" class="form-control" id="precoUnitario" required min="0" step="0.01" onchange="calcularTotal()">
                        </div>
                        <div class="form-group">
                            <label for="total" class="form-label">Total (R$)</label>
                            <input type="text" class="form-control" id="total" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataVenda" class="form-label">Data da Venda *</label>
                        <input type="datetime-local" class="form-control" id="dataVenda" required>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="fecharModal()">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="salvarVenda()">Salvar</button>
            </div>
        </div>
    </div>

    <script>
        const API_URL = '/api/venda';
        const CLIENTE_API_URL = '/api/cliente';
        const PRODUTO_API_URL = '/api/produto';
        const modal = document.getElementById('vendaModal');
        let clientes = [];
        let produtos = [];

        document.addEventListener('DOMContentLoaded', function() {
            carregarClientes();
            carregarProdutos();
            carregarVendas();
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
                const response = await fetch(CLIENTE_API_URL);
                const result = await response.json();

                if (result.success && result.data) {
                    clientes = result.data;
                    const select = document.getElementById('idCliente');
                    select.innerHTML = '<option value="">Selecione...</option>';

                    clientes.forEach(cli => {
                        const option = document.createElement('option');
                        option.value = cli.idCliente;
                        option.textContent = cli.nome + ' - ' + cli.cpf;
                        select.appendChild(option);
                    });
                }
            } catch (error) {
                showAlert('Erro ao carregar clientes', 'danger');
            }
        }

        async function carregarProdutos() {
            try {
                const response = await fetch(PRODUTO_API_URL);
                const result = await response.json();

                if (result.success && result.data) {
                    produtos = result.data;
                    const select = document.getElementById('idProduto');
                    select.innerHTML = '<option value="">Selecione...</option>';

                    produtos.forEach(prod => {
                        const option = document.createElement('option');
                        option.value = prod.idProduto;
                        option.setAttribute('data-preco', prod.preco);
                        option.textContent = prod.descricao + ' - R$ ' + prod.preco.toFixed(2);
                        select.appendChild(option);
                    });
                }
            } catch (error) {
                showAlert('Erro ao carregar produtos', 'danger');
            }
        }

        function atualizarPreco() {
            const select = document.getElementById('idProduto');
            const selectedOption = select.options[select.selectedIndex];
            if (selectedOption && selectedOption.value) {
                const preco = selectedOption.getAttribute('data-preco');
                document.getElementById('precoUnitario').value = parseFloat(preco).toFixed(2);
                calcularTotal();
            }
        }

        function calcularTotal() {
            const quantidade = parseFloat(document.getElementById('quantidade').value) || 0;
            const precoUnitario = parseFloat(document.getElementById('precoUnitario').value) || 0;
            const total = quantidade * precoUnitario;
            document.getElementById('total').value = total.toFixed(2);
        }

        async function carregarVendas() {
            try {
                const response = await fetch(API_URL);
                const result = await response.json();

                console.log('Resposta da API:', result);

                const tbody = document.getElementById('vendasTable');
                tbody.innerHTML = '';

                if (result.success && result.data && result.data.length > 0) {
                    result.data.forEach(venda => {
                        const total = venda.quantidade * venda.precoUnitario;
                        const dataFormatada = new Date(venda.dataVenda).toLocaleString('pt-BR');

                        const tr = document.createElement('tr');

                        const tdId = document.createElement('td');
                        tdId.textContent = venda.idVenda;
                        tdId.style.color = '#000';
                        tdId.style.padding = '1rem';
                        tr.appendChild(tdId);

                        const tdCliente = document.createElement('td');
                        tdCliente.textContent = venda.cliente ? venda.cliente.nome : '-';
                        tdCliente.style.color = '#000';
                        tdCliente.style.padding = '1rem';
                        tr.appendChild(tdCliente);

                        const tdProduto = document.createElement('td');
                        tdProduto.textContent = venda.produto ? venda.produto.descricao : '-';
                        tdProduto.style.color = '#000';
                        tdProduto.style.padding = '1rem';
                        tr.appendChild(tdProduto);

                        const tdQtd = document.createElement('td');
                        tdQtd.textContent = venda.quantidade;
                        tdQtd.style.color = '#000';
                        tdQtd.style.padding = '1rem';
                        tr.appendChild(tdQtd);

                        const tdPrecoUnit = document.createElement('td');
                        tdPrecoUnit.textContent = 'R$ ' + venda.precoUnitario.toFixed(2);
                        tdPrecoUnit.style.color = '#000';
                        tdPrecoUnit.style.padding = '1rem';
                        tr.appendChild(tdPrecoUnit);

                        const tdTotal = document.createElement('td');
                        tdTotal.textContent = 'R$ ' + total.toFixed(2);
                        tdTotal.style.color = '#000';
                        tdTotal.style.padding = '1rem';
                        tr.appendChild(tdTotal);

                        const tdData = document.createElement('td');
                        tdData.textContent = dataFormatada;
                        tdData.style.color = '#000';
                        tdData.style.padding = '1rem';
                        tr.appendChild(tdData);

                        const tdAcoes = document.createElement('td');
                        tdAcoes.className = 'text-end';
                        tdAcoes.style.padding = '1rem';

                        const divActions = document.createElement('div');
                        divActions.className = 'action-buttons';

                        const btnEdit = document.createElement('button');
                        btnEdit.className = 'btn btn-sm btn-primary';
                        btnEdit.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/></svg>';
                        btnEdit.onclick = () => editarVenda(venda.idVenda);

                        const btnDelete = document.createElement('button');
                        btnDelete.className = 'btn btn-sm btn-danger';
                        btnDelete.innerHTML = '<svg width="16" height="16" fill="currentColor"><path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/><path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/></svg>';
                        btnDelete.onclick = () => deletarVenda(venda.idVenda);

                        divActions.appendChild(btnEdit);
                        divActions.appendChild(btnDelete);
                        tdAcoes.appendChild(divActions);
                        tr.appendChild(tdAcoes);

                        tbody.appendChild(tr);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="8" class="text-center">Nenhuma venda encontrada</td></tr>';
                }
            } catch (error) {
                console.error('Erro ao carregar vendas:', error);
                const tbody = document.getElementById('vendasTable');
                tbody.innerHTML = '<tr><td colspan="8" class="text-center">Erro ao carregar vendas: ' + error.message + '</td></tr>';
                showAlert('Erro ao carregar vendas: ' + error.message, 'danger');
            }
        }

        function novaVenda() {
            document.getElementById('modalTitle').textContent = 'Nova Venda';
            document.getElementById('vendaForm').reset();
            document.getElementById('idVenda').value = '';

            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            document.getElementById('dataVenda').value = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;

            abrirModal();
        }

        async function editarVenda(id) {
            try {
                const url = API_URL + '/' + id;
                const response = await fetch(url);
                const result = await response.json();

                if (result.success && result.data) {
                    const venda = result.data;
                    document.getElementById('modalTitle').textContent = 'Editar Venda';
                    document.getElementById('idVenda').value = venda.idVenda;
                    document.getElementById('idCliente').value = venda.cliente ? venda.cliente.idCliente : '';
                    document.getElementById('idProduto').value = venda.produto ? venda.produto.idProduto : '';
                    document.getElementById('quantidade').value = venda.quantidade || 0;
                    document.getElementById('precoUnitario').value = venda.precoUnitario || 0;

                    if (venda.dataVenda) {
                        const date = new Date(venda.dataVenda);
                        const year = date.getFullYear();
                        const month = String(date.getMonth() + 1).padStart(2, '0');
                        const day = String(date.getDate()).padStart(2, '0');
                        const hours = String(date.getHours()).padStart(2, '0');
                        const minutes = String(date.getMinutes()).padStart(2, '0');
                        document.getElementById('dataVenda').value = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
                    }

                    calcularTotal();
                    abrirModal();
                } else {
                    showAlert('Venda não encontrada', 'danger');
                }
            } catch (error) {
                console.error('Erro ao buscar venda:', error);
                showAlert('Erro ao buscar venda', 'danger');
            }
        }

        async function salvarVenda() {
            const id = document.getElementById('idVenda').value;
            const clienteId = document.getElementById('idCliente').value;
            const produtoId = document.getElementById('idProduto').value;
            const quantidade = parseInt(document.getElementById('quantidade').value);
            const precoUnitario = parseFloat(document.getElementById('precoUnitario').value);
            const dataVenda = document.getElementById('dataVenda').value;

            if (!clienteId || !produtoId || !quantidade || !precoUnitario || !dataVenda) {
                showAlert('Preencha todos os campos obrigatórios', 'warning');
                return;
            }

            const venda = {
                cliente: { idCliente: parseInt(clienteId) },
                produto: { idProduto: parseInt(produtoId) },
                quantidade,
                precoUnitario,
                dataVenda: new Date(dataVenda).toISOString()
            };

            const url = id ? API_URL + '/' + id : API_URL;
            const method = id ? 'PUT' : 'POST';

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(venda)
                });

                const result = await response.json();

                if (result.success) {
                    showAlert(id ? 'Venda atualizada com sucesso' : 'Venda criada com sucesso', 'success');
                    fecharModal();
                    carregarVendas();
                } else {
                    showAlert(result.message || 'Erro ao salvar venda', 'danger');
                }
            } catch (error) {
                console.error('Erro ao salvar venda:', error);
                showAlert('Erro ao salvar venda', 'danger');
            }
        }

        async function deletarVenda(id) {
            if (!confirm('Deseja realmente excluir esta venda?')) {
                return;
            }

            try {
                const url = API_URL + '/' + id;
                const response = await fetch(url, {
                    method: 'DELETE'
                });

                const result = await response.json();

                if (result.success) {
                    showAlert('Venda excluída com sucesso', 'success');
                    carregarVendas();
                } else {
                    showAlert(result.message || 'Erro ao excluir venda', 'danger');
                }
            } catch (error) {
                console.error('Erro ao excluir venda:', error);
                showAlert('Erro ao excluir venda', 'danger');
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

