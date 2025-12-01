-- Inicialização do banco de dados com UTF-8
CREATE DATABASE IF NOT EXISTS gasosa
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE gasosa;

-- Tabela de categorias
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    cpf        VARCHAR(14)  UNIQUE NOT NULL,
    telefone   VARCHAR(20),
    email      VARCHAR(100)
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabela de produtos
CREATE TABLE IF NOT EXISTS produto (
    id_produto         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome               VARCHAR(100) NOT NULL,
    preco_unitario     DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT DEFAULT 0,
    id_categoria       BIGINT,
    FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabela de vendas
CREATE TABLE IF NOT EXISTS venda (
    id_venda       BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente     BIGINT NOT NULL,
    id_produto     BIGINT NOT NULL,
    quantidade     INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    data_venda     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
    FOREIGN KEY (id_produto) REFERENCES produto (id_produto)
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Dados de exemplo
INSERT INTO categoria (nome) VALUES
('Combustíveis'),
('Lubrificantes'),
('Acessórios');

INSERT INTO cliente (nome, cpf, telefone, email) VALUES
('João Silva',  '123.456.789-00', '(11) 98888-7777', 'joao@email.com'),
('Maria Santos','987.654.321-00', '(11) 97777-6666', 'maria@email.com');

INSERT INTO produto (nome, preco_unitario, quantidade_estoque, id_categoria) VALUES
('Gasolina Comum',        5.89, 10000, 1),
('Gasolina Aditivada',    6.29, 8000,  1),
('Diesel S10',            5.49, 12000, 1),
('Óleo Motor 5W30',      45.90, 150,   2),
('Limpador de Para-brisa',12.90, 200,  3);
