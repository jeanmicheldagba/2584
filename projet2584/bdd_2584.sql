-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  ven. 13 avr. 2018 à 20:03
-- Version du serveur :  5.7.19
-- Version de PHP :  5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `bdd_2584`
--

-- --------------------------------------------------------

--
-- Structure de la table `historiqueparties`
--

DROP TABLE IF EXISTS `historiqueparties`;
CREATE TABLE IF NOT EXISTS `historiqueparties` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Joueur1` varchar(30) NOT NULL,
  `Joueur2` varchar(30) NOT NULL,
  `Score1` int(11) NOT NULL,
  `Score2` int(11) NOT NULL,
  `Tuilemax1` int(11) NOT NULL,
  `Tuilemax2` int(11) NOT NULL,
  `Nbdeplacements1` int(11) NOT NULL,
  `Nbdeplacements2` int(11) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `historiqueparties`
--

INSERT INTO `historiqueparties` (`Id`, `Joueur1`, `Joueur2`, `Score1`, `Score2`, `Tuilemax1`, `Tuilemax2`, `Nbdeplacements1`, `Nbdeplacements2`) VALUES
(1, 'jean', 'Dumb', 15, 10, 8, 5, 8, 4),
(2, 'marie', 'AI', 13, 2, 8, 2, 7, 6),
(3, 'jean', 'françois', 11, 16, 5, 8, 8, 11),
(4, 'Dumb', 'camille', 20, 24, 8, 5, 12, 12),
(5, 'alexandre', 'marie', 18, 23, 5, 8, 9, 13),
(6, 'camille', 'Dumb', 30, 26, 8, 5, 19, 14),
(7, 'camille', 'Dumb', 40, 26, 8, 8, 19, 15),
(8, 'Dumb', 'Dumb', 24, 16, 5, 8, 12, 11),
(9, 'Dumb', 'alexandre', 15, 27, 8, 5, 10, 14),
(10, 'jean', 'Dumb', 11, 13, 8, 5, 7, 6),
(11, 'camille', 'adèle', 25, 15, 8, 5, 14, 8),
(12, 'simon', 'Dumb', 15, 16, 5, 8, 9, 9),
(13, 'AI', 'françois', 12, 18, 3, 8, 10, 11),
(14, 'alexandre', 'AI', 24, 21, 8, 5, 14, 13),
(15, 'alexandre', 'AI', 42, 21, 8, 8, 14, 14),
(16, 'simon', 'camille', 12, 28, 5, 8, 7, 16),
(17, 'AI', 'adèle', 12, 15, 5, 8, 8, 9),
(18, 'marie', 'Dumb', 18, 21, 5, 8, 10, 10);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
