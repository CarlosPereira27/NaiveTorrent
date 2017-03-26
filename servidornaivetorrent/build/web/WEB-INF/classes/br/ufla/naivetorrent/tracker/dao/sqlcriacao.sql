/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  lfps
 * Created: Mar 20, 2017
 */

-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Tempo de geração: 17/03/2017 às 16:25
-- Versão do servidor: 5.7.17-0ubuntu0.16.04.1
-- Versão do PHP: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `client_torrent_bd`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `info_hash_id` varchar(40) NOT NULL,
   `port` int(11) NOT NULL,
  `ip` varchar(50) NOT NULL,
  `last_conection` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura para tabela `client_torrent`
--

CREATE TABLE `client_torrent` (
  `id_client` int(11) NOT NULL,
  `id_torrent` int(11) NOT NULL,
  `download` bigint(20) NOT NULL,
  `upload` bigint(20) NOT NULL,
  `tipo_client` enum('seeder','leecher') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura para tabela `torrent`
--

CREATE TABLE `torrent` (
  `id` int(11) NOT NULL,
  `hash_id` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices de tabelas apagadas
--

--
-- Índices de tabela `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `info_hash_id` (`info_hash_id`);

--
-- Índices de tabela `torrent`
--
ALTER TABLE `torrent`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `hash_id` (`hash_id`);

--
-- AUTO_INCREMENT de tabelas apagadas
--

--
-- AUTO_INCREMENT de tabela `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de tabela `torrent`
--
ALTER TABLE `torrent`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Restrições para dumps de tabelas
--


--
-- Restrições para tabelas `client_torrent`
--
ALTER TABLE `client_torrent`
  ADD CONSTRAINT `client_torrent_ibfk_1` FOREIGN KEY (`id_torrent`) REFERENCES `torrent` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `client_torrent`
--
ALTER TABLE `client_torrent`
  ADD CONSTRAINT `client_torrent_ibfk_2` FOREIGN KEY (`id_client`) REFERENCES `client` (`id`) ON DELETE CASCADE;

--
-- Índices de tabela `client_torrent`
--
ALTER TABLE `client_torrent`
  ADD PRIMARY KEY (`id_client`, `id_torrent`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
