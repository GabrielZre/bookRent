-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 28 Lut 2023, 21:59
-- Wersja serwera: 10.4.22-MariaDB
-- Wersja PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `bookrent`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tbook`
--

CREATE TABLE `tbook` (
  `id` int(11) NOT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `author` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `isbn` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tbook`
--

INSERT INTO `tbook` (`id`, `title`, `author`, `isbn`) VALUES
(2, 'Hobbit', 'Tolkiens', '8592003857123'),
(3, 'Power of Rings', 'Tolkiens', '6285921923515'),
(5, 'Uphill Down', 'Megan Holly', '3789874510049'),
(6, 'Peppa', 'Baba Jaga', '1959021004291'),
(7, 'Psychologia Ukladow', 'J.K Maxil', '8293219002031'),
(14, 'Little Prince', 'Anonymous', '7890123412318');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tloan`
--

CREATE TABLE `tloan` (
  `id` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `book_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `loan_is_active` tinyint(1) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `surname` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tloan`
--

INSERT INTO `tloan` (`id`, `start_date`, `end_date`, `book_id`, `user_id`, `loan_is_active`, `name`, `surname`) VALUES
(16, '2023-02-27', '2023-03-13', 2, 2, 1, 'Gabriel', 'Zrebiec'),
(17, '2023-02-27', '2023-03-13', 3, 2, 1, 'Michal', 'Zrebiec'),
(18, '2023-02-10', '2023-02-24', 5, 2, 1, 'Mateusz', 'Martyniuk'),
(20, '2023-02-27', '2023-03-13', 6, 3, 0, 'Gabriel', 'Zrebiecowaty'),
(21, '2023-02-27', '2023-03-13', 7, 3, 0, 'Tomasz', 'Pasterniak'),
(22, '2023-02-27', '2023-03-13', 6, 3, 0, 'Gabriel', 'Prawdziwy');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tuser`
--

CREATE TABLE `tuser` (
  `id` int(11) NOT NULL,
  `login` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(33) COLLATE utf8_unicode_ci NOT NULL,
  `role` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tuser`
--

INSERT INTO `tuser` (`id`, `login`, `password`, `role`) VALUES
(2, 'admin', '1671dfc274ad7e8d560df28ac0eb3e45', 'ADMIN'),
(3, 'gabriel', '4754793fa06ffec9db217a94cc2215c3', 'USER'),
(4, 'adam', 'fc4a1414afc2d554746f62d6772cb4b2', 'USER');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `tbook`
--
ALTER TABLE `tbook`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `isbn` (`isbn`);

--
-- Indeksy dla tabeli `tloan`
--
ALTER TABLE `tloan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeksy dla tabeli `tuser`
--
ALTER TABLE `tuser`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `tbook`
--
ALTER TABLE `tbook`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT dla tabeli `tloan`
--
ALTER TABLE `tloan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT dla tabeli `tuser`
--
ALTER TABLE `tuser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `tloan`
--
ALTER TABLE `tloan`
  ADD CONSTRAINT `tloan_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `tbook` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
