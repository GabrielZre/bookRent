-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 26 Lut 2023, 17:07
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
  `isbn` varchar(13) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tbook`
--

INSERT INTO `tbook` (`id`, `title`, `author`, `isbn`) VALUES
(2, 'Hobbit', 'Tolkiens', '8592003857123'),
(3, 'Power of Rings', 'Tolkiens', '6285921923515');

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
  `loan_is_active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tloan`
--

INSERT INTO `tloan` (`id`, `start_date`, `end_date`, `book_id`, `user_id`, `loan_is_active`) VALUES
(4, '2023-02-09', '2023-02-27', 2, 1, 0),
(5, '2023-02-10', '2023-02-12', 3, 2, 1),
(13, '2023-02-26', '2023-03-12', 2, 2, 1);

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
(1, 'janusz', '2e3c5df4b876dfde356c18708a79d6c7', 'ADMIN'),
(2, 'admin', '1671dfc274ad7e8d560df28ac0eb3e45', 'ADMIN');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `tbook`
--
ALTER TABLE `tbook`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `tloan`
--
ALTER TABLE `tloan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `book_id_2` (`book_id`),
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `tloan`
--
ALTER TABLE `tloan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT dla tabeli `tuser`
--
ALTER TABLE `tuser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

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
