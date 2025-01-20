-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 20, 2025 at 07:45 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `librarie`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`username`, `password`) VALUES
('admin', 'parola123');

-- --------------------------------------------------------

--
-- Table structure for table `carti`
--

CREATE TABLE `carti` (
  `id` int(11) NOT NULL,
  `titlu` varchar(255) NOT NULL,
  `autor` varchar(255) NOT NULL,
  `an` int(11) NOT NULL,
  `pret` int(11) NOT NULL,
  `gen` varchar(100) NOT NULL,
  `stoc` int(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carti`
--

INSERT INTO `carti` (`id`, `titlu`, `autor`, `an`, `pret`, `gen`, `stoc`) VALUES
(4, 'Luceafar', 'Mihai Eminescu', 1892, 100, 'Drama', 17),
(5, 'Amintiri din copilarie', 'Ion Creanga', 2002, 100, 'Copii', 24),
(6, 'O scurtă istorie ilustrată a românilor', 'Neagu Djuvara', 2011, 49, 'Istorie', 45),
(7, 'Tratat de istorie a religiilor', 'Mircea Eliade', 1949, 79, 'Istorie', 44),
(8, 'Din cer au căzut trei mere', 'Narine Abgarian	', 2003, 110, 'Ficțiune', 94),
(9, 'Băiatul cu pijamale în dungi', 'John Boyne	', 2007, 29, 'Ficțiune', 46),
(10, 'Lumea Sofiei', 'Jostein Gaarder	', 1989, 39, 'Ficțiune', 87),
(11, 'Cele mai frumoase Istorii', 'Herodot', 521, 120, 'Istorie', 93),
(12, 'Rătăciți printre formule', 'Sabine Hossenfelder', 2020, 89, 'Știință', 44),
(13, 'Viețile secrete ale planetelor', 'Paul Murdin', 2022, 110, 'Știință', 94),
(14, 'Gena egoistă', 'Richard Dawkins', 1976, 25, 'Știință', 110),
(16, 'Orașul', 'Nicolas', 2025, 20, 'Clasica', 117);

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `nume` varchar(100) NOT NULL,
  `telefon` varchar(15) NOT NULL,
  `posta` varchar(100) NOT NULL,
  `cnp` char(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`id`, `nume`, `telefon`, `posta`, `cnp`) VALUES
(1, 'Nicu', '412414', 'asA@gmail.com', '1234567891234'),
(4, 'Nicolae Gorobet', '098212141', 'nicugorobet@gmail.com', '1234567891237'),
(5, 'Marian Cosmin', '+4072321124124', 'mariancosmin@gmail.com', '1234567893654'),
(6, 'Nicolas', '9834728341512', 'nicolas@gmail.com', '1234567890098'),
(7, 'Marian', '12451414141', 'marian@gmail.com', '1234567892345');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `nume` varchar(128) NOT NULL,
  `prenume` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `numar` int(12) NOT NULL,
  `adresa` varchar(128) NOT NULL,
  `titlulcartii` varchar(128) NOT NULL,
  `autor` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`nume`, `prenume`, `email`, `numar`, `adresa`, `titlulcartii`, `autor`) VALUES
('Ion', 'Vasilica', 'ion@gmail.com', 1234567812, 'Domneasca 14', 'Tratat de istorie a religiilor', 'Mircea Eliade'),
('Ion', 'Vasilica', 'ion@gmail.com', 1234567812, 'Domneasca 14', 'Gena egoistă', 'Richard Dawkins'),
('Ion', 'Vasilica', 'ion@gmail.com', 1234567812, 'Domneasca 14', 'Luceafar', 'Mihai Eminescu'),
('Nicu', 'Ion', 'nicu@gmail.com', 1234554321, 'Ion Game', 'Orașul', 'Nicolas'),
('Nicu', 'Ion', 'nicu@gmail.com', 1234554321, 'Ion Game', 'Cele mai frumoase Istorii', 'Herodot'),
('Marcu', 'Cel Mare', 'marcucelmare@gmail.com', 1234561234, 'marcu 2', 'Orașul', 'Nicolas'),
('Marcu', 'Cel Mare', 'marcucelmare@gmail.com', 1234561234, 'marcu 2', 'Cele mai frumoase Istorii', 'Herodot'),
('Marcu', 'Cel Mare', 'marcucelmare@gmail.com', 1234561234, 'marcu 2', 'Rătăciți printre formule', 'Sabine Hossenfelder'),
('Marcu', 'Cel Mare', 'marcucelmare@gmail.com', 1234561234, 'marcu 2', 'Amintiri din copilarie', 'Ion Creanga'),
('Vicuta', 'Frumoasa', 'ceamaifrumoasavicuta@gmail.com', 731406911, 'Neajlov 5', 'Amintiri din copilarie', 'Ion Creanga'),
('Vicuta', 'Frumoasa', 'ceamaifrumoasavicuta@gmail.com', 731406911, 'Neajlov 5', 'Din cer au căzut trei mere', 'Narine Abgarian	'),
('Vicuta', 'Frumoasa', 'ceamaifrumoasavicuta@gmail.com', 731406911, 'Neajlov 5', 'Cele mai frumoase Istorii', 'Herodot');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(11) NOT NULL,
  `password` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`) VALUES
('nicu21', 'nicu2003'),
('', ''),
('', ''),
('nicu21', 'nicu2003');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carti`
--
ALTER TABLE `carti`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cnp` (`cnp`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carti`
--
ALTER TABLE `carti`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
