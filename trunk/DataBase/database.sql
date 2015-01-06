-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mar 06 Janvier 2015 à 10:57
-- Version du serveur: 5.5.40
-- Version de PHP: 5.4.35-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `park_me`
--
CREATE DATABASE `park_me` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `park_me`;

-- --------------------------------------------------------

--
-- Structure de la table `booked_spot`
--

CREATE TABLE IF NOT EXISTS `booked_spot` (
  `id` int(11) NOT NULL,
  `buyer_id` int(11) NOT NULL,
  `parking_spot_id` int(11) NOT NULL,
  `booking_date` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `parking_spot`
--

CREATE TABLE IF NOT EXISTS `parking_spot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  `user_id` int(11) NOT NULL,
  `date_offer` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_when_ready` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `reserved` binary(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_parking_spot_user` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=89 ;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `mail` varchar(70) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `crypted_pwd` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Structure de la table `vehicle`
--

CREATE TABLE IF NOT EXISTS `vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `photo` varchar(50) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `parking_spot`
--
ALTER TABLE `parking_spot`
  ADD CONSTRAINT `FK_parking_spot_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `vehicle`
--
ALTER TABLE `vehicle`
  ADD CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
